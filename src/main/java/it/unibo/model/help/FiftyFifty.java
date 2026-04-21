package it.unibo.model.help;

import it.unibo.model.match.Match;

/**
 * Class representing the "50-50" help strategy, which allows the player to eliminate two incorrect answers 
 * from the available options for a question.
 */
public class FiftyFifty implements HelpStrategy {

    /**
     * {@inheritDoc}
     * This method should implement the logic to eliminate two incorrect answers from the available options for a question.
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
        return "50-50";
    }

}
