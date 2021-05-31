package com.pedrocosta.exchangelog.persistence;

import com.pedrocosta.exchangelog.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Search for currency by its code.
     *
     * @param code  Currency code
     *
     * @return Found {@link Currency} instance.
     */
    Currency findByCode(String code);
}
