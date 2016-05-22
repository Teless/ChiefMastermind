package com.business;

import com.business.dao.GameDao;
import com.business.dao.PlayerDao;
import com.controller.GameController;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Player;
import com.exception.InvalidGameConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameCreator {

    private final GameDao gameDao;
    private final PlayerDao playerDao;
    private final SecretGenerator secretGenerator;
    private final GameKeyGenerator gameKeyGenerator;

    @Inject
    public GameCreator(GameDao gameDao, PlayerDao playerDao, SecretGenerator secretGenerator,
            GameKeyGenerator gameKeyGenerator) {

        this.gameDao = gameDao;
        this.playerDao = playerDao;
        this.secretGenerator = secretGenerator;
        this.gameKeyGenerator = gameKeyGenerator;
    }

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public Game createQuickGame(String userName) {
        return createNewGame(userName, 1,
                Game.DEFAULT_ROUNDS_LIMIT,
                Game.DEFAULT_POSITIONS,
                Game.DEFAULT_SECRET_SIZE,
                Game.DEFAULT_COLORS_COUNT,
                GameStatus.MASTER_MINDING);
    }

    public Game createQuickMultiplayerGame(String userName) {
        return createNewGame(userName,
                Game.DEFAULT_MULTI_PLAYER_LIMIT,
                Game.DEFAULT_ROUNDS_LIMIT,
                Game.DEFAULT_POSITIONS,
                Game.DEFAULT_SECRET_SIZE,
                Game.DEFAULT_COLORS_COUNT,
                GameStatus.WAITING);
    }

    public Game createNewGame(String userName, int playersLimit, int roundsLimit, int positions,
            int secretSize, int colorsCount, GameStatus status) {

        validateGameParameters(playersLimit, roundsLimit, positions, secretSize, colorsCount, status);

        Game game = new Game();
        game.setGameKey(gameKeyGenerator.generateGamekey());
        game.setPlayersLimit(playersLimit);
        game.setRoundsLimit(roundsLimit);
        game.setPositions(positions);
        game.setStatus(status);
        game.setPlayersCount(1);
        game.setSecret(secretGenerator.newSecret(colorsCount, secretSize));
        gameDao.save(game);

        Player player = new Player(userName);
        player.setGame(game);
        playerDao.save(player);

        game.setPlayersCount(1);
        game.setPlayers(Collections.singletonList(player));
        gameDao.save(game);

        logger.info("The user: {} created a game with the following configurations: {}", userName, game);
        // TODO: 5/21/16 get mongodb exceptions in interceptor

        return game;
    }

    // TODO: 5/22/16 add to documentation
    private void validateGameParameters(int playersLimit, int roundsLimit, int positions,
            int secretSize, int colorsCount, GameStatus status) {

        Map<String, String> invalidParameters = new HashMap<>();
        if (playersLimit <= 0) {
            invalidParameters.put("playersLimit", String.valueOf(playersLimit));
        }

        if (roundsLimit <= 0) {
            invalidParameters.put("roundsLimit", String.valueOf(roundsLimit));
        }

        if (positions <= 0) {
            invalidParameters.put("positions", String.valueOf(positions));
        }

        if (secretSize <= 0) {
            invalidParameters.put("secretSize", String.valueOf(secretSize));
        }

        if (colorsCount <= 0) {
            invalidParameters.put("colorsCount", String.valueOf(colorsCount));
        }

        if (status.equals(GameStatus.MASTER_MINDING) && playersLimit > 1
                || status.equals(GameStatus.FINISHED)) {
            invalidParameters.put("status", String.valueOf(status));
        }

        if (!invalidParameters.isEmpty()) {
            throw new InvalidGameConfigurationException(invalidParameters);
        }
    }

}
