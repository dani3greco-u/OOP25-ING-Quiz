package it.unibo.common;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class LoggingConfiguration {

    private LoggingConfiguration() {
    }

    public static void configure() {
        final Logger rootLogger = Logger.getLogger("");

        for (final var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        final ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(final LogRecord record) {
                return "[" + record.getLevel() + "] "
                    + formatMessage(record)
                    + System.lineSeparator();
            }
        });

        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.INFO);
    }
}