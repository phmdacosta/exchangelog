package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceResponseAdapter extends TypeAdapter<ServiceResponse> {

    private GsonUtils gsonUtils;

    public ServiceResponseAdapter(GsonUtils gsonUtils) {
        this.gsonUtils = gsonUtils;
    }

    @Override
    public void write(JsonWriter writer, ServiceResponse obj) throws IOException {
        writer.beginObject();
        writer.name("success");
        writer.value(obj.isSuccess());
        writer.name("code");
        writer.value(obj.getCode().value());
        if (obj.isSuccess()) {
            writer.name("info");
            writer.jsonValue(gsonUtils.toJson(obj.getObject()));
        } else {
            writer.name("label");
            writer.value(obj.getMessage());
        }
        writer.endObject();
    }

    @Override
    public ServiceResponse read(JsonReader reader) throws IOException {
        throw new IllegalStateException("There is no support for deserializing " +
                "transformation result objects at the moment");
    }
}
