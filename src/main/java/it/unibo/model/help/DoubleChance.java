package it.unibo.model.help;

import it.unibo.model.help.api.HelpStrategy;
import it.unibo.model.question.Question;

/**
 * Class representing the "Double Chance" help strategy, which allows the player to have two attempts 
 * to answer a question correctly.
 */
public class DoubleChance implements HelpStrategy<Boolean> {

    private boolean used;

    /**
     * {@inheritDoc}
     * This method should implement the logic to allow the player to have two attempts to answer a question correctly.
     * 
     * @return true if the player can use the double chance
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
        return "Double Chance";
    }

    /**
     * Check if the double chance help can be used. It can be used only once per match.
     * 
     * @return true if the double chance help can be used, false otherwise.
     */
    @Override
    public boolean canUse() {
        return !this.used;
    }
}
