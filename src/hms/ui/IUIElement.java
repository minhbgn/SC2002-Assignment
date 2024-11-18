package hms.ui;

/**
 * Interface to implement the UI elements.
 * Used for polymorphism purposes
 */
public interface IUIElement {
    /**
     * Displays the UI element.
     * 
     * @param showOptions whether to show options or not
     */
    public void display(boolean showOptions);
}
