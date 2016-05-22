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
        GameKeyGenerator.class,
        GuessesManager.class,
        GuessProcessor.class,
        GameStatusManager.class,
        JoinGameManager.class
})
public class QuickGameTest {

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

        Game game = gameCreator.createQuickGame("Rafael");

        JoinGameStatus joinGameStatus = joinGameManager.joinGame("Intruder", game.getId().toString());
        assertEquals(JoinGameStatus.GAME_WAS_NOT_ON_WAIT, joinGameStatus);

        //<editor-fold desc="Making guesses">
        Guess guess1 = guessesManager.guess("00112233", "Rafael", game.getId().toString());
        Guess guess2 = guessesManager.guess("00002233", "Rafael", game.getId().toString());

        Guess intruderGuess = guessesManager.guess("00002233", "Intruder", game.getId().toString());

        Guess guessInvalid1 = guessesManager.guess("aaaa", "Rafael", game.getId().toString());
        Guess guessInvalid2 = guessesManager.guess("00002233", "Invalid", game.getId().toString());
        Guess guessInvalid3 = guessesManager.guess("00002233", "Rafael", new ObjectId().toString());

        Guess winnerGuess = guessesManager.guess("00001111", "Rafael", game.getId().toString());
        //</editor-fold>

        //<editor-fold desc="Validating guesses">
        Guess expectedGuess1 = new Guess();
        expectedGuess1.setNear(2);
        expectedGuess1.setExact(2);
        expectedGuess1.setStatus(GuessStatus.VALID_GUESS);
        expectedGuess1.setCode("00112233");

        Guess expectedGuess2 = new Guess();
        expectedGuess2.setNear(0);
        expectedGuess2.setExact(4);
        expectedGuess2.setStatus(GuessStatus.VALID_GUESS);
        expectedGuess2.setCode("00002233");

        Guess expectedInvalidGuess1 = Guess.emptyGuess(GuessStatus.INVALID_GUESS);
        expectedInvalidGuess1.setCode("aaaa");
        Guess expectedInvalidGuess2 = Guess.emptyGuess(GuessStatus.NOT_IN_THE_GAME);
        expectedInvalidGuess2.setCode("00002233");
        Guess expectedInvalidGuess3 = Guess.emptyGuess(GuessStatus.NOT_IN_THE_GAME);
        expectedInvalidGuess3.setCode("00002233");

        Guess expectedWinnerGuess = new Guess();
        expectedWinnerGuess.setNear(0);
        expectedWinnerGuess.setExact(8);
        expectedWinnerGuess.setStatus(GuessStatus.SOLVED);
        expectedWinnerGuess.setCode("00001111");

        Guess expectedIntruderGuess = Guess.emptyGuess(GuessStatus.NOT_IN_THE_GAME);
        expectedIntruderGuess.setCode("00002233");

        assertEquals(expectedGuess1, guess1);
        assertEquals(expectedGuess2, guess2);
        assertEquals(expectedInvalidGuess1, guessInvalid1);
        assertEquals(expectedInvalidGuess2, guessInvalid2);
        assertEquals(expectedInvalidGuess3, guessInvalid3);
        assertEquals(expectedWinnerGuess, winnerGuess);
        assertEquals(expectedIntruderGuess, intruderGuess);
        //</editor-fold>

        Game finalGameState = gameDao.find(game.getId().toString());
        Player player = playerDao.find("Rafael", game.getId().toString());

        //<editor-fold desc="Validating player">
        Player expectedPlayerState = new Player();
        expectedPlayerState.setId(player.getId());
        expectedPlayerState.setName("Rafael");
        expectedPlayerState.setRound(3);
        expectedPlayerState.setGame(finalGameState);
        expectedPlayerState.setGuesses(Arrays.asList(guess1, guess2, winnerGuess));

        assertEquals(expectedPlayerState, player);
        //</editor-fold>

        //<editor-fold desc="Validating game">
        Game expectedGameState = new Game();
        expectedGameState.setId(game.getId());
        expectedGameState.setGameKey(game.getGameKey());
        expectedGameState.setStatus(GameStatus.FINISHED);
        expectedGameState.setPlayersLimit(1);
        expectedGameState.setPlayersCount(1);
        expectedGameState.setRoundsLimit(Game.DEFAULT_ROUNDS_LIMIT);
        expectedGameState.setRound(3);
        expectedGameState.setRoundGuesses(0);
        expectedGameState.setPositions(Game.DEFAULT_POSITIONS);
        expectedGameState.setSecret("00001111");
        expectedGameState.setPlayers(Collections.singletonList(expectedPlayerState));

        assertEquals(expectedGameState, finalGameState);
        //</editor-fold>

        joinGameManager.joinGame("Intruder", game.getId().toString());
        assertEquals(JoinGameStatus.GAME_WAS_NOT_ON_WAIT, joinGameStatus);

    }

}
