package it.unibo.model.help;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.model.answer.Answer;
import it.unibo.model.help.api.HelpStrategy;
import it.unibo.model.question.Question;

/**
 * Class representing the "50-50" help strategy, which allows the player to eliminate two incorrect answers 
 * from the available options for a question.
 */
public class FiftyFifty implements HelpStrategy<List<Answer>> {


    private boolean used = false;
   
    /**
     * {@inheritDoc}
     * This method should implement the logic to eliminate two incorrect answers from the available options for a question.
     * 
     * @return the modified question with two incorrect answers eliminated.
     * @throws IllegalStateException if the help strategy has already been used.
     */
    @Override
    public List<Answer> applyHelp(final Question question) {
        if (this.used) {
            throw new IllegalStateException("Help already used");
        }
        this.used = true;
        
        List<Answer> incorrect = question.getAnswers().stream()
                .filter(a -> !a.isCorrect())
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(incorrect);
       
        return incorrect.stream().limit(2).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpName() {
        return "50-50";
    }

    @Override
    public boolean canUse() {
        return !this.used;
    }
}
