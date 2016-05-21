package com.business.dao;

import com.domain.Guess;
import com.domain.Player;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.inject.Inject;

public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

    @Inject
    public PlayerDaoImpl(Datastore datastore) {
        super(datastore);
    }

    @Override
    public boolean addGuess(Guess guess, ObjectId playerId, ObjectId gameId) {
        UpdateOperations<Player> updateOperation = datastore.createUpdateOperations(Player.class)
                .inc("round")
                .add("guesses", guess);

        Query<Player> query = datastore.createQuery(Player.class)
                .field("id").equal(playerId);

        return datastore.update(query, updateOperation).getUpdatedCount() == 1;
    }
}
