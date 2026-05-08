package it.unibo.data;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.data.api.QuestionDataRepository;
/**
 * Loads questions from a local data source. 
 */
public class LocalQuestionDataRepository implements QuestionDataRepository {

    private final String question_file_path;
    private final ObjectMapper mapper;
    public LocalQuestionDataRepository(final String question_file_path) {
        this.question_file_path = question_file_path;
        this.mapper = JsonMapper.builder()
        .findAndAddModules()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
    }

    @Override
    public List<QuestionDTO> loadQuestions() throws QuestionLoadingException {
    
        try(InputStream is = LocalQuestionDataRepository.class.getResourceAsStream(this.question_file_path)) {
            if(is == null) {
                throw new QuestionLoadingException("File not found: " + this.question_file_path);
            }
            TriviaDTO trivia = this.mapper.readValue(is, TriviaDTO.class);
            return List.copyOf(trivia.results());
        } catch(QuestionLoadingException e) {
            throw e;
        }catch (Exception e) {
            throw new QuestionLoadingException("Error loading questions from file: " + question_file_path, e);
        }
    }

}
