package com;

import com.business.*;
import com.business.dao.GameDao;
import com.business.dao.GameDaoImpl;
import com.business.dao.PlayerDao;
import com.business.dao.PlayerDaoImpl;
import com.domain.*;
import com.mock.DatastoreFactoryMock;
import org.bson.types.ObjectId;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mongodb.morphia.Datastore;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(CdiRunner.class)
@AdditionalClasses({
        GameCreator.class,
        PlayerDaoImpl.class,
        GameDaoImpl.class,
        DatastoreFactoryMock.class,
        SecretGenerator.class,
        GameKeyGenerator.class,
        JoinGameManager.class,
        GuessesManager.class,
        GuessProcessor.class,
        JoinGameManager.class
})
public class QuickMultiplayerGameTest {

    @Inject
    private GameCreator gameCreator;

    @Inject
    private JoinGameManager joinGameManager;

    @Inject
    private PlayerDao playerDao;

    @Inject
    private GameDao gameDao;

    @Inject
    private GuessesManager guessesManager;

    @Inject
    private GameStatusManager gameStatusManager;

    @Mock
    @Produces
    private SecretGenerator secretGeneratorMock;

    @Inject
    private Datastore datastore;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));
    }

    @Test
    public void testQuickGame() throws Exception {
        when(secretGeneratorMock.newSecret(Game.DEFAULT_COLORS_COUNT, Game.DEFAULT_SECRET_SIZE))
                .thenReturn("00001111");

        Game game = gameCreator.createQuickMultiplayerGame("Player1");

        JoinGameStatus joinGameStatus = joinGameManager.joinGame("Player2", game.getId().toString());
        assertEquals(JoinGameStatus.SUCCESS, joinGameStatus);

        JoinGameStatus joinGameStatusIntruder = joinGameManager.joinGame("full", game.getId().toString());
        assertEquals(JoinGameStatus.GAME_FULL, joinGameStatusIntruder);

        StartGameStatus startGameStatus = gameStatusManager.startGame(game.getGameKey(), game.getId().toString());
        assertEquals(StartGameStatus.SUCCESS, startGameStatus);

        JoinGameStatus joinGameStatusTooLate = joinGameManager.joinGame("tooLate", game.getId().toString());
        assertEquals(JoinGameStatus.GAME_WAS_NOT_ON_WAIT, joinGameStatusTooLate);

        //<editor-fold desc="Making guesses">
        Guess guess1Player1 = guessesManager.guess("00112233", "Player1", game.getId().toString());
        Guess guessTooSoonPlayer1 = guessesManager.guess("00002233", "Player1", game.getId().toString());

        Guess intruderGuess = guessesManager.guess("00002233", "Intruder", game.getId().toString());

        Guess guess1Player2 = guessesManager.guess("33112211", "Player2", game.getId().toString());

        Guess player1WinnerGuess = guessesManager.guess("00001111", "Player1", game.getId().toString());

        GameStatus statusAfterWinninGuess = gameDao.find(game.getId().toString()).getStatus();
        assertEquals(GameStatus.SOLVED, statusAfterWinninGuess);

        Guess guess2Player2 = guessesManager.guess("00112233", "Player2", game.getId().toString());
        //</editor-fold>

        //<editor-fold desc="Validating guesses">
        Guess expectedGuess1Player1 = new Guess();
        expectedGuess1Player1.setNear(2);
        expectedGuess1Player1.setExact(2);
        expectedGuess1Player1.setStatus(GuessStatus.VALID_GUESS);
        expectedGuess1Player1.setCode("00112233");

        Guess expectedGuessTooSoonPlayer1 = Guess.emptyGuess(GuessStatus.WAITING_OTHER_PLAYERS_GUESSES);
        expectedGuessTooSoonPlayer1.setCode("00002233");

        Guess expectedWinnerGuessPlayer1 = new Guess();
        expectedWinnerGuessPlayer1.setNear(0);
        expectedWinnerGuessPlayer1.setExact(8);
        expectedWinnerGuessPlayer1.setStatus(GuessStatus.SOLVED);
        expectedWinnerGuessPlayer1.setCode("00001111");

        Guess expectedGuess1Player2 = new Guess();
        expectedGuess1Player2.setNear(2);
        expectedGuess1Player2.setExact(2);
        expectedGuess1Player2.setStatus(GuessStatus.VALID_GUESS);
        expectedGuess1Player2.setCode("33112211");

        Guess expectedGuess2Player2 = new Guess();
        expectedGuess2Player2.setNear(2);
        expectedGuess2Player2.setExact(2);
        expectedGuess2Player2.setStatus(GuessStatus.VALID_GUESS);
        expectedGuess2Player2.setCode("00112233");

        Guess expectedIntruderGuess = Guess.emptyGuess(GuessStatus.NOT_IN_THE_GAME);
        expectedIntruderGuess.setCode("00002233");

        assertEquals(expectedGuess1Player1, guess1Player1);
        assertEquals(expectedGuessTooSoonPlayer1, guessTooSoonPlayer1);
        assertEquals(expectedWinnerGuessPlayer1, player1WinnerGuess);
        assertEquals(expectedGuess1Player2, guess1Player2);
        assertEquals(expectedGuess2Player2, guess2Player2);
        assertEquals(expectedIntruderGuess, intruderGuess);
        //</editor-fold>

        Game finalGameState = gameDao.find(game.getId().toString());
        Player player1 = playerDao.find("Player1", game.getId().toString());
        Player player2 = playerDao.find("Player2", game.getId().toString());

        //<editor-fold desc="Validating player1">
        Player expectedPlayer1State = new Player();
        expectedPlayer1State.setId(player1.getId());
        expectedPlayer1State.setName("Player1");
        expectedPlayer1State.setRound(2);
        expectedPlayer1State.setGame(finalGameState);
        expectedPlayer1State.setGuesses(Arrays.asList(guess1Player1, player1WinnerGuess));

        assertEquals(expectedPlayer1State, player1);
        //</editor-fold>

        //<editor-fold desc="Validating player2">
        Player expectedPlayer2State = new Player();
        expectedPlayer2State.setId(player2.getId());
        expectedPlayer2State.setName("Player2");
        expectedPlayer2State.setRound(2);
        expectedPlayer2State.setGame(finalGameState);
        expectedPlayer2State.setGuesses(Arrays.asList(guess1Player2, guess2Player2));

        assertEquals(expectedPlayer2State, player2);
        //</editor-fold>

        //<editor-fold desc="Validating game">
        Game expectedGameState = new Game();
        expectedGameState.setId(game.getId());
        expectedGameState.setGameKey(game.getGameKey());
        expectedGameState.setStatus(GameStatus.FINISHED);
        expectedGameState.setPlayersLimit(2);
        expectedGameState.setPlayersCount(2);
        expectedGameState.setRoundsLimit(Game.DEFAULT_ROUNDS_LIMIT);
        expectedGameState.setRound(2);
        expectedGameState.setRoundGuesses(0);
        expectedGameState.setPositions(Game.DEFAULT_POSITIONS);
        expectedGameState.setSecret("00001111");
        expectedGameState.setPlayers(Arrays.asList(expectedPlayer1State, expectedPlayer2State));

        assertEquals(expectedGameState, finalGameState);
        //</editor-fold>

        joinGameStatusIntruder = joinGameManager.joinGame("Intruder", game.getId().toString());
        assertEquals(JoinGameStatus.GAME_WAS_NOT_ON_WAIT, joinGameStatusIntruder);

    }
}
