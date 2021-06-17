package com.pedrocosta.exchangelog.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.models.NotificationRequest;
import com.pedrocosta.exchangelog.models.User;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.notifications.NotificationMeans;

import java.io.IOException;

public abstract class NotificationRequestAdapter<T extends NotificationRequest> extends JsonAdapter<T> {
    protected final String ID = "id";
    protected final String NAME = "name";
    protected final String MEANS = "means";
    protected final String ENABLED = "enabled";
    protected final String USER = "user";

    public NotificationRequestAdapter(GsonUtils gsonUtils) {
        super(gsonUtils);
    }

    @Override
    public void write(JsonWriter writer, T notificationRequest) throws IOException {
        writer.beginObject();
        writeParent(writer, notificationRequest);
        writeJson(writer, notificationRequest);
//        writeNested(writer, notificationRequest);
        writer.endObject();
    }

    protected void readParent(JsonReader reader, String fieldName, T notifReq) throws IOException {
        JsonToken token = reader.peek();

        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return;
        }

        if (JsonToken.NAME.equals(token)) {
            fieldName = reader.nextName();
        }

        switch (fieldName) {
            case ID:
                token = reader.peek();
                notifReq.setId(reader.nextLong());
                break;
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
//            case USER:
//                token = reader.peek();
//                User user = getGsonUtils().fromJson(reader, User.class);
//                notifReq.setUser(user);
//                break;
            default:
                reader.skipValue();
        }
    }

    private void writeParent(JsonWriter writer, T notificationRequest) throws IOException {
        writer.name(NAME);
        writer.value(notificationRequest.getName());
        writer.name(MEANS);
        writer.value(notificationRequest.getMeans().name().toLowerCase());
        writer.name(ENABLED);
        writer.value(notificationRequest.isEnabled());
    }

    private void writeNested(JsonWriter writer, T notificationRequest) throws IOException {
        writer.name(USER);
        writer.jsonValue(getGsonUtils().toJson(notificationRequest.getUser()));
    }
}