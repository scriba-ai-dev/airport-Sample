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

// Stub annotation for compilation
@interface Size {
    int max() default Integer.MAX_VALUE;
    int min() default 0;
    String message() default "";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}

/**
 * Airport parameters DTO for function dispatch and data exchange
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

    /** Operation function code (1 byte) */
    @Size(max = 1)
    private String function;

    /** First airport code (4 bytes) */
    @Size(max = 4)
    private String airport1;

    /** Second airport code (4 bytes) */
    @Size(max = 4)
    private String airport2;

    /** Search prefix text (4 bytes) */
    @Size(max = 4)
    private String prefixText;

    /** Distance in kilometers (6 bytes, zz,zz9 format) */
    @Size(max = 6)
    private String distanceKm;

    /** Distance in miles (6 bytes, zz,zz9 format) */
    @Size(max = 6)
    private String distanceMiles;

    /** Raw matched codes data (350 bytes) */
    @Size(max = 350)
    private String matchedCodesArray;

    /** Array view of matched codes (10 entries of 35 bytes each) */
    private String[] matchedCodes;

    /** File operation status code (2 bytes) */
    @Size(max = 2)
    private String fileStatus;

    public Airparams() {
        // Initialize arrays to proper size
        this.matchedCodes = new String[10];
        for (int i = 0; i < matchedCodes.length; i++) {
            matchedCodes[i] = "";
        }
        // Initialize strings to empty
        this.function = "";
        this.airport1 = "";
        this.airport2 = "";
        this.prefixText = "";
        this.distanceKm = "";
        this.distanceMiles = "";
        this.matchedCodesArray = "";
        this.fileStatus = "";
    }

    // Convenience methods for function code checking (88-level equivalents)
    public boolean isGetMatches() {
        return GET_MATCHES.equals(function);
    }

    public boolean isGetDistance() {
        return GET_DISTANCE.equals(function);
    }

    public boolean isGetDetails() {
        return GET_DETAILS.equals(function);
    }

    public boolean isOpenFile() {
        return OPEN_FILE.equals(function);
    }

    public boolean isCloseFile() {
        return CLOSE_FILE.equals(function);
    }

    public boolean isDisplayRecord() {
        return DISPLAY_RECORD.equals(function);
    }

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

    public String getMatchedCodesArray() {
        return matchedCodesArray;
    }

    public void setMatchedCodesArray(String matchedCodesArray) {
        this.matchedCodesArray = matchedCodesArray;
        // Synchronize array view - split into 10 chunks of 35 characters each
        if (matchedCodesArray != null) {
            for (int i = 0; i < 10; i++) {
                int start = i * 35;
                int end = Math.min(start + 35, matchedCodesArray.length());
                if (start < matchedCodesArray.length()) {
                    matchedCodes[i] = matchedCodesArray.substring(start, end);
                } else {
                    matchedCodes[i] = "";
                }
            }
        }
    }

    public String[] getMatchedCodes() {
        return matchedCodes;
    }

    public void setMatchedCodes(String[] matchedCodes) {
        this.matchedCodes = matchedCodes;
        // Synchronize raw array - rebuild from individual codes
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String code = (i < matchedCodes.length && matchedCodes[i] != null) ? matchedCodes[i] : "";
            // Pad or truncate to exactly 35 characters
            if (code.length() > 35) {
                code = code.substring(0, 35);
            } else {
                code = String.format("%-35s", code);
            }
            sb.append(code);
        }
        this.matchedCodesArray = sb.toString();
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }
}