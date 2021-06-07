package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.models.NotificationRequest;
import com.pedrocosta.exchangelog.utils.notifications.NotificationMeans;

import java.io.IOException;

public class NotificationRequestAdapter extends TypeAdapter<NotificationRequest> {
    protected final String NAME = "name";
    protected final String MEANS = "means";
    protected final String ENABLED = "enabled";
    protected final String USER = "user";

    @Override
    public void write(JsonWriter writer, NotificationRequest notificationRequest) throws IOException {
        writer.beginObject();
        writeObject(writer, notificationRequest);
        writer.endObject();
    }

    protected void writeObject(JsonWriter writer, NotificationRequest notificationRequest) throws IOException {
        writer.name(NAME);
        writer.value(notificationRequest.getName());
        writer.name(MEANS);
        writer.value(notificationRequest.getMeans().name().toLowerCase());
        writer.name(ENABLED);
        writer.value(notificationRequest.isEnabled());
    }

    @Override
    public NotificationRequest read(JsonReader reader) throws IOException {
        reader.beginObject();
        NotificationRequest notifReq = readObject(reader);
        reader.endObject();
        return notifReq;
    }

    protected NotificationRequest readObject(JsonReader reader) throws IOException {
        NotificationRequest notifReq = new NotificationRequest();
        String fieldName = "";
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (JsonToken.NAME.equals(token)) {
                fieldName = reader.nextName();
            }

            switch (fieldName) {
                case NAME:
                    token = reader.peek();
                    notifReq.setName(reader.nextString());
                    break;
                case MEANS:
                    token = reader.peek();
                    notifReq.setMeans(NotificationMeans
                            .valueOf(reader.nextString().toUpperCase()));
                    break;
                case ENABLED:
                    token = reader.peek();
                    notifReq.setEnabled(reader.nextBoolean());
                    break;
                case USER:
                    token = reader.peek();
                    // TODO implement user adapter
                    break;
                default: // do nothing
            }
        }
        return notifReq;
    }
}
