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
    }

    public void activateUser(String id) {
        models.stream().filter(user -> user.getAccount().getId().equals(id))
                       .forEach(user -> user.getAccount().setActive(true));
    }

    public void deactivateUser(String id) {
        models.stream().filter(user -> user.getAccount().getId().equals(id))
                       .forEach(user -> user.getAccount().setActive(false));
    }

    @Override
    public T get(String id) {
        return models.stream().filter(user -> user.getAccount().getId().equals(id))
                              .findFirst()
                              .orElse(null);
    }
}
