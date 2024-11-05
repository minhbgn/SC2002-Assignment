package hms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Prompt implements IUIElement {
    private static final ArrayList<String> BOOLEAN_TRUE_VALUES = new ArrayList<>(
        List.of("yes", "y", "true", "t", "1")
    );
    private static final ArrayList<String> BOOLEAN_FALSE_VALUES = new ArrayList<>(
        List.of("no", "n", "false", "f", "0")
    );
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    
    private final String message;

    public Prompt(String message){
        this.message = message;
    }

    public int getIntInput(){
        try {
            return Integer.parseInt(InputHandler.getInstance().getInput());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer value.");
            display(); // Display the prompt again
            return getIntInput();
        }
    }

    public String getStringInput(){
        return InputHandler.getInstance().getInput();
    }

    public boolean getBooleanInput(){
        String input = InputHandler.getInstance().getInput();
        
        if (BOOLEAN_TRUE_VALUES.contains(input.toLowerCase())){
            return true;
        }
        else if (BOOLEAN_FALSE_VALUES.contains(input.toLowerCase())){
            return false;
        }
        else {
            System.out.println("Invalid input. Please enter a valid boolean value (y/n)");
            display(); // Display the prompt again
            return getBooleanInput();
        }
    }

    public Date getDateInput(){
        System.out.println("Please enter the date in the format YYYY-MM-DD");

        String input = InputHandler.getInstance().getInput();

        try {
            return dateFormatter.parse(input);
        } catch (ParseException e) {
            System.out.println("Invalid date. Please enter a valid date in the format YYYY-MM-DD");
            display(); // Display the prompt again
            return getDateInput();
        }
    }

    @Override
    public void display(){
        System.out.println(message);
    }
}
