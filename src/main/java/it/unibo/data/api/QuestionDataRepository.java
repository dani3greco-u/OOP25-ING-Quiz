package it.unibo.data.api;

import java.util.List;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;

/**
 *  Interface for loading questions from a data source.
 */
@FunctionalInterface
public interface QuestionDataRepository {

    /**
     * Loads questions from the data source.
     * 
     * @return a list of QuestionDTO objects representing the loaded questions
     * @throws QuestionLoadingException if there is an error while loading the questions
     */
    List<QuestionDTO> loadQuestions() throws QuestionLoadingException;
}
