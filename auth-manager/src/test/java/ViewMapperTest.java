import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.user.contacts.dto.UserContactDto;
import com.pedrocosta.exchangelog.auth.user.dto.UserDto;
import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.role.dto.RoleDto;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ViewMapperTest {

    private User userModel;
    private ViewMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ViewMapper();

        userModel = new User();
        userModel.setId(1);
        userModel.setUsername("MyUserName");
        userModel.setPassword("MyP@ssword123");
//        Person person = new Person();
//        person.setFirstName("Person Name");
//        userModel.setPerson(person);
        userModel.setFirstName("Person First Name");
        userModel.setLastName("Person Last Name");
        //Contacts
        UserContact userContact1 = new UserContact();
        userContact1.setId(1);
        userContact1.setName("e-mail");
        userContact1.setValue("my-test-email@test.com");
        UserContact userContact2 = new UserContact();
        userContact2.setId(2);
        userContact2.setName("sms");
        userContact2.setValue("123456789");
        userModel.getContacts().add(userContact1);
        userModel.getContacts().add(userContact2);
        //Roles
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");
        userModel.getRoles().add(role1);
    }

    @Test
    public void test_UserDtoMappingFromUserModel() throws Exception {
        UserDto userDto = mapper.map(userModel, UserDto.class);
        assertEquals(userDto.getUsername(), userModel.getUsername());
        assertEquals(userDto.getFirstName(), userModel.getFirstName());
        assertEquals(userDto.getLastName(), userModel.getLastName());
        for (UserContactDto contactDto : userDto.getContacts()) {
            List<UserContact> found = userModel.getContacts().stream()
                    .filter(_contact -> _contact.getName().equals(contactDto.getName())
                                        && _contact.getValue().equals(contactDto.getValue()))
                    .collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }
        for (RoleDto roleDto : userDto.getRoles()) {
            List<Role> found = userModel.getRoles().stream()
                    .filter(_role -> _role.getName().equals(roleDto.getName()))
                    .collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }
    }

    @Test
    public void test_UserModelMappingFromUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername(userModel.getUsername());
//        userDto.setPerson(new PersonDto());
//        userDto.getPerson().setName(userModel.getFirstName());
        userDto.setFirstName(userModel.getFirstName());
        userDto.setLastName(userModel.getLastName());
        userDto.setContacts(new ArrayList<>());
        UserContactDto contactDto1 = new UserContactDto();
        contactDto1.setName(userModel.getContacts().get(0).getName());
        contactDto1.setValue(userModel.getContacts().get(0).getValue());
        UserContactDto contactDto2 = new UserContactDto();
        contactDto2.setName(userModel.getContacts().get(1).getName());
        contactDto2.setValue(userModel.getContacts().get(1).getValue());
        userDto.getContacts().add(contactDto1);
        userDto.getContacts().add(contactDto2);
        userDto.setRoles(new HashSet<>());
        RoleDto roleDto = new RoleDto();
        roleDto.setName(userModel.getRoles().iterator().next().getName());
        userDto.getRoles().add(roleDto);

        User user = mapper.map(userDto, User.class);
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        for (UserContact contact : user.getContacts()) {
            List<UserContactDto> found = userDto.getContacts().stream()
                    .filter(_contactDto -> _contactDto.getName().equals(contact.getName())
                            && _contactDto.getValue().equals(contact.getValue()))
                    .collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }
        for (Role role : user.getRoles()) {
            List<RoleDto> found = userDto.getRoles().stream()
                    .filter(_role -> _role.getName().equals(role.getName()))
                    .collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }
    }
}
