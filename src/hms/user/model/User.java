package hms.user.model;

import java.util.Date;
import java.util.HashMap;

import hms.common.IModel;
import hms.manager.ManagerContext;

public abstract class User implements IModel {
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
    public String toString(){
        throw new UnsupportedOperationException("Unimplemented method 'toString'");
    }

    @Override
    public void hydrate(HashMap<String, String> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hydrate'");
    }

    @Override
    public HashMap<String, String> serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }
}
