package little.nj.data;

import little.nj.exceptions.NotImplementedException;
import little.nj.reflection._Field;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;

public class Annotations extends Simple
{
    @Retention ( RetentionPolicy.RUNTIME )
    @Target ( { ElementType.TYPE, ElementType.FIELD } )
    public @interface Counted
    {
        int counter ();
    }

    @Retention ( RetentionPolicy.RUNTIME )
    @Target ( ElementType.FIELD )
    public @interface Counter
    {
        int id ();
        int adjustment () default 0;
    }

    @Retention ( RetentionPolicy.RUNTIME )
    @Target ( ElementType.FIELD )
    public @interface FixedLength
    {
        int length ();
    }

    @Retention ( RetentionPolicy.RUNTIME )
    @Target ( ElementType.FIELD )
    public @interface Encoding
    {
        String charset () default "US-ASCII";
    }

    @Retention ( RetentionPolicy.RUNTIME )
    @Target ( ElementType.FIELD )
    public @interface Ignore { }

    static abstract class _Base implements Op
    {
        protected final _Field field;

        protected _Base ( Field field )
        {
            this.field = new _Field ( field );
        }

        protected Object get ( Object struct )
        {
            return field.get ( struct );
        }

        protected void set ( Object struct, Object value )
        {
            field.set ( struct, value );
        }
    }

    static abstract class _Counted extends _Base
    {
        protected final _Field counter;
        protected final int adjustment;

        protected _Counted ( Field field, Field counter, int adjustment )
        {
            super ( field );
            this.counter = new _Field ( counter );
            this.adjustment = adjustment;
        }

        protected int count ( Object struct )
        {
            return counter.getInt ( struct ) - adjustment;
        }
    }

    static class _FinalBytes extends _Base
    {
        public _FinalBytes ( Field field )
        {
            super ( field );
        }

        @Override
        public void read ( ByteBuffer stream, Object struct ) throws IOException
        {
            byte[] buffer = (byte[]) field.get ( struct );

            stream.get ( buffer );
        }

        @Override
        public void write ( Object struct, ByteBuffer stream ) throws IOException
        {
            byte[] buffer = (byte[]) field.get ( struct );

            stream.put ( buffer );
        }
    }

    static class _Bytes extends _Counted
    {
        public _Bytes ( Field field, Field counter, int adjustment )
        {
            super ( field, counter, adjustment );
        }

        @Override
        public void read ( ByteBuffer stream, Object struct ) throws IOException
        {
            int count = count ( struct );
            byte[] buffer = new byte [ count ];
            stream.get ( buffer );
            set ( struct, buffer );
        }

        @Override
        public void write ( Object struct, ByteBuffer stream ) throws IOException
        {
            int count = count ( struct );
            byte[] buffer = (byte[]) get ( struct );
            stream.put ( buffer, 0, count );
        }
    }

    static class _FinalArray extends _Base
    {
        final Type type;

        public _FinalArray ( Field field, Type type )
        {
            super ( field );
            this.type = type;
        }

        @Override
        public void read ( ByteBuffer stream, Object struct ) throws IOException
        {
            Object v = get ( struct );

            int length = Array.getLength ( v );

            for ( int i = 0; i < length; ++i )
            {
                Array.set ( v, i, type.read ( stream ) );
            }
        }

        @Override
        public void write ( Object struct, ByteBuffer stream ) throws IOException
        {
            Object v = get ( struct );

            int length = Array.getLength ( v );

            for ( int i = 0; i < length; ++i )
            {
                type.write ( Array.get ( v, i ), stream );
            }
        }
    }

    static class _Array extends _Counted
    {
        final Type type;
        final Class component;

        public _Array ( Field field, Field counter, int adjustment, Type type )
        {
            super ( field, counter, adjustment );
            this.type = type;
            this.component = field.getType ().getComponentType ();
        }

        @Override
        public void read ( ByteBuffer stream, Object struct ) throws IOException
        {
            int count = count ( struct );
            Object arr = Array.newInstance ( component, count );
            for ( int i = 0; i < count; ++i )
            {
                Array.set ( arr, i, type.read ( stream ) );
            }
            set ( struct, arr );
        }

        @Override
        public void write ( Object struct, ByteBuffer stream ) throws IOException
        {
            int count = count ( struct );
            Object arr = get ( struct );
            for ( int i = 0; i < count; ++i )
            {
                type.write ( Array.get ( arr, i ), stream );
            }
        }
    }

    abstract static class _String extends _Base
    {
        final Charset charset;

        public _String ( Field field, Charset charset )
        {
            super ( field );
            this.charset = charset;
        }
    }

