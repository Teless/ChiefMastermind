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
                    .append(gameKey, that.gameKey)
                    .append(status, that.status)
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
                .append("gameKey", gameKey)
                .append("status", status)
                .append("players", players)
                .toString();
    }
}
