package it.unibo.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import it.unibo.model.question.Difficulty;

final class TriviaParserTest {

    private TriviaParser parser;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = JsonMapper.builder()
            .findAndAddModules()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
        this.parser = new TriviaParser(this.mapper);
    }

    @Test
    void testLoadQuestionsContent() throws QuestionLoadingException {
        String json = """
            {
              "response_code": 0,
              "results": [
                {
                  "type": "multiple",
                  "difficulty": "medium",
                  "category": "Science: Computers",
                  "question": "What does AD stand for?",
                  "correct_answer": "Active Directory",
                  "incorrect_answers": ["Alternative Drive", "Analog Device", "Administrative Domain"]
                }
              ]
            }
            """;

        assertThrows(QuestionLoadingException.class, () -> {
            parser.parseTrivia("invalid json");
        });
        
        List<QuestionDTO> questions = parser.parseTrivia(json);

        assertNotNull(questions);
        assertEquals(1, questions.size());
        QuestionDTO first = questions.get(0);
        assertEquals("multiple", first.type());
        assertEquals(Difficulty.MEDIUM, first.difficulty());
        assertEquals("Science: Computers", first.category());
        assertTrue(first.question().contains("AD stand for"));
        assertEquals("Active Directory", first.correctAnswer());
        assertNotNull(first.incorrectAnswers());
        assertEquals(3, first.incorrectAnswers().size());
        assertTrue(first.incorrectAnswers().contains("Alternative Drive"));        
    }
}