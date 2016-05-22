package com.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.List;

@Entity("games")
public class Game implements MasterEntity {

    @Id
    private ObjectId id;
    private String gameKey;
    private GameStatus status;
    private int playersLimit;
    private int playersCount;
    private int roundsLimit;
    private int round;
    private int roundGuesses;
    private int positions;
    private String secret;

    public static final int DEFAULT_MULTI_PLAYER_LIMIT = 2;
    public static final int DEFAULT_ROUNDS_LIMIT = 8;
    public static final int DEFAULT_POSITIONS = 8;
    public static final int DEFAULT_SECRET_SIZE = 8;
    public static final int DEFAULT_COLORS_COUNT = 8;

    @Reference
    private List<Player> players;

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getGameKey() {
        return gameKey;
    }

    public void setGameKey(String gameKey) {
        this.gameKey = gameKey;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getPlayersLimit() {
        return playersLimit;
    }

    public void setPlayersLimit(int playersLimit) {
        this.playersLimit = playersLimit;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public int getRoundsLimit() {
        return roundsLimit;
    }

    public void setRoundsLimit(int roundsLimit) {
        this.roundsLimit = roundsLimit;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void incRound() {
        ++round;
    }

    public int getRoundGuesses() {
        return roundGuesses;
    }

    public void setRoundGuesses(int roundGuesses) {
        this.roundGuesses = roundGuesses;
    }

    public void incRoundGuesses() {
        ++roundGuesses;
    }

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals;

        if (this == o) {
            equals = true;
        } else if (o == null || getClass() != o.getClass()) {
            equals = false;
        } else {
            Game that = (Game) o;

            equals = new EqualsBuilder()
                    .append(id, that.id)
                    .append(status, that.status)
                    .append(playersLimit, that.playersLimit)
                    .append(playersCount, that.playersCount)
                    .append(roundsLimit, that.roundsLimit)
                    .append(round, that.round)
                    .append(roundGuesses, that.roundGuesses)
                    .append(positions, that.positions)
                    .append(secret, that.secret)
                    .append(gameKey, that.gameKey)
                    .append(players, that.players)
                    .isEquals();
        }

        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(gameKey)
                .toHashCode();
    }

    @Override
    public String toString() {
        StandardToStringStyle style = new StandardToStringStyle();
        style.setUseClassName(false);
        style.setUseIdentityHashCode(false);

        return new ToStringBuilder(this, style)
                .append("id", id)
                .append("status", status)
                .append("playersLimit", playersLimit)
                .append("playersCount", playersCount)
                .append("roundsLimit", roundsLimit)
                .append("round", round)
                .append("roundGuesses", roundGuesses)
                .append("positions", positions)
                .append("secret", secret)
                .append("gameKey", gameKey)
                .append("players", players)
                .toString();
    }
}
