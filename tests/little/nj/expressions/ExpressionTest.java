package little.nj.expressions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.tests.MockObjects.Ob;
import little.nj.expressions.predicates.Predicate;
import little.nj.util.Statics;

import org.junit.*;


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
        IExpressionIterable<Ob> ext = new ExpressionIterable<>(obs);
        
        Ob result = ext.first(new Predicate<Ob>() {

            @Override
            public Boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(0, obs.indexOf(result));
    }
    
    @Test
    public void test_Last() {
        IExpressionIterable<Ob> ext = new ExpressionIterable<>(obs);
        
        Ob result = ext.last(new Predicate<Ob>() {

            @Override
            public Boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        assertNotNull(result);
        assertEquals(1, result.getField());
        assertEquals(90, obs.indexOf(result));
    }
    
    @Test
    public void test_Where() {
        
        IExpressionIterable<Ob> ext = new ExpressionIterable<>(obs);
                
        IExpressionIterable<Ob> result = ext.where(new Predicate<Ob>() {

            @Override
            public Boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } });
        
        int i = 0;
        for(@SuppressWarnings("unused") Ob j : result)
            ++i;
        
        assertEquals(ONES, i);
    }
    
    @Test
    public void test_Select() {
        
        IExpressionIterable<Ob> ext = new ExpressionIterable<>(obs);
        
        IExpressionIterable<String> result = ext.select(new IExpression<String, Ob>() {

            @Override
            public String evaluate(Ob obj) {
                return obj.getField() == 1 ? "Hello World" : Statics.EMPTY_STRING; 
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
        
        IExpressionIterable<Ob> ext = new ExpressionIterable<>(obs);
                
        assertEquals(ONES, ext.count(new Predicate<Ob>() {

            @Override
            public Boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
    }
    
    @Test
    public void test_Contains() {
        IExpressionIterable<Ob> ext = new ExpressionIterable<>(obs);
        
        assertEquals(true, ext.contains(new Predicate<Ob>() {

            @Override
            public Boolean evaluate(Ob obj) {
                return obj.getField() == 1;
            } }));
    }
    
    @Test
    public void test_PredicateIterator_Null_Protector() {
        
        IExpressionIterable<Ob> ext =
                new ExpressionIterable<>(obs);
                
        assertEquals(0, ext.count(new Predicate<Ob>() {

            @Override
            public Boolean evaluate(Ob obj) {
                return null;
            } }));
    }

}
