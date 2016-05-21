package com.business.dao;

import com.domain.Game;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

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
    public boolean joinGame(String userName, String gameId) {
        return false;
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
