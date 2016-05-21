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
    private int maxRounds;
    private int round;

    // TODO: 21-May-16 generate pattern
    // TODO: 21-May-16 set default value for maxRounds

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

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
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
                    .append(maxRounds, that.maxRounds)
                    .append(round, that.round)
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
                .append("maxRounds", maxRounds)
                .append("round", round)
                .append("gameKey", gameKey)
                .append("players", players)
                .toString();
    }
}
