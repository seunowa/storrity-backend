/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.util.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Seun Owa
 * 
 * Minimal, dependency-free CSV reader/writer.
 *
 * Supports the common RFC4180 rules we actually need here:
 *  - fields separated by commas
 *  - fields containing a comma, a double quote, or a newline are wrapped in double quotes
 *  - a double quote inside a quoted field is escaped as two double quotes ("")
 *
 * Deliberately does NOT try to be a general-purpose CSV library — it's just enough
 * to make product import/export round-trip cleanly and stay editable in Excel/Notepad.
 */
public class CsvUtils {
    private CsvUtils() {
    }

    /**
     * Reads an entire CSV stream into rows of raw string fields.
     * The first row (if present) is the header — callers are responsible for treating it as such.
     */
    public static List<String[]> readAll(InputStream in) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder field = new StringBuilder();
            List<String> currentRow = new ArrayList<>();
            boolean inQuotes = false;
            boolean rowHasContent = false;

            int c;
            while ((c = reader.read()) != -1) {
                char ch = (char) c;

                if (inQuotes) {
                    if (ch == '"') {
                        reader.mark(1);
                        int next = reader.read();
                        if (next == '"') {
                            field.append('"'); // escaped quote
                        } else {
                            inQuotes = false;
                            if (next != -1) {
                                reader.reset();
                            }
                        }
                    } else {
                        field.append(ch);
                    }
                    rowHasContent = true;
                    continue;
                }

                switch (ch) {
                    case '"':
                        inQuotes = true;
                        rowHasContent = true;
                        break;
                    case ',':
                        currentRow.add(field.toString());
                        field.setLength(0);
                        rowHasContent = true;
                        break;
                    case '\r':
                        // ignore, \n (or EOF) will terminate the row
                        break;
                    case '\n':
                        currentRow.add(field.toString());
                        field.setLength(0);
                        rows.add(currentRow.toArray(new String[0]));
                        currentRow = new ArrayList<>();
                        rowHasContent = false;
                        break;
                    default:
                        field.append(ch);
                        rowHasContent = true;
                }
            }

            // flush trailing row that wasn't newline-terminated
            if (rowHasContent || field.length() > 0 || !currentRow.isEmpty()) {
                currentRow.add(field.toString());
                rows.add(currentRow.toArray(new String[0]));
            }
        }
        return rows;
    }

    public static String escapeField(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuoting = value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r");
        if (!needsQuoting) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public static String toCsvLine(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escapeField(fields[i]));
        }
        return sb.toString();
    }

    /**
     * Small streaming writer so callers don't have to buffer the whole export in memory.
     */
    public static class RowWriter implements AutoCloseable {
        private final Writer writer;

        public RowWriter(OutputStream out) {
            this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        }

        public void writeRow(String[] fields) throws IOException {
            writer.write(toCsvLine(fields));
            writer.write("\r\n");
        }

        @Override
        public void close() throws IOException {
            writer.flush();
            writer.close();
        }
    }
}
