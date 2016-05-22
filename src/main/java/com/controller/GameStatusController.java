package com.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
import com.business.GameStatusManager;
import com.business.ParametersValidator;
import com.domain.Game;
import com.domain.StartGameStatus;

import javax.inject.Inject;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import static com.business.ParametersValidator.containsNullValue;
import static com.business.ParametersValidator.isGameIdValid;
import static com.business.ParametersValidator.sendBadRequest;

@Path("/game")
@Controller
public class GameStatusController {

    private final Result result;
    private final GameStatusManager gameStatusManager;

    /**
     * @deprecated CDI use
     */
    @Deprecated
    public GameStatusController() {
        this(null, null);
    }

    @Inject
    public GameStatusController(Result result, GameStatusManager gameStatusManager) {
        this.result = result;
        this.gameStatusManager = gameStatusManager;
    }

    // TODO: 5/22/16 display winner players
    @Get
    public void status(String gameId) {
        if (containsNullValue(gameId) || !isGameIdValid(gameId)) {
            ParametersValidator.sendBadRequest(result);
        } else {
            Game game = gameStatusManager.getGameStatus(gameId);

            result.use(json())
                    .withoutRoot()
                    .from(game)
                    .exclude("secret", "gameKey")
                    .recursive()
                    .serialize();
        }
    }

    @Post
    @Consumes(value = "application/json")
    public void start(String gameKey, String gameId) {
        if (containsNullValue(gameKey, gameId) || !isGameIdValid(gameId)) {
            sendBadRequest(result);
        } else {
            StartGameStatus startGameStatus = gameStatusManager.startGame(gameKey, gameId);

            result.use(json())
                    .withoutRoot()
                    .from(startGameStatus)
                    .serialize();
        }
    }

}
