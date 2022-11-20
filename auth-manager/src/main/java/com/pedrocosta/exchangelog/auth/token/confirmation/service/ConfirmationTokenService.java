package com.pedrocosta.exchangelog.auth.token.confirmation.service;

import com.pedrocosta.exchangelog.auth.token.confirmation.ConfirmationToken;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import javassist.NotFoundException;

public interface ConfirmationTokenService {
    /**
     * Create or update a confirmation token in database
     * @param confirmToken  {@link ConfirmationToken}
     * @throws SaveDataException    Exception throw when something
     *                              goes wrong with business saving validation
     */
    ConfirmationToken save(ConfirmationToken confirmToken) throws SaveDataException;

    /**
     * Find a token in database
     * @param token Token value
     * @return  Found {@link ConfirmationToken}
     * @throws NotFoundException    Throw if it not exists
     */
    ConfirmationToken find(String token) throws NotFoundException;

    /**
     * Do confirmation of token by user
     * @param token Token value
     * @throws SaveDataException    Exception throw when something
     *                              goes wrong with business saving validation
     */
    void confirm(String token) throws SaveDataException, IllegalArgumentException;
}
