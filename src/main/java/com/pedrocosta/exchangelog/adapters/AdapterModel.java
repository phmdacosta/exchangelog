package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;

public interface AdapterModel<T> {
    public TypeAdapter<T> getAdapter();
}
