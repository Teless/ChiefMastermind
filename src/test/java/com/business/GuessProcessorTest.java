package com.business;

import com.domain.Guess;
import com.domain.GuessStatus;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(CdiRunner.class)
public class GuessProcessorTest {

    @Inject
    private GuessProcessor guessProcessor;

    @Test
    public void testProcessGuessNoMatch() throws Exception {
        Guess guess = guessProcessor.processGuess("0011", "2222");

        Guess expected = new Guess();
        expected.setNear(0);
        expected.setExact(0);
        expected.setCode("0011");

        assertEquals(expected, guess);
    }

    @Test
    public void testProcessGuessSomeNear() throws Exception {
        Guess guess = guessProcessor.processGuess("0011", "1222");

        Guess expected = new Guess();
        expected.setNear(1);
        expected.setExact(0);
        expected.setCode("0011");

        assertEquals(expected, guess);
    }

    @Test
    public void testProcessGuessSomeExact() throws Exception {
        Guess guess = guessProcessor.processGuess("0011", "2012");

        Guess expected = new Guess();
        expected.setNear(0);
        expected.setExact(2);
        expected.setCode("0011");

        assertEquals(expected, guess);
    }

    @Test
    public void testProcessGuessSomeNearAndExact() throws Exception {
        Guess guess = guessProcessor.processGuess("11233525", "12345252");

        Guess expected = new Guess();
        expected.setNear(5);
        expected.setExact(1);
        expected.setCode("11233525");

        assertEquals(expected, guess);
    }

    @Test
    public void testProcessGuessSomeNearAndExact2() throws Exception {
        Guess guess = guessProcessor.processGuess("0001", "0011");

        Guess expected = new Guess();
        expected.setNear(0);
        expected.setExact(3);
        expected.setCode("0001");

        assertEquals(expected, guess);
    }

    @Test
    public void testProcessGuessSomeMatch() throws Exception {
        Guess guess = guessProcessor.processGuess("3321", "3321");

        Guess expected = new Guess();
        expected.setNear(0);
        expected.setExact(4);
        expected.setCode("3321");

        assertEquals(expected, guess);
    }

}