package com.business;

import com.business.dao.GameDao;
import com.business.dao.PlayerDao;
import com.domain.Game;
import com.domain.GameStatus;
import com.domain.Guess;
import com.domain.Player;
import com.exception.UnexpectedException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.domain.GuessStatus.*;

public class GuessesManager {

    private final GameDao gameDao;
    private final PlayerDao playerDao;
    private final GuessProcessor guessProcessor;
    private final LoadingCache<String, Mutex> lockPerGame;
    private static final Logger logger = LoggerFactory.getLogger(GuessesManager.class);

    @Inject
    public GuessesManager(GameDao gameDao, PlayerDao playerDao, GuessProcessor guessProcessor) {
        this.gameDao = gameDao;
        this.playerDao = playerDao;
        this.guessProcessor = guessProcessor;

        lockPerGame = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build(CacheLoader.from(Mutex::new));
    }

    public Guess guess(String code, String userName, String gameId) {
        Guess guess;

        Player player = playerDao.find(userName, gameId);
        if (player == null) {
            guess = Guess.emptyGuess(NOT_IN_THE_GAME);
            logger.warn("Player: {} tried to guess in the wrong game: {}", userName, gameId);
        } else {
            guess = guess(code, player);
        }

        guess.setCode(code);

        return guess;
    }

    private Guess guess(String code, Player player) {
        Guess guess;

        Game game = player.getGame();
        if (game.getStatus() == GameStatus.WAITING) {
            guess = Guess.emptyGuess(GAME_IS_WAITING_FOR_MORE);
            logger.warn("Player: {} tried to guess before the game ({}) started.",
                    player.getName(), game.getId().toString());

        } else if (player.getRound() != game.getRound()) {
            guess = Guess.emptyGuess(WAITING_OTHER_PLAYERS_GUESSES);
            logger.warn("Player: {} tried to guess before other players finished the round on game: {}",
                    player.getName(), game.getId().toString());

        } else if (!isCodeValid(code, game)) {
            guess = Guess.emptyGuess(INVALID_GUESS);
            logger.warn("Player: {} sent an invalid guess ({}) for game: {}", player.getName(), code,
                    game.getId().toString());

        } else {
            guess = processValidGuess(code, player, game);

        }

        return guess;
    }

    private Guess processValidGuess(String code, Player player, Game game) {
        Guess guess = guessProcessor.processGuess(code, game.getSecret());

        if (guess.getExact() == game.getSecret().length()) {
            // TODO: 5/22/16  mark in the game all that solved
            guess.setStatus(SOLVED);
            logger.info("Exact guess ({}) from player: {} on game: {}", code, player.getName(),
                    game.getId().toString());
        } else {
            guess.setStatus(VALID_GUESS);
            logger.info("Valid guess ({}) from player: {} on game: {}", code, player.getName(),
                    game.getId().toString());
        }

        updateGameRound(game);
        playerDao.addGuess(guess, player.getId(), game.getId());

        return guess;
    }

    private boolean isCodeValid(String code, Game game) {
        int expectedCodeSize = game.getSecret().length();
        String pattern = "[0-" + (expectedCodeSize - 1) + "]+";

        return code != null && code.length() == expectedCodeSize && code.matches(pattern);
    }

    private void updateGameRound(Game game) {
        try {
            Mutex mutex = lockPerGame.get(game.getId().toString());
            mutex.acquire();

            try {
                if (game.getRoundGuesses() == game.getPlayersCount() - 1) {
                    gameDao.incRound(game.getId());
                    gameDao.resetRoundGuesses(game.getId());
                } else {
                    gameDao.incRoundGuesses(game.getId());
                }
            } finally {
                mutex.release();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new UnexpectedException("Error while trying to update game round", e);
        }
    }

}
