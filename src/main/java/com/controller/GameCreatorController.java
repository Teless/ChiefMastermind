package com.controller;

import br.com.caelum.vraptor.*;
import com.business.GameCreator;
import com.business.JoinGameManager;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.JoinGameStatus;
import com.mongodb.DuplicateKeyException;

import javax.inject.Inject;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import static com.business.ParametersValidator.containsNullValue;
import static com.business.ParametersValidator.isGameIdValid;
import static com.business.ParametersValidator.sendBadRequest;

@Path("/game")
@Controller
public class GameCreatorController {

    private final Result result;
    private final GameCreator gameCreator;
    private final JoinGameManager joinGameManager;

    /**
     * @deprecated CDI
     */
    @Deprecated
    public GameCreatorController() {
        this(null, null, null);
    }

    @Inject
    public GameCreatorController(Result result, GameCreator gameCreator, JoinGameManager joinGameManager) {
        this.result = result;
        this.gameCreator = gameCreator;
        this.joinGameManager = joinGameManager;
    }

    @Post
    @Consumes(value = "application/json")
    public void quickGame(String userName) {
        if (userName == null) {
            sendBadRequest(result);
        } else {
            Game game = gameCreator.createQuickGame(userName);

            result.use(json())
                    .serializeNulls()
                    .withoutRoot()
                    .from(game)
                    .exclude("secret")
                    .recursive()
                    .serialize();
        }
    }

    @Post
    @Consumes(value = "application/json")
    public void quickMultiplayerGame(String userName) {
        if (userName == null) {
            sendBadRequest(result);
        } else {
            Game game = gameCreator.createQuickMultiplayerGame(userName);

            result.use(json())
                    .serializeNulls()
                    .withoutRoot()
                    .from(game)
                    .exclude("secret")
                    .recursive()
                    .serialize();
        }
    }

    @Post
    @Consumes(value = "application/json")
    public void newGame(String userName, int playersLimit, int roundsLimit, int positions,
            int secretSize, int colorsCount) {

        if (containsNullValue(userName, playersLimit, roundsLimit, positions,
                secretSize, colorsCount)) {
            sendBadRequest(result);
        } else {
            Game game = gameCreator.createNewGame(userName, playersLimit, roundsLimit,
                    positions, secretSize, colorsCount, GameStatus.WAITING);

            result.use(json())
                    .serializeNulls()
                    .withoutRoot()
                    .from(game)
                    .exclude("secret")
                    .recursive()
                    .serialize();
        }
    }

    @Post
    @Consumes(value = "application/json")
    public void join(String userName, String gameId) {
        if (containsNullValue(userName, gameId) || !isGameIdValid(gameId)) {
            sendBadRequest(result);
        } else {
            try {
                JoinGameStatus joinGameStatus = joinGameManager.joinGame(userName, gameId);

                result.use(json())
                        .withoutRoot()
                        .from(joinGameStatus)
                        .serialize();
            } catch (DuplicateKeyException e) {
                result.use(http()).setStatusCode(400);
                result.use(http()).body("There is another player with the same name in the game");
            }
        }
    }

}
