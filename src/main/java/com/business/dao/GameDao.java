package com.business.dao;

import com.domain.Game;
import org.bson.types.ObjectId;

import java.util.List;

public interface GameDao {

    ObjectId save(Game game);

    boolean joinGame(String userName, String gameId);

    boolean startGame(String gamekey, String gameId);

    Game find(String id);

    List<Game> list();

}
