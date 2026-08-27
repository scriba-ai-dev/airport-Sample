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

import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main business logic program providing airport operations via indexed file access
 */
public class Aircode {
    private static final Logger log = LoggerFactory.getLogger(Aircode.class);

    /** File operation status code */
    private String fileStatus = "00";

    /** Flag indicating if airport was found */
    private int airportFound = 0;

    /** Current airport code being processed */
    private String airport;

    /** Environment variable name for airport data file */
    private static final String AIRPORT_DAT = "dd_airports";

    /** Buffer for airport code matches */
    private String aircodeArray = "";

    /** Array of matched airport codes */
    private String[] aircodeMatches = new String[10];

    /** Index counter for matches */
    private int idx = 0;

    /** Loop counter */
    private int j = 0;

    /** Length of search prefix */
    private int prefixLength = 0;

    /** Maximum number of matches to return */
    private static final int MAX_TO_RETURN = 10;

    /** Latitude of first airport */
    private double lat1;

    /** Longitude of first airport */
    private double long1;

    /** Latitude of second airport */
    private double lat2;

    /** Longitude of second airport */
    private double long2;

    /** Calculated distance in kilometers */
    private double distance;

    /** Calculated distance in miles */
    private double distanceM;

    /** Sign component of angle */
    private String fileAngleSign;

    /** Degrees component of angle */
    private int fileAngleDegs;

    /** Minutes component of angle */
    private double fileAngleMins;

    /** Converted angle in radians */
    private double outAngle;

    /** Radius of earth in kilometers */
    private static final double RADIUS_OF_EARTH = 6371.0;

    /** Kilometers per mile conversion factor */
    private static final double KM_PER_MILE = 1.609344;

    /** Helper variable for floating point calculations */
    private double fpHelper;

    /** First airport record for distance calculation */
    private Airrec a1Rec = new Airrec();

    /** Second airport record for distance calculation */
    private Airrec a2Rec = new Airrec();

    /** File record buffer */
    private Airrec fRec = new Airrec();

    // In-memory indexed file storage
    private TreeMap<String, Airrec> airportMap = new TreeMap<>();

    /**
     * Main entry point that dispatches based on function code
     */
    public void main(String lnkFunction, String lnkAirport1, String lnkAirport2, String lnkPrefixText,
                    Airrec lnkRec, Airparams lnkDistanceResult, Airparams lnkMatchedCodesArray, Airparams lnkFileStatus) {

        // Set function in linkage area for condition evaluation
        if (lnkDistanceResult != null) {
            lnkDistanceResult.setFunction(lnkFunction);
        }

        // Dispatch based on function code using the 88-level VALUES
        if ("1".equals(lnkFunction)) { // get-matches
            getCodeMatches(lnkPrefixText, lnkMatchedCodesArray, lnkFileStatus);
        } else if ("2".equals(lnkFunction)) { // get-distance
            distanceBetweenAirports(lnkAirport1, lnkAirport2, lnkDistanceResult, lnkFileStatus);
        } else if ("3".equals(lnkFunction)) { // get-details
            lookupOneAirport(lnkAirport1, lnkRec, lnkFileStatus);
        } else if ("4".equals(lnkFunction)) { // open-file
            openAirfile(lnkFileStatus);
        } else if ("5".equals(lnkFunction)) { // close-file
            closeAirfile(lnkFileStatus);
        } else if ("6".equals(lnkFunction)) { // display-record
            displayAirport();
        }
    }

    /**
     * Looks up single airport details
     */
    private void lookupOneAirport(String lnkAirport1, Airrec lnkRec, Airparams lnkFileStatus) {
        // Initialize lnk-rec
        lnkRec.setCode("");
        lnkRec.setName("");
        lnkRec.setCity("");
        lnkRec.setCountry("");
        lnkRec.setLatSign("");
        lnkRec.setLatDegs(0);
        lnkRec.setLatMins(0);
        lnkRec.setLongSign("");
        lnkRec.setLongDegs(0);
        lnkRec.setLongMins(0);

        airport = lnkAirport1;
        findAirport(lnkFileStatus);

        if (airportFound == 1) {
            // Move f-rec to lnk-rec
            lnkRec.setCode(fRec.getCode());
            lnkRec.setName(fRec.getName());
            lnkRec.setCity(fRec.getCity());
            lnkRec.setCountry(fRec.getCountry());
            lnkRec.setLatSign(fRec.getLatSign());
            lnkRec.setLatDegs(fRec.getLatDegs());
            lnkRec.setLatMins(fRec.getLatMins());
            lnkRec.setLongSign(fRec.getLongSign());
            lnkRec.setLongDegs(fRec.getLongDegs());
            lnkRec.setLongMins(fRec.getLongMins());
        }
    }

