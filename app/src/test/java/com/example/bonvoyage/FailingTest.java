package com.example.bonvoyage;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FailingTest {
    @Test
    public void testPass() {
        int result = 5 + 2 * 2;
        assertEquals(result, 15);
    }
}