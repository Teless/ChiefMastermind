package com.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Guess {

    private short near;
    private short exact;
    private String guess;

    public short getNear() {
        return near;
    }

    public void setNear(short near) {
        this.near = near;
    }

    public short getExact() {
        return exact;
    }

    public void setExact(short exact) {
        this.exact = exact;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals;

        if (this == o) {
            equals = true;
        } else if (o == null || getClass() != o.getClass()) {
            equals = false;
        } else {
            Guess that = (Guess) o;

            equals = new EqualsBuilder()
                    .append(guess, that.guess)
                    .append(near, that.near)
                    .append(exact, that.exact)
                    .isEquals();
        }

        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(guess)
                .toHashCode();
    }

    @Override
    public String toString() {
        StandardToStringStyle style = new StandardToStringStyle();
        style.setUseClassName(false);
        style.setUseIdentityHashCode(false);

        return new ToStringBuilder(this, style)
                .append("near", near)
                .append("exact", exact)
                .append("guess", guess)
                .toString();
    }
}
