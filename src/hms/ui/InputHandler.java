package hms.ui;

import java.util.Scanner;

/**
 * InputHandler class is a singleton class that handles the input from the user.
 * It uses the Scanner class to get the input from the user.
 */
public class InputHandler {
    /** The instance of the InputHandler class. */
    public static InputHandler Instance = null;
    /** The scanner object to get the input from the user. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Private constructor of the InputHandler class.
     * <p>
     * It is private to prevent the creation of multiple instances of the class.
     * Instead, the getInstance method should be used to get the instance of the class.
     */
    private InputHandler() { }

    /**
     * Returns the instance of the InputHandler class.
     * If the instance is null, it creates a new instance of the class
     * and returns it.
     * @return The instance of the InputHandler class.
     */
    public static InputHandler getInstance() {
        if (Instance == null) {
            Instance = new InputHandler();
        }
        return Instance;
    }

    /**
     * Gets the input from the user.
     * @return The input from the user.
     */
    public String getInput() { return scanner.nextLine(); }
}
