package it.unibo.model.data.leaderboard.api;

import java.util.List;

import it.unibo.model.data.leaderboard.LeaderboardEntry;

/**
 * Defines the operations for managing the game leaderboard.
 */
public interface Leaderboard {

    /**
     * Records a player's score.
     * If the player already exists, the stored entry is updated only when
     * the new score is greater than the previous one.
     *
     * @param playerName the player's name
     * @param score the score achieved
     */
    void recordScore(String playerName, int score);

    /**
     * Returns the leaderboard entries ordered by descending score.
     *
     * @return the ordered leaderboard entries
     */
    List<LeaderboardEntry> getEntries();
}