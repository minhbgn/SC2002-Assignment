package hms.common;

import java.util.function.Function;

/**
 * Represents a search criterion used to filter models in the hospital management system.
 *
 * @param <S> the type of the object to be filtered
 * @param <T> the type of the value to be matched
 */
public class SearchCriterion <S, T> {
    /** The function to retrieve the value from the object. */
    private final Function<S, T> getter;
    /** The value to be matched. */
    private final T value;

    /**
     * Constructs a new SearchCriterion with the specified getter function and value.
     *
     * @param getter the function to retrieve the value from the object
     * @param value the value to be matched
     */
    public SearchCriterion(Function<S, T> getter, T value) {
        this.getter = getter;
        this.value = value;
    }

    /**
     * Checks if the specified object matches the search criterion.
     *
     * @param obj the object to be checked
     * @return true if the object matches the search criterion, false otherwise
     */
    public boolean match(S obj) {
        return getter.apply(obj).equals(value);
    }
}