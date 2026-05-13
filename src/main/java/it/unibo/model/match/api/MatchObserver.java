package it.unibo.model.match.api;

import it.unibo.common.Observer;
import it.unibo.model.match.MatchEvent;

/**
 * Observer specialized for MatchEvents.
 */
@FunctionalInterface
public interface MatchObserver extends Observer<MatchEvent> { }
