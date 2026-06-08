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

import java.util.logging.Logger;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

/**
 * Airport code service module providing lookup, prefix matching, and distance calculation operations
 */
public class Aircode {
    private static final Logger log = Logger.getLogger(Aircode.class.getName());

    private String fileStatus = "00";
    private int airportFound;
    private String airport;
    private Airrec fRec;
    private Airrec a1Rec;
    private Airrec a2Rec;
    private String aircodeArray;
    private String[] aircodeMatches;
    private int idx;
    private int j;
    private int prefixLength;
    private double lat1;
    private double long1;
    private double lat2;
    private double long2;
    private double distance;
    private double distanceM;
    private String faSign;
    private int faDegs;
    private int faMins;
    private double outAngle;
    private double fpHelper;
    private RandomAccessFile airfile;

    private static final String AIRPORT_DAT = "dd_airports";
    private static final int MAXTORETURN = 10;
    private static final double RADIUS_OF_EARTH = 6371.0;
    private static final double KM_PER_MILE = 1.609344;

    public Aircode() {
        this.fRec = new Airrec();
        this.a1Rec = new Airrec();
        this.a2Rec = new Airrec();
        this.aircodeMatches = new String[MAXTORETURN];
    }

    /**
     * Main entry point dispatching to specific operations based on function code
     */
    public void execute(String lnkFunction, String lnkAirport1, String lnkAirport2, String lnkPrefixText,
                       Airrec lnkRec, Airparams lnkDistanceResult, String[] lnkMatchedCodesArray, String[] lnkFileStatus) {

        // Dispatch based on function code
        if (Airparams.GET_MATCHES.equals(lnkFunction)) {
            getCodeMatches(lnkPrefixText, lnkMatchedCodesArray, lnkFileStatus);
        } else if (Airparams.GET_DISTANCE.equals(lnkFunction)) {
            distanceBetweenAirports(lnkAirport1, lnkAirport2, lnkDistanceResult);
        } else if (Airparams.GET_DETAILS.equals(lnkFunction)) {
            lookupOneAirport(lnkAirport1, lnkRec);
        } else if (Airparams.OPEN_FILE.equals(lnkFunction)) {
            openAirfile(lnkFileStatus);
        } else if (Airparams.CLOSE_FILE.equals(lnkFunction)) {
            closeAirfile(lnkFileStatus);
        } else if (Airparams.DISPLAY_RECORD.equals(lnkFunction)) {
            displayAirport();
        }
    }

    private void lookupOneAirport(String lnkAirport1, Airrec lnkRec) {
        // Initialize record
        lnkRec.setCode("");
        lnkRec.setName("");
        lnkRec.setCity("");
        lnkRec.setCountry("");
        lnkRec.setLatSign("");
        lnkRec.setLatDegs("0");
        lnkRec.setLatMins("0");
        lnkRec.setLongSign("");
        lnkRec.setLongDegs("0");
        lnkRec.setLongMins("0");

        airport = lnkAirport1;
        findAirport(new String[1]);

        if (airportFound == 1) {
            // Copy f-rec to lnk-rec
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

    private void distanceBetweenAirports(String lnkAirport1, String lnkAirport2, Airparams lnkDistanceResult) {
        // Initialize distance result
        lnkDistanceResult.setDistanceKm("");
        lnkDistanceResult.setDistanceMiles("");

        // Find first airport
        airport = lnkAirport1;
        findAirport(new String[1]);

        if (airportFound == 1) {
            // Copy f-rec to a1-rec
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

            // Find second airport
            airport = lnkAirport2;
            findAirport(new String[1]);

            if (airportFound == 1) {
                // Copy f-rec to a2-rec
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

                // Store results with truncation to integer fields
                long distanceKm = (long) distance;
                long distanceMiles = (long) distanceM;
                lnkDistanceResult.setDistanceKm(String.valueOf(distanceKm));
                lnkDistanceResult.setDistanceMiles(String.valueOf(distanceMiles));
            }
        }
    }

    private void calculateAirportDistance() {
        // Convert a1 latitude to radians
        faSign = a1Rec.getLatSign();
        faDegs = Integer.parseInt(a1Rec.getLatDegs());
        faMins = Integer.parseInt(a1Rec.getLatMins());
        convertAngle();
        lat1 = outAngle;

        // Convert a1 longitude to radians
        faSign = a1Rec.getLongSign();
        faDegs = Integer.parseInt(a1Rec.getLongDegs());
        faMins = Integer.parseInt(a1Rec.getLongMins());
        convertAngle();
        long1 = outAngle;

        // Convert a2 latitude to radians
        faSign = a2Rec.getLatSign();
        faDegs = Integer.parseInt(a2Rec.getLatDegs());
        faMins = Integer.parseInt(a2Rec.getLatMins());
        convertAngle();
        lat2 = outAngle;

        // Convert a2 longitude to radians
        faSign = a2Rec.getLongSign();
        faDegs = Integer.parseInt(a2Rec.getLongDegs());
        faMins = Integer.parseInt(a2Rec.getLongMins());
        convertAngle();
        long2 = outAngle;

        // Spherical law of cosines
        distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                           Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1))
                  * RADIUS_OF_EARTH;

        distanceM = distance / KM_PER_MILE;
    }

