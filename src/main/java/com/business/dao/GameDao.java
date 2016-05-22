package com.business.dao;

import com.domain.Game;
import com.domain.Player;

public interface GameDao extends Dao<Game> {

    boolean tryToJoinGame(Player player, Game game);

    boolean startGame(String gameKey, String gameId);

}
