package hms.manager;

import hms.common.IModel;
import hms.common.SearchCriterion;
import hms.common.id.IdManager;
import hms.common.id.IdParser;
import hms.user.model.Admin;
import hms.user.model.Doctor;
import hms.user.model.Patient;
import hms.user.model.Pharmacist;
import hms.user.model.User;
import hms.user.repository.AdminRepository;
import hms.user.repository.DoctorRepository;
import hms.user.repository.PatientRepository;
import hms.user.repository.PharmacistRepository;
import hms.user.repository.UserRepository;
import hms.utils.CSVFileHandler;
import java.util.HashMap;
import java.util.List;

/**
 * Manager class for all user types.
 */
public class UserManager implements IManager {
    private boolean initialized = false;
    // Maps the repository class to the file path
    private final HashMap<Class<?>, String> repoFilepaths = new HashMap<>();
    // Maps the repository class to the repository instance
    private final HashMap<Class<?>, UserRepository<?>> repositories = new HashMap<>();
    // Maps the user class to the repository instance
    private final HashMap<Class<?>, UserRepository<?>> userToRepositories = new HashMap<>();

    /**
     * Constructor for the UserManager.
     * @param ctx The ManagerContext.
     * @param patientRepoFilepath The file path to the patient repository.
     * @param doctorRepoFilepath The file path to the doctor repository.
     * @param pharmacistRepoFilepath The file path to the pharmacist repository.
     * @param adminRepoFilepath The file path to the admin repository.
     */
    public UserManager(
        ManagerContext ctx,
        String patientRepoFilepath, 
        String doctorRepoFilepath, 
        String pharmacistRepoFilepath, 
        String adminRepoFilepath
    ) {
        // Initialize the repository file paths mappping
        repoFilepaths.put(PatientRepository.class, patientRepoFilepath);
        repoFilepaths.put(DoctorRepository.class, doctorRepoFilepath);
        repoFilepaths.put(PharmacistRepository.class, pharmacistRepoFilepath);
        repoFilepaths.put(AdminRepository.class, adminRepoFilepath);

        // Initialize the repositories
        repositories.put(PatientRepository.class, new PatientRepository(ctx));
        repositories.put(DoctorRepository.class, new DoctorRepository(ctx));
        repositories.put(PharmacistRepository.class, new PharmacistRepository(ctx));
        repositories.put(AdminRepository.class, new AdminRepository(ctx));

        // Initialize the user to repository mapping
        userToRepositories.put(Patient.class, repositories.get(PatientRepository.class));
        userToRepositories.put(Doctor.class, repositories.get(DoctorRepository.class));
        userToRepositories.put(Pharmacist.class, repositories.get(PharmacistRepository.class));
        userToRepositories.put(Admin.class, repositories.get(AdminRepository.class));
    }

    /**
     * Authenticates a user.
     * @param id The ID of the user.
     * @param password The password of the user.
     * @return The user class if the authentication is successful, null otherwise.
     */
    @SuppressWarnings("unchecked")
    public Class<? extends User> authenticate(String id, String password) {
        try {
            Class<? extends IModel> userClass = IdParser.getClass(id);

            if (!User.class.isAssignableFrom(userClass)) {
                return null;
            }

            UserRepository<? extends User> repository = getRepositoryByUser((Class<? extends User>) userClass);
            
            User user = repository.get(id);
    
            return (user != null && user.getAccount().authenticate(password)) ?
                (Class<? extends User>) userClass : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Checks if a user exists.
     * @param id The ID of the user.
     * @return True if the user exists, false otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean hasUser(String id) {
        try {
            Class<? extends IModel> userClass = IdParser.getClass(id);
    
            if (!User.class.isAssignableFrom(userClass)) return false;
            
            return getRepositoryByUser((Class<? extends User>) userClass).get(id) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get the repository matching the user class.
     * @param userClass The user class.
     * @return The repository.
     */
    @SuppressWarnings("unchecked")
    public <T extends User> UserRepository<T> getRepositoryByUser(Class<T> userClass) {
        if (userToRepositories.containsKey(userClass)) {
            return (UserRepository<T>) userToRepositories.get(userClass);
        }
        else{
            throw new IllegalArgumentException("Repository not found");
        }
    }

    /**
     * Get the repository matching the repository class.
     * @param repositoryClass The repository class.
     * @return The repository.
     */
    @SuppressWarnings("unchecked")
    public <T extends UserRepository<?>> T getRepository(Class<T> repositoryClass) {
        if (repositories.containsKey(repositoryClass)) {
            return (T) repositories.get(repositoryClass);
        }
        else{
            throw new IllegalArgumentException("Repository not found");
        }
    }
    
    /**
     * Get a list of user that satisfies a list of criteria
     * @param userClass	Type of user we need to find
     * @param criteria The search criteria
     * @return the list of user that satisfies all criteria
     */
    public <T extends User> List<T> getUser(Class<T> userClass, 
    		List<SearchCriterion<T,?>> criteria){
    	UserRepository<T> repository = getRepositoryByUser(userClass);
        
        return repository.findWithFilters(criteria);
    }

    @Override
    public void initialize() {
        if (!initialized) {
            IdManager.registerClass(Patient.class, "PA");
            IdManager.registerClass(Doctor.class, "DO");
            IdManager.registerClass(Pharmacist.class, "PH");
            IdManager.registerClass(Admin.class, "AD");
            
            load();

            initialized = true;
        }
    }
    
    @Override
    public void load() {
        CSVFileHandler csvFileHandler = new CSVFileHandler();
        
        // Iterate through all repositories
        for (Class<?> repoClass : repoFilepaths.keySet()) {
            // Retrieve the file path and repository
            String filepath = repoFilepaths.get(repoClass);
            UserRepository<?> repository = repositories.get(repoClass);

            // Read the CSV file and parse the data into the repository
            repository.parse(csvFileHandler.read(filepath));
        }   
    }    

	@Override
	public void save() {
        CSVFileHandler csvFileHandler = new CSVFileHandler();
        
        // Iterate through all repositories
        for (Class<?> repoClass : repoFilepaths.keySet()) {
            // Retrieve the file path and repository
            String filepath = repoFilepaths.get(repoClass);
            UserRepository<?> repository = repositories.get(repoClass);

            // Serialize the repository and write it to the CSV file
            csvFileHandler.write(filepath, repository.serialize());
        }
	}
}
