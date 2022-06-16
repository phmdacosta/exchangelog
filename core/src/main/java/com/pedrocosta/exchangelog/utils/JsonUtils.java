package com.pedrocosta.exchangelog.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.pedrocosta.exchangelog.api.RestResponse;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import com.pedrocosta.springutils.jsonmanager.stream.JsonReader;
import com.pedrocosta.springutils.output.Log;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Utility class to serialize JSON.
 *
 * @author Pedro H. M . da Costa
 * @since 1.0
 */
@Component
public class JsonUtils extends com.pedrocosta.springutils.jsonmanager.JsonUtils {

    @Override
    public JsonUtils setTypeAdapterFactory(UtilsTypeAdapterFactory typeAdapterFactory) {
        super.setTypeAdapterFactory(typeAdapterFactory);
        return this;
    }

    public final String toJson(Object obj, String type) throws NullPointerException, InvalidParameterException {
        return toJson(obj, null, type);
    }

    public final String toJson(Object obj, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(obj.getClass(), adapter, type).toJson(obj);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null, type);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter<T> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(json, classOfT);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException, IOException {
        return fromJson(reader, classOfT, null, type);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter<T> adapter, String type) throws NullPointerException, InvalidParameterException, IOException {
        return createGson(classOfT, adapter, type).fromJson(reader.gsonReader(), classOfT);
    }

    protected Gson createGson(Class<?> classOfT, TypeAdapter<?> typeAdapter, String type) throws NullPointerException, InvalidParameterException {
        if (getGsonBuilder() == null) {
            initGsonBuilder();
        }

        if (typeAdapter == null && getTypeAdapterFactory() != null) {
            typeAdapter = getTypeAdapterFactory()
                    .create(classOfT, type);
        }

        if (typeAdapter != null) {
            getGsonBuilder().registerTypeAdapter(classOfT, typeAdapter);
        } else {
            String warnMsg = String.format(
                    "Could not find adapter of %s.",
                    classOfT.getSimpleName());
            if (type != null && !type.isEmpty()) {
                warnMsg = String.format(
                        "Could not find adapter of %s for api type %s.",
                        classOfT.getSimpleName(), type);
            }
            Log.warn(this, warnMsg);
        }

        return getGsonBuilder().create();
    }
}
