package com.pedrocosta.exchangelog.services;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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

    public ServiceResponse<List<Exchange>> getQuoteRate(@NotNull String baseCcy, String[] quoteCcy) {
        return getQuoteRate(baseCcy, quoteCcy, 1D, null);
    }

    public ServiceResponse<List<Exchange>> getQuoteRate(@NotNull String baseCcy, String[] quoteCcy, Date valDate) {
        return getQuoteRate(baseCcy, quoteCcy, 1D, valDate);
    }

    public ServiceResponse<List<Exchange>> getQuoteRate(@NotNull String baseCcy, String[] quoteCcy, Double amount) {
        return getQuoteRate(baseCcy, quoteCcy, amount, null);
    }

    public ServiceResponse<List<Exchange>> getQuoteRate(@NotNull String baseCode, String[] quoteCodes, Double amount, Date valDate) {
        // Check arguments
        if (baseCode == null) {
            return ServiceResponse.<List<Exchange>>createError(HttpStatus.BAD_REQUEST,
                    messages.getMessage("bad.request"))
                    .setException(new IllegalArgumentException());
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
                    return ServiceResponse.createError(HttpStatus.NOT_FOUND,
                            messages.getMessage("could.not.find", "quote value"));
                }

//                double rate = CustomMath.multiply(amount, exchange.getRate());
                BigDecimal rate = exchange.getRate().multiply(BigDecimal.valueOf(amount));
                exchange.setRate(rate);
                exchanges.add(exchange);
            }
        }

        // Everything is ok
        return ServiceResponse.<List<Exchange>>createSuccess().setObject(exchanges);
    }

    private List<Exchange> getExchanges(String baseCode, Date valDate) {
        CurrencyService ccyService = (CurrencyService) factory.create(CurrencyService.class);
        Currency base = ccyService.find(baseCode).getObject();

        ExchangeService exchService = (ExchangeService) factory.create(ExchangeService.class);
        return exchService.find(base, valDate).getObject();
    }

    private Exchange getExchange(String baseCode, String quoteCode, Date valDate) {
        CurrencyService ccyService = (CurrencyService) factory.create(CurrencyService.class);
        Currency base = ccyService.find(baseCode).getObject();
        Currency quote = ccyService.find(quoteCode).getObject();

        ExchangeService exchService = (ExchangeService) factory.create(ExchangeService.class);

        return exchService.find(base, quote, valDate).getObject();
    }
}
