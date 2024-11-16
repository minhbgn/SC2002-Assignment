package hms.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
    public ArrayList<HashMap<String, String>> read(String filePath) {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            String[] headers = {};
            if (scanner.hasNextLine()) {
                headers = scanner.nextLine().split(",");

                // Remove quotes from headers
                for (int i = 0; i < headers.length; i++) {
                    headers[i] = headers[i].strip();
                    headers[i] = headers[i].replace("\"", "");
                }
            }
            while (scanner.hasNextLine()) {
                String[] values = scanner.nextLine().split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].strip();
                    values[i] = values[i].replace("\"", "");
                }

                HashMap<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], values[i]);
                }
                
                data.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    /**
     * Writes data to a CSV file.
     *
     * @param filePath the path to the CSV file
     * @param data the data to be written to the CSV file
     */
    public void write(String filePath, List<HashMap<String,String>> data) {
        if (data.isEmpty()) {
            return;
        }
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            // Write headers
            String[] headers = data.get(0).keySet().toArray(String[]::new);
            writer.println(String.join(",", headers));

            // Write rows
            for (HashMap<String, String> row : data) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    values.add(row.getOrDefault(header, ""));
                }
                writer.println(String.join(",", values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}