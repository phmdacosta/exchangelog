package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to execute business for batch processes.
 *
 * @author Pedro H M da Costa
 */
public interface BackOfficeService extends BusinessService {
    /**
     * Calculate all remain quote rates base on existing rates.
     *
     * @param exchanges List of existing exchanges
     *
     * @return List of old exchanges with new ones in addition.
     */
    List<Exchange> calculateOthersQuotes(List<Exchange> exchanges);
}
