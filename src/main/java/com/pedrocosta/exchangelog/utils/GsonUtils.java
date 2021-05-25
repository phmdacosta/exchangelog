package com.pedrocosta.exchangelog.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.pedrocosta.exchangelog.adapters.AdapterFactory;
import com.pedrocosta.exchangelog.adapters.AdapterModel;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class GsonUtils {

    private final AdapterFactory adapterFactory;

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
        return createGson(obj.getClass(), null, type).toJson(obj);
    }

    public final String toJson(Object obj, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(obj.getClass(), adapter, type).toJson(obj);
    }

    public final <T> T fromJson(String json, Class<T> clasOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(json, clasOfT, null, null);
    }

    public final <T> T fromJson(String json, Class<T> clasOfT, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(json, clasOfT, adapter, null);
    }

    public final <T> T fromJson(String json, Class<T> clasOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(json, clasOfT, null, type);
    }

    public final <T> T fromJson(String json, Class<T> clasOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(clasOfT, adapter, type).fromJson(json, clasOfT);
    }

    private <T> Gson createGson(Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        if (adapter == null) {
            if (adapterFactory == null) {
                throw new NullPointerException(MessageProperties.get(
                        "error.adapter.not.set"));
            }

            adapter = adapterFactory.create(classOfT, type);
        }

        Gson gson = new Gson();

        if (adapter != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(classOfT, adapter);
            gson = builder.create();
        } else {
            throw new InvalidParameterException(
                    MessageProperties.get("error.adapter.not.found",
                            classOfT.getSimpleName(), type));
        }

        return gson;
    }
}
