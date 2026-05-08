package it.unibo.model.help;

import it.unibo.model.help.api.HelpStrategy;
import it.unibo.model.match.Match;

/**
 * Class representing the "Switch" help strategy, which allows the player to switch the current question with a new one.
 */
public class Switch implements HelpStrategy {

    /**
     * {@inheritDoc}
     * This method should implement the logic to allow the player to switch the current question with a new one.
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
        return "Switch";
    }

}
