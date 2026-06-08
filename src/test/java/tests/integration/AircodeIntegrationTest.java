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
import org.junit.jupiter.api.AfterEach;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class AircodeIntegrationTest {

    private Aircode aircode;
    private Airparams airparams;
    private Airrec airrec;
    private static final String TEST_AIRPORT_FILE = "src/airport/airports.seq";

    @BeforeEach
    void setUp() throws IOException {
        aircode = new Aircode();
        airparams = new Airparams();
        airrec = new Airrec();
        
        // Create test directory
        Files.createDirectories(Paths.get("src/airport"));
        
        // Create test airport data file with a few sample records (104 bytes each)
        StringBuilder testData = new StringBuilder();
        
        // LAX record
        String laxRecord = String.format("%-4s%-30s%-30s%-20s%-1s%03d%06d%-1s%03d%06d",
            "LAX", "Los Angeles International", "Los Angeles", "USA", "+", 34, 30000, "-", 118, 15000);
        testData.append(laxRecord);
        
        // JFK record  
        String jfkRecord = String.format("%-4s%-30s%-30s%-20s%-1s%03d%06d%-1s%03d%06d",
            "JFK", "John F Kennedy International", "New York", "USA", "+", 40, 38000, "-", 73, 47000);
        testData.append(jfkRecord);
        
        Files.write(Paths.get(TEST_AIRPORT_FILE), testData.toString().getBytes(StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_AIRPORT_FILE));
    }

    @Test
    void testAircodeEntryPointResolvesWithoutError() {
        // Test that Aircode.main() can be invoked without throwing exceptions
        assertDoesNotThrow(() -> {
            aircode.main("4", "", "", "", airrec, airparams, airparams, airparams);
        });
    }

    @Test
    void testOpenFileAndLookupAirport() {
        // Test successful airport file opening and lookup
        
        // First open the file
        aircode.main("4", "", "", "", airrec, airparams, airparams, airparams);
        assertEquals("00", airparams.getFileStatus(), "File should open successfully");
        
        // Then lookup an airport
        Airrec resultRecord = new Airrec();
        Airparams resultStatus = new Airparams();
        
        aircode.main("3", "LAX", "", "", resultRecord, airparams, airparams, resultStatus);
        
        // Verify airport was found
        assertEquals("LAX", resultRecord.getCode(), "Airport code should match");
        assertEquals("Los Angeles International", resultRecord.getName().trim(), "Airport name should match");
        
        // Close the file
        aircode.main("5", "", "", "", airrec, airparams, airparams, airparams);
    }

    @Test
    void testDistanceCalculationBetweenAirports() {
        // Test distance calculation functionality
        
        // Open file first
        aircode.main("4", "", "", "", airrec, airparams, airparams, airparams);
        
        // Calculate distance between LAX and JFK
        Airparams distanceResult = new Airparams();
        aircode.main("2", "LAX", "JFK", "", airrec, distanceResult, airparams, airparams);
        
        // Verify distance calculation was performed
        assertNotNull(distanceResult.getDistanceKm(), "Distance in km should be calculated");
        assertNotNull(distanceResult.getDistanceMiles(), "Distance in miles should be calculated");
        
        // Distance between LAX and JFK should be substantial (> 0)
        String kmStr = distanceResult.getDistanceKm();
        String milesStr = distanceResult.getDistanceMiles();
        
        if (kmStr != null && !kmStr.isEmpty()) {
            int kmDistance = Integer.parseInt(kmStr);
            assertTrue(kmDistance > 0, "Distance should be greater than 0");
        }
        
        // Close the file
        aircode.main("5", "", "", "", airrec, airparams, airparams, airparams);
    }
}