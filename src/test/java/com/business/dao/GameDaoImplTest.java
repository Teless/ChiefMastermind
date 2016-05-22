package com.business.dao;

import com.business.GameKeyGenerator;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Player;
import com.mock.DatastoreFactoryMock;
import com.mock.SystemPropertiesMock;
import org.bson.types.ObjectId;
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
@AdditionalClasses({DatastoreFactoryMock.class, SystemPropertiesMock.class})
public class GameDaoImplTest {

    @Inject
    private GameDaoImpl gameDao;

    @Inject
    private PlayerDaoImpl playerDao;

    @Inject
    private Datastore datastore;

    private Game gameWaiting;
    private Game gameRunning;
    private Game gameFinished;
    private Player playerWaiting;
    private Player playerPlaying;
    private Player playerWinner;

    @Before
    public void setUp() throws Exception {
        datastore.delete(datastore.createQuery(Game.class));
        datastore.delete(datastore.createQuery(Player.class));

        //<editor-fold desc="Set waiting game">
        gameWaiting = new Game();
        gameWaiting.setStatus(GameStatus.WAITING);
        gameWaiting.setGameKey("secret");
        gameWaiting.setPlayers(Collections.emptyList());
        gameWaiting.setPlayersLimit(4);
        gameWaiting.setRoundsLimit(2);
        gameDao.save(gameWaiting);

        playerWaiting = new Player("Initial waiting player");
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
        gameRunning.setPlayersLimit(4);
        gameRunning.setRoundsLimit(2);
        gameDao.save(gameRunning);

        playerPlaying = new Player("Initial playing player");
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
        gameFinished.setPlayersLimit(4);
        gameFinished.setRoundsLimit(2);
        gameDao.save(gameFinished);

        playerWinner = new Player("Initial winner player");
        playerWinner.setGame(gameFinished);
        playerDao.save(playerWinner);

        gameFinished.setPlayers(Collections.singletonList(playerWinner));
        gameDao.save(gameFinished);
        //</editor-fold>
    }

    @Test
    public void testList() throws Exception {
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }

    @Test
    public void testFind() throws Exception {
        Game gameFetched = gameDao.find(gameRunning.getId().toString());
        assertEquals(gameRunning, gameFetched);
    }

    @Test
    public void testSave() throws Exception {
        Game newGame = new Game();
        newGame.setStatus(GameStatus.MASTER_MINDING);
        newGame.setGameKey("secret");
        newGame.setPlayers(Collections.emptyList());

        ObjectId id = gameDao.save(newGame);
        newGame.setId(id);

        Game gameFetched = gameDao.find(id.toString());
        assertEquals(newGame, gameFetched);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished, newGame), gameDao.list());
    }

    @Test
    public void testTryToJoinWaitingGame() throws Exception {
        Player player = new Player("MasterMind");
        player.setGame(gameWaiting);
        playerDao.save(player);

        boolean joined = gameDao.tryToJoinGame(player, gameWaiting);
        assertTrue("The player did not joined the game", joined);

        gameWaiting.setPlayers(Arrays.asList(playerWaiting, player));
        gameWaiting.setPlayersCount(1);

        Game gameFetched = gameDao.find(gameWaiting.getId().toString());
        assertEquals(gameWaiting, gameFetched);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }

    @Test
    public void testTryToJoinRunningGame() throws Exception {
        Player player = new Player("MasterMind");
        player.setGame(gameRunning);
        playerDao.save(player);

        boolean joined = gameDao.tryToJoinGame(player, gameRunning);
        assertFalse("The player was not suppose to be able to join the game", joined);

        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }

    @Test
    public void testTryToJoinFinishedGame() throws Exception {
        Player player = new Player("MasterMind");
        player.setGame(gameFinished);
        playerDao.save(player);

        boolean joined = gameDao.tryToJoinGame(player, gameFinished);
        assertFalse("The player was not suppose to be able to join the game", joined);

        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTryToJoinGameWithoutSavingPlayer() throws Exception {
        Player player = new Player("MasterMind");
        player.setGame(gameWaiting);

        gameDao.tryToJoinGame(player, gameWaiting);
    }

    @Test
    public void testTryToJoinExceedLimit() throws Exception {
        Game newGame = new Game();
        newGame.setStatus(GameStatus.WAITING);
        newGame.setGameKey("secret");
        newGame.setPlayers(Collections.emptyList());
        newGame.setPlayersLimit(2);
        newGame.setRoundsLimit(2);
        gameDao.save(newGame);

        boolean firstJoinResult = gameDao.tryToJoinGame(playerWaiting, newGame);
        assertTrue(firstJoinResult);

        boolean secondJoinResult = gameDao.tryToJoinGame(playerPlaying, newGame);
        assertTrue(secondJoinResult);

        boolean thirdJoinResult = gameDao.tryToJoinGame(playerWinner, newGame);
        assertFalse(thirdJoinResult);
    }

    @Test
    public void testStartGameWaiting() throws Exception {
        boolean started = gameDao.startGame(gameWaiting.getGameKey(), gameWaiting.getId().toString());
        gameWaiting.setStatus(GameStatus.MASTER_MINDING);

        assertTrue("The game didn't start", started);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }

    @Test
    public void testStartGameRunning() throws Exception {
        boolean started = gameDao.startGame(gameRunning.getGameKey(), gameRunning.getId().toString());

        assertFalse("The game started", started);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }

    @Test
    public void testStartGameFinished() throws Exception {
        boolean started = gameDao.startGame(gameFinished.getGameKey(), gameFinished.getId().toString());

        assertFalse("The game started", started);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), gameDao.list());
    }
}