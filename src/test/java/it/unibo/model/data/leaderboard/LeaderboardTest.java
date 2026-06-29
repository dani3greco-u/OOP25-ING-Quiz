package it.unibo.model.data.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.data.leaderboard.LeaderboardRepository;
import it.unibo.model.data.leaderboard.api.Leaderboard;

// CHECKSTYLE: MagicNumber OFF
//CHECKSTYLE: MultipleStringLiterals OFF
/**
 * Tests the default leaderboard implementation.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class LeaderboardTest {

    /**
     * Tests that a score is recorded when the player is not already
     * present in the leaderboard.
     */
    @Test
    void testRecordNewPlayerScore() {
        final InMemoryLeaderboardRepository repository = new InMemoryLeaderboardRepository();

        final Leaderboard leaderboard = new LeaderboardImpl(repository);

        leaderboard.recordScore("Daniele", 8);

        final List<LeaderboardEntry> entries =
            leaderboard.getEntries();

        assertEquals(
            1,
            entries.size(),
            "The leaderboard should contain one entry"
        );

        assertEquals(
            "Daniele",
            entries.get(0).playerName(),
            "The stored player name should match the submitted name"
        );

        assertEquals(
            8,
            entries.get(0).score(),
            "The stored score should match the submitted score"
        );
    }

    /**
     * Tests that an existing player's score is updated when the new
     * score is greater than the stored score.
     */
    @Test
    void testUpdatePlayerWithHigherScore() {
        final InMemoryLeaderboardRepository repository = new InMemoryLeaderboardRepository();

        final Leaderboard leaderboard = new LeaderboardImpl(repository);

        leaderboard.recordScore("Daniele", 5);
        leaderboard.recordScore("Daniele", 10);

        final List<LeaderboardEntry> entries =
            leaderboard.getEntries();

        assertEquals(
            1,
            entries.size(),
            "The leaderboard should contain only one entry for the player"
        );

        assertEquals(
            10,
            entries.get(0).score(),
            "The stored score should be updated to the higher score"
        );
    }

    /**
     * Tests that an existing player's score is not updated when the new
     * score is lower than the stored score.
     */
    @Test
    void testKeepPlayerWithHigherStoredScore() {
        final InMemoryLeaderboardRepository repository = new InMemoryLeaderboardRepository();

        final Leaderboard leaderboard = new LeaderboardImpl(repository);

        leaderboard.recordScore("Daniele", 10);
        leaderboard.recordScore("Daniele", 5);

        final List<LeaderboardEntry> entries = leaderboard.getEntries();

        assertEquals(
            1,
            entries.size(),
            "The leaderboard should contain only one entry for the player"
        );

        assertEquals(
            10,
            entries.get(0).score(),
            "The stored score should remain unchanged"
        );
    }

    /**
     * Tests that player names are compared without distinguishing
     * uppercase and lowercase characters.
     */
    @Test
    void testPlayerNameComparisonIsCaseInsensitive() {
        final InMemoryLeaderboardRepository repository = new InMemoryLeaderboardRepository();

        final Leaderboard leaderboard =
            new LeaderboardImpl(repository);

        leaderboard.recordScore("Daniele", 5);
        leaderboard.recordScore("daniele", 9);

        final List<LeaderboardEntry> entries =
            leaderboard.getEntries();

        assertEquals(
            1,
            entries.size(),
            "Player names differing only by case should identify the same player"
        );

        assertEquals(
            9,
            entries.get(0).score(),
            "The stored score should be updated for the same player"
        );
    }

    /**
     * Tests that leaderboard entries are returned in descending score order.
     */
    @Test
    void testEntriesAreOrderedByDescendingScore() {
        final InMemoryLeaderboardRepository repository = new InMemoryLeaderboardRepository();

        final Leaderboard leaderboard = new LeaderboardImpl(repository);

        leaderboard.recordScore("Alice", 5);
        leaderboard.recordScore("Bob", 12);
        leaderboard.recordScore("Charlie", 8);

        final List<LeaderboardEntry> entries = leaderboard.getEntries();

        assertEquals(
            "Bob",
            entries.get(0).playerName(),
            "The player with the highest score should be first"
        );

        assertEquals(
            "Charlie",
            entries.get(1).playerName(),
            "The player with the second highest score should be second"
        );

        assertEquals(
            "Alice",
            entries.get(2).playerName(),
            "The player with the lowest score should be last"
        );
    }

    /**
     * In-memory repository used to isolate leaderboard tests from
     * file-system persistence.
     */
    private static final class InMemoryLeaderboardRepository implements LeaderboardRepository {

        private List<LeaderboardEntry> entries = new ArrayList<>();

        /**
         * {@inheritDoc}
         */
        @Override
        public List<LeaderboardEntry> loadEntries() {
            return List.copyOf(this.entries);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void saveEntries(final List<LeaderboardEntry> newEntries) {
            this.entries = new ArrayList<>(newEntries);
        }
    }
}
