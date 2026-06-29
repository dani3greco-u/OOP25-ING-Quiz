package it.unibo.view.swing.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Plays audio resources stored in the application classpath.
 */
public final class SoundPlayer {

    private static final Logger LOGGER = Logger.getLogger(SoundPlayer.class.getName());

    /**
     * Plays the specified audio resource.
     *
     * @param resourcePath the classpath path of the audio resource
     */
    public void play(final String resourcePath) {
        Objects.requireNonNull(
            resourcePath,
            "The sound resource path cannot be null"
        );

        final InputStream resourceStream = SoundPlayer.class.getResourceAsStream(resourcePath);

        if (resourceStream == null) {
            LOGGER.warning(
                "[SOUND PLAYER] Sound resource not found: "
                    + resourcePath
            );
            return;
        }

        try (
            InputStream bufferedStream = new BufferedInputStream(resourceStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream)) {
                final Clip clip = AudioSystem.getClip();

                clip.open(audioStream);
                clip.start();
        } catch (
            final UnsupportedAudioFileException
                | IOException
                | LineUnavailableException exception
        ) {
            LOGGER.warning(
                "[SOUND PLAYER] Unable to play sound "
                    + resourcePath
                    + ": "
                    + exception.getMessage()
            );
        }
    }
}