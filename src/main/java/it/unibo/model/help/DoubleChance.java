package it.unibo.model.help;

import it.unibo.model.help.api.HelpStrategy;
import it.unibo.model.match.Match;

/**
 * Class representing the "Double Chance" help strategy, which allows the player to have two attempts 
 * to answer a question correctly.
 */
public class DoubleChance implements HelpStrategy {

    /**
     * {@inheritDoc}
     * This method should implement the logic to allow the player to have two attempts to answer a question correctly.
     * 
     * @return true if the help was applied successfully, false otherwise.
     */
    @Override
    public boolean applyHelp(final Match match) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyHelp'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpName() {
        return "Double Chance";
    }
}
