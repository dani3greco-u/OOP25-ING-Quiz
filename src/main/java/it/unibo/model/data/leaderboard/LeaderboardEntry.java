package it.unibo.model.data.leaderboard;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a leaderboard entry containing the player's best score
 * and the date and time when it was achieved.
 *
 * @param playerName the player's name
 * @param score the player's best score
 * @param achievedAt the date and time when the score was achieved
 */
public record LeaderboardEntry(
    String playerName,
    int score,
    LocalDateTime achievedAt
) {

    /**
     * Creates a validated leaderboard entry.
     *
     * @throws NullPointerException if playerName or achievedAt is null
     * @throws IllegalArgumentException if playerName is blank or score is negative
     */
    public LeaderboardEntry {
        Objects.requireNonNull(playerName, "Player name cannot be null");
        Objects.requireNonNull(achievedAt, "Achievement date cannot be null");

        if (playerName.isBlank()) {
            throw new IllegalArgumentException(
                "Player name cannot be blank"
            );
        }

        if (score < 0) {
            throw new IllegalArgumentException(
                "Score cannot be negative"
            );
        }

        playerName = playerName.trim();
    }
}
