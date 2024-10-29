package hms.manager;

import java.util.HashMap;

import hms.user.repository.UserRepository;

public class UserManager implements IManager {
    private final HashMap<Class<?>, UserRepository<?>> repositories = new HashMap<>();

    public UserManager(
        String patientRepoFilepath, 
        String doctorRepoFilepath, 
        String pharmacistRepoFilepath, 
        String adminRepoFilepath
    ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T extends UserRepository<?>> T getRepository(Class<T> repositoryClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final HashMap<String, String> load(String filepath) {
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public final void save(String filepath, HashMap<String, String> data) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
