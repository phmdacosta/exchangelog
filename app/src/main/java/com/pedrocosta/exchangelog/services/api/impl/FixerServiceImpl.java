package com.pedrocosta.exchangelog.services.api.impl;

import com.pedrocosta.exchangelog.api.exceptions.RestApiException;
import com.pedrocosta.exchangelog.currency.Currency;
import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exchange.Exchange;
import com.pedrocosta.exchangelog.request.RestResponse;
import com.pedrocosta.exchangelog.request.api.FixerRequester;
import com.pedrocosta.exchangelog.services.api.FixerService;
import com.pedrocosta.exchangelog.services.api.utils.ApiTypes;
import com.pedrocosta.springutils.DateUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Service to connect with Fixer webservice.
 *
 * @author Pedro H M da Costa
 */
@Service
public class FixerServiceImpl implements FixerService {

    private final String API_NAME = ApiTypes.FIXER;

    private final FixerRequester requester;

    public FixerServiceImpl(FixerRequester requester) {
        this.requester = requester;
    }

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
    @Override
    public Currency loadCurrency(String code) throws JSONException, NoSuchDataException {
//        ServiceResponse<Currency> result = ServiceResponse.createSuccess();
        if (code == null) {
            throw new NoSuchDataException(
                    Messages.get("api.ccy.not.found", "NULL", API_NAME));
        }

        Currency result = null;

        RestResponse response = requester.symbols();

        if (response.isSuccess()) {
            JSONObject json = response.getJsonObject();
            Log.info(this, json.toString());

            JSONObject jsonCcy = json.getJSONObject("symbols");
            Iterator itCodes = jsonCcy.keys();

            while (itCodes.hasNext()) {
                if (code.equals(itCodes.next())) {
                    result = new Currency(code, jsonCcy.getString(code));
//                        result.setObject(new Currency(code, jsonCcy.getString(code)));
                    break;
                }
            }

        } else {
            Log.error(this, getErrorMsg(response));
//            throw new NoSuchDataException(
//                    Messages.get("api.ccy.not.found", code, API_NAME));
//                result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                        Messages.get("api.ccy.not.found", code, API_NAME));
        }

        if (result == null) {
            throw new NoSuchDataException(
                    Messages.get("api.ccy.not.found", code, API_NAME));
        }

        return result;
    }

    /**
     * Load all currencies from Fixer API.
     *
     * @return {@link ServiceResponse} object with a list of {@link Currency}.
     * @throws JSONException if any json error happens
     * @throws NoSuchDataException if not found
     */
    @Override
    public List<Currency> loadCurrencies() throws JSONException, NoSuchDataException {
//        ServiceResponse<List<Currency>> result = ServiceResponse.createSuccess();
        List<Currency> result = new ArrayList<>();

        RestResponse response = requester.symbols();

        if (response.isSuccess()) {
            JSONObject json = response.getJsonObject();
            Log.info(this, json.toString());

            JSONObject jsonCcy = json.getJSONObject("symbols");
            Iterator itCodes = jsonCcy.keys();

//            List<Currency> currencies = new ArrayList<>();

            while (itCodes.hasNext()) {
                String code = (String) itCodes.next();
                result.add(new Currency(code, jsonCcy.getString(code)));
            }

//            result.setObject(currencies);

        } else {
            Log.error(this, getErrorMsg(response));
//            throw new ServiceException(Messages.get("api.no.ccy.found", API_NAME));
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("api.no.ccy.found", API_NAME));
        }

        if (result.isEmpty()) {
            throw new NoSuchDataException(
                    Messages.get("api.no.ccy.found", API_NAME));
        }

        return result;
    }

    /**
     * Get quote rate between a base currency and others.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     *
     * @return {@link ServiceResponse} object with a list of {@link Exchange}.
     */
    public List<Exchange> getQuoteRate(String baseCode) throws NoSuchDataException, RestApiException {
        return getQuoteRate(baseCode, null);
    }

    /**
     * Get quote rate between a base currency and others.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     * @param quoteCodes    Quote currency array to what rate will be found.
     *
     * @return {@link ServiceResponse} object with a list of {@link Exchange}.
     */
    public List<Exchange> getQuoteRate(String baseCode, String[] quoteCodes) throws NoSuchDataException, RestApiException {
//        ServiceResponse<List<Exchange>> result = ServiceResponse.createSuccess();
        List<Exchange> result = new ArrayList<>();

        try {
            String params;
            if (quoteCodes != null && quoteCodes.length > 0)
                params = "base="+baseCode+"&symbols="+quoteCodes;
            else
                params = "base="+baseCode;

            RestResponse response = requester.latest(params);

            JSONObject jsonExch = response.getJsonObject();
            List<Exchange> quotes = new ArrayList<>();

            if (response.isSuccess()) {

                Log.info(this, jsonExch.toString());

                Currency base = new Currency(baseCode, null);

                JSONObject jsonRates = jsonExch.getJSONObject("rates");
                Iterator itRates = jsonRates.keys();

                while (itRates.hasNext()) {
                    String quoteCode = (String) itRates.next();
                    if (quoteCode.equals(baseCode)) {
                        continue;
                    }

                    Currency quote = new Currency(quoteCode, null);
                    Date jsonDate = DateUtils.stringToDate(jsonExch.getString("date"), "yyyy-MM-dd");

                    double baseRate = jsonRates.getDouble(quoteCode);
                    result.add(createExchange(base, quote, baseRate, jsonDate));
//                    quotes.add(createExchange(base, quote, baseRate, jsonDate));
                }

//                result.setObject(quotes);
            } else {
                throw new NoSuchDataException(
                        Messages.get("api.no.exchange.found", API_NAME),
                        new RestApiException(getErrorMsg(response)));
//                Log.error(this, getErrorMsg(response));
//                throw new ServiceException(Messages.get("api.no.exchange.found", API_NAME));
//                result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                        Messages.get("api.no.exchange.found", API_NAME));
            }
        } catch (JSONException e) {
            throw new NoSuchDataException(
                    Messages.get("api.no.exchange.found", API_NAME), e);
//            Log.error(this, e);
//            throw new ServiceException(Messages.get("api.no.exchange.found", API_NAME));
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("api.no.exchange.found", API_NAME));
        }

        if (result.isEmpty()) {
            throw new NoSuchDataException(
                    Messages.get("api.no.exchange.found", API_NAME));
        }

        return result;
    }

    /**
     * Amount and date definition are not supported by this version.
     */
    @Override
    public List<Exchange> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate) throws NoSuchDataException, RestApiException {
        return getQuoteRate(baseCode, quoteCodes);
    }

    private Exchange createExchange(Currency ccy1, Currency ccy2, BigDecimal rate, Date valueDate) {
        Exchange exchange = new Exchange();
        exchange.setBaseCurrency(ccy1);
        exchange.setQuoteCurrency(ccy2);
        exchange.setValueDate(valueDate);
        exchange.setRate(rate);
        return exchange;
    }

    private Exchange createExchange(Currency ccy1, Currency ccy2, Double rate, Date valueDate) {
        return createExchange(ccy1, ccy2, BigDecimal.valueOf(rate), valueDate);
    }

    private String getErrorMsg(RestResponse response) throws JSONException {
        return response.getJsonObject().getJSONObject("error").getString("info");
    }
}
