package hms.system.service;

import hms.ui.MenuNavigator;

/**
 * Interface to implement different user services.
 * Used for polymorphism purposes
 */
public interface IService {
    public void execute(MenuNavigator menuNav);
}
