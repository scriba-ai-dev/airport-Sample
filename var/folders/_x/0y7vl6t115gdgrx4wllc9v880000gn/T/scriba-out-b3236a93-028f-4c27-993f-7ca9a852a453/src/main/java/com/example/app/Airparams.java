/*
 * @scriba-ai-generated: true
 * @scriba-marker-version: 1
 * @scriba-source-language: cobol
 * @scriba-target-language: java
 * @scriba-conversion-id: proj-bn31
 * @scriba-timestamp: 2026-06-08T08:15:57.004Z
 * @scriba-platform-version: 0.1.0
 */
package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AirparamsIntegrationTest {

    @Test
    void shouldInstantiateWithoutError() {
        // Test that the module entry point resolves without error
        Airparams airparams = new Airparams();
        assertNotNull(airparams);
    }

    @Test
    void shouldSynchronizeMatchedCodesArrayAndIndividualCodes() {
        // Test happy path with matched codes array synchronization
        Airparams airparams = new Airparams();
        String testArray = "LAX - Los Angeles International   JFK - John F Kennedy Intl         SFO - San Francisco Intl          DFW - Dallas/Fort Worth Intl       ORD - Chicago O'Hare Intl          ATL - Hartsfield Jackson Atlanta  MIA - Miami International          SEA - Seattle-Tacoma Intl          DEN - Denver International         BOS - Logan International          ";
        
        airparams.setMatchedCodesArray(testArray);
        
        assertNotNull(airparams.getMatchedCodes());
        assertEquals(10, airparams.getMatchedCodes().length);
        assertTrue(airparams.getMatchedCodes()[0].startsWith("LAX"));
        assertTrue(airparams.getMatchedCodes()[1].startsWith("JFK"));
        assertEquals(testArray, airparams.getMatchedCodesArray());
    }

    @Test
    void shouldHandleInvalidMatchedCodesArrayLength() {
        // Test boundary condition with invalid array length
        Airparams airparams = new Airparams();
        String shortArray = "LAX - Los Angeles";
        
        airparams.setMatchedCodesArray(shortArray);
        
        // Should not crash and matched codes should remain null
        assertNull(airparams.getMatchedCodes());
        assertEquals(shortArray, airparams.getMatchedCodesArray());
    }

    @Test
    void shouldSynchronizeIndividualCodesToArray() {
        // Test reverse synchronization from individual codes to array
        Airparams airparams = new Airparams();
        String[] codes = new String[10];
        codes[0] = "LAX - Los Angeles International";
        codes[1] = "JFK - John F Kennedy Intl";
        for (int i = 2; i < 10; i++) {
            codes[i] = "";
        }
        
        airparams.setMatchedCodes(codes);
        
        assertNotNull(airparams.getMatchedCodesArray());
        assertEquals(350, airparams.getMatchedCodesArray().length());
        assertTrue(airparams.getMatchedCodesArray().startsWith("LAX - Los Angeles International"));
    }

    @Test
    void shouldHandleDistanceResultOperations() {
        // Test distance result nested class operations
        Airparams airparams = new Airparams();
        
        airparams.setDistanceKm("1,234");
        airparams.setDistanceMiles("767");
        
        assertNotNull(airparams.getDistanceResult());
        assertEquals("1,234", airparams.getDistanceKm());
        assertEquals("767", airparams.getDistanceMiles());
        assertEquals("1,234", airparams.getDistanceResult().getDistanceKm());
        assertEquals("767", airparams.getDistanceResult().getDistanceMiles());
    }
}