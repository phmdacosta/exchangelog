package com.pedrocosta.exchangelog.currency;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.services.BusinessService;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
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
        List<Currency> currencies = new ArrayList<>();
        try {
            BusinessService apiService = (BusinessService) getServiceFactory()
                    .create(getProjectEngine());
            currencies = apiService.loadCurrencies();
        } catch (NoSuchDataException e) {
            Log.warn(this, e.getMessage());
        }
        return currencies;
    }

    @Override
    public List<Currency> doProcess(List<Currency> currencies) throws Exception {
        List<Currency> currenciesToSave = new ArrayList<>(currencies.size());

        CurrencyService service = getServiceFactory().create(CurrencyService.class);
        final List<Currency> existingCcyList = service.findAll();
        if (existingCcyList.isEmpty()) {
            currenciesToSave = currencies;
        } else {
            List<Currency> auxCurrenciesToSave = currenciesToSave;
            currencies.forEach(ccy -> {
                if (!existingCcyList.contains(ccy)) {
                    auxCurrenciesToSave.add(ccy);
                }
            });
        }

        return currenciesToSave;
    }

    @Override
    public void doWrite(List<Currency> list) throws Exception {
        Log.info(this, Messages.get("task.writing",
                getProjectEngine()));

        try {
            getServiceFactory().create(CurrencyService.class).saveAll(list);
        } catch (SaveDataException e) {
            Log.error(this, e);
        }
    }
}
