package com.business.dao;

import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Player;
import com.exception.NoResultFound;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.inject.Inject;

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
    public boolean tryToJoinGame(Player player, String gameId) throws NoResultFound {
        if (player.getId() == null) {
            throw new IllegalArgumentException("Player id was not defined");
        } else {
            Game game = find(gameId);

            if (game == null) {
                throw new NoResultFound("game", gameId);
            } else {
                return joinGame(player, game);
            }
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

        return datastore.update(query, updateOperation).getUpdatedCount() == 1;
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
