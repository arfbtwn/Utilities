package little.nj.data;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Complex < T > extends Sequence < T >
{
    public interface Get < T, V >
    {
        V get ( T struct );
    }

    Get < T, Integer > length;

    public Complex ( Class < T > clz )
    {
        super ( clz );
    }

    public Complex < T > length ( Get < T, Integer > length )
    {
        this.length = length;
        return this;
    }

    @Override
    public T read ( ByteBuffer stream ) throws IOException
    {
        T o = _new ();

        int start = stream.position ();

        for ( Op x : seq )
        {
            int idx = stream.position () - start;
            int end = length.get ( o );

            if ( 0 < end && end <= idx )
                break;

            x.read ( stream, o );
        }

        return o;
    }

    @Override
    public void write ( T struct, ByteBuffer stream ) throws IOException
    {
        if ( null == struct ) return;

        int start = stream.position ();

        for ( Op x : seq )
        {
            int idx = stream.position () - start;
            int end = length.get ( struct );

            if ( 0 < end && end <= idx )
                break;

            x.write ( struct, stream );
        }
    }
}
