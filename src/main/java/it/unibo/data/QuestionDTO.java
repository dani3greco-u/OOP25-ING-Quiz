package it.unibo.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.unibo.model.question.Difficulty;

/**
 * Data Transfer Object (DTO) representing a quiz question.
 * It contains the question text, its difficulty level, a list of possible answers, and the index 
 * of the correct answer in the list. 
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
