package eu.oberon.oss.serz.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SERZUtilityTest {


    public static Stream<Arguments> testMain() {
        return Stream.of(
                Arguments.of(0, new String[]{"--directory", "./", "--conversion-type", "xml-to-bin"}),
                Arguments.of(0, new String[]{"--directory", "./", "--conversion-type", "bin-to-xml"}),
                Arguments.of(0, new String[]{"-V"}),
                Arguments.of(0, new String[]{"-h"}),
                Arguments.of(0, new String[]{"--directory", "./", "--file-pattern","'.*'", "--conversion-type", "xml-to-bin"}),
                Arguments.of(0, new String[]{"--directory", "./", "--conversion-type", "xml-to-bin", "--recursive"}),
                Arguments.of(0, new String[]{"--directory", "./", "--conversion-type", "xml-to-bin", "--file-pattern", ".*"}),
                Arguments.of(2, new String[]{"--directory", "./", "--conversion-type", "xml-to-bin", "--file-pattern", "*.*"}),
                Arguments.of(3, new String[]{"--directory", "pom.xml", "--conversion-type", "xml-to-bin"}),
                Arguments.of(10, new String[]{"--directory", "./", "--conversion-type", "invalid"})
        );
    }

    private CommandLine cli;

    @BeforeEach
    void setUp() {
        SERZUtility serzUtility = new SERZUtility();
        cli = new CommandLine(serzUtility);
    }

    @ParameterizedTest
    @MethodSource
    void testMain(int expectedReturnValue, String[] cliParameters) {
        assertEquals(expectedReturnValue, cli.execute(cliParameters));
    }
}