package com.business;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(CdiRunner.class)
public class GameKeyGeneratorTest {

    @Inject
    private GameKeyGenerator gameKeyGenerator;

    @Test
    public void generateGameKey() throws Exception {
        String secret = gameKeyGenerator.generateGameKey();
        assertEquals(24, secret.length());
    }

}