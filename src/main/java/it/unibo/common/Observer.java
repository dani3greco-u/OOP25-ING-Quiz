package it.unibo.common;

/**
 * An observer in the observer pattern.
 * 
 * @param <T> the type of the data that the observer observes.
 */
public interface Observer<T> {

    /**
     * Updates the observer with the given data.
     * 
     * @param notify the data to update the observer with.
     */
    void update(T notify);
}
