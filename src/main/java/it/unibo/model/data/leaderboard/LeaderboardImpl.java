package it.unibo.model.data.leaderboard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.data.leaderboard.LeaderboardRepository;
import it.unibo.model.data.leaderboard.api.Leaderboard;

/**
 * Default implementation of the leaderboard.
 */
public final class LeaderboardImpl implements Leaderboard {

    private final LeaderboardRepository repository;

    /**
     * Creates a leaderboard using the specified repository.
     *
     * @param repository the repository used to persist leaderboard entries
     */
    public LeaderboardImpl(final LeaderboardRepository repository) {
        this.repository = Objects.requireNonNull(
            repository,
            "Leaderboard repository cannot be null"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordScore(final String playerName, final int score) {
        final LeaderboardEntry newEntry = new LeaderboardEntry(playerName, score, LocalDateTime.now());

        final List<LeaderboardEntry> entries = new ArrayList<>(this.repository.loadEntries());

        final Optional<LeaderboardEntry> existingEntry =
            entries.stream()
                .filter(entry ->
                    entry.playerName().equalsIgnoreCase(
                        newEntry.playerName()
                    )
                )
                .findFirst();

        if (existingEntry.isEmpty()) {
            entries.add(newEntry);
            this.repository.saveEntries(entries);
            return;
        }

        final LeaderboardEntry previousEntry = existingEntry.orElseThrow();

        if (newEntry.score() > previousEntry.score()) {
            entries.remove(previousEntry);
            entries.add(newEntry);
            this.repository.saveEntries(entries);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LeaderboardEntry> getEntries() {
        return this.repository.loadEntries()
            .stream()
            .sorted(
                Comparator
                    .comparingInt(LeaderboardEntry::score)
                    .reversed()
                    .thenComparing(LeaderboardEntry::achievedAt)
            )
            .toList();
    }
}