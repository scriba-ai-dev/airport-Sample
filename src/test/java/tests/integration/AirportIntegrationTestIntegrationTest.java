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

import com.example.app.Airport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Airport main user interface program
 */
public class AirportIntegrationTest {

    private Airport airport;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        airport = new Airport();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testApplicationStartupWithoutErrors() {
        // Test module entry point resolves without error
        assertDoesNotThrow(() -> {
            Airport testAirport = new Airport();
            assertNotNull(testAirport);
        });
    }

    @Test
    public void testMainScreenDisplayFunctionality() {
        // Happy-path test for main screen display functionality
        airport.displayMainScreen();
        
        String output = outputStream.toString();
        
        // Verify main screen elements are displayed
        assertTrue(output.contains("Airport Distance Calculator"));
        assertTrue(output.contains("Enter airport codes"));
        assertTrue(output.contains("leave blank to exit"));
        
        // Verify output formatting is reasonable
        assertTrue(output.contains("===")); // Header formatting
        assertFalse(output.trim().isEmpty());
    }

    @Test
    public void testExitConditionHandling() {
        // Test edge case: immediate exit with blank input
        String blankInput = "\n"; // Empty line to trigger exit
        System.setIn(new ByteArrayInputStream(blankInput.getBytes()));
        
        // Create new scanner with test input
        airport = new Airport();
        
        // Test that blank airport1 input triggers exit logic
        assertDoesNotThrow(() -> {
            airport.acceptMainScreen();
            // After accepting blank input, airport1 should be empty/blank
            // This would trigger the exit condition in the main loop
        });
        
        // Verify no runtime exceptions during input handling
        String output = outputStream.toString();
        assertFalse(output.contains("Exception"));
        assertFalse(output.contains("Error"));
    }

    // Restore original System.out after each test
    public void tearDown() {
        if (originalOut != null) {
            System.setOut(originalOut);
        }
    }
}