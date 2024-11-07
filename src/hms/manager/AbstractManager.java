package hms.manager;

import hms.common.AbstractRepository;
import hms.utils.CSVFileHandler;

/**
 * Abstract class for all managers
 * @param <T> Repository type
 */
public abstract class AbstractManager<T extends AbstractRepository<?>> implements IManager {
    private boolean initialized = false;
    private final String filepath;
    protected final ManagerContext ctx;
    protected T repository;

    /**
     * Constructor for the AbstractManager
     * @param ctx Manager context
     * @param filepath File path
     */
    public AbstractManager(ManagerContext ctx, String filepath){
        this.ctx = ctx;
        this.filepath = filepath;
    }

    /**
     * Initialize the manager
     */
    @Override
    public void initialize() {
        if (!initialized) {
            load();
            initialized = true;
        }
    }

    /**
     * Load data from file
     */
    @Override
    public void load(){
        CSVFileHandler csvFileHandler = new CSVFileHandler();

        repository.parse(csvFileHandler.read(filepath));
    }

    /**
     * Save data to file
     */
    @Override
    public void save(){
        CSVFileHandler csvFileHandler = new CSVFileHandler();

        csvFileHandler.write(filepath, repository.serialize());
    }
}
