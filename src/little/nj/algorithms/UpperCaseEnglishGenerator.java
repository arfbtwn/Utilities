package little.nj.algorithms;

public class UpperCaseEnglishGenerator extends PermutationGenerator<Byte>
{
    UpperCaseEnglishGenerator(Callback<Byte> callback)
    {
        super(callback, convert("ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes()));
    }

    private static Byte[] convert(byte[] primitive)
    {
        Byte[] rv = new Byte[primitive.length];
        for(int i = 0; i < rv.length; ++i)
            rv[i] = primitive[i];
        return rv;
    }
}