    static class _FixedString extends _String
    {
        final int length;

        public _FixedString ( Field field, Charset charset, int length )
        {
            super ( field, charset );
            this.length = length;
        }

        @Override
        public void read ( ByteBuffer stream, Object struct ) throws IOException
        {
            byte[] buffer = new byte [ length ];
            stream.get ( buffer );
            set ( struct, new String ( buffer, charset ) );
        }

        @Override
        public void write ( Object struct, ByteBuffer stream ) throws IOException
        {
            String val = (String) get ( struct );
            byte[] bytes = val.getBytes ( charset );
            byte[] buffer = new byte [ length ];
            System.arraycopy ( bytes, 0, buffer, 0, bytes.length < length ? bytes.length : length );
            stream.put ( buffer );
        }
    }

    static class _Simple extends _Base
    {
        final Type type;

        public _Simple ( Field field, Type type )
        {
            super ( field );
            this.type = type;
        }

        @Override
        public void read ( ByteBuffer stream, Object struct ) throws IOException
        {
            set ( struct, type.read ( stream ) );
        }

        @Override
        public void write ( Object struct, ByteBuffer stream ) throws IOException
        {
            type.write ( get ( struct ), stream );
        }
    }

    private Field counter ( Field field )
    {
        Counted id = field.getAnnotation ( Counted.class );

        if ( null == id ) return null;

        Field[] fields = field.getDeclaringClass ().getDeclaredFields ();

        for ( Field f : fields )
        {
            Counter counter = f.getAnnotation ( Counter.class );

            if ( null == counter ) continue;

            if ( counter.id () != id.counter () ) continue;

            return f;
        }

        return null;
    }

    private int adjustment ( Field field )
    {
        if ( null == field ) return 0;

        Counter counter = field.getAnnotation ( Counter.class );

        return counter.adjustment ();
    }

    private Op bytes ( Field field )
    {
        Field counter = counter ( field );
        int adjustment = adjustment ( counter );

        if ( Modifier.isFinal ( field.getModifiers () ) )
        {
            return new _FinalBytes ( field );
        }
        else
        {
            return new _Bytes ( field, counter, adjustment );
        }
    }

    private Op array ( Field field )
    {
        Field counter = counter ( field );
        int adjustment = adjustment ( counter );
        Type type = get ( field.getType ().getComponentType () );

        if ( Modifier.isFinal ( field.getModifiers () ) )
        {
            return new _FinalArray ( field, type );
        }
        else
        {
            return new _Array ( field, counter, adjustment, type );
        }
    }

    private Op collection ( Field field )
    {
        throw new NotImplementedException ();
    }

    private Op string ( Field field )
    {
        Encoding    enc = field.getAnnotation ( Encoding.class );
        FixedLength len = field.getAnnotation ( FixedLength.class );

        Charset set;

        if ( null != enc )
        {
            set = Charset.forName ( enc.charset () );
        }
        else
        {
            set = Charset.forName ( "US-ASCII" );
        }

        if ( null == len )
        {
            throw new NotImplementedException ();
        }

        return new _FixedString ( field, set, len.length () );
    }

    private Op simple ( Field field )
    {
        return new _Simple ( field, get ( field.getType () ) );
    }

    private Op build ( Field field )
    {
        Class target = field.getType();

        if ( target.isArray () )
        {
            if ( byte[].class.equals ( target ) )
            {
                return bytes ( field );
            }
            else
            {
                return array ( field );
            }
        }
        else if ( Collection.class.isAssignableFrom ( target ) )
        {
            return collection ( field  );
        }
        else if ( String.class.equals ( target ) )
        {
            return string ( field );
        }
        else
        {
            return simple ( field );
        }
    }

    @Override
    @SuppressWarnings ( "unchecked" )
    protected <T> Sequence < T > build ( Class < T > clz )
    {
        Counted id = clz.getAnnotation ( Counted.class );

        Sequence < T > seq;

        if ( null == id )
        {
            seq = new Sequence <> ( clz );
        }
        else
        {
            seq = new Complex <> ( clz );

            for ( Field f : clz.getDeclaredFields () )
            {
                Counter counter = f.getAnnotation ( Counter.class );

                if ( null == counter ) continue;

                if ( counter.id () != id.counter () ) continue;

                ((Complex < T >) seq).length ( o -> new _Field ( f ).getInt ( o ) );
            }
        }

        for ( Field x : clz.getFields () )
        {
            if ( Modifier.isStatic ( x.getModifiers () ) )
            {
                continue;
            }

            seq.next ( build ( x ) );
        }

        return seq;
    }
}
