package it.unibo.model.data;

/**
 * Exception thrown when there is an error loading questions from a data source. 
 */ 
public class QuestionLoadingException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of QuestionLoadingException with the specified detail message.
     * 
     * @param message the detail message
     */
    public QuestionLoadingException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of QuestionLoadingException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public QuestionLoadingException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
