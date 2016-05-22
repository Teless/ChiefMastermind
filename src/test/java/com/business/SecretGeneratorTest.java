package com.business;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(CdiRunner.class)
public class SecretGeneratorTest {

    @Inject
    private SecretGenerator secretGenerator;

    @Test
    public void testNewSecret() throws Exception {
        String result = secretGenerator.newSecret(4, 3);

        assertEquals(3, result.length());
        assertTrue(result.matches("[0-3]+"));
    }

}