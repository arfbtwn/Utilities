package little.nj.data;

import static little.nj.data.Types.*;

public interface Serializer
{
        boolean    has ( Class clz );
    <T> Serializer use ( Class < T > clz, Type < T > type );
    <T> Type < T > get ( Class < T > clz );

    default Serializer useDefault ()
    {
        return use ( byte.class,      Byte )
              .use ( Byte.class,      Byte )
              .use ( char.class,      Character )
              .use ( Character.class, Character )
              .use ( short.class,     Short )
              .use ( Short.class,     Short )
              .use ( int.class,       Integer )
              .use ( Integer.class,   Integer )
              .use ( long.class,      Long )
              .use ( Long.class,      Long )
              .use ( double.class,    Double)
              .use ( Double.class,    Double);
    }
}
