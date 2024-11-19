package hms.manager;

import hms.common.AbstractRepository;
import hms.utils.CSVFileHandler;

/**
 * Abstract class for a simple manager which manages one type of repository
 * This class provides a basic implementation of the IManager interface
 * @param <T> Repository type
 */
public abstract class AbstractManager<T extends AbstractRepository<?>> implements IManager {
    /** Whether the manager has been initialized */
    private boolean initialized = false;
    /** File path */
    private final String filepath;
    /** Manager context that the manager belongs to */
    protected final ManagerContext ctx;
    /** Repository that the manager manages */
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

    @Override
    public void initialize() {
        if (!initialized) {
            load();
            initialized = true;
        }
    }

    @Override
    public void load(){
        CSVFileHandler csvFileHandler = new CSVFileHandler();

        repository.parse(csvFileHandler.read(filepath));
    }

    @Override
    public void save(){
        CSVFileHandler csvFileHandler = new CSVFileHandler();

        csvFileHandler.write(filepath, repository.serialize());
    }
}
