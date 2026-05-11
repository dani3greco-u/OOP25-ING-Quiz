package it.unibo.model.question.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import it.unibo.data.QuestionDTO;
import it.unibo.model.answer.Answer;
import it.unibo.model.question.Question;

/**
 * Factory for creating questions.
 */
public class QuestionFactory {

    /**
     * Creates a Question object from a QuestionDTO.
     * 
     * @param dto the QuestionDTO to convert
     * @return a Question object created from the given QuestionDTO
     */
    public Question fromDTO(final QuestionDTO dto) {
        final List<Answer> answers = new ArrayList<>();

        answers.add(new Answer(dto.correctAnswer(), true));
        for (final String incorrect : dto.incorrectAnswers()) {
            answers.add(new Answer(incorrect, false));
        }

        Collections.shuffle(answers);

        return new Question(
            UUID.randomUUID().toString(),
            dto.question(),
            dto.difficulty(),
            answers
        );
    }
}
