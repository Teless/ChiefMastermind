package com.business;

import com.domain.Guess;

import java.util.Arrays;

public class GuessProcessor {

    private static final char VISITED_CHARACTER = 'v';

    public Guess processGuess(String code, String secret) {
        int[] charsCount = new int[secret.length()];
        Arrays.fill(charsCount, 0);

        for (int i = 0; i < secret.length(); i++) {
            ++charsCount[secret.charAt(i) - '0'];
        }

        int exact = 0;
        int near = 0;
        char[] codeChars = code.toCharArray();
        char[] secretChars = secret.toCharArray();
        for (int i = 0; i < code.length(); i++) {
            int convertedIndex = codeChars[i] - '0';
            if (codeChars[i] == secretChars[i]) {
                ++exact;
                codeChars[i] = VISITED_CHARACTER;

                //Reduce the number os occurrences of the char, avoiding that a char
                //will be counted as an exact hit and near
                --charsCount[convertedIndex];
            }
        }

        for (int i = 0; i < code.length(); i++) {
            if (codeChars[i] != VISITED_CHARACTER) {
                int convertedIndex = codeChars[i] - '0';
                if ( charsCount[convertedIndex] > 0) {
                    ++near;
                    --charsCount[convertedIndex];
                }
            }
        }

        Guess guess = new Guess();
        guess.setCode(code);
        guess.setExact(exact);
        guess.setNear(near);

        return guess;
    }

}
