package little.nj.data;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Types
{
    private Types () { }

    public static final Type < Byte > Byte = new Type < Byte > ()
    {
        @Override
        public Byte read ( ByteBuffer stream ) throws IOException
        {
            return stream.get ();
        }

        @Override
        public void write ( Byte struct, ByteBuffer stream ) throws IOException
        {
            stream.put ( struct );
        }
    };

    public static final Type < Character > Character = new Type < Character > ()
    {
        @Override
        public Character read ( ByteBuffer stream ) throws IOException
        {
            return stream.getChar ();
        }

        @Override
        public void write ( Character struct, ByteBuffer stream ) throws IOException
        {
            stream.putChar ( struct );
        }
    };

    public static final Type < Short > Short = new Type < Short > ()
    {
        @Override
        public Short read ( ByteBuffer stream ) throws IOException
        {
            return stream.getShort ();
        }

        @Override
        public void write ( Short struct, ByteBuffer stream ) throws IOException
        {
            stream.putShort ( struct );
        }
    };

    public static final Type < Integer > Integer = new Type < Integer > ()
    {
        @Override
        public Integer read ( ByteBuffer stream ) throws IOException
        {
            return stream.getInt ();
        }

        @Override
        public void write ( Integer struct, ByteBuffer stream ) throws IOException
        {
            stream.putInt ( struct );
        }
    };

    public static final Type < Long > Long = new Type < Long > ()
    {
        @Override
        public Long read ( ByteBuffer stream ) throws IOException
        {
            return stream.getLong ();
        }

        @Override
        public void write ( Long struct, ByteBuffer stream ) throws IOException
        {
            stream.putLong ( struct );
        }
    };

    public static final Type < Double > Double = new Type < Double > ()
    {

        @Override
        public Double read ( ByteBuffer stream ) throws IOException
        {
            return stream.getDouble ();
        }

        @Override
        public void write ( Double struct, ByteBuffer stream ) throws IOException
        {
            stream.putDouble ( struct );
        }
    };
}
