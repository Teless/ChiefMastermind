package com.business;

import com.business.dao.GameDao;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.StartGameStatus;
import com.exception.UnexpectedException;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CdiRunner.class)
public class GameStatusManagerTest {

    @Mock
    @Produces
    private GameDao gameDaoMock;

    @Inject
    private GameStatusManager gameStatusManager;

    @Test
    public void getGameStatus() throws Exception {
        Game gameMock = mock(Game.class);
        when(gameDaoMock.find("id")).thenReturn(gameMock);

        Game game = gameStatusManager.getGameStatus("id");
        assertEquals(gameMock, game);
    }

    @Test
    public void startGame() throws Exception {
        when(gameDaoMock.startGame("key", "id")).thenReturn(true);

        StartGameStatus status = gameStatusManager.startGame("key", "id");

        assertEquals(StartGameStatus.SUCCESS, status);
    }

    @Test
    public void startGameNotFound() throws Exception {
        when(gameDaoMock.find("id")).thenReturn(null);
        when(gameDaoMock.startGame("key", "id")).thenReturn(false);

        StartGameStatus status = gameStatusManager.startGame("key", "id");

        assertEquals(StartGameStatus.GAME_NOT_FOUND, status);
    }

    @Test
    public void startGameRunning() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.MASTER_MINDING);

        when(gameDaoMock.find("id")).thenReturn(game);
        when(gameDaoMock.startGame("key", "id")).thenReturn(false);

        StartGameStatus status = gameStatusManager.startGame("key", "id");

        assertEquals(StartGameStatus.GAME_WAS_NOT_ON_WAIT, status);
    }

    @Test
    public void startGameFinished() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.FINISHED);

        when(gameDaoMock.find("id")).thenReturn(game);
        when(gameDaoMock.startGame("key", "id")).thenReturn(false);

        StartGameStatus status = gameStatusManager.startGame("key", "id");

        assertEquals(StartGameStatus.GAME_WAS_NOT_ON_WAIT, status);
    }

    @Test
    public void startGameWrongKey() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.WAITING);
        game.setGameKey("other key");

        when(gameDaoMock.find("id")).thenReturn(game);
        when(gameDaoMock.startGame("key", "id")).thenReturn(false);

        StartGameStatus status = gameStatusManager.startGame("key", "id");

        assertEquals(StartGameStatus.WRONG_GAME_KEY, status);
    }

    @Test(expected = UnexpectedException.class)
    public void startGameUnexpectedError() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.WAITING);
        game.setGameKey("key");

        when(gameDaoMock.find("id")).thenReturn(game);
        when(gameDaoMock.startGame("key", "id")).thenReturn(false);

        gameStatusManager.startGame("key", "id");
    }

}