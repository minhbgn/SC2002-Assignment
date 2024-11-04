package hms.user.model;

import hms.common.*;
import hms.manager.*;

import java.util.*;
import java.text.*;
public class User implements IModel {
    private final Account account;
    protected ManagerContext ctx;
    public String name;
    public boolean isMale;
    public String contact;
    public Date dob;

    public User(ManagerContext ctx) {
        this.ctx = ctx;
        this.account = new Account();
    }

    public User(ManagerContext ctx, String name, boolean isMale, String contact, Date dob) {
        this.ctx = ctx;
        this.account = new Account();
        this.name = name;
        this.isMale = isMale;
        this.contact = contact;
        this.dob = dob;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "User{" +
               "Name='" + name + '\'' +
               ", Gender=" + (isMale ? "Male" : "Female") +
               ", Contact='" + contact + '\'' +
               ", DOB=" + (dob != null ? dateFormat.format(dob) : "N/A") +
               '}';
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        this.name = data.getOrDefault("name", "Unknown");
        this.isMale = Boolean.parseBoolean(data.getOrDefault("isMale", "true"));
        this.contact = data.getOrDefault("contact", "N/A");

        String dobString = data.get("dob");
        if (dobString != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.dob = dateFormat.parse(dobString);
            } catch (ParseException e) {
                e.printStackTrace();
                this.dob = null;
            }
        } else {
            this.dob = null;
        }
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name != null ? name : "Unknown");
        data.put("isMale", String.valueOf(isMale));
        data.put("contact", contact != null ? contact : "N/A");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        data.put("dob", dob != null ? dateFormat.format(dob) : "N/A");

        return data;
    }
}
