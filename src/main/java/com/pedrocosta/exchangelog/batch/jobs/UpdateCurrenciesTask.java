package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.services.BusinessService;
import com.pedrocosta.exchangelog.services.CurrencyService;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class UpdateCurrenciesTask extends ScheduledTask<List<Currency>, List<Currency>> {
    @Override
    public List<Currency> doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Log.info(this, Messages.get("task.reading", getProjectEngine()));
        List<Currency> currencies = new ArrayList<>();
        BusinessService apiService = (BusinessService) getServiceFactory()
                .create(getProjectEngine());
        ServiceResponse<List<Currency>> response = apiService.loadCurrencies();
        if (response.isSuccess()) {
            currencies = response.getObject();
        }
        return currencies;
    }

    @Override
    public List<Currency> doProcess(List<Currency> currencies) throws Exception {
        Log.info(this, Messages.get("task.processing", getProjectEngine()));
        List<Currency> currenciesToSave = new ArrayList<>(currencies.size());
        CurrencyService service = (CurrencyService) getServiceFactory()
                .create(CurrencyService.class);
        final ServiceResponse<List<Currency>> response = service.findAll();
        currencies.forEach(ccy -> {
            if (response.getObject().contains(ccy)) {
                currenciesToSave.add(ccy);
            }
        });
        return currenciesToSave;
    }

    @Override
    public void doWrite(List<Currency> list) throws Exception {
        Log.info(this, Messages.get("task.writing",
                getProjectEngine()));
        CurrencyService service = (CurrencyService) getServiceFactory()
                .create(CurrencyService.class);
        ServiceResponse<List<Currency>> response = service.saveAll(list);

        if (response.getObject() != null) {
            Log.info(this, Messages.get("total.saved",
                    String.valueOf(response.getObject().size())));
        }
        if (!response.isSuccess()) {
            Log.error(this, response.getMessage());
        }
    }
}
