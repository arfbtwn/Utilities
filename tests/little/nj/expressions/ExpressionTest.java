package little.nj.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.tests.MockObjects.Ob;
import little.nj.expressions.predicates.Predicate;
import little.nj.util.StringUtil;

import org.junit.Before;
import org.junit.Test;


public class ExpressionTest {

    static final int TOTAL = 100;
    static final int ONES = 10;
    
    protected List<Ob> obs;
    
    @Before
    public void setUp() {
        obs = new ArrayList<Ob>();
        
        int i;
        for(i = 0; i < TOTAL ; ++i) {
            obs.add(new Ob(i % ONES == 0 ? 1 : 2));
        }
    }
    
    @Test
    public void test_First() {
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        Ob result = ext.first(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(0, obs.indexOf(result));
    }
    
    @Test
    public void test_Last() {
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        Ob result = ext.last(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(90, obs.indexOf(result));
    }
    
    @Test
    public void test_Where() {
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
                
        ExpressionEngine<Ob> result = ext.where(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        int i = 0;
        for(@SuppressWarnings("unused") Ob j : result)
            ++i;
        
        assertEquals(ONES, i);
    }
    
    @Test
    public void test_Select() {
        
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
        
        assertEquals(ONES, i);
        assertEquals(TOTAL - ONES, j);
        
    }
    
    @Test
    public void test_Count() {
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
                
        assertEquals(ONES, ext.count(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
    }
    
    @Test
    public void test_Contains() {
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        assertEquals(true, ext.contains(new Predicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
    }
    
    @Test
    public void test_Union() {
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        List<Ob> rv = ext.union(obs).toList();
        
        assertEquals(obs.size(), rv.size());
        
        assertEquals(obs, rv);
    }
    
    @Test
    public void test_Minus() {
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        List<Ob> rv = ext.minus(obs).toList();
        
        assertEquals(0, rv.size());
        
    }
    
    @Test
    public void test_Intersect() {
        
        ExpressionEngine<Ob> ext = new ExpressionEngineImpl<>(obs);
        
        List<Ob> rv = ext.intersect(obs).toList();
        
        assertEquals(obs.size(), rv.size());
    }

}
