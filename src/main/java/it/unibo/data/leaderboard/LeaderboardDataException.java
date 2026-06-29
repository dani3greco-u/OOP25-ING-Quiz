package it.unibo.data.leaderboard;

/**
 * Signals an error while loading or saving leaderboard data.
 */
public final class LeaderboardDataException extends RuntimeException {

    /**
     * Creates a leaderboard data exception with the specified message
     * and cause.
     *
     * @param message the detail message
     * @param cause the original cause of the error
     */
    public LeaderboardDataException(final String message, final Throwable cause) {
        super(message, cause);
    }
}