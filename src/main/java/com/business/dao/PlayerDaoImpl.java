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

    // TODO: 21-May-16 dont let player play before others
    // TODO: 21-May-16 dont let player play in a waiting game
    // TODO: 21-May-16 syncronize checks (bad sync) or query and count the players (index will help)
    // TODO: 21-May-16 check if guess is valid

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
