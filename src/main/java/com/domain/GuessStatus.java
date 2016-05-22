package com.domain;

public enum GuessStatus {

    VALID_GUESS,
    WAITING_OTHER_PLAYERS_GUESSES,
    NOT_IN_THE_GAME,
    INVALID_GUESS,
    GAME_IS_WAITING_FOR_MORE,
    GAME_HAS_ENDED,
    SOLVED;

}
