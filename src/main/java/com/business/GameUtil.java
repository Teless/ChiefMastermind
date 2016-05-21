package com.business;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class GameUtil {

    private GameUtil() {

    }

    public static String generateGamekey() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
