package com.business;

import com.domain.Game;
import com.domain.User;

import java.util.Collections;

public class GameManager {

    public void quickGame(String userName) {
        User user = new User(userName);

        Game game = new Game();
        game.setGameKey(GameUtil.generateGamekey());
        game.setUsers(Collections.singletonList(user));

    }

    public void newGame(String userName) {

    }

}
