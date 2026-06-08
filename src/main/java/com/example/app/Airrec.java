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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) @Retention(RetentionPolicy.RUNTIME) @interface Size { int max(); }

/**
 * Airport record structure representing airport information with code, name, city, country and geographic coordinates
 */
public class Airrec {
    @Size(max=4)
    private String code;

    @Size(max=30)
    private String name;

    @Size(max=30)
    private String city;

    @Size(max=20)
    private String country;

    @Size(max=1)
    private String latSign;

    @Size(max=3)
    private String latDegs;

    @Size(max=6)
    private String latMins;

    @Size(max=1)
    private String longSign;

    @Size(max=3)
    private String longDegs;

    @Size(max=6)
    private String longMins;

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

    public String getLatSign() {
        return latSign;
    }

    public void setLatSign(String latSign) {
        this.latSign = latSign;
    }

    public String getLatDegs() {
        return latDegs;
    }

    public void setLatDegs(String latDegs) {
        this.latDegs = latDegs;
    }

    public String getLatMins() {
        return latMins;
    }

    public void setLatMins(String latMins) {
        this.latMins = latMins;
    }

    public String getLongSign() {
        return longSign;
    }

    public void setLongSign(String longSign) {
        this.longSign = longSign;
    }

    public String getLongDegs() {
        return longDegs;
    }

    public void setLongDegs(String longDegs) {
        this.longDegs = longDegs;
    }

    public String getLongMins() {
        return longMins;
    }

    public void setLongMins(String longMins) {
        this.longMins = longMins;
    }
}