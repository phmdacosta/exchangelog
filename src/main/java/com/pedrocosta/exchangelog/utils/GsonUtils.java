package com.pedrocosta.exchangelog.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.pedrocosta.exchangelog.adapters.AdapterFactory;
import com.pedrocosta.exchangelog.exceptions.NotSupportedException;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class GsonUtils {

    private final AdapterFactory adapterFactory;
//    private Gson gson;
    private GsonBuilder builder;

    public GsonUtils(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    public final String toJson(Object obj) throws NullPointerException, InvalidParameterException {
        return toJson(obj, null,null);
    }

    public  final String toJson(Object obj, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return toJson(obj, adapter, null);
    }

    public final String toJson(Object obj, String type) throws NullPointerException, InvalidParameterException {
        return toJson(obj, null, type);
    }

    public final String toJson(Object obj, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(obj.getClass(), adapter, type).toJson(obj);
    }

    public final <T> T fromJson(String json, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, adapter, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null, type);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(json, classOfT);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, null, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, adapter, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, null, type);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(reader, classOfT);
    }

    private <T> Gson createGson(Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        if (builder == null) {
            builder = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }

        if (adapter == null && adapterFactory != null) {
            if (type != null && !type.isBlank()) {
                adapter = adapterFactory.create(classOfT, type);
                if (adapter != null) {
                    builder.registerTypeAdapter(classOfT, adapter);
                } else {
                    Log.warn(this, Messages.get("error.adapter.not.found",
                            classOfT.getSimpleName(), type));
                }
            } else {
                builder.registerTypeAdapterFactory(adapterFactory);
            }
        }

        return builder.create();
    }
}
