package hms.utils;

import java.util.HashMap;

public class CSVFileHandler {
    private String filePath;

    public CSVFileHandler(String filePath) {
        this.filePath = filePath;
    }

    public HashMap<String,String> read() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void write(HashMap<String,String> data) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
