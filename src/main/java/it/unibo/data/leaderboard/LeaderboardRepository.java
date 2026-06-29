package it.unibo.data.leaderboard;

import java.util.List;

import it.unibo.model.data.leaderboard.LeaderboardEntry;

/**
 * Defines the operations required to load and save leaderboard entries.
 */
public interface LeaderboardRepository {

    /**
     * Loads all leaderboard entries.
     *
     * @return the stored leaderboard entries
     */
    List<LeaderboardEntry> loadEntries();

    /**
     * Saves all leaderboard entries.
     *
     * @param entries the leaderboard entries to save
     */
    void saveEntries(List<LeaderboardEntry> entries);
}