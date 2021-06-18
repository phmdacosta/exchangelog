package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.ServiceException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.services.BackOfficeService;
import com.pedrocosta.exchangelog.services.BusinessService;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.utils.Defaults;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import org.codehaus.jettison.json.JSONException;
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
        List<Exchange> rates = new ArrayList<>();
        try {
            rates = apiService.getQuoteRate(baseCcyCode,
                    null, 1D, new Date());
        } catch (NoSuchDataException e) {
            Log.warn(this, e.getMessage());
        }

        if (!rates.isEmpty()) {
            BusinessService batchService = (BusinessService) getServiceFactory().create(BackOfficeService.class);
            try {
                Currency base = batchService.loadCurrency(baseCcyCode);

                for (Exchange exchange : rates) {
                    String quoteCode = exchange.getQuoteCurrency().getCode();
                    if (quoteCode.equals(baseCcyCode)) {
                        continue;
                    }
                    Currency quote = batchService.loadCurrency(quoteCode);
                    Date valDate = exchange.getValueDate();

                    // We don't need to save valuation date with time
                    Calendar valDateCalendar = Calendar.getInstance();
                    valDateCalendar.setTime(valDate);
                    valDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    valDateCalendar.set(Calendar.MINUTE, 0);
                    valDateCalendar.set(Calendar.SECOND, 0);
                    valDateCalendar.set(Calendar.MILLISECOND, 0);

                    // Exchange rate for base currency
                    BigDecimal baseRate = exchange.getRate();
                    Exchange toSave = new Exchange(base, quote, baseRate, valDateCalendar.getTime());
                    updateExchangeWithExistingId(toSave);
                    quotes.add(toSave);
                }
            } catch (NoSuchDataException | JSONException e) {
                Log.error(this, e);
            }
        }
//        ServiceResponse<List<Exchange>> response =
//                apiService.getQuoteRate(baseCcyCode,
//                        null, 1D, new Date());

//        if (response.isSuccess()) {
//            BusinessService batchService = (BusinessService) getServiceFactory().create(BackOfficeService.class);
//            ServiceResponse<Currency> responseCcy = batchService.loadCurrency(baseCcyCode);
//
//            if (!responseCcy.isSuccess()) {
//                throw new ServiceException(responseCcy.getMessage());
//            }
//
//            Currency base = responseCcy.getObject().clone();
//
//            List<Exchange> rates = response.getObject();
//
//            for (Exchange exchange : rates) {
//                String quoteCode = exchange.getQuoteCurrency().getCode();
//                if (quoteCode.equals(baseCcyCode)) {
//                    continue;
//                }
//                responseCcy = batchService.loadCurrency(quoteCode);
//
//                if (!responseCcy.isSuccess()) {
//                    throw new ServiceException(responseCcy.getMessage());
//                }
//
//                Currency quote = batchService.loadCurrency(quoteCode).getObject();
//                Date valDate = response.getExecTime();
//
//                // We don't need to save valuation date with time
//                Calendar valDateCalendar = Calendar.getInstance();
//                valDateCalendar.setTime(valDate);
//                valDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
//                valDateCalendar.set(Calendar.MINUTE, 0);
//                valDateCalendar.set(Calendar.SECOND, 0);
//                valDateCalendar.set(Calendar.MILLISECOND, 0);
//
//                // Exchange rate for base currency
//                BigDecimal baseRate = exchange.getRate();
//                Exchange toSave = new Exchange(base, quote, baseRate, valDateCalendar.getTime());
//                updateExchangeWithExistingId(toSave);
//                quotes.add(toSave);
//            }
//        } else {
//            Log.error(this, response.getCode() + ": " + response.getMessage());
//            Log.error(this, response.getException());
//        }

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

            Exchange toSave = new Exchange(exchange.getQuoteCurrency(),
                    exchange.getBaseCurrency(), quoteRate,
                    exchange.getValueDate());
            updateExchangeWithExistingId(toSave);
            newList.add(toSave);
        }

        // Calculate rate for others currencies as base
        BackOfficeService batchService =
                (BackOfficeService) getServiceFactory().create(BackOfficeService.class);
        newList = batchService.calculateOthersQuotes(newList);

        return newList;
    }

    @Override
    public void doWrite(List<Exchange> list) throws Exception {
        ((ExchangeService) getServiceFactory().create(ExchangeService.class))
                .saveAll(list);
//        ServiceResponse<List<Exchange>> response = exchService.saveAll(list);
//
//        if (response.getObject() != null) {
//            Log.info(this, Messages.get("total.saved",
//                    String.valueOf(response.getObject().size())));
//        }
//        if (!response.isSuccess()) {
//            Log.error(this, response.getMessage());
//        }
    }

    /**
     * Update passing exchange object with its id from data, if it exists.
     * @param exchange {@link Exchange} object to be updated.
     */
    private void updateExchangeWithExistingId(Exchange exchange) {
        ExchangeService exchService = (ExchangeService) getServiceFactory().create(Exchange.class);
        Exchange dbExchange = exchService.find(exchange.getBaseCurrency(),
                exchange.getQuoteCurrency(), exchange.getValueDate());
        if (dbExchange != null) {
            exchange.setId(dbExchange.getId());
        }
//        ServiceResponse<Exchange> exchResp = exchService.find(
//                exchange.getBaseCurrency(), exchange.getQuoteCurrency(), exchange.getValueDate());
//        if (exchResp.isSuccess()) {
//            exchange.setId(exchResp.getObject().getId());
//        }
    }

    private void removeDuplicates(List<Exchange> exchanges) {

    }
}
