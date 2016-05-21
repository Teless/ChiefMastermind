package com.business.dao;

import com.domain.Game;
import com.domain.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface GameDao {

    ObjectId save(Game game);

    boolean joinGame(User user, String gameId);

    boolean startGame(String gameKey, String gameId);

    Game find(String id);

    List<Game> list();

}
