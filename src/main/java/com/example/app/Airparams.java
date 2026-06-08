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

/**
 * Parameter structure for airport service calls defining function codes and data exchange format between UI and service layers.
 */
public class Airparams {

    /** Function code for get-matches operation */
    public static final String GET_MATCHES = "1";

    /** Function code for get-distance operation */
    public static final String GET_DISTANCE = "2";

    /** Function code for get-details operation */
    public static final String GET_DETAILS = "3";

    /** Function code for open-file operation */
    public static final String OPEN_FILE = "4";

    /** Function code for close-file operation */
    public static final String CLOSE_FILE = "5";

    /** Function code for display-record operation */
    public static final String DISPLAY_RECORD = "6";

    /** Function code indicating the operation to perform */
    private String function;

    /** First airport code (4 characters) */
    private String airport1;

    /** Second airport code (4 characters) */
    private String airport2;

    /** Prefix text for matching operations (4 characters) */
    private String prefixText;

    /** Distance result in kilometers (formatted with comma) */
    private String distanceKm;

    /** Distance result in miles (formatted with comma) */
    private String distanceMiles;

    /** Array of matched airport codes (350 characters total) */
    private String matchedCodesArray;

    /** Individual matched codes (10 entries of 35 characters each) */
    private String[] matchedCodes;

    /** File operation status code (2 characters) */
    private String fileStatus;

    /** Distance result structure containing both km and miles */
    private DistanceResult distanceResult;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getAirport1() {
        return airport1;
    }

    public void setAirport1(String airport1) {
        this.airport1 = airport1;
    }

    public String getAirport2() {
        return airport2;
    }

    public void setAirport2(String airport2) {
        this.airport2 = airport2;
    }

    public String getPrefixText() {
        return prefixText;
    }

    public void setPrefixText(String prefixText) {
        this.prefixText = prefixText;
    }

    public String getMatchedCodesArray() {
        return matchedCodesArray;
    }

    public void setMatchedCodesArray(String matchedCodesArray) {
        this.matchedCodesArray = matchedCodesArray;
        // Synchronize with individual codes array - split the 350-character string into 10 entries of 35 chars each
        if (matchedCodesArray != null && matchedCodesArray.length() == 350) {
            this.matchedCodes = new String[10];
            for (int i = 0; i < 10; i++) {
                int start = i * 35;
                int end = start + 35;
                this.matchedCodes[i] = matchedCodesArray.substring(start, end);
            }
        }
    }

    public String[] getMatchedCodes() {
        return matchedCodes;
    }

    public void setMatchedCodes(String[] matchedCodes) {
        this.matchedCodes = matchedCodes;
        // Synchronize with array field - concatenate all entries into a single 350-character string
        if (matchedCodes != null && matchedCodes.length == 10) {
            StringBuilder sb = new StringBuilder(350);
            for (String code : matchedCodes) {
                if (code != null) {
                    // Pad or truncate to exactly 35 characters
                    if (code.length() >= 35) {
                        sb.append(code.substring(0, 35));
                    } else {
                        sb.append(String.format("%-35s", code));
                    }
                } else {
                    sb.append(String.format("%-35s", ""));
                }
            }
            this.matchedCodesArray = sb.toString();
        }
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public DistanceResult getDistanceResult() {
        return distanceResult;
    }

    public void setDistanceResult(DistanceResult distanceResult) {
        this.distanceResult = distanceResult;
    }

    /**
     * Nested class representing the distance result structure from COBOL
     */
    public static class DistanceResult {
        /** Distance in kilometers (formatted with comma) */
        private String distanceKm;

        /** Distance in miles (formatted with comma) */
        private String distanceMiles;

        public String getDistanceKm() {
            return distanceKm;
        }

        public void setDistanceKm(String distanceKm) {
            this.distanceKm = distanceKm;
        }

        public String getDistanceMiles() {
            return distanceMiles;
        }

        public void setDistanceMiles(String distanceMiles) {
            this.distanceMiles = distanceMiles;
        }
    }

    // Convenience methods for backward compatibility
    public String getDistanceKm() {
        return distanceResult != null ? distanceResult.getDistanceKm() : null;
    }

    public void setDistanceKm(String distanceKm) {
        if (distanceResult == null) {
            distanceResult = new DistanceResult();
        }
        distanceResult.setDistanceKm(distanceKm);
    }

    public String getDistanceMiles() {
        return distanceResult != null ? distanceResult.getDistanceMiles() : null;
    }

    public void setDistanceMiles(String distanceMiles) {
        if (distanceResult == null) {
            distanceResult = new DistanceResult();
        }
        distanceResult.setDistanceMiles(distanceMiles);
    }
}