package it.unibo.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.unibo.model.question.Difficulty;

/**
 * Data Transfer Object (DTO) representing a quiz question.
 * 
 * @param type The type of the question.
 * @param difficulty The difficulty level of the question.
 * @param category The category of the question.
 * @param question The text of the question.
 * @param correctAnswer The correct answer to the question.
 * @param incorrectAnswers A list of incorrect answers to the question.
 */
public record QuestionDTO(
    String type,
    Difficulty difficulty,
    String category,
    String question,

    @JsonProperty("correct_answer") 
    String correctAnswer,

    @JsonProperty("incorrect_answers") 
    List<String> incorrectAnswers) {
} 
