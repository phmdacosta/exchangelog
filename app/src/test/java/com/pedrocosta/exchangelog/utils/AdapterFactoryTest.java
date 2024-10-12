package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.exchange.Exchange;
import com.pedrocosta.exchangelog.exchange.BAD_ExchangeAdapter;
import com.pedrocosta.exchangelog.notification.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.notification.adapters.BAD_QuoteNotificationRequestAdapter;
import com.pedrocosta.json.manager.adapter.AdapterFactory;
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
        assert adapterFactory.create(Exchange.class) instanceof BAD_ExchangeAdapter;
    }

    @Test
    public void testJsonQuoteNotificationRequestAdapterInstantiate() {
        assert adapterFactory.create(QuoteNotificationRequest.class) instanceof BAD_QuoteNotificationRequestAdapter;
    }
}
