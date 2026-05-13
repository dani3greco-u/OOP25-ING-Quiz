package it.unibo.model.match;

/**
 * Enum representing the different types of events that can occur during a match.
 */
public enum MatchEventType {
    MATCH_STARTED,
    QUESTION_CHANGED,
    ANSWER_SUBMITTED,
    CORRECT_ANSWER,
    WRONG_ANSWER,
    HELP_USED,
    MATCH_WON,
    MATCH_LOST,
    MATCH_ENDED
}
