package it.unibo.data.api;

import java.util.List;

import it.unibo.data.QuestionDTO;
import it.unibo.data.QuestionLoadingException;

/**
 *  Interface for loading questions from a data source.
 */
public interface QuestionDataRepository {

    List<QuestionDTO> loadQuestions() throws QuestionLoadingException, QuestionLoadingException;
}
