package eu.oberon.oss.serz.cli.util.files;

import eu.oberon.oss.serz.cli.util.picocli.ResourceBundleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

class FileNameCollectorStatisticsTest {

    private FileNameCollectorStatistics statistics;

    @BeforeEach
    void setUp() {
        statistics = new FileNameCollectorStatistics();
    }

    @Test
    void test_InvalidStartCall() {
        assertDoesNotThrow(statistics::start);
        assertThrows(IllegalStateException.class, statistics::start);
    }

    @Test
    void test_InvalidStopCall() {
        assertThrows(IllegalStateException.class, statistics::stop);
        assertDoesNotThrow(statistics::start);
        assertDoesNotThrow(statistics::stop);
        assertThrows(IllegalStateException.class, statistics::stop);
    }

    @Test
    void test_counters() {
        IllegalStateException exception;

        exception = assertThrows(IllegalStateException.class, statistics::toString);
        assertTrue(exception.getMessage().contains("start"));

        assertDoesNotThrow(statistics::start);
        for (int d = 0; d < 7; d++) {
            statistics.incrementDirectoriesProcessed();
        }
        for (int m = 0; m < 11; m++) {
            statistics.incrementFilesMatchingFilter();
        }
        for (int f = 0; f < 13; f++) {
            statistics.incrementFilesProcessed();
        }
        exception = assertThrows(IllegalStateException.class, statistics::toString);
        assertTrue(exception.getMessage().contains("stop"));

        assertDoesNotThrow(statistics::stop);

        assertEquals(7, statistics.getDirectoriesProcessed());
        assertEquals(11, statistics.getFilesMatchingFilter());
        assertEquals(13, statistics.getFilesProcessed());

        String elapsed = statistics.getElapsedTime();
        String formatString = ResourceBundleProvider.getEntry("serz.utility.cli.processing.response.info");
        String expectedString = MessageFormat.format(formatString, elapsed, 7, 13, 11);
        assertEquals(expectedString, statistics.toString());
    }

}