package com.business;

import com.business.dao.GameDao;
import com.controller.GameController;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.StartGameStatus;
import com.exception.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class GameStatusManager {

    private final GameDao gameDao;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Inject
    public GameStatusManager(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Game getGameStatus(String gameId) {
        return gameDao.find(gameId);
    }

    public StartGameStatus startGame(String gameKey, String gameId) {
        boolean started = gameDao.startGame(gameKey, gameId);

        StartGameStatus status;
        if (started) {
            status = StartGameStatus.SUCCESS;
            logger.info("Game: {} started", gameId);
        } else {
            status = processStartGameStatusError(gameKey, gameId);
        }

        return status;
    }

    private StartGameStatus processStartGameStatusError(String gameKey, String gameId) {
        StartGameStatus status;

        Game game = gameDao.find(gameId);
        if (game == null) {
            status = StartGameStatus.GAME_NOT_FOUND;
            logger.warn("An attempt to start the game: {} (key: {}) was made, but the game couldn't be found",
                    gameId, gameKey);

        } else if (gameWasNotWaiting(game)) {
            status = StartGameStatus.GAME_WAS_NOT_ON_WAIT;
            logger.warn("An attempt to start the game: {} (key: {}) was made, but the game status was: {}",
                    gameId, gameKey, game.getStatus());

        } else if (!gameyKeyMatch(gameKey, game)) {
            status = StartGameStatus.WRONG_GAME_KEY;
            logger.warn("An attempt to start the game: {} (key: {}) was made, but the key was wrong", gameId, gameKey);

        } else {
            throw new UnexpectedException("For an unknown motive, it was not possible to start the game of id: "
                    + gameId + ", key: " + gameKey);
        }

        return status;
    }

    private boolean gameWasNotWaiting(Game game) {
        return !game.getStatus().equals(GameStatus.WAITING);
    }

    private boolean gameyKeyMatch(String gameKey, Game game) {
        return game.getGameKey().equals(gameKey);
    }

}
