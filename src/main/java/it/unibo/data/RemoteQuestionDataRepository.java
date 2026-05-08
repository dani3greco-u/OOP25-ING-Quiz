package it.unibo.data;

import java.util.List;

import it.unibo.data.api.QuestionDataRepository;

/**
 * Loads questions from a remote data source.
 */
public class RemoteQuestionDataRepository implements QuestionDataRepository {

    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadQuestions'");
    }

}
