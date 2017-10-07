package little.nj.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.MockObjects.Ob;
import little.nj.expressions.predicates.Predicate;
import little.nj.util.StringUtil;

import org.junit.BeforeClass;
import org.junit.Test;


public class ExpressionTest {

    static final int TOTAL = 1000000;
    static final int ONES = TOTAL / 10;

    private static List<Ob> obs;

    private ExpressionEngine<Ob> createEngine() {
        return new ExpressionEngineImpl<Ob>(obs);
    }

    @BeforeClass
    public static void setUp() {
        startTime(String.format("setUp: Total='%d', Ones='%d', Twos='%d'",
                TOTAL, getOnes(), getTwos()));

        obs = new ArrayList<Ob>(TOTAL);

        int i;
        for(i = 0; i < TOTAL ; ++i) {
            obs.add(new Ob(i % ONES == 0 ? 1 : 2));
        }

        endTime();
    }

    private static int getOnes() {
        return TOTAL / ONES;
    }

    private static int getTwos() {
        return TOTAL - TOTAL / ONES;
    }

    private static long start;

    private static void startTime(String msg) {
        System.out.println(msg);
        start = System.nanoTime();
    }

    private static void endTime() {
        String msg = String.format(
                "Time Per 'N': %d ns",
                (System.nanoTime() - start) / TOTAL
                );
        System.out.println(msg);
    }

    @Test
    public void test_First() {
        startTime("test_First");

        ExpressionEngine<Ob> ext = createEngine();

        Ob result = ext.first(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });

        endTime();

        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(0, obs.indexOf(result));
    }

    @Test
    public void test_Last() {
        startTime("test_Last");

        ExpressionEngine<Ob> ext = createEngine();

        Ob result = ext.last(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 2;
            } });

        endTime();

        assertNotNull(result);
        assertEquals(2, result.getField());
        assertEquals(999999, obs.indexOf(result));
    }

    @Test
    public void test_Where() {
        startTime("test_Where");

        ExpressionEngine<Ob> ext = createEngine();

        ExpressionEngine<Ob> result = ext.where(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });

        endTime();

        int i = 0;
        for(@SuppressWarnings("unused") Ob j : result)
            ++i;

        endTime();

        assertEquals(getOnes(), i);
    }

    @Test
    public void test_Select() {
        startTime("test_Select");

        ExpressionEngine<Ob> ext = createEngine();

        ExpressionEngine<String> result = ext.select(new Expression<Ob, String>() {

            @Override
            public String evaluate(Ob obj) {
                return obj.getField() == 1 ? "Hello World" : StringUtil.EMPTY_STRING;
            } });

        endTime();

        int i = 0, j = 0;
        for (String k : result)
            if ("Hello World".equals(k))
                ++i;
            else
                ++j;

        endTime();

        assertEquals(getOnes(), i);
        assertEquals(getTwos(), j);
    }

    @Test
    public void test_Count() {
        startTime("test_Count");

        ExpressionEngine<Ob> ext = createEngine();

        int result = ext.count(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });

        endTime();

        assertEquals(getOnes(), result);
    }

    @Test
    public void test_Contains() {
        startTime("test_Contains");

        ExpressionEngine<Ob> ext = createEngine();

        boolean result = ext.contains(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });

        endTime();

        assertEquals(true, result);
    }

    @Test
    public void test_Union() {
        startTime("test_Union");

        ExpressionEngine<Ob> ext = createEngine();

        List<Ob> rv = ext.union(obs).toList();

        endTime();

        assertEquals(obs.size(), rv.size());

        assertEquals(obs, rv);
    }

    @Test
    public void test_Minus() {
        startTime("test_Minus");

        ExpressionEngine<Ob> ext = createEngine();

        List<Ob> rv = ext.minus(obs).toList();

        endTime();

        assertEquals(0, rv.size());
    }

    @Test
    public void test_Intersect() {
        startTime("test_Intersect");

        ExpressionEngine<Ob> ext = createEngine();

        List<Ob> rv = ext.intersect(obs).toList();

        endTime();

        assertEquals(obs.size(), rv.size());
    }

}
