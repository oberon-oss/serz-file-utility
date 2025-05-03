package eu.oberon.oss.serz.cli.util.files;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileNameCollectorTest {

    public static Stream<Arguments> collectFiles() {
        return Stream.of(
                Arguments.of(7, 13, 13, new File("src/test/resources"), ".*", true),
                Arguments.of(7, 13, 4, new File("src/test/resources"), "f1.*", true),
                Arguments.of(7, 13, 2, new File("src/test/resources"), "f1-b.*", true),
                Arguments.of(7, 13, 1, new File("src/test/resources"), "f1-b.bin", true),
                Arguments.of(7, 13, 1, new File("src/test/resources"), "f1-b.xml", true),
                Arguments.of(7, 13, 5, new File("src/test/resources"), ".*bin", true),
                Arguments.of(7, 13, 6, new File("src/test/resources"), ".*xml", true)
        );
    }

    @ParameterizedTest
    @MethodSource
    void collectFiles(int dirCount, int fileCount, int matchCount, File directory, Pattern pattern, boolean recursive) {
        System.out.println("Collecting files in " + directory.getAbsolutePath());
        FileNameCollectorResult result = FileNameCollector.collectFiles(directory, pattern, recursive);
        assertEquals(dirCount, result.getStatistics().getDirectoriesProcessed());
        assertEquals(fileCount, result.getStatistics().getFilesProcessed());
        assertEquals(matchCount, result.getStatistics().getFilesMatchingFilter());
    }
}