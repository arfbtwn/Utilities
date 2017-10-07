package little.nj.reflection;

import java.lang.reflect.Field;

public class _Field
{
    static RuntimeException _runtime ( Exception e )
    {
        return new RuntimeException ( e );
    }

    public final Field field;

    public _Field ( Field field )
    {
        this.field = field;
    }

    public Object get ( Object struct )
    {
        try
        {
            return field.get ( struct );
        }
        catch ( IllegalAccessException e )
        {
            throw _runtime ( e );
        }
    }

    public int getInt ( Object struct )
    {
        try
        {
            return field.getInt ( struct );
        }
        catch ( IllegalAccessException e )
        {
            throw _runtime ( e );
        }
    }

    public void set ( Object struct, Object value )
    {
        try
        {
            field.set ( struct, value );
        }
        catch ( IllegalAccessException e )
        {
            throw _runtime ( e );
        }
    }
}
