package com.pedrocosta.exchangelog.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.exceptions.NotSupportedException;
import com.pedrocosta.exchangelog.models.Person;
import com.pedrocosta.exchangelog.models.UserContact;
import com.pedrocosta.exchangelog.utils.ContactTypes;

import java.io.IOException;

public class UserContactAdapter extends JsonAdapter<UserContact> {
    @Override
    protected void writeJson(JsonWriter writer, UserContact contact) throws IOException, NotSupportedException {
        writer.name(contact.getName());
        writer.value(contact.getValue());
    }

    @Override
    protected UserContact readJson(JsonReader reader) throws IOException, NotSupportedException {
        UserContact contact = new UserContact();
        while (reader.hasNext()) {
            if (JsonToken.NAME.equals(reader.peek())) {
                contact.setType(ContactTypes.valueOf(reader.nextName()));
                reader.peek();
                contact.setValue(reader.nextString());
            }
        }
        return contact;
    }
}
