package com.pedrocosta.exchangelog.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.exceptions.NotSupportedException;
import com.pedrocosta.exchangelog.models.Person;
import com.pedrocosta.exchangelog.models.User;
import com.pedrocosta.exchangelog.models.UserContact;
import com.pedrocosta.exchangelog.utils.ContactTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends JsonAdapter<User>{
    private final String ID = "id";
    private final String USER_NAME = "username";
    private final String PASSWORD = "password";
    private final String CONTACTS = "contacts";
    private final String NAME = "name";

    @Override
    protected void writeJson(JsonWriter writer, User user) throws IOException, NotSupportedException {
        writer.name(USER_NAME);
        writer.value(user.getName());
        writer.name(CONTACTS);
        writer.jsonValue(getGsonUtils().toJson(user.getContacts()));
//        writer.beginArray();
//        for (UserContact contact : user.getContacts()) {
//            writer.beginObject();
//            writer.name(contact.getName());
//            writer.value(contact.getValue());
//            writer.endObject();
//        }
//        writer.endArray();
        writer.name(NAME);
        writer.value(user.getPerson().getName());
    }

    @Override
    protected User readJson(JsonReader reader) throws IOException, NotSupportedException {
        User user = new User();
        String fieldName = "";
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (JsonToken.NAME.equals(token)) {
                fieldName = reader.nextName();
            }

            switch (fieldName) {
                case ID:
                    token = reader.peek();
                    user.setId(reader.nextLong());
                    break;
                case USER_NAME:
                    token = reader.peek();
                    user.setName(reader.nextString());
                    break;
                case PASSWORD:
                    token = reader.peek();
                    user.setPassword(reader.nextString());
                    break;
                case CONTACTS:
                    token = reader.peek();
                    user.setContacts(readContacts(reader, token));
                    break;
                case NAME:
                    user.setPerson(new Person());
                    user.getPerson().setName(reader.nextString());
                    break;
                default: reader.skipValue();
            }
        }
        return user;
    }

    private List<UserContact> readContacts(JsonReader reader, JsonToken token) throws IOException {
        List<UserContact> contacts = new ArrayList<>();
        reader.beginArray();
        while (!JsonToken.END_ARRAY.equals(token)) {
            token = reader.peek();
            if (JsonToken.BEGIN_OBJECT.equals(token)) {
                reader.beginObject();
            }
            if (JsonToken.END_OBJECT.equals(token)) {
                reader.endObject();
            }
            if (JsonToken.NAME.equals(token)) {
                UserContact contact = new UserContact();
                contact.setType(ContactTypes.get(reader.nextName()));
                token = reader.peek();
                contact.setValue(reader.nextString());
                contacts.add(contact);
            }
        }
        reader.endArray();
        return contacts;
    }
}
