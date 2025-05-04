package eu.oberon.oss.serz.cli.util.files;

import eu.oberon.oss.serz.cli.util.picocli.ResourceBundleProvider;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.text.MessageFormat;
import java.time.Duration;

/**
 * Class that collects the run time statistics for the collection run.
 *
 * @author fhdumay
 * @since 1.0.0
 */
@Getter
@Log4j2
public class FileNameCollectorStatistics {
    private int directoriesProcessed = 0;
    private int filesProcessed = 0;
    private int filesMatchingFilter = 0;
    private long timeStarted;
    private long timeEnded;

    FileNameCollectorStatistics() {

    }

    void start() {
        if (timeStarted != 0) {
            throw new IllegalStateException("Already started");
        }
        timeStarted = System.currentTimeMillis();
    }

    void stop() {
        if (timeEnded != 0) {
            throw new IllegalStateException("Already stopped");
        }
        if (timeStarted == 0) {
            throw new IllegalStateException("Not started yet");
        }
        timeEnded = System.currentTimeMillis();
    }

    void incrementDirectoriesProcessed() {
        directoriesProcessed++;
    }

    void incrementFilesProcessed() {
        filesProcessed++;
    }

    void incrementFilesMatchingFilter() {
        filesMatchingFilter++;
    }

    @Override
    public String toString() {
        String elapsed = getElapsedTime();
        String formatString = ResourceBundleProvider.getEntry("serz.utility.cli.processing.response.info");
        return MessageFormat.format(formatString, elapsed, directoriesProcessed, filesProcessed, filesMatchingFilter);
    }

    String getElapsedTime() {
        Duration duration = Duration.ofMillis(timeEnded - timeStarted);
        checkRunCompleted();
        return String.format("%02d:%02d:%02d.%03d",
                duration.toHours(),
                duration.toMinutes() - (duration.toHours() * 60),
                duration.toSeconds() - (duration.toMinutes() * 60),
                duration.toMillis() - (duration.toSeconds() * 60)
        );
    }

    void checkRunCompleted() {
        if (timeStarted == 0) {
            throw new IllegalStateException("Not started yet");
        }
        if (timeEnded == 0) {
            throw new IllegalStateException("Not stopped yet");
        }
    }
}
