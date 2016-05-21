package com.business.dao;

import com.business.GameUtil;
import com.domain.Game;
import com.domain.GameStatus;
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
import java.util.Arrays;
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

    private Game gameWaiting;
    private Player playerWaiting;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));

        //<editor-fold desc="Set waiting game">
        gameWaiting = new Game();
        gameWaiting.setStatus(GameStatus.WAITING);
        gameWaiting.setGameKey(GameUtil.generateGamekey());
        gameWaiting.setPlayers(Collections.emptyList());
        gameDao.save(gameWaiting);

        playerWaiting = new Player("Initial waiting player");
        playerWaiting.setGame(gameWaiting);
        playerDao.save(playerWaiting);

        gameWaiting.setPlayers(Collections.singletonList(playerWaiting));
        gameDao.save(gameWaiting);
        //</editor-fold>

    }

    @Test
    public void testList() throws Exception {
        assertEquals(Collections.singletonList(playerWaiting), playerDao.list());
    }

    @Test
    public void testFind() throws Exception {
        Player playerFetched = playerDao.find(playerWaiting.getId().toString());
        assertEquals(playerWaiting, playerFetched);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveDuplicatedName() throws Exception {
        Player player = new Player(playerWaiting.getName());
        player.setGame(gameWaiting);
        playerDao.save(player);
    }

}