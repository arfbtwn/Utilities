package little.nj.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.tests.MockObjects.Ob;
import little.nj.expressions.predicates.IPredicate;
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
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
        
        Ob result = ext.first(new IPredicate<Ob>() {

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
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
        
        Ob result = ext.last(new IPredicate<Ob>() {

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
        
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
                
        IExpressionEngine<Ob> result = ext.where(new IPredicate<Ob>() {

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
        
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
        
        IExpressionEngine<String> result = ext.select(new IExpression<Ob, String>() {

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
        
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
                
        assertEquals(ONES, ext.count(new IPredicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
    }
    
    @Test
    public void test_Contains() {
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
        
        assertEquals(true, ext.contains(new IPredicate<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
    }
    
    @Test
    public void test_Union() {
        IExpressionEngine<Ob> ext = new ExpressionEngine<>(obs);
        
        List<Ob> rv = ext.union(obs).toList();
        
        assertEquals(obs.size(), rv.size());
        
        assertEquals(obs, rv);
    }

}
