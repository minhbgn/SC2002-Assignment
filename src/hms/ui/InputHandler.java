package hms.ui;

import java.util.Scanner;

/**
 * Handles the input from the user
 */
public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Singleton implementation ò InputHandler: only 1 instance for the whole system
     */
    public static InputHandler Instance = null;

    private InputHandler() {
    }

    /**
     * Make one instance of this handler if none exist
     * @return an instance of the input handler
     */
    public static InputHandler getInstance() {
        if (Instance == null) {
            Instance = new InputHandler();
        }
        return Instance;
    }

    /**
     * Getting the input of the user, always returning in String type
     * @return the input of the user, in String type
     */
    public String getInput() {
        return scanner.nextLine();
    }
}
