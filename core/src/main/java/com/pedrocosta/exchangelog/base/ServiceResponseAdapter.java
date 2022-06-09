package com.pedrocosta.exchangelog.base;

import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.jsonmanager.adapter.JsonWriteAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.WriteTypeAdapter;
import com.pedrocosta.utils.jsonmanager.stream.JsonWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceResponseAdapter extends WriteTypeAdapter<ServiceResponse<?>> {

    @Override
    public void write(JsonWriter writer, ServiceResponse<?> obj) throws IOException, NotSupportedException {
        writer.add("timestamp", obj.getTimestamp().toString());
        writer.add("success", obj.isSuccess());
        writer.add("code", obj.getCode().value());
        if (!obj.isSuccess()) {
            String msg = obj.getMessage();
            if (msg.isBlank() && !obj.getException().getMessage().isBlank()) {
                msg = obj.getException().getMessage();
            }
            writer.add("message", msg);
        }
        writer.addJson("info", this.getJsonUtils().toJson(obj.getObject()));
    }
}
