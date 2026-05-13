package it.unibo.model.match;

public record MatchEvent(
    MatchEventType type,
    MatchState state,
    int score,
    int questionNumber
) { }
