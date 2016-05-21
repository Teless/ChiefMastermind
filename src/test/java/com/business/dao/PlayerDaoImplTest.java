package com.business.dao;

import com.business.GameUtil;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Guess;
import com.domain.Player;
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
@AdditionalClasses({DatastoreFactory.class, SystemPropertiesMock.class})
public class PlayerDaoImplTest {

    @Inject
    private GameDaoImpl gameDao;

    @Inject
    private PlayerDaoImpl playerDao;

    @Inject
    private Datastore datastore;

    private Game gameRunning;
    private Player playerMasterMinding;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));

        //<editor-fold desc="Set game">
        gameRunning = new Game();
        gameRunning.setStatus(GameStatus.MASTER_MINDING);
        gameRunning.setGameKey(GameUtil.generateGamekey());
        gameRunning.setPlayers(Collections.emptyList());
        gameRunning.setPlayersLimit(2);
        gameRunning.setPlayersCount(1);
        gameDao.save(gameRunning);

        playerMasterMinding = new Player("Initial waiting player");
        playerMasterMinding.setGame(gameRunning);
        playerDao.save(playerMasterMinding);

        gameRunning.setPlayers(Collections.singletonList(playerMasterMinding));
        gameDao.save(gameRunning);
        //</editor-fold>

    }

    @Test
    public void testList() throws Exception {
        assertEquals(Collections.singletonList(playerMasterMinding), playerDao.list());
    }

    @Test
    public void testFind() throws Exception {
        Player playerFetched = playerDao.find(playerMasterMinding.getId().toString());
        assertEquals(playerMasterMinding, playerFetched);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveDuplicatedName() throws Exception {
        Player player = new Player(playerMasterMinding.getName());
        player.setGame(gameRunning);
        playerDao.save(player);
    }

    @Test
    public void testAddGuess() throws Exception {
        Guess guess = new Guess();
        guess.setGuess("aaaaaaaa");
        guess.setExact(3);
        guess.setNear(2);

        boolean result = playerDao.addGuess(guess, playerMasterMinding.getId(), gameRunning.getId());
        assertTrue(result);

        playerMasterMinding.setRound(1);
        playerMasterMinding.setGuesses(Collections.singletonList(guess));
        Player playerFound = playerDao.find(playerMasterMinding.getId().toString());

        assertEquals(playerMasterMinding, playerFound);
    }
}