    /**
     * Calculates distance between two airports
     */
    private void distanceBetweenAirports(String lnkAirport1, String lnkAirport2, Airparams lnkDistanceResult, Airparams lnkFileStatus) {
        // Initialize lnk-distance-result
        lnkDistanceResult.setDistanceKm("");
        lnkDistanceResult.setDistanceMiles("");

        airport = lnkAirport1;
        findAirport(lnkFileStatus);

        if (airportFound == 1) {
            // Move f-rec to a1-rec
            a1Rec.setCode(fRec.getCode());
            a1Rec.setName(fRec.getName());
            a1Rec.setCity(fRec.getCity());
            a1Rec.setCountry(fRec.getCountry());
            a1Rec.setLatSign(fRec.getLatSign());
            a1Rec.setLatDegs(fRec.getLatDegs());
            a1Rec.setLatMins(fRec.getLatMins());
            a1Rec.setLongSign(fRec.getLongSign());
            a1Rec.setLongDegs(fRec.getLongDegs());
            a1Rec.setLongMins(fRec.getLongMins());

            airport = lnkAirport2;
            findAirport(lnkFileStatus);

            if (airportFound == 1) {
                // Move f-rec to a2-rec
                a2Rec.setCode(fRec.getCode());
                a2Rec.setName(fRec.getName());
                a2Rec.setCity(fRec.getCity());
                a2Rec.setCountry(fRec.getCountry());
                a2Rec.setLatSign(fRec.getLatSign());
                a2Rec.setLatDegs(fRec.getLatDegs());
                a2Rec.setLatMins(fRec.getLatMins());
                a2Rec.setLongSign(fRec.getLongSign());
                a2Rec.setLongDegs(fRec.getLongDegs());
                a2Rec.setLongMins(fRec.getLongMins());

                calculateAirportDistance();

                // MOVE distance TO distance-km - truncates to 6 digits per PIC 9(6)
                long distanceKm = (long) distance;
                lnkDistanceResult.setDistanceKm(String.valueOf(distanceKm));

                // MOVE distance-m TO distance-miles - truncates to 6 digits per PIC 9(6)
                long distanceMiles = (long) distanceM;
                lnkDistanceResult.setDistanceMiles(String.valueOf(distanceMiles));
            }
        }
    }

