package com.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Guess {

    private int near;
    private int exact;
    private String code;
    private GuessStatus status;

    public static Guess emptyGuess(GuessStatus status) {
        Guess guess = new Guess();
        guess.setStatus(status);

        return guess;
    }

    public int getNear() {
        return near;
    }

    public void setNear(int near) {
        this.near = near;
    }

    public int getExact() {
        return exact;
    }

    public void setExact(int exact) {
        this.exact = exact;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GuessStatus getStatus() {
        return status;
    }

    public void setStatus(GuessStatus status) {
        this.status = status;
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
                    .append(code, that.code)
                    .append(near, that.near)
                    .append(exact, that.exact)
                    .append(status, that.status)
                    .isEquals();
        }

        return equals;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
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
                .append("code", code)
                .append("status", status)
                .toString();
    }
}
