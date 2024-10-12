package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.CoreService;
import com.pedrocosta.exchangelog.api.exceptions.RestApiException;
import com.pedrocosta.exchangelog.currency.Currency;
import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exchange.Exchange;
import org.codehaus.jettison.json.JSONException;

import java.util.Date;
import java.util.List;

public interface BusinessService extends CoreService {
    /**
     * Load all currencies from third party exchange API.
     *
     * @return List of {@link Currency} objects.
     * @throws JSONException if any json error happens
     * @throws NoSuchDataException if not found
     */
    List<Currency> loadCurrencies() throws JSONException, NoSuchDataException;

    /**
     * Load a specific currency from third party exchange API.
     *
     * @param code Currency code
     *
     * @return Found {@link Currency}.
     *
     * @throws JSONException if any json error happens
     * @throws NoSuchDataException if not found
     */
    Currency loadCurrency(String code) throws JSONException, NoSuchDataException;

    /**
     * Get quote rate between a base currency and others from third party exchange API.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     * @param quoteCodes    Quote currency array to what rate will be found.
     *
     * @return List of {@link Exchange} objects.
     * @throws NoSuchDataException if not found
     */
    List<Exchange> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate) throws NoSuchDataException, RestApiException;
}
