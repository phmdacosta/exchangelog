package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.exceptions.NotSupportedException;
import com.pedrocosta.exchangelog.utils.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

public class CollectionAdapter extends JsonAdapter<Collection<?>> {

    private Collection<?> type;

    public CollectionAdapter setType(Class<Collection<?>> typeClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        type = typeClass.getConstructor(new Class[0]).newInstance();
        return this;
    }

    @Override
    protected void beginWriter(JsonWriter writer) throws IOException {
        writer.beginArray();
    }

    @Override
    protected void endWriter(JsonWriter writer) throws IOException {
        writer.endArray();
    }

    @Override
    protected void beginReader(JsonReader reader) throws IOException {
        reader.beginArray();
    }

    @Override
    protected void endReader(JsonReader reader) throws IOException {
        reader.endArray();
    }

    @Override
    protected void writeJson(JsonWriter writer, Collection<?> col) throws IOException, NotSupportedException {
        col.forEach(x -> {
            try {
                writer.value(getGsonUtils().toJson(x));
            } catch (IOException e) {
                throw new NotSupportedException(e);
            }
        });
    }

    @Override
    protected Collection<?> readJson(JsonReader reader) throws IOException, NotSupportedException {
        if (type == null) {
            throw new NotSupportedException(); // TODO message
        }
        Collection<?> collection = type;
        JsonToken token = null;
        while (!JsonToken.END_ARRAY.equals(token)) {
            token = reader.peek();
//            collection.add(getGsonUtils().fromJson(reader, ));
        }
        return null;
    }
}
