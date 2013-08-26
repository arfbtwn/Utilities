package little.nj.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.tests.MockObjects.Ob;
import little.nj.expressions.predicates.Predicate;
import little.nj.util.StringUtil;

import org.junit.BeforeClass;
import org.junit.Test;


public class ExpressionTest {

    static final int TOTAL = 1_000_000;
    static final int ONES = TOTAL / 10;
    
    private static List<Ob> obs;
    
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
        String msg = String.format("Time Taken: %dns", System.nanoTime() - start);
        System.out.println(msg);
    }
    
    @Test
    public void test_First() {
        startTime("test_First");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        Ob result = ext.first(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(0, obs.indexOf(result));
        
        endTime();
    }
    
    @Test
    public void test_Last() {
        startTime("test_Last");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        Ob result = ext.last(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(9 * ONES, obs.indexOf(result));
        
        endTime();
    }
    
    @Test
    public void test_Where() {
        startTime("test_Where");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
                
        ExpressionEngine<Ob> result = ext.where(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        int i = 0;
        for(@SuppressWarnings("unused") Ob j : result)
            ++i;
        
        assertEquals(getOnes(), i);
        
        endTime();
    }
    
    @Test
    public void test_Select() {
        startTime("test_Select");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        ExpressionEngine<String> result = ext.select(new Expression<Ob, String>() {

            @Override
            public String evaluate(Ob obj) {
                return obj.getField() == 1 ? "Hello World" : StringUtil.EMPTY_STRING; 
            } });
        
        int i = 0, j = 0;
        for (String k : result)
            if ("Hello World".equals(k))
                ++i;
            else
                ++j;
        
        assertEquals(getOnes(), i);
        assertEquals(getTwos(), j);

        endTime();
    }
    
    @Test
    public void test_Count() {
        startTime("test_Count");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
                
        assertEquals(getOnes(), ext.count(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
        
        endTime();
    }
    
    @Test
    public void test_Contains() {
        startTime("test_Contains");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        assertEquals(true, ext.contains(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
        
        endTime();
    }
    
    @Test
    public void test_Union() {
        startTime("test_Union");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        List<Ob> rv = ext.union(obs).toList();
        
        assertEquals(obs.size(), rv.size());
        
        assertEquals(obs, rv);
        
        endTime();
    }
    
    @Test
    public void test_Minus() {
        startTime("test_Minus");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        List<Ob> rv = ext.minus(obs).toList();
        
        assertEquals(0, rv.size());
        
        endTime();
    }
    
    @Test
    public void test_Intersect() {
        startTime("test_Intersect");
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        List<Ob> rv = ext.intersect(obs).toList();
        
        assertEquals(obs.size(), rv.size());
        
        endTime();
    }

}
