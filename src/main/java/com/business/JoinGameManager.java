package com.business;

import com.business.dao.GameDao;
import com.business.dao.PlayerDao;
import com.controller.GameStatusController;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.JoinGameStatus;
import com.domain.Player;
import com.exception.UnexpectedException;
import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class JoinGameManager {

    private final GameDao gameDao;
    private final PlayerDao playerDao;

    private static final Logger logger = LoggerFactory.getLogger(GameStatusController.class);

    @Inject
    public JoinGameManager(GameDao gameDao, PlayerDao playerDao) {
        this.gameDao = gameDao;
        this.playerDao = playerDao;
    }

    // TODO: 5/22/16 add to documentation
    public JoinGameStatus joinGame(String userName, String gameId) throws DuplicateKeyException {
        Game game = gameDao.find(gameId);

        JoinGameStatus status;
        if (game == null) {
            status = JoinGameStatus.GAME_NOT_FOUND;
        } else {
            Player player = new Player(userName);
            player.setGame(game);
            playerDao.save(player);

            boolean joined = gameDao.tryToJoinGame(player, game);
            if (joined) {
                status = JoinGameStatus.SUCCESS;
                logger.info("User: {} joined the game: {}", userName, gameId);
            } else {
                status = processJoinGameStatusError(userName, gameId);
                playerDao.remove(player.getId());
            }
        }

        return status;
    }

    private JoinGameStatus processJoinGameStatusError(String userName, String gameId) {
        JoinGameStatus status;

        Game game = gameDao.find(gameId);
        if (game == null) {
            status = JoinGameStatus.GAME_NOT_FOUND;
            logger.warn("User: {} tried to enter a game ({}) that couldn't be found", userName, gameId);
        } else if (!game.getStatus().equals(GameStatus.WAITING)) {
            status = JoinGameStatus.GAME_WAS_NOT_ON_WAIT;
            logger.warn("User: {} tried to enter the game: {} but the game's status was: ",
                    userName, gameId, game.getStatus());
        } else if (game.getPlayersCount() >= game.getPlayersLimit()) {
            status = JoinGameStatus.GAME_FULL;
            logger.warn("User: {} tried to enter the game: {} but it was full", userName, gameId);
        } else {
            throw new UnexpectedException("For an unknown motive, it was not possible to let the player: "
                    + userName + " to join the game of id: " + gameId);
        }

        return status;
    }

}
