package it.unibo.data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a quiz question.
 * It contains the question text, its difficulty level, a list of possible answers, and the index 
 * of the correct answer in the list. 
 */
public record QuestionDTO(
        String text,
        int difficulty,
        List<String> answers,
        int correctAnswerIndex) {
} 
