package com.pedrocosta.exchangelog.api;

import com.sun.istack.NotNull;

public interface GetServiceRequest<RESP> {
    RESP get(@NotNull final String function, final Class<RESP> respClass);
    RESP get(@NotNull final String function, final String params, final Class<RESP> respClass);
}
