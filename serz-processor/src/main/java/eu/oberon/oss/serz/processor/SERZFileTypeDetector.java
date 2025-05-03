package eu.oberon.oss.serz.processor;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.Arrays;

@Log4j2
public class SERZFileTypeDetector extends FileTypeDetector {

    @Override
    public String probeContentType(Path path) throws IOException {
        return checkSERZSignature(path) ? "application/serz-binary" : null;
    }

    private static final byte[] BYTES = new byte[]{0x53, 0x45, 0x52, 0x5a, 0x00, 0x00, 0x01, 0x00};

    public static boolean isValidSERZFile(Path path) throws IOException {
        return checkSERZSignature(path);
    }

    private static boolean checkSERZSignature(Path path) throws IOException {
        byte[] header = new byte[8];
        boolean isSerzFile = false;
        try (InputStream is = Files.newInputStream(path)) {
            if (is.read(header) == header.length) {
                isSerzFile = Arrays.equals(header, BYTES);
                if (!isSerzFile) {
                    LOGGER.debug("SERZ SIGNATURE NOT FOUND in: {}", path);
                } else if (!path.getFileName().toString().endsWith(".bin")) {
                    LOGGER.info("Acceptable file with non .bin extension: {}", path);
                }
            }
        }
        return isSerzFile;
    }
}
