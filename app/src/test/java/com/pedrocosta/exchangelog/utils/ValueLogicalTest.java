package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.services.api.utils.ValueLogical;
import org.junit.jupiter.api.Test;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ValueLogicalTest {

    private final double valLess = 25.654D;
    private final double valGreater = 70.342D;

    @Test
    public void testInstanceEquals() {
        ValueLogical valueLogical = ValueLogical.get("=");
        assert ValueLogical.EQUALS == valueLogical;
    }

    @Test
    public void testInstanceLessThen() {
        ValueLogical valueLogical = ValueLogical.get("<");
        assert ValueLogical.LESS_THEN == valueLogical;
    }

    @Test
    public void testInstanceGreaterThen() {
        ValueLogical valueLogical = ValueLogical.get(">");
        assert ValueLogical.GREATER_THEN == valueLogical;
    }

    @Test
    public void testInstanceLessAndEquals() {
        ValueLogical valueLogical = ValueLogical.get("<=");
        assert ValueLogical.LESS_AND_EQUALS == valueLogical;
    }

    @Test
    public void testInstanceGreaterAndEquals() {
        ValueLogical valueLogical = ValueLogical.get(">=");
        assert ValueLogical.GREATER_AND_EQUALS == valueLogical;
    }

    @Test
    public void testInstanceNull() {
        ValueLogical valueLogical = ValueLogical.get("lskff");
        assert null == valueLogical;
    }

    @Test
    public void testValueIsEquals() {
        ValueLogical valueLogical = ValueLogical.EQUALS;
        assert valueLogical.assertTrue(valLess, valLess);
    }

    @Test
    public void testValueIsLessThen() {
        ValueLogical valueLogical = ValueLogical.LESS_THEN;
        assert valueLogical.assertTrue(valLess, valGreater);
    }

    @Test
    public void testValueIsGreaterThen() {
        ValueLogical valueLogical = ValueLogical.GREATER_THEN;
        assert valueLogical.assertTrue(valGreater, valLess);
    }

    @Test
    public void testValueIsLessAndEquals() {
        ValueLogical valueLogical = ValueLogical.LESS_AND_EQUALS;
        assert valueLogical.assertTrue(valLess, valLess);
        assert valueLogical.assertTrue(valLess, valGreater);
    }

    @Test
    public void testValueIsGreaterAndEquals() {
        ValueLogical valueLogical = ValueLogical.GREATER_AND_EQUALS;
        assert valueLogical.assertTrue(valGreater, valGreater);
        assert valueLogical.assertTrue(valGreater, valLess);
    }

    @Test
    public void testValueIsNotEquals() {
        ValueLogical valueLogical = ValueLogical.EQUALS;
        assert !valueLogical.assertTrue(valLess, valGreater);
    }

    @Test
    public void testValueIsNotLessThen() {
        ValueLogical valueLogical = ValueLogical.LESS_THEN;
        assert !valueLogical.assertTrue(valGreater, valLess);
    }

    @Test
    public void testValueIsNotGreaterThen() {
        ValueLogical valueLogical = ValueLogical.GREATER_THEN;
        assert !valueLogical.assertTrue(valLess, valGreater);
    }

    @Test
    public void testValueIsNotLessAndEquals() {
        ValueLogical valueLogical = ValueLogical.LESS_AND_EQUALS;
        assert !valueLogical.assertTrue(valGreater, valLess);
    }

    @Test
    public void testValueIsNotGreaterAndEquals() {
        ValueLogical valueLogical = ValueLogical.GREATER_AND_EQUALS;
        assert !valueLogical.assertTrue(valLess, valGreater);
    }
}
