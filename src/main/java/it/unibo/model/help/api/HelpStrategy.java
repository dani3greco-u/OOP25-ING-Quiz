package it.unibo.model.help.api;

import it.unibo.model.question.Question;

/**
 * Defines the interface for help strategies that can be applied during a match.
 * 
 * @param <T> the type of the result returned by the help strategy when applied.
 */
public interface HelpStrategy<T> {

    /**
     * Apply the help strategy.
     * 
     * @param question the question to which the help is being applied.
     * @return the result of applying the help strategy.
     */
    T applyHelp(Question question);

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
