package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WebService extends BaseService {

    public WebService(ServiceFactory factory, Environment env,
                      Messages messages) {
        super(factory, env, messages);
    }

    public List<Exchange> getQuoteRate(@NotNull String baseCcy, String[] quoteCcy) throws NoSuchDataException {
        return getQuoteRate(baseCcy, quoteCcy, 1D, null);
    }

    public List<Exchange> getQuoteRate(@NotNull String baseCcy, String[] quoteCcy, Date valDate) throws NoSuchDataException {
        return getQuoteRate(baseCcy, quoteCcy, 1D, valDate);
    }

    public List<Exchange> getQuoteRate(@NotNull String baseCcy, String[] quoteCcy, Double amount) throws NoSuchDataException {
        return getQuoteRate(baseCcy, quoteCcy, amount, null);
    }

    public List<Exchange> getQuoteRate(@NotNull String baseCode, String[] quoteCodes, Double amount, Date valDate) throws NoSuchDataException {
        // Check arguments
        if (baseCode == null) {
            throw new NoSuchDataException(messages.getMessage("bad.request"));
        }

        List<Exchange> exchanges = new ArrayList<>();

        if (quoteCodes == null || quoteCodes.length == 0) {
            exchanges = getExchanges(baseCode, valDate);
        }

        if (exchanges.isEmpty()) {
            for (String quoteCode : quoteCodes) {
                Date targetDate = valDate;
                if (valDate == null) {
                    targetDate = new Date();
                }
                Exchange exchange = getExchange(baseCode, quoteCode, targetDate);

                if (exchange == null) {
                    throw new NoSuchDataException(messages.getMessage("could.not.find",
                            "quote value"));
                }

//                double rate = CustomMath.multiply(amount, exchange.getRate());
                BigDecimal rate = exchange.getRate().multiply(BigDecimal.valueOf(amount));
                exchange.setRate(rate);
                exchanges.add(exchange);
            }
        }

        // Everything is ok
        return exchanges;
    }

    private List<Exchange> getExchanges(String baseCode, Date valDate) {
        CurrencyService ccyService = (CurrencyService) factory.create(CurrencyService.class);
        Currency base = ccyService.find(baseCode);

        ExchangeService exchService = (ExchangeService) factory.create(ExchangeService.class);
        return exchService.find(base, valDate);
    }

    private Exchange getExchange(String baseCode, String quoteCode, Date valDate) {
        CurrencyService ccyService = (CurrencyService) factory.create(CurrencyService.class);
        Currency base = ccyService.find(baseCode);
        Currency quote = ccyService.find(quoteCode);

        ExchangeService exchService = (ExchangeService) factory.create(ExchangeService.class);

        return exchService.find(base, quote, valDate);
    }
}
