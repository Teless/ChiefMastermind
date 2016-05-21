package com.business.dao;

import com.domain.Game;
import com.domain.GameStatus;
import com.domain.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

public class GameDaoImpl implements GameDao {

    private final Datastore datastore;

    /**
     * @deprecated CDI
     */
    @Deprecated
    public GameDaoImpl() {
        this(null);
    }

    @Inject
    public GameDaoImpl(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public ObjectId save(Game game) {
        Key<Game> key = datastore.save(game);

        return (ObjectId) key.getId();
    }

    @Override
    public boolean joinGame(User user, String gameId) {
        UpdateOperations<Game> updateOperation = datastore.createUpdateOperations(Game.class)
                .add("users", user, false);

        Query<Game> query = datastore.createQuery(Game.class)
                .field("id").equal(new ObjectId(gameId))
                .field("status").equal(GameStatus.WAITING);

        UpdateResults result = datastore.update(query, updateOperation);

        return result.getUpdatedCount() == 1;
    }

    @Override
    public boolean startGame(String gamekey, String gameId) {
        return false;
    }

    @Override
    public Game find(String id) {
        List<Game> games = datastore.createQuery(Game.class)
                .field("id").equal(new ObjectId(id))
                .asList();

        return games.isEmpty() ? null : games.get(0);
    }

    public List<Game> list() {
        return datastore.createQuery(Game.class).asList();
    }

}
