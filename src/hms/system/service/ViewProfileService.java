package hms.system.service;

import hms.ui.AbstractMenu;
import hms.ui.MenuNavigator;
import hms.ui.Prompt;
import hms.ui.SimpleMenu;
import hms.ui.UserOption;
import hms.user.model.Admin;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.model.Pharmacist;
import hms.user.model.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewProfileService implements IService {
    /** The user to view the profile of */
    private final User user;

    // Options
    private final UserOption updateDobOption = new UserOption("Update Date of Birth", this::handleUpdateDob);
    private final UserOption updateContactOption = new UserOption("Update Contact", this::handleUpdateContact);
    private final UserOption changePasswordOption = new UserOption("Change password", this::handleChangePassword);

    /** Profile menu, saved to allow for easy updating */
    private SimpleMenu profileMenu;

    public ViewProfileService(User user) {
        this.user = user;
    }

    private void handleUpdateDob() {
        Date newDob = Prompt.getDateInput("Enter your new date of birth: ");
        user.dob = newDob;

        // Update the profile menu title with the new information
        profileMenu.title = getProfileInfo();
    }

    private void handleUpdateContact() {
        String newContact = Prompt.getStringInput("Enter your new contact info: ");
        user.contact = newContact;

        // Update the profile menu title with the new information
        profileMenu.title = getProfileInfo();
    }

    private void handleChangePassword() {
        String oldPassword = Prompt.getStringInput("Enter your old password: ");
        if(!user.getAccount().authenticate(oldPassword)){
            System.out.println("Wrong password");
            return;
        }

        String newPassword = Prompt.getStringInput("Enter your new password: ");
        String newPasswordConfirm = Prompt.getStringInput("Confirm your new password: ");
    
        if(!newPassword.equals(newPasswordConfirm)){
            System.out.println("Passwords do not match");
            return;
        }
        
        user.getAccount().setPassword(newPassword);
        System.out.println("Password changed successfully");
    }

    /**
     * Utility method to get the account type text
     * @return The account type text
     */
    private String getAccountTypeText() {
        if (user instanceof Patient) return "Patient";
        if (user instanceof Doctor) return "Doctor";
        if (user instanceof Pharmacist) return "Pharmacist";
        if (user instanceof Admin) return "Admin";

        return "Unknown Account Type";
    }

    /**
     * Utility method to get the profile information
     * @return The profile information
     */
    private String getProfileInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return String.format("""
            Patient Profile\n\n\
            Name: %s\n\
            Gender: %s\n\
            Contact: %s\n\
            Date of Birth: %s\n\
            Account Type: %s\n\
            Account Status: %s""",
            user.name, (user.isMale ? "Male" : "Female"), user.contact, sdf.format(user.dob),
            getAccountTypeText(), (user.getAccount().isActive() ? "Active" : "Inactive")
        );
    }

    private AbstractMenu getMenu() {
        profileMenu = new SimpleMenu(getProfileInfo(), List.of(
            updateContactOption, updateDobOption, changePasswordOption
        ));

        return profileMenu;
    }

    @Override
    public void execute(MenuNavigator menuNav) {
        menuNav.addMenu(getMenu());
    }
}
