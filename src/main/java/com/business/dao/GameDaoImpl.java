package com.business.dao;

import com.domain.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

public class GameDaoImpl extends DaoImpl<Game> implements GameDao {

    @Inject
    public GameDaoImpl(Datastore datastore) {
        super(datastore);
    }

    @Override
    public boolean tryToJoinGame(Player player, Game game) {
        if (player.getId() == null) {
            throw new IllegalArgumentException("Player id was not defined");
        } else {
            return joinGame(player, game);
        }
    }

    private boolean joinGame(Player player, Game game) {
        UpdateOperations<Game> updateOperation = datastore.createUpdateOperations(Game.class)
                .inc("playersCount")
                .add("players", player);

        Query<Game> query = datastore.createQuery(Game.class)
                .field("playersCount").lessThan(game.getPlayersLimit())
                .field("id").equal(game.getId())
                .field("status").equal(GameStatus.WAITING);

        UpdateResults updateResults = datastore.update(query, updateOperation);
        return updateResults.getUpdatedCount() == 1;
    }

    @Override
    public boolean startGame(String gameKey, String gameId) {
        UpdateOperations<Game> updateOperation = datastore.createUpdateOperations(Game.class)
                .set("status", GameStatus.MASTER_MINDING);

        Query<Game> query = datastore.createQuery(Game.class)
                .field("gameKey").equal(gameKey)
                .field("id").equal(new ObjectId(gameId))
                .field("status").equal(GameStatus.WAITING);

        UpdateResults updateResults = datastore.update(query, updateOperation);
        return updateResults.getUpdatedCount() == 1;
    }


}
