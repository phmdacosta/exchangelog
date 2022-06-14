package com.pedrocosta.exchangelog;

import com.pedrocosta.springutils.exception.NotSupportedException;
import com.pedrocosta.springutils.jsonmanager.adapter.WriteTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.stream.JsonWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestResponseAdapter extends WriteTypeAdapter<RestResponse<?>> {

    @Override
    public void write(JsonWriter writer, RestResponse<?> obj) throws IOException, NotSupportedException {
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
