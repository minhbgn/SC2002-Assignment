package hms.utils;

import java.io.*;
import java.util.*;

/**
 * Utility class for handling CSV file operations.
 */
public class CSVFileHandler {

    /**
     * Reads a CSV file and returns the data as a list of hash maps.
     * Each hash map represents a row, with the keys being the column headers.
     *
     * @param filePath the path to the CSV file
     * @return a list of hash maps containing the CSV data
     */
    public List<HashMap<String, String>> read(String filePath) {
        List<HashMap<String, String>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            while ((line = reader.readLine()) != null) {
                if (headers == null) {
                    headers = parseCSVLine(line);
                } else {
                    String[] values = parseCSVLine(line);
                    HashMap<String, String> row = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        row.put(headers[i], values[i]);
                    }
                    data.add(row);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    // Double quote within quoted field, add one quote
                    current.append('\"');
                    i++; // Skip the next quote
                } else {
                    // Toggle the inQuotes flag
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                values.add(current.toString());
                current.setLength(0);
            } else {
                // Regular character
                current.append(c);
            }
        }
        values.add(current.toString());
        return values.toArray(new String[0]);
    }

    /**
     * Writes data to a CSV file.
     *
     * @param filePath the path to the CSV file
     * @param data the data to be written to the CSV file
     */
    public void write(String filePath, List<HashMap<String, String>> data) {
        if (data.isEmpty()) {
            return;
        }
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Write headers
            String[] headers = data.get(0).keySet().toArray(new String[0]);
            writer.println(String.join(",", headers));

            // Write rows
            for (HashMap<String, String> row : data) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    String value = row.getOrDefault(header, "");
                    // Replace " with ""
                    value = value.replace("\"", "\"\"");
                    if (value.contains(",") || value.contains("\"")) {
                        value = "\"" + value + "\"";
                    }
                    values.add(value);
                }
                writer.println(String.join(",", values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}