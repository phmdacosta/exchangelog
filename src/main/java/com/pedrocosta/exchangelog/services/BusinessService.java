package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import org.codehaus.jettison.json.JSONException;

import java.util.Date;
import java.util.List;

public interface BusinessService extends CoreService {
    public ServiceResponse<List<Currency>> loadCurrencies() throws JSONException;
    public ServiceResponse<Currency> loadCurrency(String code) throws JSONException;
    public ServiceResponse<List<Exchange>> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate);
}
