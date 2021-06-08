package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.adapters.AdapterFactory;
import com.pedrocosta.exchangelog.adapters.ExchangeAdapter;
import com.pedrocosta.exchangelog.adapters.QuoteNotificationRequestAdapter;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class AdapterFactoryTest {

    private AdapterFactory adapterFactory;

    @BeforeEach
    public void setUp() {
        adapterFactory = new AdapterFactory();
    }

    @Test
    public void testJsonExchangeAdapterInstantiate() {
        assert adapterFactory.create(Exchange.class) instanceof ExchangeAdapter;
    }

    @Test
    public void testJsonQuoteNotificationRequestAdapterInstantiate() {
        assert adapterFactory.create(QuoteNotificationRequest.class) instanceof QuoteNotificationRequestAdapter;
    }
}
