package it.unibo.common;

/**
 * A subject in the observer pattern.
 * 
 * @param <T> the type of the data that the subject holds.
 */
public interface Subject<T> {

    /**
     * Adds an observer to the subject.
     * 
     * @param observer the observer to be added to the subject.
     */
    void addObserver(Observer<T> observer);

    /**
     * Removes an observer from the subject.
     * 
     * @param observer the observer to be removed from the subject.
     */
    void removeObserver(Observer<T> observer);

    /**
     * Notifies all observers of the subject about a change in the data.
     * 
     * @param notify the data to notify the observers about.
     */
    void notifyObservers(T notify);
}
