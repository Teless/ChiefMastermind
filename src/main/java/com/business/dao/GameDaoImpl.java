package com.business.dao;

import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Player;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.inject.Inject;
import java.util.List;

public class GameDaoImpl extends DaoImpl<Game> implements GameDao {

    /**
     * @deprecated CDI
     */
    @Deprecated
    public GameDaoImpl() {
        this(null);
    }

    @Inject
    public GameDaoImpl(Datastore datastore) {
        super(datastore);
    }

    @Override
    public boolean joinGame(Player player, String gameId) {
        if (player.getId() == null) {
            throw new IllegalArgumentException("Player id was not defined");
        } else {
            UpdateOperations<Game> updateOperation = datastore.createUpdateOperations(Game.class)
                    .add("players", player);

            Query<Game> query = datastore.createQuery(Game.class)
                    .field("id").equal(new ObjectId(gameId))
                    .field("status").equal(GameStatus.WAITING);

            return datastore.update(query, updateOperation).getUpdatedCount() == 1;
        }
    }

    @Override
    public boolean startGame(String gameKey, String gameId) {
        UpdateOperations<Game> updateOperation = datastore.createUpdateOperations(Game.class)
                .set("status", GameStatus.MASTER_MINDING);

        Query<Game> query = datastore.createQuery(Game.class)
                .field("gameKey").equal(gameKey)
                .field("id").equal(new ObjectId(gameId))
                .field("status").equal(GameStatus.WAITING);

        return datastore.update(query, updateOperation).getUpdatedCount() == 1;
    }

}
