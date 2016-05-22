package com.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

import java.util.List;

@Entity("players")
@Indexes({
        @Index(fields = {
                @Field(value = "name", type = IndexType.ASC),
                @Field(value = "game", type = IndexType.ASC)
        }, options = @IndexOptions(
                unique = true
        ))
})
public class Player implements MasterEntity {

    @Id
    private ObjectId id;
    private String name;
    private int round;

    @Reference
    private Game game;

    @Embedded
    private List<Guess> guesses;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals;

        if (this == o) {
            equals = true;
        } else if (o == null || getClass() != o.getClass()) {
            equals = false;
        } else {
            Player that = (Player) o;

            EqualsBuilder equalsBuilder = new EqualsBuilder()
                    .append(id, that.id)
                    .append(name, that.name)
                    .append(round, that.round)
                    .append(guesses, that.guesses);

            if (game == null || that.game == null) {
                equalsBuilder.append(game, that.game);
            } else {
                equalsBuilder.append(game.getId(), that.game.getId());
                equalsBuilder.append(game.getGameKey(), that.game.getGameKey());
            }

            equals = equalsBuilder.isEquals();
        }

        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        StandardToStringStyle style = new StandardToStringStyle();
        style.setUseClassName(false);
        style.setUseIdentityHashCode(false);

        ToStringBuilder toStringBuilder = new ToStringBuilder(this, style)
                .append("id", id)
                .append("name", name)
                .append("round", round)
                .append("guesses", guesses);

        if (game == null) {
            toStringBuilder.append("game", "null");
        } else {
            toStringBuilder.append("game", game.getGameKey());
        }

        return toStringBuilder.build();
    }
}
