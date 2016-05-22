package com.controller;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Path("/")
public class GameController {

    private Result result;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    /**
     * @deprecated CDI use
     */
    @Deprecated
    public GameController() {
    }

    @Inject
    public GameController(Result result) {
        this.result = result;
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void defaultQuickGame(String user) {
        // TODO: 21-May-16 log
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void quickGame(String user, int roundsLimit, int numberOfPositions) {
        // TODO: 21-May-16 log
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void defaultNewGame(String user) {
        // TODO: 21-May-16 log
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void newGame(String user, int playerLimit, int roundsLimit, int numberOfPositions) {
        // TODO: 21-May-16 log
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void join(String user, String gameId) {
        // TODO: 21-May-16 log
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void gameStatus(String gameId) {
        // TODO: 21-May-16 log
        // TODO: 5/21/16 exclude secret and gamekey
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void start(String gamekey, String gameId) {
        // TODO: 21-May-16 log
    }

    @Post
    @Consumes(value = "application/json", options = WithoutRoot.class)
    public void guess(String code, String user, String gameId) {
        // TODO: 21-May-16 log
    }

}
