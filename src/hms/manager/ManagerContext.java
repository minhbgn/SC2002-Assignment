package hms.manager;

import java.util.HashMap;
import java.util.Map;

public class ManagerContext {
    private final Map<Class<?>, IManager> managers = new HashMap<>();

    public void addManager(Class<?> managerClass, IManager manager) {
        managers.put(managerClass, manager);
    }

    @SuppressWarnings("unchecked")
    public <T extends IManager> T getManager(Class<T> managerClass) {
        return (T) managers.get(managerClass);
    }
}
