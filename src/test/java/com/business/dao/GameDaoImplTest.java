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

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
@AdditionalClasses({DatastoreFactory.class, SystemPropertiesMock.class})
public class GameDaoImplTest {

    @Inject
    private GameDaoImpl dao;

    @Inject
    private Datastore datastore;

    private ObjectId initialWaitingGameId;
    private ObjectId initialRunningGameId;
    private ObjectId initialFinishedGameId;

    @Before
    public void setUp() throws Exception {
        Query<Game> query = datastore.createQuery(Game.class);
        datastore.delete(query);

        User userWaiting = new User("Initial waiting user");
        Game gameWaiting = new Game();
        gameWaiting.setStatus(GameStatus.WAITING);
        gameWaiting.setGameKey(GameUtil.generateGamekey());
        gameWaiting.setUsers(Collections.singletonList(userWaiting));
        initialWaitingGameId = dao.save(gameWaiting);

        User userPlaying = new User("Initial playing user");
        Game gameRunning = new Game();
        gameRunning.setStatus(GameStatus.MASTER_MINDING);
        gameRunning.setGameKey(GameUtil.generateGamekey());
        gameRunning.setUsers(Collections.singletonList(userPlaying));
        initialRunningGameId = dao.save(gameRunning);

        User userWinner = new User("Initial winner user");
        Game gameFinished = new Game();
        gameFinished.setStatus(GameStatus.FINISHED);
        gameFinished.setGameKey(GameUtil.generateGamekey());
        gameFinished.setUsers(Collections.singletonList(userWinner));
        initialFinishedGameId = dao.save(gameFinished);
    }

    @Test
    public void testCreateGame() throws Exception {
        User user = new User("Rafael");

        Game game = new Game();
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setGameKey(GameUtil.generateGamekey());
        game.setUsers(Collections.singletonList(user));

        ObjectId id = dao.save(game);

        Game gameFetched = dao.find(id.toString());
        assertEquals(game, gameFetched);
        assertEquals(4, dao.list().size());
    }

    @Test
    public void testJoinWaitingGame() throws Exception {
        User user = new User("MasterMind");
    }

    @Test
    public void testJoinRunningGame() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testJoinFinishedGame() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testJoinGameUserNameConflict() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testStartGame() throws Exception {
        throw new UnsupportedOperationException();
    }
}