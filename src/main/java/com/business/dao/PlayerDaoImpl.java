package com.business.dao;

import com.domain.Guess;
import com.domain.Player;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.inject.Inject;
import java.util.List;

public class PlayerDaoImpl extends DaoImpl<Player> implements PlayerDao {

    @Inject
    public PlayerDaoImpl(Datastore datastore) {
        super(datastore);
    }

    @Override
    public Player find(String userName, String gameId) {
        DBRef gameRef = new DBRef("games", new ObjectId(gameId));

        List<Player> result = datastore.createQuery(Player.class)
                .field("name").equal(userName)
                .filter("game", gameRef)
                .asList();

        return result.isEmpty() ? null : result.get(0);
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
