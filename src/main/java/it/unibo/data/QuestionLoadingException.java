package it.unibo.data;

/**
 * Exception thrown when there is an error loading questions from a data source. 
 * */ 
public class QuestionLoadingException extends Exception {

    /**
     * Creates a new instance of QuestionLoadingException with the specified detail message.
     * @param message
     */
    public QuestionLoadingException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of QuestionLoadingException with the specified detail message and cause.
     * @param message
     * @param cause
     */
    public QuestionLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

}
