package com.business.dao;

import com.domain.Game;
import com.domain.Player;

public interface GameDao extends Dao<Game> {

    boolean joinGame(Player user, String gameId);

    boolean startGame(String gameKey, String gameId);

    Game find(String id);

}
