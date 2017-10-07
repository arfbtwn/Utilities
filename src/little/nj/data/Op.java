package little.nj.data;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Op < T >
{
    void read  ( ByteBuffer stream, T struct ) throws IOException;
    void write ( T struct, ByteBuffer stream ) throws IOException;
}
