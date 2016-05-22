package com.business;

import com.business.dao.GameDao;
import com.business.dao.PlayerDao;
import com.domain.*;
import org.bson.types.ObjectId;
import org.jboss.weld.exceptions.UnsupportedOperationException;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(CdiRunner.class)
public class GuessesManagerTest {

    @Mock
    @Produces
    private GameDao gameDaoMock;

    @Mock
    @Produces
    private PlayerDao playerDaoMock;

    @Mock
    @Produces
    private GuessProcessor guessProcessorMock;

    @Inject
    private GuessesManager guessesManager;

    @Test
    public void testGuessExact() throws Exception {
        Game game = new Game();
        game.setRound(0);
        game.setSecret("0011");
        game.setPlayersCount(2);
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setId(new ObjectId(new Date()));

        Player player = new Player();
        player.setGame(game);
        player.setRound(0);

        Guess outputGuess = new Guess();
        outputGuess.setCode("0011");
        outputGuess.setExact(4);
        outputGuess.setNear(0);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);
        when(guessProcessorMock.processGuess("0011", "0011")).thenReturn(outputGuess);

        Guess guess = guessesManager.guess("0011", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = new Guess();
        expected.setCode("0011");
        expected.setExact(4);
        expected.setNear(0);
        expected.setStatus(GuessStatus.SOLVED);

        assertEquals(expected, guess);
        assertEquals(GameStatus.SOLVED, game.getStatus());
    }

    @Test
    public void testWinningGuess() throws Exception {
        Game game = new Game();
        game.setRound(0);
        game.setSecret("0011");
        game.setPlayersCount(1);
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setId(new ObjectId(new Date()));

        Player player = new Player();
        player.setGame(game);
        player.setRound(0);

        Guess outputGuess = new Guess();
        outputGuess.setCode("0011");
        outputGuess.setExact(4);
        outputGuess.setNear(0);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);
        when(guessProcessorMock.processGuess("0011", "0011")).thenReturn(outputGuess);

        Guess guess = guessesManager.guess("0011", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = new Guess();
        expected.setCode("0011");
        expected.setExact(4);
        expected.setNear(0);
        expected.setStatus(GuessStatus.SOLVED);

        assertEquals(expected, guess);
        assertEquals(GameStatus.FINISHED, game.getStatus());
    }

    @Test
    public void testGuessNear() throws Exception {
        Game game = new Game();
        game.setRound(0);
        game.setSecret("0011");
        game.setPlayersCount(2);
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setId(new ObjectId(new Date()));

        Player player = new Player();
        player.setGame(game);
        player.setRound(0);

        Guess outputGuess = new Guess();
        outputGuess.setCode("0001");
        outputGuess.setExact(3);
        outputGuess.setNear(1);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);
        when(guessProcessorMock.processGuess("0001", "0011")).thenReturn(outputGuess);

        Guess guess = guessesManager.guess("0001", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = new Guess();
        expected.setCode("0001");
        expected.setExact(3);
        expected.setNear(1);
        expected.setStatus(GuessStatus.VALID_GUESS);

        assertEquals(expected, guess);
    }

    @Test
    public void testGuessSolvedGame() throws Exception {
        Game game = new Game();
        game.setRound(0);
        game.setSecret("0011");
        game.setPlayersCount(2);
        game.setStatus(GameStatus.SOLVED);
        game.setId(new ObjectId(new Date()));

        Player player = new Player();
        player.setGame(game);
        player.setRound(0);

        Guess outputGuess = new Guess();
        outputGuess.setCode("0001");
        outputGuess.setExact(3);
        outputGuess.setNear(1);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);
        when(guessProcessorMock.processGuess("0001", "0011")).thenReturn(outputGuess);

        Guess guess = guessesManager.guess("0001", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = new Guess();
        expected.setCode("0001");
        expected.setExact(3);
        expected.setNear(1);
        expected.setStatus(GuessStatus.VALID_GUESS);

        assertEquals(expected, guess);
    }

    @Test
    public void testGuessPlayerNotInTheGame() throws Exception {
        when(playerDaoMock.find("userName", "gameId")).thenReturn(null);

        Guess guess = guessesManager.guess("code", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = Guess.emptyGuess(GuessStatus.NOT_IN_THE_GAME);
        expected.setCode("code");

        assertEquals(expected, guess);
    }

    @Test
    public void testGuessGameIsWaitingForMore() throws Exception {
        Game game = new Game();
        game.setId(new ObjectId(new Date()));
        game.setStatus(GameStatus.WAITING);

        Player player = new Player();
        player.setGame(game);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);

        Guess guess = guessesManager.guess("code", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = Guess.emptyGuess(GuessStatus.GAME_IS_WAITING_FOR_MORE);
        expected.setCode("code");

        assertEquals(expected, guess);
    }

    @Test
    public void testGuessGameIsFinish() throws Exception {
        Game game = new Game();
        game.setId(new ObjectId(new Date()));
        game.setStatus(GameStatus.FINISHED);

        Player player = new Player();
        player.setGame(game);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);

        Guess guess = guessesManager.guess("code", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = Guess.emptyGuess(GuessStatus.GAME_HAS_ENDED);
        expected.setCode("code");

        assertEquals(expected, guess);
    }

    @Test
    public void testGuessBeforeOthers() throws Exception {
        Game game = new Game();
        game.setRound(0);
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setId(new ObjectId(new Date()));

        Player player = new Player();
        player.setGame(game);
        player.setRound(1);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);

        Guess guess = guessesManager.guess("code", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = Guess.emptyGuess(GuessStatus.WAITING_OTHER_PLAYERS_GUESSES);
        expected.setCode("code");

        assertEquals(expected, guess);
    }

    @Test
    public void testGuessInvalidGuess() throws Exception {
        Game game = new Game();
        game.setRound(0);
        game.setSecret("aaa");
        game.setStatus(GameStatus.MASTER_MINDING);
        game.setId(new ObjectId(new Date()));

        Player player = new Player();
        player.setGame(game);
        player.setRound(0);

        when(playerDaoMock.find("userName", "gameId")).thenReturn(player);

        Guess guess = guessesManager.guess("code", "userName", "gameId");

        verify(playerDaoMock, times(1)).find("userName", "gameId");

        Guess expected = Guess.emptyGuess(GuessStatus.INVALID_GUESS);
        expected.setCode("code");

        assertEquals(expected, guess);
    }

}