    /**
     * Performs spherical law of cosines distance calculation
     */
    private void calculateAirportDistance() {
        // Convert a1-latitude to radians
        fileAngleSign = a1Rec.getLatSign();
        fileAngleDegs = a1Rec.getLatDegs();
        fileAngleMins = a1Rec.getLatMins();
        convertAngle();
        lat1 = outAngle;

        // Convert a1-longitude to radians
        fileAngleSign = a1Rec.getLongSign();
        fileAngleDegs = a1Rec.getLongDegs();
        fileAngleMins = a1Rec.getLongMins();
        convertAngle();
        long1 = outAngle;

        // Convert a2-latitude to radians
        fileAngleSign = a2Rec.getLatSign();
        fileAngleDegs = a2Rec.getLatDegs();
        fileAngleMins = a2Rec.getLatMins();
        convertAngle();
        lat2 = outAngle;

        // Convert a2-longitude to radians
        fileAngleSign = a2Rec.getLongSign();
        fileAngleDegs = a2Rec.getLongDegs();
        fileAngleMins = a2Rec.getLongMins();
        convertAngle();
        long2 = outAngle;

        // Spherical law of cosines
        distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                            Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1)) * RADIUS_OF_EARTH;
        distanceM = distance / KM_PER_MILE;
    }

    /**
     * Converts ASCII angle format to floating point radians
     */
    private void convertAngle() {
        // If fa-mins = 0, set to 1
        if (fileAngleMins == 0) {
            fileAngleMins = 1;
        }

        fpHelper = fileAngleMins;

        // Perform until fp-helper < 1.0
        while (fpHelper >= 1.0) {
            fpHelper = fpHelper * 0.1;
        }

        fpHelper = fpHelper * 60;
        fileAngleMins = fpHelper;
        fileAngleMins = (long) fpHelper;

        outAngle = fileAngleDegs + (fileAngleMins / 60.0);

        if ("-".equals(fileAngleSign)) {
            outAngle = outAngle * -1;
        }

        outAngle = (outAngle * Math.PI) / 180;
    }

    /**
     * Displays airport information
     */
    private void displayAirport() {
        log.info("{} {}", fRec.getCode(), fRec.getName());
        log.info("     {}  Lat:{}{}.{} Lon:{}{}.{}",
                fRec.getCountry(),
                fRec.getLatSign(), fRec.getLatDegs(), fRec.getLatMins(),
                fRec.getLongSign(), fRec.getLongDegs(), fRec.getLongMins());
    }

    /**
     * Finds airports matching prefix pattern
     */
    private void getCodeMatches(String lnkPrefixText, Airparams lnkMatchedCodesArray, Airparams lnkFileStatus) {
        lnkFileStatus.setFileStatus("00");
        idx = 0;
        aircodeArray = "";
        for (int i = 0; i < aircodeMatches.length; i++) {
            aircodeMatches[i] = "";
        }
        prefixLength = 0;

        // Count characters before space
        String prefixText = lnkPrefixText.trim();
        prefixLength = prefixText.length();

        String upperPrefix = prefixText.toUpperCase(Locale.ROOT);

        // START airfile key >= f-code
        String startKey = airportMap.ceilingKey(upperPrefix);
        if (startKey != null) {
            // Read airfile next record equivalent - iterate through map
            for (Map.Entry<String, Airrec> entry : airportMap.tailMap(startKey, true).entrySet()) {
                String code = entry.getKey();
                Airrec record = entry.getValue();

                // Check if code matches prefix and haven't exceeded max
                if (code.length() >= prefixLength &&
                    code.substring(0, prefixLength).equals(upperPrefix) &&
                    idx < MAX_TO_RETURN) {

                    idx++;
                    // STRING f-code delimited by space " - " delimited by size f-name delimited by size
                    String match = code.trim() + " - " + record.getName().trim();
                    aircodeMatches[idx - 1] = match;
                } else if (code.length() < prefixLength ||
                          !code.substring(0, prefixLength).equals(upperPrefix)) {
                    break; // Exit when no longer matching prefix
                }
            }
        } else {
            lnkFileStatus.setFileStatus(fileStatus);
        }

        // Copy results to linkage area
        String[] resultArray = new String[idx];
        for (j = 0; j < idx; j++) {
            resultArray[j] = aircodeMatches[j];
        }
        lnkMatchedCodesArray.setMatchedCodes(resultArray);
    }

    /**
     * Searches for specific airport by code
     */
    private void findAirport(Airparams lnkFileStatus) {
        airportFound = 0;

        // Initialize f-rec
        fRec.setCode("");
        fRec.setName("");
        fRec.setCity("");
        fRec.setCountry("");
        fRec.setLatSign("");
        fRec.setLatDegs(0);
        fRec.setLatMins(0);
        fRec.setLongSign("");
        fRec.setLongDegs(0);
        fRec.setLongMins(0);

        String upperCode = airport.toUpperCase(Locale.ROOT);

        // START airfile key = f-code
        Airrec foundRecord = airportMap.get(upperCode);
        if (foundRecord == null) {
            lnkFileStatus.setFileStatus(fileStatus);
        } else {
            // Read airfile next record - copy found record to fRec
            fRec.setCode(foundRecord.getCode());
            fRec.setName(foundRecord.getName());
            fRec.setCity(foundRecord.getCity());
            fRec.setCountry(foundRecord.getCountry());
            fRec.setLatSign(foundRecord.getLatSign());
            fRec.setLatDegs(foundRecord.getLatDegs());
            fRec.setLatMins(foundRecord.getLatMins());
            fRec.setLongSign(foundRecord.getLongSign());
            fRec.setLongDegs(foundRecord.getLongDegs());
            fRec.setLongMins(foundRecord.getLongMins());

            airportFound = 1;
            lnkFileStatus.setFileStatus("00");
        }
    }

    /**
     * Opens the airport data file
     */
    private void openAirfile(Airparams lnkFileStatus) {
        try {
            // Load from sequential export file
            InputStream is = getClass().getResourceAsStream("/airports.seq");
            if (is == null) {
                // Fall back to environment variable path
                String filename = System.getenv(AIRPORT_DAT);
                if (filename != null) {
                    is = new FileInputStream(filename);
                }
            }

            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.length() >= 104) { // Total record length
                            // Parse fixed-width record per copybook layout
                            String code = line.substring(0, 4).trim();
                            String name = line.substring(4, 34).trim();
                            String city = line.substring(34, 64).trim();
                            String country = line.substring(64, 84).trim();
                            String latSign = line.substring(84, 85);
                            int latDegs = Integer.parseInt(line.substring(85, 88).trim());
                            int latMins = Integer.parseInt(line.substring(88, 94).trim());
                            String longSign = line.substring(94, 95);
                            int longDegs = Integer.parseInt(line.substring(95, 98).trim());
                            int longMins = Integer.parseInt(line.substring(98, 104).trim());

                            Airrec record = new Airrec();
                            record.setCode(code);
                            record.setName(name);
                            record.setCity(city);
                            record.setCountry(country);
                            record.setLatSign(latSign);
                            record.setLatDegs(latDegs);
                            record.setLatMins(latMins);
                            record.setLongSign(longSign);
                            record.setLongDegs(longDegs);
                            record.setLongMins(longMins);

                            airportMap.put(code.toUpperCase(Locale.ROOT), record);
                        }
                    }
                }
                fileStatus = "00";
            } else {
                fileStatus = "35"; // File not found
            }
        } catch (Exception e) {
            log.error("Error opening airport file: {}", e.getMessage());
            fileStatus = "30"; // Permanent I/O error
        }

        lnkFileStatus.setFileStatus(fileStatus);
    }

    /**
     * Closes the airport data file
     */
    private void closeAirfile(Airparams lnkFileStatus) {
        // In-memory implementation - just clear the map
        airportMap.clear();
        fileStatus = "00";
        lnkFileStatus.setFileStatus(fileStatus);
    }
}