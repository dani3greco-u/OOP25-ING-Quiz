package it.unibo.data.leaderboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.unibo.model.data.leaderboard.LeaderboardEntry;

/**
 * Loads and saves leaderboard entries in a local JSON file.
 */
public final class JsonLeaderboardRepository implements LeaderboardRepository {

    private static final Path DEFAULT_FILE_PATH = Path.of("data", "leaderboard.json");

    private final Path filePath;
    private final ObjectMapper mapper;

    /**
     * Creates a repository that stores the leaderboard in the default file.
     */
    public JsonLeaderboardRepository() {
        this(
            DEFAULT_FILE_PATH,
            new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        );
    }

    /**
     * Creates a repository with configurable dependencies.
     *
     * @param filePath the path of the leaderboard JSON file
     * @param mapper the object mapper used to read and write JSON
     */
    public JsonLeaderboardRepository(final Path filePath, final ObjectMapper mapper) {
        this.filePath = Objects.requireNonNull(
            filePath,
            "Leaderboard file path cannot be null"
        );

        this.mapper = Objects.requireNonNull(
            mapper,
            "Object mapper cannot be null"
        ).copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LeaderboardEntry> loadEntries() {
        if (Files.notExists(this.filePath)) {
            return List.of();
        }

        try {
            final List<LeaderboardEntry> entries =
                this.mapper.readValue(
                    this.filePath.toFile(),
                    new TypeReference<>() { }
                );

            return entries == null
                ? List.of()
                : List.copyOf(entries);
        } catch (final IOException exception) {
            throw new LeaderboardDataException(
                "Unable to load leaderboard entries",
                exception
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveEntries(final List<LeaderboardEntry> entries) {
        Objects.requireNonNull(
            entries,
            "Leaderboard entries cannot be null"
        );

        try {
            final Path parentDirectory = this.filePath.getParent();

            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            this.mapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(this.filePath.toFile(), List.copyOf(entries));
        } catch (final IOException exception) {
            throw new LeaderboardDataException(
                "Unable to save leaderboard entries",
                exception
            );
        }
    }
}
