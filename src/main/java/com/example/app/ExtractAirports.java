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
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

/**
 * Deterministic flat export of the Micro Focus ISAM file `airports.dat`.
 * Record layout (from airports.str), fixed 104 bytes, key = f-code:
 *   f-code    PIC X(4)    1:4
 *   f-name    PIC X(30)   5:30
 *   f-city    PIC X(30)   35:30
 *   f-country PIC X(20)   65:20
 *   f-geo     20 bytes    85:20
 *     f-lat-sign  X      85:1   f-lat-degs 9(3) 86:3  f-lat-mins 9(6) 89:6
 *     f-long-sign X      95:1   f-long-degs 9(3) 96:3 f-long-mins 9(6) 99:6
 * Emits: airports.seq (104-byte fixed sequential, 1:1 to f-rec) + airports.csv (review).
 */
public class ExtractAirports {

    /** Record size constant */
    private static final int REC = 104;

    /** Pattern for validating airport codes */
    private static final Pattern codePattern = Pattern.compile("^[A-Z0-9 ]{4}$");

    /** Pattern for validating geographic coordinates */
    private static final Pattern geoPattern = Pattern.compile("^[+-]\\d{9}[+-]\\d{9}$");

    /** Pattern for validating printable characters */
    private static final Pattern printablePattern = Pattern.compile("^[\\x20-\\x7E]+$");

    /**
     * Main entry point for airport data extraction
     */
    public static void main(String[] args) throws IOException {
        byte[] src = Files.readAllBytes(Paths.get("src/airport/airports.dat"));

        Map<String, byte[]> records = extractRecords(src);

        List<String> keys = new ArrayList<>(records.keySet());
        Collections.sort(keys);

        writeSequentialFile(records, keys);
        writeCsvFile(records, keys);

        System.out.printf("extracted %d records -> airports.seq (%d bytes), airports.csv%n",
                          keys.size(), keys.size() * REC);
        System.out.printf("first: %s  last: %s%n",
                          keys.get(0), keys.get(keys.size() - 1));
        System.out.printf("sample KEF present: %s  LHR present: %s%n",
                          records.containsKey("KEF"), records.containsKey("LHR"));
    }

    /**
     * Convert bytes to ASCII string using latin1 encoding
     */
    private static String ascii(byte[] bytes) {
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    /**
     * Validate airport code format
     */
    private static boolean isCode(String s) {
        return codePattern.matcher(s).matches();
    }

    /**
     * Validate geographic coordinate format
     */
    private static boolean isGeo(String s) {
        return geoPattern.matcher(s).matches();
    }

    /**
     * Validate printable character format
     */
    private static boolean isPrintable(String s) {
        return printablePattern.matcher(s).matches();
    }

    /**
     * Extract and validate airport records from source data
     */
    private static Map<String, byte[]> extractRecords(byte[] src) {
        Map<String, byte[]> recs = new HashMap<>();

        for (int i = 0; i + REC <= src.length; i++) {
            byte[] slice = Arrays.copyOfRange(src, i, i + REC);
            String code = ascii(Arrays.copyOfRange(slice, 0, 4));
            String name = ascii(Arrays.copyOfRange(slice, 4, 34));
            String geo = ascii(Arrays.copyOfRange(slice, 84, 104));

            if (!isCode(code) || code.trim().isEmpty() || !isGeo(geo) || !isPrintable(name)) {
                continue;
            }

            String key = code.trim();
            if (!recs.containsKey(key)) {
                recs.put(key, slice);
            }

            // a valid record consumes its slot; skip ahead
            i += REC - 1;
        }

        return recs;
    }

    /**
     * Write sequential binary file output
     */
    private static void writeSequentialFile(Map<String, byte[]> records, List<String> keys) throws IOException {
        byte[] seq = new byte[keys.size() * REC];

        for (int idx = 0; idx < keys.size(); idx++) {
            byte[] record = records.get(keys.get(idx));
            System.arraycopy(record, 0, seq, idx * REC, REC);
        }

        Files.write(Paths.get("src/airport/airports.seq"), seq);
    }

    /**
     * Write CSV file output for review
     */
    private static void writeCsvFile(Map<String, byte[]> records, List<String> keys) throws IOException {
        List<String> csv = new ArrayList<>();
        csv.add("code,name,city,country,lat_sign,lat_degs,lat_mins,long_sign,long_degs,long_mins");

        for (String k : keys) {
            String r = ascii(records.get(k));

            String[] fields = {
                r.substring(0, 4).trim(),
                r.substring(4, 34).trim(),
                r.substring(34, 64).trim(),
                r.substring(64, 84).trim(),
                r.substring(84, 85),
                r.substring(85, 88),
                r.substring(88, 94),
                r.substring(94, 95),
                r.substring(95, 98),
                r.substring(98, 104)
            };

            StringBuilder line = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                if (i > 0) line.append(",");
                line.append(escapeCsv(fields[i]));
            }

            csv.add(line.toString());
        }

        Files.write(Paths.get("src/airport/airports.csv"),
                   (String.join("\n", csv) + "\n").getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Escape CSV field if it contains commas
     */
    private static String escapeCsv(String field) {
        if (field.contains(",")) {
            return "\"" + field + "\"";
        }
        return field;
    }
}