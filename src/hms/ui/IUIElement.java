package hms.ui;

/**
 * Base interface for all UI elements. It provides a method to display the UI element.
 */
public interface IUIElement {
    /**
     * Displays the UI element.
     * <p>
     * Since this is a CLI application, all UI elements
     * should have an option to display the options, 
     * as the user can only interact with the application using the options.
     * @param showOptions Whether to display the options or not.
     */
    public void display(boolean showOptions);
}
