package hms.user.model;

import hms.common.IModel;
import hms.manager.ManagerContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class User implements IModel {
    protected Account account;
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
        this.account.hydrate(data);

        this.name = data.getOrDefault("name", "Unknown");
        this.isMale = Boolean.parseBoolean(data.getOrDefault("isMale", "true"));
        this.contact = data.getOrDefault("contact", "N/A");

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.dob = dateFormat.parse(data.get("dob"));
        } catch (ParseException | NullPointerException e) {
            this.dob = null;
        }
    }

    @Override
    public HashMap<String, String> serialize() {
        HashMap<String, String> data = new HashMap<>();
        data.putAll(this.account.serialize());

        data.put("name", name != null ? name : "Unknown");
        data.put("isMale", String.valueOf(isMale));
        data.put("contact", contact != null ? contact : "N/A");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        data.put("dob", dob != null ? dateFormat.format(dob) : "N/A");

        return data;
    }
}
