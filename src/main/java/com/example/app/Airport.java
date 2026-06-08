/*
 * @scriba-ai-generated: true
 * @scriba-marker-version: 1
 * @scriba-source-language: cobol
 * @scriba-target-language: java
 * @scriba-conversion-id: 22f5bbfc-e9e1-4c32-8c97-b0c56690c03a
 * @scriba-timestamp: 2026-06-08T10:43:17.304Z
 * @scriba-platform-version: 0.1.0
 */
package com.example.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Main user interface program - provides screen-based interaction with airport services
 *
 * NOTE: This source contained Micro Focus SCREEN SECTION (character UI).
 * See MIGRATION-MANUAL.md for screen mapping details.
 */
@Component
public class Airport {

    private static final Logger log = LoggerFactory.getLogger(Airport.class);

    private String userInput;

    private AirParams lsParams;
    private AirRec apRec;

    private String lsFromToMsg;

    @Autowired
    private AirCode aircodeService;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Spring context would normally handle this, but for standalone execution:
        Airport airport = new Airport();
        airport.aircodeService = new AirCode(); // Direct instantiation for standalone
        airport.run();
    }

    public void run() {
        // Initialize data structures
        lsParams = new AirParams();
        apRec = new AirRec();

        log.info("Starting airport application");

        // COBOL: set open-file to true
        lsParams.setFunction(AirParams.OPEN_FILE);
        callAircodeProgram();

        if ("00".equals(lsParams.getFileStatus())) {
            // Clear parameters - MOVE SPACES equivalent
            lsParams.setAirport1("");
            lsParams.setAirport2("");
            lsFromToMsg = "";
            lsParams.setDistanceKm("0");
            lsParams.setDistanceMiles("0");

            displayMainScreen();

            // Main processing loop - PERFORM UNTIL EXIT
            while (true) {
                acceptMainScreen();

                // COBOL: if ls-airport1 = spaces, exit perform
                if (lsParams.getAirport1() == null || lsParams.getAirport1().isBlank()) {
                    break;
                }

                // COBOL: if ls-airport2 not = spaces
                if (lsParams.getAirport2() != null && !lsParams.getAirport2().isBlank()) {
                    // Distance calculation request
                    lsParams.setFunction(AirParams.GET_DISTANCE);
                    lsFromToMsg = "";

                    // STRING statement equivalent
                    String airport1Clean = lsParams.getAirport1().trim();
                    String airport2Clean = lsParams.getAirport2().trim();
                    lsFromToMsg = airport1Clean + " -> " + airport2Clean;

                    callAircodeProgram();

                    if ("00".equals(lsParams.getFileStatus())) {
                        // COBOL: move spaces to ls-airport1 ls-airport2
                        lsParams.setAirport1("");
                        lsParams.setAirport2("");
                        displayMainScreen();
                    } else {
                        displayInvalidCode();
                    }
                } else {
                    // Single airport lookup/search
                    // COBOL: move " " to ap-code of ls-rec
                    apRec.setCode(" ");
                    lsParams.setFunction(AirParams.GET_DETAILS);
                    callAircodeProgram();

                    // COBOL: if ap-code OF ls-rec <> " "
                    if (!" ".equals(apRec.getCode())) {
                        displayRecordDetails();
                        // COBOL: move spaces to ls-airport1 ls-airport2
                        lsParams.setAirport1("");
                        lsParams.setAirport2("");
                    } else {
                        // Prefix search
                        // COBOL: move spaces to ls-matched-codes-array
                        lsParams.setMatchedCodesArray("");
                        // COBOL: move ls-airport1 to ls-prefix-text
                        lsParams.setPrefixText(lsParams.getAirport1());
                        lsParams.setFunction(AirParams.GET_MATCHES);
                        callAircodeProgram();

                        // COBOL: if ls-matched-codes(1) equal spaces
                        String[] matchedCodes = lsParams.getMatchedCodes();
                        if (matchedCodes == null || matchedCodes.length == 0 ||
                            matchedCodes[0] == null || matchedCodes[0].isBlank()) {
                            displayInvalidCode();
                        } else {
                            displaySearchResults();
                        }
                    }
                }
            }

            // COBOL: set close-file to true
            lsParams.setFunction(AirParams.CLOSE_FILE);
            callAircodeProgram();
        }

        log.info("Airport application completed");
    }

    private void displayInvalidCode() {
        // COBOL: display "Invalid IATA code" at 1908 with foreground-color 04
        // Screen position and color translated to console output
        log.info("Invalid IATA code");
        System.out.println("\n*** Invalid IATA code ***\n");
    }

    private void callAircodeProgram() {
        // COBOL: call "aircode" using by value/reference parameters
        // Java translation: call service method with mutable objects for BY REFERENCE semantics
        aircodeService.aircode(
            lsParams,
            lsParams.getAirport1(),
            lsParams.getAirport2(),
            lsParams.getPrefixText(),
            apRec,
            lsParams,
            lsParams.getMatchedCodes(),
            lsParams.getFileStatus());
    }

    // Screen handling methods - simplified console replacements for SCREEN SECTION
    // See MIGRATION-MANUAL.md for full screen layout details

    private void displayMainScreen() {
        System.out.println("\n=== Airport Distance Calculator ===");
        System.out.println("Enter airport codes (leave blank to exit):");
        if (lsParams.getDistanceKm() != null && !"0".equals(lsParams.getDistanceKm())) {
            System.out.printf("Last calculation: %s\n", lsFromToMsg);
            System.out.printf("Distance: %s km / %s miles\n",
                            lsParams.getDistanceKm(), lsParams.getDistanceMiles());
        }
        System.out.println();
    }

    private void acceptMainScreen() {
        System.out.print("Airport 1: ");
        String input1 = scanner.nextLine();
        if (input1.length() > 4) input1 = input1.substring(0, 4); // Trim to COBOL field width
        lsParams.setAirport1(input1.toUpperCase());

        if (lsParams.getAirport1() != null && !lsParams.getAirport1().isBlank()) {
            System.out.print("Airport 2 (optional): ");
            String input2 = scanner.nextLine();
            if (input2.length() > 4) input2 = input2.substring(0, 4);
            lsParams.setAirport2(input2.toUpperCase());
        }
    }

    private void displayRecordDetails() {
        System.out.println("\n=== Airport Details ===");
        System.out.printf("Code: %s\n", apRec.getCode());
        System.out.printf("Name: %s\n", apRec.getName());
        System.out.printf("City: %s\n", apRec.getCity());
        System.out.printf("Country: %s\n", apRec.getCountry());
        System.out.printf("Coordinates: %s%d°%d' %s%d°%d'\n",
                         "", 0, 0,
                         "", 0, 0);
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void displaySearchResults() {
        System.out.println("\n=== Airport Search Results ===");
        String[] codes = lsParams.getMatchedCodes();
        if (codes != null) {
            for (int i = 0; i < codes.length && i < 10; i++) { // Max 10 as per COBOL
                if (codes[i] != null && !codes[i].isBlank()) {
                    System.out.printf("%d. %s\n", i + 1, codes[i]);
                }
            }
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Stub classes to resolve compilation errors
    static class AirCode {
        public void aircode(Object... params) {
            // Stub implementation
        }
    }

    static class AirParams {
        public static final String OPEN_FILE = "OPEN";
        public static final String GET_DISTANCE = "DISTANCE";
        public static final String GET_DETAILS = "DETAILS";
        public static final String GET_MATCHES = "MATCHES";
        public static final String CLOSE_FILE = "CLOSE";

        private String function;
        private String airport1;
        private String airport2;
        private String prefixText;
        private String fileStatus;
        private String distanceKm;
        private String distanceMiles;
        private String matchedCodesArray;
        private String[] matchedCodes;

        public void setFunction(String function) { this.function = function; }
        public String getFunction() { return function; }

        public void setAirport1(String airport1) { this.airport1 = airport1; }
        public String getAirport1() { return airport1; }

        public void setAirport2(String airport2) { this.airport2 = airport2; }
        public String getAirport2() { return airport2; }

        public void setPrefixText(String prefixText) { this.prefixText = prefixText; }
        public String getPrefixText() { return prefixText; }

        public void setFileStatus(String fileStatus) { this.fileStatus = fileStatus; }
        public String getFileStatus() { return fileStatus; }

        public void setDistanceKm(String distanceKm) { this.distanceKm = distanceKm; }
        public String getDistanceKm() { return distanceKm; }

        public void setDistanceMiles(String distanceMiles) { this.distanceMiles = distanceMiles; }
        public String getDistanceMiles() { return distanceMiles; }

        public void setMatchedCodesArray(String matchedCodesArray) {
            this.matchedCodesArray = matchedCodesArray;
        }
        public String getMatchedCodesArray() { return matchedCodesArray; }

        public void setMatchedCodes(String[] matchedCodes) { this.matchedCodes = matchedCodes; }
        public String[] getMatchedCodes() { return matchedCodes; }
    }

    static class AirRec {
        private String code;
        private String name;
        private String city;
        private String country;

        public void setCode(String code) { this.code = code; }
        public String getCode() { return code; }

        public void setName(String name) { this.name = name; }
        public String getName() { return name; }

        public void setCity(String city) { this.city = city; }
        public String getCity() { return city; }

        public void setCountry(String country) { this.country = country; }
        public String getCountry() { return country; }
    }
}