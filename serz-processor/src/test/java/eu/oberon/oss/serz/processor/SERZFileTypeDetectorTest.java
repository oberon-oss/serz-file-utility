package eu.oberon.oss.serz.processor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SERZFileTypeDetectorTest {

    public static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of("src/test/resources/Railworks/DEs_HV_HS_G_0T.bin", true, "application/serz-binary"),
                Arguments.of("src/test/resources/Railworks/DEs_HV_HS_G_0T.blah", true, "application/serz-binary"),
                Arguments.of("src/test/resources/Railworks/DEs_HV_HS_G_0T.to-short.bin", false, null),
                Arguments.of("src/test/resources/Railworks/DEs_HV_HS_G_0T.xml", false, null)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void probeContentType(Path path, boolean expectValid, String expectedContentType) {
        String contentType = assertDoesNotThrow(() -> new SERZFileTypeDetector().probeContentType(path));
        assertEquals(expectedContentType, contentType);

    }

    @ParameterizedTest
    @MethodSource("testData")
    void isValidSERZFile(Path path, boolean expectValid, String expectedContentType) {
        boolean isValid = assertDoesNotThrow(() -> SERZFileTypeDetector.isValidSERZFile(path));
        assertEquals(expectValid, isValid);
    }
}