package it.unibo.model.help;

import it.unibo.model.help.api.HelpStrategy;
import it.unibo.model.question.Question;

/**
 * Class representing the "Switch" help strategy, which allows the player to switch the current question with a new one.
 */
public class Switch implements HelpStrategy<Boolean> {

    private boolean used;

    /**
     * {@inheritDoc}
     * This method should implement the logic to allow the player to switch the current question with a new one.
     * 
     * @return true if the switch was successful, false otherwise
     * @throws IllegalStateException if the help has already been used
     */
    @Override
    public Boolean applyHelp(final Question question) {
        if (this.used) {
            throw new IllegalStateException("Help already used");
        }
        this.used = true;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpName() {
        return "Switch";
    }

    @Override
    public boolean canUse() {
        return !this.used;
    }

}
