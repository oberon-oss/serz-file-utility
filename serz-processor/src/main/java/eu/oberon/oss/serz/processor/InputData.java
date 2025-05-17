package eu.oberon.oss.serz.processor;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@SuppressWarnings("unused")
public class InputData {
    @Getter
    private final Path source;
    private final byte[] buffer;

    public InputData(Path source) throws IOException {
        this.source = source;
        try (InputStream is = Files.newInputStream(source)) {
            buffer = is.readAllBytes();
        }
    }


    /**
     * Returns the requested byte array
     *
     * @param offset offset within the data buffer.
     * @param length number of bytes to read from the byte array
     * @return the request data as an array of bytes.
     * @since 1.0.0
     */
    public byte[] getBytes(int offset, int length) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0: " + offset);
        }
        if (length < 0) {
            throw new IllegalArgumentException("length < 0: " + length);
        }

        if (length > buffer.length - offset) {
            throw new IndexOutOfBoundsException();
        }

        return Arrays.copyOfRange(buffer, offset, offset + length);
    }
}
