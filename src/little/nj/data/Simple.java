package little.nj.data;

import little.nj.exceptions.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class Simple implements Serializer
{
    protected final Map < Class, Type > types = new HashMap<> ();

    protected <T> Type < T > build ( Class < T > clz )
    {
        throw new NotImplementedException ();
    }

    public final boolean has ( Class clz )
    {
        synchronized ( types )
        {
            return types.containsKey ( clz );
        }
    }

    public final <T> Simple use ( Class < T > clz, Type < T > type )
    {
        synchronized ( types )
        {
            types.put ( clz, type );

            return this;
        }
    }

    @SuppressWarnings ( "unchecked" )
    public final <T> Type < T > get ( Class < T > clz )
    {
        if ( types.containsKey ( clz ) )
        {
            return types.get ( clz );
        }

        synchronized ( types )
        {
            if ( types.containsKey ( clz ) )
            {
                return types.get ( clz );
            }

            Type type = build ( clz );

            types.put ( clz, type );

            return type;
        }
    }
}
