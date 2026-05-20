package it.unibo.model.help.api;

import it.unibo.model.question.Question;

/**
 * Defines the interface for help strategies that can be applied during a match.
 */
public interface HelpStrategy<T> {

    /**
     * Apply the help strategy.
     * 
     * @param question the question to which the help is being applied.
     * @return the result of applying the help strategy.
     */
    T applyHelp(final Question question);

    /**
     * Check if the help can be used.
     * 
     * @return true if the help strategy can be used, false otherwise.
     */
    boolean canUse();

    /**
     * Get the name of the help strategy.
     * 
     * @return the name of the help strategy.
     */
    String getHelpName();
}
