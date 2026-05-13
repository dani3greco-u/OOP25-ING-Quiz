package it.unibo.model.match;

/**
 * The MatchEvent record represents an event that occurs during a match, encapsulating the type of event, 
 * the current state of the match, the score, and the question number.
 * 
 * @param type the type of the match event
 * @param state the current state of the match
 * @param score the current score of the match
 * @param questionNumber the current question number in the match
 */
public record MatchEvent(
    MatchEventType type,
    MatchState state,
    int score,
    int questionNumber
) { }
