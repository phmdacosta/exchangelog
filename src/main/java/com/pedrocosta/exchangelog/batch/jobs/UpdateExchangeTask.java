package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.exceptions.ServiceException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.services.*;
import com.pedrocosta.exchangelog.utils.Defaults;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.math.BigDecimal;
import java.util.*;

/**
 * Task to update database with new exchanges information from API.
 * @author Pedro H M da Costa
 */
public class UpdateExchangeTask extends ScheduledTask<List<Exchange>, List<Exchange>> {

    @Override
    public List<Exchange> doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<Exchange> quotes = new ArrayList<>();

        String baseCcyCode = getEnvironment().getProperty(PropertyNames.PROJECT_DEFAULT_CURRENCY);
        BusinessService apiService = (BusinessService) getServiceFactory().create(getProjectEngine());
        ServiceResponse<List<Exchange>> response =
                apiService.getQuoteRate(baseCcyCode,
                        null, 1D, new Date());

        if (response.isSuccess()) {
            BusinessService batchService = (BusinessService) getServiceFactory().create(BackOfficeService.class);
            ServiceResponse<Currency> responseCcy = batchService.loadCurrency(baseCcyCode);

            if (!responseCcy.isSuccess()) {
                throw new ServiceException(responseCcy.getMessage());
            }

            Currency base = responseCcy.getObject().clone();

            List<Exchange> rates = response.getObject();
            Iterator<Exchange> itRates = rates.iterator();

            while (itRates.hasNext()) {
                Exchange exchange = itRates.next();
                String quoteCode = exchange.getQuoteCurrency().getCode();
                if (quoteCode.equals(baseCcyCode)) {
                    continue;
                }
                responseCcy = batchService.loadCurrency(quoteCode);

                if (!responseCcy.isSuccess()) {
                    throw new ServiceException(responseCcy.getMessage());
                }

                Currency quote = batchService.loadCurrency(quoteCode).getObject();
                Date valDate = response.getExecTime();

                // We don't need to save valuation date with time
                Calendar valDateCalendar = Calendar.getInstance();
                valDateCalendar.setTime(valDate);
                valDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                valDateCalendar.set(Calendar.MINUTE, 0);
                valDateCalendar.set(Calendar.SECOND, 0);
                valDateCalendar.set(Calendar.MILLISECOND, 0);

                // Exchange rate for base currency
                BigDecimal baseRate = exchange.getRate();
                quotes.add(new Exchange(base, quote, baseRate, valDateCalendar.getTime()));
            }
        } else {
            Log.error(this, response.getCode() + ": " + response.getMessage());
            Log.error(this, response.getException());
        }

        return quotes;
    }

    @Override
    public List<Exchange> doProcess(List<Exchange> list) throws Exception {
        List<Exchange> newList = new ArrayList<>(list);

        for (Exchange exchange : list) {
            BigDecimal quoteRate = BigDecimal.ZERO;
            int scale = 2; // minimum scale
            // To handle highly inflated currencies
            while (quoteRate.doubleValue() == 0) {
                quoteRate = BigDecimal.ONE.setScale(++scale, Defaults.ROUNDING_MODE)
                        .divide(exchange.getRate(), Defaults.ROUNDING_MODE);
            }

            newList.add(new Exchange(exchange.getQuoteCurrency(),
                    exchange.getBaseCurrency(), quoteRate,
                    exchange.getValueDate()));
        }

        // Calculate rate for others currencies as base
        BackOfficeService batchService =
                (BackOfficeService) getServiceFactory().create(BackOfficeService.class);
        newList = batchService.calculateOthersQuotes(newList);

        return newList;
    }

    @Override
    public void doWrite(List<Exchange> list) throws Exception {
        ExchangeService exchService =
                (ExchangeService) getServiceFactory().create(ExchangeService.class);
        ServiceResponse<List<Exchange>> response = exchService.saveAll(list);

        if (response.getObject() != null) {
            Log.info(this, Messages.get("total.saved",
                    String.valueOf(response.getObject().size())));
        }
        if (!response.isSuccess()) {
            Log.error(this, response.getMessage());
        }
    }
}
