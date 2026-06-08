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

/**
 * COBOL record structure for airport data with geographic coordinates
 */
public class Airrec {

    /** Airport code (4 characters) */
    private String code;

    /** Airport name (30 characters) */
    private String name;

    /** Airport city (30 characters) */
    private String city;

    /** Airport country (20 characters) */
    private String country;

    /** Geographic coordinates group */
    private Geo geo;

    public static class Geo {
    /** Latitude sign (1 character) */
        private String latSign;

    /** Latitude degrees (3 digits) */
        private int latDegs;

    /** Latitude minutes (6 digits) */
        private int latMins;

    /** Longitude sign (1 character) */
        private String longSign;

    /** Longitude degrees (3 digits) */
        private int longDegs;

    /** Longitude minutes (6 digits) */
        private int longMins;

        public String getLatSign() {
            return latSign;
        }

        public void setLatSign(String latSign) {
            this.latSign = latSign;
        }

        public int getLatDegs() {
            return latDegs;
        }

        public void setLatDegs(int latDegs) {
            this.latDegs = latDegs;
        }

        public int getLatMins() {
            return latMins;
        }

        public void setLatMins(int latMins) {
            this.latMins = latMins;
        }

        public String getLongSign() {
            return longSign;
        }

        public void setLongSign(String longSign) {
            this.longSign = longSign;
        }

        public int getLongDegs() {
            return longDegs;
        }

        public void setLongDegs(int longDegs) {
            this.longDegs = longDegs;
        }

        public int getLongMins() {
            return longMins;
        }

        public void setLongMins(int longMins) {
            this.longMins = longMins;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }
}