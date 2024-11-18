package hms.ui;

public class UserOption {
    private final String text;
    private final Runnable action;

    public UserOption(String text, Runnable action){
        this.text = text;
        this.action = action;
    }

    public String getText(){
        return text;
    }

    public void execute(){
        action.run();
    }
}
