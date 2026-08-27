/*
 * @scriba-ai-generated: true
 * @scriba-marker-version: 1
 * @scriba-source-language: cobol
 * @scriba-target-language: java
 * @scriba-conversion-id: 7620ea58-b930-4cbd-936f-465f2eb2cc25
 * @scriba-timestamp: 2026-06-08T11:34:22.782Z
 * @scriba-platform-version: 0.1.0
 */
package com.example.app;

import java.util.Scanner;

public class Airport {

    private String userInput;
    private Airparams airparams;
    private Airrec airportRecord;
    private String fromToMessage;

    public static void main(String[] args) {
        Airport app = new Airport();
        app.run();
    }

    public void run() {
        // Clear screen equivalent
        System.out.print("\033[2J\033[H");

        this.airparams = new Airparams();
        this.airportRecord = new Airrec();

        // Set open-file to true and perform call-aircode-program
        this.airparams.setFunction("4"); // open-file function
        callAircodeProgram();

        if ("00".equals(this.airparams.getFileStatus())) {
            // Clear airports and distance values
            this.airparams.setAirport1("");
            this.airparams.setAirport2("");
            this.fromToMessage = "";
            this.airparams.setDistanceKm("0");
            this.airparams.setDistanceMiles("0");

            // Interactive input loop - simplified for console
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Display equivalent to G-DISTSCRN
                displayDistanceScreen();

                System.out.print("Airport 1: ");
                String airport1 = scanner.nextLine().trim();

                if (airport1.isEmpty()) {
                    break; // exit perform
                }

                System.out.print("Airport 2: ");
                String airport2 = scanner.nextLine().trim();

                this.airparams.setAirport1(airport1);
                this.airparams.setAirport2(airport2);

                if (!airport2.isEmpty()) {
                    // Set get-distance to true
                    this.airparams.setFunction("2"); // get-distance function
                    this.fromToMessage = "";
                    this.fromToMessage = airport1 + " -> " + airport2;

                    callAircodeProgram();

                    if ("00".equals(this.airparams.getFileStatus())) {
                        // Clear airports for next iteration
                        this.airparams.setAirport1("");
                        this.airparams.setAirport2("");

                        displayDistanceScreen();
                    } else {
                        displayInvalidCode();
                    }
                } else {
                    // Single airport - get details or matches
                    this.airportRecord.setCode(" ");
                    this.airparams.setFunction("3"); // get-details function
                    callAircodeProgram();

                    if (!" ".equals(this.airportRecord.getCode())) {
                        displayAirportRecord();

                        // Clear airports for next iteration
                        this.airparams.setAirport1("");
                        this.airparams.setAirport2("");
                    } else {
                        // No exact match found, search for prefix matches
                        this.airparams.setMatchedCodesArray("");
                        this.airparams.setPrefixText(airport1);
                        this.airparams.setFunction("1"); // get-matches function
                        callAircodeProgram();

                        if (this.airparams.getMatchedCodes() != null &&
                            this.airparams.getMatchedCodes().length > 0 &&
                            !this.airparams.getMatchedCodes()[0].trim().isEmpty()) {
                            displaySearchResults();
                        } else {
                            displayInvalidCode();
                        }
                    }
                }
            }

            scanner.close();

            // Set close-file to true and perform call-aircode-program
            this.airparams.setFunction("5"); // close-file function
            callAircodeProgram();
        }
    }

    private void displayDistanceScreen() {
        // Equivalent to G-DISTSCRN display
        System.out.println("Distance: " + this.airparams.getDistanceKm() + " km, " +
                          this.airparams.getDistanceMiles() + " miles");
        if (!this.fromToMessage.isEmpty()) {
            System.out.println(this.fromToMessage);
        }
    }

    private void displayAirportRecord() {
        // Equivalent to G-DISTREC display
        System.out.println("Airport record displayed");
    }

    private void displaySearchResults() {
        // Equivalent to G-DISTSRCH display
        System.out.println("Search results displayed");
    }

    private void displayInvalidCode() {
        System.out.println("Invalid IATA code");
    }

    private void callAircodeProgram() {
        Aircode aircode = new Aircode();
        aircode.main(
            this.airparams.getFunction(),
            this.airparams.getAirport1(),
            this.airparams.getAirport2(),
            this.airparams.getPrefixText(),
            this.airportRecord,
            this.airparams,
            this.airparams,
            this.airparams
        );
    }
}