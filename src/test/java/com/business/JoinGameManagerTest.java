package com.business;

import com.business.dao.GameDaoImpl;
import com.business.dao.PlayerDaoImpl;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.JoinGameStatus;
import com.domain.Player;
import com.mock.DatastoreFactoryMock;
import com.mongodb.DuplicateKeyException;
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
@AdditionalClasses({GameDaoImpl.class, DatastoreFactoryMock.class})
public class JoinGameManagerTest {

    @Inject
    private GameDaoImpl gameDao;

    @Inject
    private PlayerDaoImpl playerDao;

    @Inject
    private JoinGameManager joinGameManager;

    @Inject
    private Datastore datastore;

    @Inject
    private DatastoreFactoryMock datastoreFactory;

    private Game gameWaiting;
    private Game gameRunning;
    private Game gameFinished;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));

        //<editor-fold desc="Set waiting game">
        gameWaiting = new Game();
        gameWaiting.setStatus(GameStatus.WAITING);
        gameWaiting.setGameKey("secret");
        gameWaiting.setPlayers(Collections.emptyList());
        gameWaiting.setPlayersLimit(2);
        gameWaiting.setRoundsLimit(2);
        gameWaiting.setPlayersCount(1);
        gameDao.save(gameWaiting);

        Player playerWaiting = new Player("Initial waiting player");
        playerWaiting.setGame(gameWaiting);
        playerDao.save(playerWaiting);

        gameWaiting.setPlayers(Collections.singletonList(playerWaiting));
        gameDao.save(gameWaiting);
        //</editor-fold>

        //<editor-fold desc="Set running game">
        gameRunning = new Game();
        gameRunning.setStatus(GameStatus.MASTER_MINDING);
        gameRunning.setGameKey("secret");
        gameRunning.setPlayers(Collections.emptyList());
        gameRunning.setPlayersLimit(2);
        gameRunning.setRoundsLimit(2);
        gameRunning.setPlayersCount(1);
        gameDao.save(gameRunning);

        Player playerPlaying = new Player("Initial playing player");
        playerPlaying.setGame(gameRunning);
        playerDao.save(playerPlaying);

        gameRunning.setPlayers(Collections.singletonList(playerPlaying));
        gameDao.save(gameRunning);
        //</editor-fold>

        //<editor-fold desc="Set finished game">
        gameFinished = new Game();
        gameFinished.setStatus(GameStatus.FINISHED);
        gameFinished.setGameKey("secret");
        gameFinished.setPlayers(Collections.emptyList());
        gameFinished.setPlayersLimit(2);
        gameFinished.setRoundsLimit(2);
        gameFinished.setPlayersCount(1);
        gameDao.save(gameFinished);

        Player playerWinner = new Player("Initial winner player");
        playerWinner.setGame(gameFinished);
        playerDao.save(playerWinner);

        gameFinished.setPlayers(Collections.singletonList(playerWinner));
        gameDao.save(gameFinished);
        //</editor-fold>
    }

    @Test
    public void testJoinGame() throws Exception {
        JoinGameStatus status = joinGameManager.joinGame("Rafael", gameWaiting.getId().toString());
        assertEquals(JoinGameStatus.SUCCESS, status);
    }

    @Test
    public void testJoinGameFull() throws Exception {
        JoinGameStatus status = joinGameManager.joinGame("Rafael1", gameWaiting.getId().toString());
        assertEquals(JoinGameStatus.SUCCESS, status);

        status = joinGameManager.joinGame("Rafael2", gameWaiting.getId().toString());
        assertEquals(JoinGameStatus.GAME_FULL, status);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testJoinGameDuplicatePlayers() throws Exception {
        JoinGameStatus status = joinGameManager.joinGame("Rafael", gameWaiting.getId().toString());
        assertEquals(JoinGameStatus.SUCCESS, status);

        joinGameManager.joinGame("Rafael", gameWaiting.getId().toString());
    }

    @Test
    public void testJoinRunningGame() throws Exception {
        JoinGameStatus status = joinGameManager.joinGame("Rafael", gameRunning.getId().toString());
        assertEquals(JoinGameStatus.GAME_WAS_NOT_ON_WAIT, status);
    }

    @Test
    public void testJoinFinishedGame() throws Exception {
        JoinGameStatus status = joinGameManager.joinGame("Rafael", gameFinished.getId().toString());
        assertEquals(JoinGameStatus.GAME_WAS_NOT_ON_WAIT, status);
    }

    @Test
    public void testJoinNonexistentGame() throws Exception {
        JoinGameStatus status = joinGameManager.joinGame("Rafael", "012345678901234567890123");
        assertEquals(JoinGameStatus.GAME_NOT_FOUND, status);
    }

}