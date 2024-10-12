import com.pedrocosta.exchangelog.auth.person.Person;
import com.pedrocosta.exchangelog.auth.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CloneTest {
    private Person person;

    @BeforeEach
    public void setUp() throws Exception {
        person = new Person()
                .setId(1)
                .setFirstName("Pedro")
                .setLastName("Costa")
                .setBirthday(LocalDateTime.of(1989,12,10,0,0));
    }

    @Test
    public void test_Person() throws Exception {
        Person clone = person.clone();
        Assertions.assertEquals(clone, person);
    }

    @Test
    public void test_User() throws Exception {
        User user = (User) new User()
                .setId(1)
                .setFirstName("Pedro")
                .setLastName("Costa")
                .setBirthday(LocalDateTime.of(1989,12,10,0,0));
        user.setUsername("phmcosta").setPassword("password");
        User clone = user.clone();
        Assertions.assertEquals(clone, user);
    }
}
