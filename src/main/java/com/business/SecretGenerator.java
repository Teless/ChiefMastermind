package com.business;

import java.util.Random;

public class SecretGenerator {

    public String newSecret(int totalColor, int size) {
        Random random = new Random();

        char[] secret = new char[size];
        for (int i = 0; i < size; i++) {
            secret[i] = Character.forDigit(random.nextInt(totalColor), 10);
        }

        return String.valueOf(secret);
    }

}
