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

/**
 * COBOL copybook airparams.cpy - Airport operation parameters and results
 */
public class Airparams {
    /**
     * Function code: 1=get-matches, 2=get-distance, 3=get-details, 4=open-file, 5=close-file, 6=display-record
     */
    private String function;

    /**
     * First airport code (4 characters)
     */
    private String airport1;

    /**
     * Second airport code (4 characters)
     */
    private String airport2;

    /**
     * Prefix text for matching (4 characters)
     */
    private String prefixText;

    /**
     * Distance in kilometers (6 characters, formatted zz,zz9)
     */
    private String distanceKm;

    /**
     * Distance in miles (6 characters, formatted zz,zz9)
     */
    private String distanceMiles;

    /**
     * Distance result structure containing both km and miles
     */
    public static class DistanceResult {
        public String distanceKm;
        public String distanceMiles;

        public DistanceResult(String km, String miles) {
            this.distanceKm = km;
            this.distanceMiles = miles;
        }
    }
    private DistanceResult distanceResult;

    /**
     * Raw matched codes array (350 characters)
     */
    private String matchedCodesArray;

    /**
     * Matched codes as array of 10 entries, 35 characters each
     */
    private String[] matchedCodes;

    /**
     * File operation status (2 characters)
     */
    private String fileStatus;

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
        return distanceResult != null ? distanceResult.distanceKm : null;
    }

    public void setDistanceKm(String distanceKm) {
        if (distanceResult == null) {
            distanceResult = new DistanceResult(distanceKm, null);
        } else {
            distanceResult.distanceKm = distanceKm;
        }
    }

    public String getDistanceMiles() {
        return distanceResult != null ? distanceResult.distanceMiles : null;
    }

    public void setDistanceMiles(String distanceMiles) {
        if (distanceResult == null) {
            distanceResult = new DistanceResult(null, distanceMiles);
        } else {
            distanceResult.distanceMiles = distanceMiles;
        }
    }

    public String getMatchedCodesArray() {
        return matchedCodesArray;
    }

    public void setMatchedCodesArray(String matchedCodesArray) {
        this.matchedCodesArray = matchedCodesArray;
        // Update the matchedCodes array when the raw array is set
        if (matchedCodesArray != null) {
            this.matchedCodes = new String[10];
            for (int i = 0; i < 10; i++) {
                int start = i * 35;
                int end = Math.min(start + 35, matchedCodesArray.length());
                if (start < matchedCodesArray.length()) {
                    String entry = matchedCodesArray.substring(start, end);
                    // Pad to 35 characters if necessary
                    if (entry.length() < 35) {
                        entry = String.format("%-35s", entry);
                    }
                    this.matchedCodes[i] = entry;
                } else {
                    this.matchedCodes[i] = String.format("%-35s", "");
                }
            }
        } else {
            this.matchedCodes = null;
        }
    }

    public String[] getMatchedCodes() {
        return matchedCodes;
    }

    public void setMatchedCodes(String[] matchedCodes) {
        this.matchedCodes = matchedCodes;
        // Update the raw array when the codes array is set
        if (matchedCodes != null) {
            StringBuilder sb = new StringBuilder(350);
            for (int i = 0; i < 10; i++) {
                String entry = i < matchedCodes.length && matchedCodes[i] != null ? matchedCodes[i] : "";
                // Ensure each entry is exactly 35 characters
                if (entry.length() > 35) {
                    entry = entry.substring(0, 35);
                } else if (entry.length() < 35) {
                    entry = String.format("%-35s", entry);
                }
                sb.append(entry);
            }
            this.matchedCodesArray = sb.toString();
        } else {
            this.matchedCodesArray = null;
        }
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    /**
     * Check if function is get-matches (value "1")
     */
    public boolean isGetMatches() {
        return "1".equals(function);
    }

    /**
     * Check if function is get-distance (value "2")
     */
    public boolean isGetDistance() {
        return "2".equals(function);
    }

    /**
     * Check if function is get-details (value "3")
     */
    public boolean isGetDetails() {
        return "3".equals(function);
    }

    /**
     * Check if function is open-file (value "4")
     */
    public boolean isOpenFile() {
        return "4".equals(function);
    }

    /**
     * Check if function is close-file (value "5")
     */
    public boolean isCloseFile() {
        return "5".equals(function);
    }

    /**
     * Check if function is display-record (value "6")
     */
    public boolean isDisplayRecord() {
        return "6".equals(function);
    }
}