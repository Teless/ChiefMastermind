package com.business.dao;

import com.business.GameUtil;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.User;
import com.mock.SystemPropertiesMock;
import org.bson.types.ObjectId;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(CdiRunner.class)
@AdditionalClasses({DatastoreFactory.class, SystemPropertiesMock.class})
public class GameDaoImplTest {

    @Inject
    private GameDaoImpl dao;

    @Inject
    private Datastore datastore;

    private Game gameWaiting;
    private Game gameRunning;
    private Game gameFinished;

    @Before
    public void setUp() throws Exception {
        Query<Game> query = datastore.createQuery(Game.class);
        datastore.delete(query);

        User userWaiting = new User("Initial waiting user");
        gameWaiting = new Game();
        gameWaiting.setStatus(GameStatus.WAITING);
        gameWaiting.setGameKey(GameUtil.generateGamekey());
        gameWaiting.setUsers(Collections.singletonList(userWaiting));
        gameWaiting.setId(dao.save(gameWaiting));

        User userPlaying = new User("Initial playing user");
        gameRunning = new Game();
        gameRunning.setStatus(GameStatus.MASTER_MINDING);
        gameRunning.setGameKey(GameUtil.generateGamekey());
        gameRunning.setUsers(Collections.singletonList(userPlaying));
        gameRunning.setId(dao.save(gameRunning));

        User userWinner = new User("Initial winner user");
        gameFinished = new Game();
        gameFinished.setStatus(GameStatus.FINISHED);
        gameFinished.setGameKey(GameUtil.generateGamekey());
        gameFinished.setUsers(Collections.singletonList(userWinner));
        gameFinished.setId(dao.save(gameFinished));
    }

    @Test
    public void testCreateGame() throws Exception {
        User user = new User("Rafael");

        Game newGame = new Game();
        newGame.setStatus(GameStatus.MASTER_MINDING);
        newGame.setGameKey(GameUtil.generateGamekey());
        newGame.setUsers(Collections.singletonList(user));

        ObjectId id = dao.save(newGame);
        newGame.setId(id);

        Game gameFetched = dao.find(id.toString());
        assertEquals(newGame, gameFetched);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished, newGame), dao.list());
    }

    @Test
    public void testJoinWaitingGame() throws Exception {
        User user = new User("MasterMind");

        boolean joinned = dao.joinGame(user, gameWaiting.getId().toString());
        assertTrue("The user did not joined the game", joinned);

        gameWaiting.setUsers(Arrays.asList(
                new User("Initial waiting user"),
                new User("MasterMind")
        ));

        Game gameFetched = dao.find(gameWaiting.getId().toString());
        assertEquals(gameWaiting, gameFetched);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }

    @Test
    public void testJoinRunningGame() throws Exception {
        User user = new User("MasterMind");

        boolean joinned = dao.joinGame(user, gameRunning.getId().toString());
        assertFalse("The user was not suppose to be able to join the game", joinned);

        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }

    @Test
    public void testJoinFinishedGame() throws Exception {
        User user = new User("MasterMind");

        boolean joinned = dao.joinGame(user, gameFinished.getId().toString());
        assertFalse("The user was not suppose to be able to join the game", joinned);

        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }

    @Test
    public void testJoinGameUserNameConflict() throws Exception {
        User user = new User("Initial waiting user");

        boolean joinned = dao.joinGame(user, gameWaiting.getId().toString());
        assertFalse("The user was not suppose to be able to join the game", joinned);

        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }

    @Test
    public void testStartGameWaiting() throws Exception {
        boolean started = dao.startGame(gameWaiting.getGameKey(), gameWaiting.getId().toString());
        gameWaiting.setStatus(GameStatus.MASTER_MINDING);

        assertTrue("The game didn't start", started);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }

    @Test
    public void testStartGameRunning() throws Exception {
        boolean started = dao.startGame(gameRunning.getGameKey(), gameRunning.getId().toString());

        assertFalse("The game started", started);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }

    @Test
    public void testStartGameFinished() throws Exception {
        boolean started = dao.startGame(gameFinished.getGameKey(), gameFinished.getId().toString());

        assertFalse("The game started", started);
        assertEquals(Arrays.asList(gameWaiting, gameRunning, gameFinished), dao.list());
    }
}