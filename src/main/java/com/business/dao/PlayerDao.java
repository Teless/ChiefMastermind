package com.business.dao;

import com.domain.Guess;
import com.domain.Player;
import org.bson.types.ObjectId;

public interface PlayerDao extends Dao<Player> {

    boolean addGuess(Guess guess, ObjectId playerId, ObjectId gameId);

}
