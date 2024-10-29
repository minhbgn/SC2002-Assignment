package hms.user.repository;

import hms.common.AbstractRepository;
import hms.manager.ManagerContext;
import hms.user.model.User;

/**
 * Abstract base class for a repository that manages a collection of users.
 * 
 * @param <T> The type of user that this repository manages, which must extend the User class.
 */
public abstract class UserRepository<T extends User> extends AbstractRepository<T> {
    public UserRepository(ManagerContext managerContext) {
        super(managerContext);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void activateUser(String id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deactivateUser(String id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public T get(String id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
