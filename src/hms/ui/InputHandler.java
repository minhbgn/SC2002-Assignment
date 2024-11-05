package hms.ui;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);
    public static InputHandler Instance = null;

    private InputHandler() {
    }

    public static InputHandler getInstance() {
        if (Instance == null) {
            Instance = new InputHandler();
        }
        return Instance;
    }

    public String getInput() {
        return scanner.nextLine();
    }
}
