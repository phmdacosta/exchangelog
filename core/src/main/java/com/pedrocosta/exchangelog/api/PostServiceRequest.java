package com.pedrocosta.exchangelog.api;

import com.sun.istack.NotNull;

public interface PostServiceRequest<RESP> {
    RESP post(@NotNull final String function, final Object body, final Class<RESP> respClass);
    RESP post(@NotNull final String function, final String params, final Object body, final Class<RESP> respClass);
}
