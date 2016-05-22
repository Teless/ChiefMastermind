package com.business;

import com.business.dao.GameDaoImpl;
import com.business.dao.PlayerDaoImpl;
import com.domain.*;
import com.mock.DatastoreFactoryMock;
import org.bson.types.ObjectId;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Test
    public void testGuessFinishedGame() throws Exception {
        game.setStatus(GameStatus.FINISHED);
        gameDao.save(game);

        Guess guess = guessesManager.guess("0001", player.getName(), game.getId().toString());

        Guess expected = new Guess();
        expected.setCode("0001");
        expected.setExact(3);
        expected.setNear(0);
        expected.setStatus(GuessStatus.VALID_GUESS);

        assertEquals(expected, guess);
    }

}