package little.nj.data;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Buffer
{
    public static final Buffer Instance = new Buffer ();

    final Serializer serializer;

    public Buffer ()
    {
        this.serializer = new Annotations ().useDefault ();
    }

    public Buffer ( Serializer serializer )
    {
        this.serializer = serializer;
    }

    public <T> T read ( Class < T > clz, ByteBuffer stream ) throws IOException
    {
        Type < T > type = serializer.get ( clz );

        return type.read ( stream );
    }

    @SuppressWarnings ( "unchecked" )
    public <T> void write ( T struct, ByteBuffer stream ) throws IOException
    {
        if ( null == struct ) throw new IllegalArgumentException ();

        Type < T > type = serializer.get ( (Class < T >) struct.getClass () );

        type.write ( struct, stream );
    }
}
