package hms.system.service;

import hms.ui.MenuNavigator;

/**
 * Interface for all services in the system. All services must implement this interface.
 * Services are used to perform operations on the system and communicate with the System layer,
 * the Manager layer, and the Model layer (just for getting data or updating public variables).
 */
public interface IService {
    /**
     * Executes the service. This method is called by the MenuNavigator when the user selects
     * a menu option that corresponds to this service.
     * @param menuNav The MenuNavigator object that called this service.
     */
    public void execute(MenuNavigator menuNav);
}
