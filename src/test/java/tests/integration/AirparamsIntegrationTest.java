/*
 * @scriba-ai-generated: true
 * @scriba-marker-version: 1
 * @scriba-source-language: cobol
 * @scriba-target-language: java
 * @scriba-conversion-id: proj-e473
 * @scriba-timestamp: 2026-06-08T11:34:25.158Z
 * @scriba-platform-version: 0.1.0
 */
package com.example.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AirparamsIntegrationTest {

    private Airparams airparams;

    @BeforeEach
    void setUp() {
        airparams = new Airparams();
    }

    @Test
    void testAirparamsInstantiationSucceeds() {
        // Test that Airparams can be instantiated without error
        assertDoesNotThrow(() -> {
            Airparams params = new Airparams();
        });
        
        // Verify initial state
        assertNotNull(airparams);
        assertNull(airparams.getFunction());
        assertNull(airparams.getAirport1());
        assertNull(airparams.getAirport2());
    }

    @Test
    void testFunctionCodesAndValidation() {
        // Test setting each function code and validation methods
        airparams.setFunction("1");
        assertTrue(airparams.isGetMatches());
        assertFalse(airparams.isGetDistance());
        
        airparams.setFunction("2");
        assertTrue(airparams.isGetDistance());
        assertFalse(airparams.isGetMatches());
        
        airparams.setFunction("3");
        assertTrue(airparams.isGetDetails());
        
        airparams.setFunction("4");
        assertTrue(airparams.isOpenFile());
        
        airparams.setFunction("5");
        assertTrue(airparams.isCloseFile());
        
        airparams.setFunction("6");
        assertTrue(airparams.isDisplayRecord());
        
        // Test invalid function code
        airparams.setFunction("9");
        assertFalse(airparams.isGetMatches());
        assertFalse(airparams.isGetDistance());
    }

    @Test
    void testMatchedCodesArraySynchronization() {
        // Test the bidirectional synchronization between matched codes array and individual codes
        String[] testCodes = {"LAX - Los Angeles Intl", "LHR - London Heathrow", "JFK - John F Kennedy"};
        airparams.setMatchedCodes(testCodes);
        
        // Verify array was set correctly
        assertNotNull(airparams.getMatchedCodes());
        assertEquals("LAX - Los Angeles Intl", airparams.getMatchedCodes()[0]);
        
        // Verify raw array was updated
        String rawArray = airparams.getMatchedCodesArray();
        assertNotNull(rawArray);
        assertEquals(350, rawArray.length(), "Raw array should be exactly 350 characters");
        assertTrue(rawArray.startsWith("LAX - Los Angeles Intl"), "Raw array should start with first entry");
        
        // Test setting raw array and verifying codes are updated
        StringBuilder testRawArray = new StringBuilder(350);
        for (int i = 0; i < 10; i++) {
            String entry = (i < 3) ? testCodes[i] : "";
            testRawArray.append(String.format("%-35s", entry));
        }
        
        airparams.setMatchedCodesArray(testRawArray.toString());
        assertNotNull(airparams.getMatchedCodes());
        assertEquals(10, airparams.getMatchedCodes().length);
        assertEquals("LAX - Los Angeles Intl          ", airparams.getMatchedCodes()[0]);
    }
}