    private void convertAngle() {
        // Convert ASCII file value to floating point RADIAN value
        if (faMins == 0) {
            faMins = 1;
        }

        fpHelper = faMins;
        while (fpHelper >= 1.0) {
            fpHelper = fpHelper * 0.1;
        }

        fpHelper = fpHelper * 60;
        faMins = (int) fpHelper;

        outAngle = faDegs + (faMins / 60.0);
        if ("-".equals(faSign)) {
            outAngle = outAngle * -1;
        }
        outAngle = (outAngle * Math.PI) / 180;
    }

    private void displayAirport() {
        log.info(fRec.getCode() + " " + fRec.getName());
        log.info("     " + fRec.getCountry() + " Lat:" + fRec.getLatSign() + fRec.getLatDegs() + "." + fRec.getLatMins() + " Lon:" + fRec.getLongSign() + fRec.getLongDegs() + "." + fRec.getLongMins());
    }

    private void getCodeMatches(String lnkPrefixText, String[] lnkMatchedCodesArray, String[] lnkFileStatus) {
        lnkFileStatus[0] = "00";
        idx = 0;
        aircodeArray = "";

        // Initialize aircode-matches array
        for (int i = 0; i < MAXTORETURN; i++) {
            aircodeMatches[i] = "";
        }

        prefixLength = 0;

        // Count characters before space
        for (int i = 0; i < lnkPrefixText.length(); i++) {
            if (lnkPrefixText.charAt(i) == ' ') {
                break;
            }
            prefixLength++;
        }

        lnkPrefixText = lnkPrefixText.toUpperCase(Locale.ROOT);

        try {
            if (airfile != null) {
                airfile.seek(0);
                boolean found = false;

                // Read through file looking for matches
                while (true) {
                    try {
                        byte[] recordBytes = new byte[104]; // Airrec total size
                        int bytesRead = airfile.read(recordBytes);
                        if (bytesRead < 104) break;

                        String code = new String(recordBytes, 0, 4, StandardCharsets.UTF_8).trim();
                        String name = new String(recordBytes, 4, 30, StandardCharsets.UTF_8).trim();

                        if (code.length() >= prefixLength &&
                            code.substring(0, prefixLength).equals(lnkPrefixText.substring(0, prefixLength))) {

                            if (idx < MAXTORETURN) {
                                aircodeMatches[idx] = code + " - " + name;
                                idx++;
                            } else {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            lnkFileStatus[0] = "99";
        }

        // Copy results to the result collection
        for (j = 0; j < idx; j++) {
            lnkMatchedCodesArray[j] = aircodeMatches[j];
        }
    }

    private void findAirport(String[] lnkFileStatus) {
        airportFound = 0;

        // Initialize f-rec
        fRec.setCode("");
        fRec.setName("");
        fRec.setCity("");
        fRec.setCountry("");
        fRec.setLatSign("");
        fRec.setLatDegs("0");
        fRec.setLatMins("0");
        fRec.setLongSign("");
        fRec.setLongDegs("0");
        fRec.setLongMins("0");

        String upperAirport = airport.toUpperCase(Locale.ROOT);

        try {
            if (airfile != null) {
                airfile.seek(0);

                // Search for exact match
                while (true) {
                    try {
                        byte[] recordBytes = new byte[104]; // Airrec total size
                        int bytesRead = airfile.read(recordBytes);
                        if (bytesRead < 104) break;

                        String code = new String(recordBytes, 0, 4, StandardCharsets.UTF_8).trim();

                        if (code.equals(upperAirport)) {
                            // Parse the complete record
                            fRec.setCode(code);
                            fRec.setName(new String(recordBytes, 4, 30, StandardCharsets.UTF_8).trim());
                            fRec.setCity(new String(recordBytes, 34, 30, StandardCharsets.UTF_8).trim());
                            fRec.setCountry(new String(recordBytes, 64, 20, StandardCharsets.UTF_8).trim());
                            fRec.setLatSign(new String(recordBytes, 84, 1, StandardCharsets.UTF_8));
                            fRec.setLatDegs(new String(recordBytes, 85, 3, StandardCharsets.UTF_8).trim());
                            fRec.setLatMins(new String(recordBytes, 88, 6, StandardCharsets.UTF_8).trim());
                            fRec.setLongSign(new String(recordBytes, 94, 1, StandardCharsets.UTF_8));
                            fRec.setLongDegs(new String(recordBytes, 95, 3, StandardCharsets.UTF_8).trim());
                            fRec.setLongMins(new String(recordBytes, 98, 6, StandardCharsets.UTF_8).trim());

                            airportFound = 1;
                            lnkFileStatus[0] = fileStatus;
                            break;
                        }
                    } catch (IOException | NumberFormatException e) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            lnkFileStatus[0] = "99";
        }

        if (airportFound == 0) {
            lnkFileStatus[0] = fileStatus;
        }
    }

    private void openAirfile(String[] lnkFileStatus) {
        try {
            String airfileName = System.getenv(AIRPORT_DAT);
            if (airfileName == null) {
                airfileName = "airports.dat";
            }

            File file = new File(airfileName);
            if (file.exists()) {
                airfile = new RandomAccessFile(file, "r");
                fileStatus = "00";
            } else {
                fileStatus = "35"; // File not found
            }
        } catch (IOException e) {
            fileStatus = "99";
        }

        lnkFileStatus[0] = fileStatus;
    }

    private void closeAirfile(String[] lnkFileStatus) {
        try {
            if (airfile != null) {
                airfile.close();
                airfile = null;
            }
            fileStatus = "00";
        } catch (IOException e) {
            fileStatus = "99";
        }

        lnkFileStatus[0] = fileStatus;
    }
}