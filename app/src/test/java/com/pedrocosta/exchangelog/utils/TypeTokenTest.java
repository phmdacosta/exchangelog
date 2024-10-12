package com.pedrocosta.exchangelog.utils;

import com.google.gson.reflect.TypeToken;
import com.pedrocosta.exchangelog.currency.Currency;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

public class TypeTokenTest {

    @Test
    public void testTypeTokenClass() {
        Type typeCcy = new TypeToken<Currency>(){}.getType();
        System.out.println(typeCcy.getTypeName());
        System.out.println(typeCcy.getClass().getSimpleName());

        Type typeListCcy = new TypeToken<List<Currency>>(){}.getType();
        System.out.println(typeListCcy.getTypeName());
        System.out.println(typeListCcy.getClass().getSimpleName());
    }
}
