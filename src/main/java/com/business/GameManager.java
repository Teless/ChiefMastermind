package com.business;

import com.domain.Game;
import com.domain.Player;

import java.util.Collections;

public class GameManager {

    // TODO: 21-May-16 check if the player is joining a running or finished game

    public void quickGame(String userName) {
        Player user = new Player(userName);

        Game game = new Game();
        game.setGameKey(GameUtil.generateGamekey());
        game.setPlayers(Collections.singletonList(user));

    }

    public void newGame(String userName) {

    }

}
