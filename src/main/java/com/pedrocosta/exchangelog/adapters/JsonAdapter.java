package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Log;

import java.io.IOException;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public abstract class JsonAdapter<T> extends TypeAdapter<T> {
    private final GsonUtils gsonUtils;

    public JsonAdapter(GsonUtils gsonUtils) {
        this.gsonUtils = gsonUtils;
    }

    public GsonUtils getGsonUtils() {
        return gsonUtils;
    }

    @Override
    public void write(JsonWriter writer, T t) throws IOException {
        writer.beginObject();
        writeJson(writer, t);
        writer.endObject();
    }

    @Override
    public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        reader.beginObject();
        T obj = null;
        try {
            obj = readJson(reader);
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        reader.endObject();
        return obj;
    }

    protected abstract void writeJson(JsonWriter writer, T obj) throws IOException;

    protected abstract T readJson(JsonReader reader) throws IOException;
}
