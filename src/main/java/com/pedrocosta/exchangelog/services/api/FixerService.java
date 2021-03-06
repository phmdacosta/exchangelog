package com.pedrocosta.exchangelog.services.api;

import com.pedrocosta.exchangelog.exceptions.ApiException;
import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.services.BusinessService;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service to connect with Fixer webservice.
 *
 * @author Pedro H M da Costa
 */
public interface FixerService extends BusinessService {

    /**
     * Load a specific currency from Fixer API.
     *
     * @param code Currency code
     *
     * @return {@link ServiceResponse} object with {@link Currency}.
     *
     * @throws JSONException if any json error happens
     * @throws NoSuchDataException if not found
     */
    @Override Currency loadCurrency(String code) throws JSONException, NoSuchDataException;

    /**
     * Load all currencies from Fixer API.
     *
     * @return {@link ServiceResponse} object with a list of {@link Currency}.
     * @throws JSONException if any json error happens
     * @throws NoSuchDataException if not found
     */
    @Override List<Currency> loadCurrencies() throws JSONException, NoSuchDataException;

    /**
     * Get quote rate between a base currency and others.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     *
     * @return {@link ServiceResponse} object with a list of {@link Exchange}.
     */
    List<Exchange> getQuoteRate(String baseCode) throws NoSuchDataException, ApiException;

    /**
     * Get quote rate between a base currency and others.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     * @param quoteCodes    Quote currency array to what rate will be found.
     *
     * @return {@link ServiceResponse} object with a list of {@link Exchange}.
     */
    List<Exchange> getQuoteRate(String baseCode, String[] quoteCodes) throws NoSuchDataException, ApiException;

    /**
     * Amount and date definition are not supported by this version.
     */
    @Deprecated
    @Override
    List<Exchange> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate) throws NoSuchDataException, ApiException;
}
