package com.business;

import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GameKeyGenerator {

    public String generateGameKey() {
        return new ObjectId().toString();
    }

}
