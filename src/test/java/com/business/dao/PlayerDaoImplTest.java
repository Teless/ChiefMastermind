package com.business.dao;

import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Guess;
import com.domain.Player;
import com.mock.DatastoreFactoryMock;
import com.mock.SystemPropertiesMock;
import com.mongodb.DuplicateKeyException;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;

import javax.inject.Inject;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(CdiRunner.class)
@AdditionalClasses({DatastoreFactoryMock.class, SystemPropertiesMock.class})
public class PlayerDaoImplTest {

    @Inject
    private GameDaoImpl gameDao;

    @Inject
    private PlayerDaoImpl playerDao;

    @Inject
    private Datastore datastore;

    private Game gameRunning;
    private Player playerRunning;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));

        //<editor-fold desc="Set game">
        gameRunning = new Game();
        gameRunning.setStatus(GameStatus.MASTER_MINDING);
        gameRunning.setGameKey("secret");
        gameRunning.setPlayers(Collections.emptyList());
        gameRunning.setPlayersLimit(2);
        gameRunning.setPlayersCount(1);
        gameDao.save(gameRunning);

        playerRunning = new Player("Initial waiting player");
        playerRunning.setGame(gameRunning);
        playerDao.save(playerRunning);

        gameRunning.setPlayers(Collections.singletonList(playerRunning));
        gameDao.save(gameRunning);
        //</editor-fold>
    }

    @Test
    public void testList() throws Exception {
        assertEquals(Collections.singletonList(playerRunning), playerDao.list());
    }

    @Test
    public void testFind() throws Exception {
        Player playerFetched = playerDao.find(playerRunning.getId().toString());
        assertEquals(playerRunning, playerFetched);
    }

    @Test
    public void testFindWithGame() throws Exception {
        Player playerFetched = playerDao.find(playerRunning.getName(), gameRunning.getId().toString());
        assertEquals(playerRunning, playerFetched);
    }

    @Test
    public void testFindWithInvalidGame() throws Exception {
        Player playerFetched = playerDao.find(playerRunning.getName(), "012345678901234567890123");
        assertNull(playerFetched);
    }

    @Test
    public void testFindWithGameInvalidUser() throws Exception {
        Player playerFetched = playerDao.find("ow", gameRunning.getId().toString());
        assertNull(playerFetched);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveDuplicatedName() throws Exception {
        Player player = new Player(playerRunning.getName());
        player.setGame(gameRunning);
        playerDao.save(player);
    }

    @Test
    public void testAddGuess() throws Exception {
        Guess guess = new Guess();
        guess.setCode("aaaaaaaa");
        guess.setExact(3);
        guess.setNear(2);

        boolean result = playerDao.addGuess(guess, playerRunning.getId(), gameRunning.getId());
        assertTrue(result);

        playerRunning.setRound(1);
        playerRunning.setGuesses(Collections.singletonList(guess));
        Player playerFound = playerDao.find(playerRunning.getId().toString());

        assertEquals(playerRunning, playerFound);
    }
}