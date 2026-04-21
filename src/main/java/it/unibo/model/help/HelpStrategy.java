package it.unibo.model.help;

import it.unibo.model.match.Match;

/**
 * Defines the interface for help strategies that can be applied during a match.
 */
public interface HelpStrategy {

    /**
     * Apply the help strategy.
     * 
     * @param match the current match to which the help is being applied.
     * @return true if the help was applied successfully, false otherwise.
     */
    boolean applyHelp(Match match);

    /**
     * Get the name of the help strategy.
     * 
     * @return the name of the help strategy.
     */
    String getHelpName();
}
