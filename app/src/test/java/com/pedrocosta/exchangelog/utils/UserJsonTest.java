package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.services.api.utils.ContactTypes;
import com.pedrocosta.exchangelog.user.User;
import com.pedrocosta.exchangelog.user.UserContact;
import com.pedrocosta.exchangelog.user.models.Person;
import com.pedrocosta.json.manager.adapter.AdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserJsonTest {

    private JsonUtils jsonUtils;
    private User user;

    @BeforeEach
    public void setUp() {
        jsonUtils = new JsonUtils(new AdapterFactory());

        user = new User()
                .setId(123)
                .setName("persontest")
                .setPassword("123456test!")
                .addContact(new UserContact()
                        .setId(1)
                        .setType(ContactTypes.EMAIL)
                        .setValue("test@test.com"))
                .addContact(new UserContact()
                        .setId(2)
                        .setType(ContactTypes.PHONE)
                        .setValue("11111111"))
                .setPerson(new Person()
                        .setId(55)
                        .setName("Person Test"));
    }

    @Test
    public void testCreateJson() {
        String generateJson = jsonUtils.toJson(user);
        Log.info(this, generateJson);
        assert jsonForUserSerialize().equals(generateJson);
    }

//    @Test
    public void testCreateObjectFromJson() {
        User deserialized = jsonUtils.fromJson(
                jsonForUserDeserialize(), User.class);
        assert isExact(user, deserialized);
    }

    private String jsonForUserSerialize() {
        String result =
            "{" +
                "\"username\":\"persontest\"" +
                ",\"contacts\":[" +
                    "{" +
                        "\"email\":\"test@test.com\"" +
                    "}" +
                    ",{" +
                        "\"phone\":\"11111111\"" +
                    "}" +
                "]" +
                ",\"name\":\"Person Test\"" +
            "}";

        return result;
    }

    private String jsonForUserDeserialize() {
        String result =
                "{" +
                    "\"id\":123" +
                    ",\"username\":\"persontest\"" +
                    ",\"password\":\"123456test!\"" +
                    ",\"contacts\":[" +
                        "{" +
                            "\"email\":\"test@test.com\"" +
                        "}" +
                        ",{" +
                            "\"phone\":\"11111111\"" +
                        "}" +
                    "]" +
                    ",\"name\":\"Person Test\"" +
                "}";

        return result;
    }

    private boolean isExact(User o1, User o2) {
        return o1.getId() == o2.getId()
                && o1.getName().equals(o2.getName())
                && o1.getPassword().equals(o2.getPassword())
                && o1.getPerson().equals(o2.getPerson())
                && o1.getContacts().equals(o2.getContacts());
    }
}
