/*
 * @scriba-ai-generated: true
 * @scriba-marker-version: 1
 * @scriba-source-language: cobol
 * @scriba-target-language: java
 * @scriba-conversion-id: b3236a93-028f-4c27-993f-7ca9a852a453
 * @scriba-timestamp: 2026-06-08T08:15:54.486Z
 * @scriba-platform-version: 0.1.0
 */
package com.example.app;

import java.util.Scanner;

/**
 * Main application entry point with text UI for airport lookup and distance calculations
 */
public class Airport {

    private String userInput;
    private Airparams airparams;
    private Airrec airrec;
    private String fromToMsg;

    private Aircode aircodeService;

    /**
     * Main entry point
     */
    public static void main(String[] args) {
        Airport airport = new Airport();
        airport.run();
    }

    /**
     * Main program logic
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        this.aircodeService = new Aircode();

        // Initialize working storage
        this.airparams = new Airparams();
        this.airrec = new Airrec();
        this.fromToMsg = "";

        // Clear screen
        System.out.println("Starting airport application");

        // Open file
        airparams.setFunction(Airparams.OPEN_FILE);
        callAircodeProgram();

        if ("00".equals(airparams.getFileStatus())) {
            // Initialize display fields
            airparams.setAirport1("");
            airparams.setAirport2("");
            fromToMsg = "";
            airparams.setDistanceKm("0");
            airparams.setDistanceMiles("0");

            // Display initial screen
            displayMainScreen();

            // Main input loop
            while (true) {
                acceptMainScreen(scanner);

                if (airparams.getAirport1() == null || airparams.getAirport1().isBlank()) {
                    break;
                }

                if (airparams.getAirport2() != null && !airparams.getAirport2().isBlank()) {
                    // Calculate distance between two airports
                    airparams.setFunction(Airparams.GET_DISTANCE);
                    fromToMsg = "";
                    String airport1Clean = airparams.getAirport1().trim();
                    String airport2Clean = airparams.getAirport2().trim();
                    fromToMsg = airport1Clean + " -> " + airport2Clean;

                    callAircodeProgram();

                    if ("00".equals(airparams.getFileStatus())) {
                        airparams.setAirport1("");
                        airparams.setAirport2("");
                        displayMainScreen();
                    } else {
                        displayInvalidCode();
                    }
                } else {
                    // Single airport lookup
                    airrec.setCode(" ");
                    airparams.setFunction(Airparams.GET_DETAILS);
                    callAircodeProgram();

                    if (!" ".equals(airrec.getCode())) {
                        // Display airport details
                        displayAirportRecord();
                        airparams.setAirport1("");
                        airparams.setAirport2("");
                    } else {
                        // Search for matching airports by prefix
                        airparams.setMatchedCodesArray("");
                        airparams.setPrefixText(airparams.getAirport1());
                        airparams.setFunction(Airparams.GET_MATCHES);
                        callAircodeProgram();

                        if (airparams.getMatchedCodes() != null &&
                            airparams.getMatchedCodes().length > 0 &&
                            !airparams.getMatchedCodes()[0].isBlank()) {
                            displaySearchResults();
                        } else {
                            displayInvalidCode();
                        }
                    }
                }
            }

            // Close file
            airparams.setFunction(Airparams.CLOSE_FILE);
            callAircodeProgram();
        }
    }

    /**
     * Display invalid IATA code error message
     */
    private void displayInvalidCode() {
        System.out.println("Invalid IATA code");
    }

    /**
     * Call aircode service with current parameters
     */
    private void callAircodeProgram() {
        String[] fileStatus = new String[1];
        fileStatus[0] = airparams.getFileStatus();

        aircodeService.execute(
            airparams.getFunction(),
            airparams.getAirport1(),
            airparams.getAirport2(),
            airparams.getPrefixText(),
            airrec,
            airparams,
            airparams.getMatchedCodes(),
            fileStatus
        );

        airparams.setFileStatus(fileStatus[0]);
    }

    private void displayMainScreen() {
        System.out.println("=== Airport Distance Calculator ===");
        System.out.println("From Airport: " + (airparams.getAirport1() != null ? airparams.getAirport1() : ""));
        System.out.println("To Airport: " + (airparams.getAirport2() != null ? airparams.getAirport2() : ""));
        if (!fromToMsg.isEmpty()) {
            System.out.println("Route: " + fromToMsg);
            System.out.println("Distance: " + airparams.getDistanceKm() + " km, " + airparams.getDistanceMiles() + " miles");
        }
    }

    private void acceptMainScreen(Scanner scanner) {
        System.out.print("Enter from airport (or blank to exit): ");
        String input1 = scanner.nextLine();
        airparams.setAirport1(input1);

        if (input1 != null && !input1.isBlank()) {
            System.out.print("Enter to airport (optional): ");
            String input2 = scanner.nextLine();
            airparams.setAirport2(input2);
        }
    }

    private void displayAirportRecord() {
        System.out.println("=== Airport Details ===");
        System.out.println("Code: " + airrec.getCode());
        System.out.println("Name: " + airrec.getName());
        System.out.println("City: " + airrec.getCity());
        System.out.println("Country: " + airrec.getCountry());
        System.out.println("Coordinates: " + airrec.getLatSign() + airrec.getLatDegs() + "°" + airrec.getLatMins() + "' " +
                           airrec.getLongSign() + airrec.getLongDegs() + "°" + airrec.getLongMins() + "'");
    }

    private void displaySearchResults() {
        System.out.println("=== Search Results ===");
        System.out.println("Matching airports for prefix: " + airparams.getPrefixText());
        if (airparams.getMatchedCodes() != null) {
            for (int i = 0; i < airparams.getMatchedCodes().length; i++) {
                String code = airparams.getMatchedCodes()[i];
                if (code != null && !code.isBlank()) {
                    System.out.println("Match " + (i + 1) + ": " + code);
                }
            }
        }
    }
}