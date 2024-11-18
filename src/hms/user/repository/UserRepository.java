package hms.user.repository;

import hms.common.AbstractRepository;
import hms.common.SearchCriterion;
import hms.manager.ManagerContext;
import hms.user.model.User;
import java.util.List;

/**
 * Abstract base class for a repository that manages a collection of users.
 * 
 * @param <T> The type of user that this repository manages, which must extend the User class.
 */
public abstract class UserRepository<T extends User> extends AbstractRepository<T> {
    public UserRepository(ManagerContext managerContext) {
        super(managerContext);
    }

    /**
     * Activates the user with the specified ID.
     *
     * @param id The ID of the user to activate.
     */
    public void activateUser(String id) {
        models.stream().filter(user -> user.getAccount().getId().equals(id))
                       .forEach(user -> user.getAccount().setActive(true));
    }

    /**
     * Deactivates the user with the specified ID.
     *
     * @param id The ID of the user to deactivate.
     */
    public void deactivateUser(String id) {
        models.stream().filter(user -> user.getAccount().getId().equals(id))
                       .forEach(user -> user.getAccount().setActive(false));
    }

    /**
     * Retrieves the user with the specified ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID, or null if no such user exists.
     */
    @Override
    public T get(String id) {
        return findWithFilters(List.of(
            new SearchCriterion<>((T u) -> u.getAccount().getId(), id)
        )).stream().findFirst().orElse(null);
    }
}
