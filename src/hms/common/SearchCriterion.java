package hms.common;

import java.util.function.Function;

public class SearchCriterion <S, T> {
    private final Function<S, T> getter;
    private final T value;

    public SearchCriterion(Function<S, T> getter, T value) {
        this.getter = getter;
        this.value = value;
    }

    public boolean match(S obj) {
        return getter.apply(obj).equals(value);
    }
}
