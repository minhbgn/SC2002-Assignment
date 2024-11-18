package hms.ui;

public class UserOption {
    private final String text;
    private final Runnable action;

    /**
     * Constructs a UserOption with the specified text and action.
     *
     * @param text   the text description of the user option
     * @param action the action to be performed when the user option is selected
     */
    public UserOption(String text, Runnable action){
        this.text = text;
        this.action = action;
    }

    /**
     * Returns the text description of the user option.
     * @return the text description
     */
    public String getText(){
        return text;
    }

    /**
     * Executes the action associated with this user option.
     */
    public void execute(){
        action.run();
    }
}
