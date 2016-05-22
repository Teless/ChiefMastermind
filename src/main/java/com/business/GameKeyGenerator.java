package com.business;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GameKeyGenerator {

    public String generateGamekey() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
