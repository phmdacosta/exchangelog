package com.pedrocosta.exchangelog.api;

import com.pedrocosta.exchangelog.exceptions.ExternalServiceException;
import com.sun.istack.NotNull;

public interface GetServiceRequest<RESP> {
    RESP get(@NotNull final String function, final Class<RESP> respClass) throws ExternalServiceException;
    RESP get(@NotNull final String function, final String params, final Class<RESP> respClass) throws ExternalServiceException;
}
