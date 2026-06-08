/*
 * @scriba-ai-generated: true
 * @scriba-marker-version: 1
 * @scriba-source-language: cobol
 * @scriba-target-language: java
 * @scriba-conversion-id: proj-gafc
 * @scriba-timestamp: 2026-06-08T10:43:19.034Z
 * @scriba-platform-version: 0.1.0
 */
package integration;

import com.example.app.Airparams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Airparams DTO for function dispatch and data exchange
 */
public class AirparamsIntegrationTest {

    @Test
    public void testModuleInitializationWithoutErrors() {
        // Test module entry point resolves without error
        assertDoesNotThrow(() -> {
            Airparams params = new Airparams();
            assertNotNull(params);
            // Verify proper initialization
            assertNotNull(params.getMatchedCodes());
            assertEquals(10, params.getMatchedCodes().length);
        });
    }

    @Test
    public void testHappyPathFunctionDispatch() {
        // Happy-path call with plausible airport service inputs
        Airparams params = new Airparams();
        
        // Test distance calculation scenario
        params.setFunction(Airparams.GET_DISTANCE);
        params.setAirport1("KJFK");
        params.setAirport2("KLAX");
        params.setDistanceKm("04,149");
        params.setDistanceMiles("02,576");
        params.setFileStatus("00");
        
        // Verify function code checking methods work
        assertTrue(params.isGetDistance());
        assertFalse(params.isGetMatches());
        assertFalse(params.isOpenFile());
        
        // Verify data integrity
        assertEquals("KJFK", params.getAirport1());
        assertEquals("KLAX", params.getAirport2());
        assertEquals("04,149", params.getDistanceKm());
        assertEquals("02,576", params.getDistanceMiles());
        assertEquals("00", params.getFileStatus());
    }

    @Test
    public void testMatchedCodesArraySynchronization() {
        // Test edge case of matched codes array/string synchronization
        Airparams params = new Airparams();
        
        // Test setting individual matched codes
        String[] codes = new String[10];
        codes[0] = "KJFK - John F Kennedy Intl"; // 27 chars (under 35 limit)
        codes[1] = "KLAX - Los Angeles International Airport"; // 40 chars (over 35 limit)
        codes[2] = "KORD - Chicago O'Hare"; // 22 chars
        
        params.setMatchedCodes(codes);
        
        // Verify array was synchronized to raw string properly
        String rawArray = params.getMatchedCodesArray();
        assertNotNull(rawArray);
        assertEquals(350, rawArray.length()); // 10 entries * 35 chars each
        
        // Verify first entry is properly padded/truncated
        String firstEntry = rawArray.substring(0, 35);
        assertTrue(firstEntry.startsWith("KJFK - John F Kennedy Intl"));
        
        // Verify second entry is truncated to 35 chars
        String secondEntry = rawArray.substring(35, 70);
        assertEquals(35, secondEntry.length());
        assertTrue(secondEntry.startsWith("KLAX - Los Angeles International A")); // Truncated
        
        // Test reverse synchronization
        params.setMatchedCodesArray(rawArray);
        String[] retrievedCodes = params.getMatchedCodes();
        assertEquals(10, retrievedCodes.length);
        assertTrue(retrievedCodes[0].startsWith("KJFK"));
        assertTrue(retrievedCodes[1].startsWith("KLAX"));
    }
}