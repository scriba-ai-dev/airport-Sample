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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class ExtractAirportsIntegrationTest {

    private static final String TEST_DATA_DIR = "src/airport";
    private static final String TEST_INPUT_FILE = TEST_DATA_DIR + "/airports.dat";
    private static final String TEST_SEQ_FILE = TEST_DATA_DIR + "/airports.seq";
    private static final String TEST_CSV_FILE = TEST_DATA_DIR + "/airports.csv";

    @BeforeEach
    void setUp() throws IOException {
        // Create test directories
        Files.createDirectories(Paths.get(TEST_DATA_DIR));
        
        // Create a minimal test airports.dat file with valid record structure (104 bytes each)
        String testRecord = String.format("%-4s%-30s%-30s%-20s%-1s%03d%06d%-1s%03d%06d",
            "LAX", "Los Angeles International", "Los Angeles", "USA", "+", 34, 30000, "-", 118, 15000);
        Files.write(Paths.get(TEST_INPUT_FILE), testRecord.getBytes(StandardCharsets.ISO_8859_1));
    }

    @AfterEach
    void cleanUp() throws IOException {
        // Clean up test files
        Files.deleteIfExists(Paths.get(TEST_INPUT_FILE));
        Files.deleteIfExists(Paths.get(TEST_SEQ_FILE));
        Files.deleteIfExists(Paths.get(TEST_CSV_FILE));
    }

    @Test
    void testMainExecutesWithoutError() {
        // Test that the main method can be invoked without throwing exceptions
        assertDoesNotThrow(() -> {
            ExtractAirports.main(new String[]{});
        });
    }

    @Test
    void testExtractorProcessesValidAirportData() throws IOException {
        // Execute the extractor
        ExtractAirports.main(new String[]{});
        
        // Verify output files are created
        assertTrue(Files.exists(Paths.get(TEST_SEQ_FILE)), "Sequential file should be created");
        assertTrue(Files.exists(Paths.get(TEST_CSV_FILE)), "CSV file should be created");
        
        // Verify sequential file has expected size (104 bytes per record)
        byte[] seqContent = Files.readAllBytes(Paths.get(TEST_SEQ_FILE));
        assertTrue(seqContent.length > 0, "Sequential file should not be empty");
        assertEquals(0, seqContent.length % 104, "Sequential file size should be multiple of 104 bytes");
        
        // Verify CSV file contains header and data
        String csvContent = Files.readString(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        assertTrue(csvContent.contains("code,name,city,country"), "CSV should contain expected header");
        assertTrue(csvContent.contains("LAX"), "CSV should contain test airport code");
    }

    @Test
    void testExtractorHandlesEmptyInputFile() throws IOException {
        // Create empty input file
        Files.write(Paths.get(TEST_INPUT_FILE), new byte[0]);
        
        // Execute the extractor
        assertDoesNotThrow(() -> {
            ExtractAirports.main(new String[]{});
        });
        
        // Verify empty output files are still created
        assertTrue(Files.exists(Paths.get(TEST_SEQ_FILE)), "Sequential file should be created even for empty input");
        assertTrue(Files.exists(Paths.get(TEST_CSV_FILE)), "CSV file should be created even for empty input");
        
        // Verify empty sequential file
        byte[] seqContent = Files.readAllBytes(Paths.get(TEST_SEQ_FILE));
        assertEquals(0, seqContent.length, "Sequential file should be empty for empty input");
        
        // Verify CSV contains only header
        String csvContent = Files.readString(Paths.get(TEST_CSV_FILE), StandardCharsets.UTF_8);
        String[] lines = csvContent.trim().split("\n");
        assertEquals(1, lines.length, "CSV should contain only header line for empty input");
    }
}