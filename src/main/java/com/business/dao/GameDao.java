package com.business.dao;

import com.domain.Game;
import com.domain.Player;
import com.exception.NoResultFound;

public interface GameDao extends Dao<Game> {

    /**
     * Try to include a player in a game
     *
     * @param player player
     * @param gameId game id
     * @return {@code true} if joined the game, {@code false} if the game capacity exceeded or if it was
     * not waiting for players
     * @throws NoResultFound throw in case the game does not exists
     */
    boolean tryToJoinGame(Player player, String gameId) throws NoResultFound;

    /**
     * Start a game
     *
     * @param gameKey key of the game (only the owner should know)
     * @param gameId  game id
     *
     * @return {@code true} if started, {@code false} if the game doesn't, if it was
     * not waiting for players or if the key is wrong
     */
    boolean startGame(String gameKey, String gameId);

}
