package com.controller;

import br.com.caelum.vraptor.*;
import com.business.GuessesManager;
import com.domain.Guess;

import javax.inject.Inject;

import static br.com.caelum.vraptor.view.Results.json;
import static com.business.ParametersValidator.*;

@Path("/game")
@Controller
public class GuessController {

    private final Result result;
    private final GuessesManager guessesManager;

    /**
     * @deprecated CDI
     */
    @Deprecated
    public GuessController() {
        this(null, null);
    }

    @Inject
    public GuessController(Result result, GuessesManager guessesManager) {
        this.result = result;
        this.guessesManager = guessesManager;
    }

    @Post
    @Consumes(value = "application/json")
    public void guess(String code, String userName, String gameId) {
        if (containsNullValue(code, userName, gameId) || !isGameIdValid(gameId)) {
            sendBadRequest(result);
        } else {
            Guess guess = guessesManager.guess(code, userName, gameId);

            result.use(json())
                    .withoutRoot()
                    .from(guess)
                    .recursive()
                    .serialize();
        }
    }

}
