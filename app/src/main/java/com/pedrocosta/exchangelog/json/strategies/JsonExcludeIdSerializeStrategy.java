package com.pedrocosta.exchangelog.json.strategies;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonExcludeIdSerializeStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getName().equals("id");
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
