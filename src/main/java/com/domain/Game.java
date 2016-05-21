package com.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity("games")
public class Game {

    @Id
    private ObjectId id;
    private String gameKey;
    private GameStatus status;

    @Embedded
    private List<User> users;

    public ObjectId getId() {
        return id;
    }

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
                    .append(users, that.users)
                    .isEquals();
        }

        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        StandardToStringStyle style = new StandardToStringStyle();
        style.setUseClassName(false);
        style.setUseIdentityHashCode(false);

        return new ToStringBuilder(this)
                .append("id", id)
                .append("gameKey", gameKey)
                .append("status", status)
                .append("users", users)
                .toString();
    }
}
