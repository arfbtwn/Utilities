package little.nj.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Sequence < T > implements Type < T >
{
    protected final Class < T >       clz;
    protected final List < Op < T > > seq;

    public Sequence ( Class < T > clz )
    {
        this.clz = clz;
        this.seq = new ArrayList <> ();
    }

    public Sequence < T > next ( Op < T > op )
    {
        seq.add ( op );
        return this;
    }

    protected T _new ()
    {
        try
        {
            return clz.newInstance ();
        }
        catch ( InstantiationException | IllegalAccessException e )
        {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public T read ( ByteBuffer stream ) throws IOException
    {
        T o = _new ();

        for ( Op x : seq )
        {
            x.read ( stream, o );
        }

        return o;
    }

    @Override
    public void write ( T struct, ByteBuffer stream ) throws IOException
    {
        if ( null == struct ) return;

        for ( Op x : seq )
        {
            x.write ( struct, stream );
        }
    }
}
