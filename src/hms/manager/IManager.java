package hms.manager;

import java.util.HashMap;

public interface IManager {
    public HashMap<String,String> load(String filepath);
    public void save(String filepath, HashMap<String,String> data);
}
