package com.pedrocosta.exchangelog.utils;

import com.sun.istack.Nullable;

import java.math.BigDecimal;

/**
 * ENUM to help convert string type operator into logical one.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public enum ValueLogical {

    LESS_THEN("Less then", "<"),
    GREATER_THEN("Greater then", ">"),
    EQUALS("Equals", "="),
    LESS_AND_EQUALS("Less then and equals", "<="),
    GREATER_AND_EQUALS("Greater then and equals", ">="),
    MINIMUM("Minimum value", "min"),
    MAXIMUM("Maximum value", "max");

    private final String description;
    private final String operator;

    ValueLogical(String name, String operator) {
        this.description = name;
        this.operator = operator;
    }

    public String getDescription() {
        return description;
    }

    public String getOperator() {
        return operator;
    }

    /**
     * Generates a {@link ValueLogical} instance based on operator signal.
     *
     * @param str Operator signal
     * @return {@link ValueLogical} instance
     */
    @Nullable
    public static ValueLogical get(String str) {
        ValueLogical[] enumValues = ValueLogical.values();

        for (ValueLogical val : enumValues) {
            if (val.getDescription().equals(str)
                    || val.getOperator().equals(str))
                return val;
        }

        return null;
    }

    /**
     * Check condition of logical operation.
     *
     * @param val1 Reference value
     * @param val2 Value to compare
     * @return  True if two value comparison respects logical operation of the enum,
     *          False otherwise.
     */
    public boolean assertTrue(BigDecimal val1, BigDecimal val2) {
        if (val1 == null) {
            return false;
        }

        switch (this) {
            case EQUALS:
                return val1.compareTo(val2) == 0;
            case LESS_THEN:
                return val1.compareTo(val2) < 0;
            case GREATER_THEN:
                return val1.compareTo(val2) > 0;
            case LESS_AND_EQUALS:
                return val1.compareTo(val2) < 0 || val1.compareTo(val2) == 0;
            case GREATER_AND_EQUALS:
                return val1.compareTo(val2) > 0 || val1.compareTo(val2) == 0;
            default:
                return false;
        }
    }

    /**
     * Check condition of logical operation.
     *
     * @param val1 Reference value
     * @param val2 Value to compare
     * @return  True if two value comparison respects logical operation of the enum,
     *          False otherwise.
     */
    public boolean assertTrue(double val1, double val2) {
        return assertTrue(BigDecimal.valueOf(val1), BigDecimal.valueOf(val2));
    }

    public boolean isMin() {
        return MINIMUM.equals(this);
    }

    public boolean isMax() {
        return MAXIMUM.equals(this);
    }
}
