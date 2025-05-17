package eu.oberon.oss.serz.processor.tags;

import java.nio.ByteBuffer;
import java.nio.file.Path;

@SuppressWarnings("unused")
public abstract class SERZTag {
    private final byte[] pattern;
    private final long offset;
    private final ByteBuffer buffer;

    protected SERZTag(byte[] pattern, long offset, ByteBuffer buffer) {
        this.pattern = pattern;
        this.offset = offset;
        this.buffer = buffer;
    }

    byte[] getTagPattern(){
        return pattern;
    }

    long getOffset(){
        return offset;
    }

    Path getSource(){
        return null;
    }
}
