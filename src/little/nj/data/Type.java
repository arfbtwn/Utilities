package little.nj.data;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Type < T >
{
    T    read  ( ByteBuffer stream )           throws IOException;
    void write ( T struct, ByteBuffer stream ) throws IOException;
}
