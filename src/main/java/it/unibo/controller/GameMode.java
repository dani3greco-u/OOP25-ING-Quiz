package it.unibo.controller;

/**
 * Represents the available quiz game modes.
 */
public enum GameMode {

    /**
     * Standard game mode whose score is recorded in the leaderboard.
     */
    NORMAL,

    /**
     * Training mode whose score is not recorded in the leaderboard.
     */
    TRAINING
}