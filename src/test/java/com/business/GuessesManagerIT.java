package com.business;

import com.business.dao.GameDaoImpl;
import com.business.dao.PlayerDaoImpl;
import com.domain.*;
import com.mock.DatastoreFactoryMock;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;

import javax.inject.Inject;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
@AdditionalClasses({
        DatastoreFactoryMock.class,
        PlayerDaoImpl.class,
        GameDaoImpl.class,
        GuessProcessor.class
})
public class GuessesManagerIT {

    @Inject
    private GameDaoImpl gameDao;

    @Inject
    private PlayerDaoImpl playerDao;

    @Inject
    private Datastore datastore;

    @Inject
    private GuessesManager guessesManager;

    private Game game;
    private Player player;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));

        //<editor-fold desc="Set waiting game">
        game = new Game();
        game.setSecret("0011");
        game.setPlayersCount(2);
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setPlayers(Collections.emptyList());
        gameDao.save(game);

        player = new Player("userName");
        player.setGame(game);
        playerDao.save(player);

        game.setPlayers(Collections.singletonList(player));
        gameDao.save(game);
        //</editor-fold>
    }

    @Test
    public void testGuessExact() throws Exception {
        Guess guess = guessesManager.guess("0011", player.getName(), game.getId().toString());

        Guess expected = new Guess();
        expected.setCode("0011");
        expected.setExact(4);
        expected.setNear(0);
        expected.setStatus(GuessStatus.SOLVED);

        assertEquals(expected, guess);

        Game fetchedGame = gameDao.find(this.game.getId().toString());
        assertEquals(GameStatus.SOLVED, fetchedGame.getStatus());
        assertEquals(1, fetchedGame.getRoundGuesses());
    }

    @Test
    public void testGuessNear() throws Exception {
        Guess guess = guessesManager.guess("0001", player.getName(), game.getId().toString());

        Guess expected = new Guess();
        expected.setCode("0001");
        expected.setExact(3);
        expected.setNear(0);
        expected.setStatus(GuessStatus.VALID_GUESS);

        assertEquals(expected, guess);

        Game fetchedGame = gameDao.find(this.game.getId().toString());
        assertEquals(GameStatus.MASTER_MINDING, fetchedGame.getStatus());
        assertEquals(1, fetchedGame.getRoundGuesses());
    }

    @Test
    public void testGuessSolvedGame() throws Exception {
        game.setStatus(GameStatus.SOLVED);
        gameDao.save(game);

        Guess guess = guessesManager.guess("0001", player.getName(), game.getId().toString());

        Guess expected = new Guess();
        expected.setCode("0001");
        expected.setExact(3);
        expected.setNear(0);
        expected.setStatus(GuessStatus.VALID_GUESS);

        assertEquals(expected, guess);

        Game fetchedGame = gameDao.find(this.game.getId().toString());
        assertEquals(GameStatus.SOLVED, fetchedGame.getStatus());
        assertEquals(1, fetchedGame.getRoundGuesses());
    }

    @Test
    public void testWinningGuess() throws Exception {
        game.setPlayersCount(1);
        gameDao.save(game);

        Guess guess = guessesManager.guess("0011", player.getName(), game.getId().toString());

        Guess expected = new Guess();
        expected.setCode("0011");
        expected.setExact(4);
        expected.setNear(0);
        expected.setStatus(GuessStatus.SOLVED);

        assertEquals(expected, guess);

        Game fetchedGame = gameDao.find(this.game.getId().toString());
        assertEquals(GameStatus.FINISHED, fetchedGame.getStatus());
        assertEquals(0, fetchedGame.getRoundGuesses());
    }

    @Test
    public void testExaustingRounds() throws Exception {
        game.setPlayersCount(1);
        game.setRoundsLimit(1);
        gameDao.save(game);

        Guess guess1 = guessesManager.guess("0011", player.getName(), game.getId().toString());
        Guess guess2 = guessesManager.guess("0011", player.getName(), game.getId().toString());

        Guess expectedGuess1 = new Guess();
        expectedGuess1.setCode("0011");
        expectedGuess1.setExact(4);
        expectedGuess1.setNear(0);
        expectedGuess1.setStatus(GuessStatus.SOLVED);

        Guess expectedGuess2 = Guess.emptyGuess(GuessStatus.GAME_HAS_ENDED);
        expectedGuess2.setCode("0011");

        assertEquals(expectedGuess1, guess1);
        assertEquals(expectedGuess2, guess2);

        Game fetchedGame = gameDao.find(this.game.getId().toString());
        assertEquals(GameStatus.FINISHED, fetchedGame.getStatus());
        assertEquals(0, fetchedGame.getRoundGuesses());
    }

}