package com.business;

import com.domain.Game;
import com.domain.Player;

import java.util.Collections;

public class GameManager {

    // TODO: 21-May-16 check if the player is joining a running or finished game
    // TODO: 21-May-16 catch duplicated key
    // TODO: 21-May-16 secrect code


    public void quickGame(String userName) {
        Player user = new Player(userName);

        Game game = new Game();
        game.setGameKey(GameUtil.generateGamekey());
        game.setPlayers(Collections.singletonList(user));
    }

    public void newGame(String user, int playerLimit, int roundLimit) {
        // TODO: 21-May-16 log
    }

    public void join(String user, String gameId) {
        // TODO: 21-May-16 log
    }

    public void start(String gamekey, String gameId) {
        // TODO: 21-May-16 log
    }

}
