package hms.ui;

/** Represents a user option in the UI. */
public class UserOption {
    /** The text to display for this option. */
    private final String text;
    /** The action to perform when this option is selected. */
    private final Runnable action;

    /**
     * Creates a new UserOption.
     * @param text The text to display for this option.
     * @param action The action to perform when this option is selected.
     */
    public UserOption(String text, Runnable action){
        this.text = text;
        this.action = action;
    }

    public String getText(){ return text; }

    /** Perform the action */
    public void execute(){ action.run(); }
}
