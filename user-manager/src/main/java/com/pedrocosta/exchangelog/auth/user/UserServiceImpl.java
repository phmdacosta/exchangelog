package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.auth.api.NotificationRestService;
import com.pedrocosta.exchangelog.auth.registration.NotificationRequest;
import com.pedrocosta.exchangelog.auth.registration.token.ConfirmationToken;
import com.pedrocosta.exchangelog.auth.registration.token.ConfirmationTokenService;
import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.role.RoleRepository;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.utils.ContactType;
import com.pedrocosta.exchangelog.auth.utils.TokenProperties;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import com.pedrocosta.utils.mailsender.FileParser;
import com.pedrocosta.utils.mailsender.HtmlFileParser;
import javassist.NotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final NotificationRestService notifRestService;
    private final Environment env;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService,
                           NotificationRestService notifRestService, Environment env) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.notifRestService = notifRestService;
        this.env = env;
    }

    @Override
    public User save(User user) throws SaveDataException {
        User saved = userRepository.save(user);
        if (saved.getId() == 0) {
            throw new SaveDataException(Messages.get("error.not.saved",
                    "user"));
        }
        return saved;
    }

    @Override
    public User find(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User find(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                        String.format("user %s", username))));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(User user, Role role) throws SaveDataException {
        if (user != null) {
            Log.info(this, String.format("Adding role %s to user %s", role.getName(), user.getUsername()));
            user.getRoles().add(role);
            this.save(user);
        }
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws SaveDataException {
        try {
            User user = this.find(username);
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                            String.format("role %s", roleName))));
            this.addRoleToUser(user, role);
        } catch (NotFoundException e) {
            throw new SaveDataException(e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(Messages.get("not.found",
                        String.format("user %s", username))));
    }

    @Override
    public String register(User user) throws SaveDataException, IllegalArgumentException {
        UserContact emailContact = user.getContacts().stream()
                .filter(c -> ContactType.EMAIL.matches(c.getName()))
                .findFirst().orElse(null);

        if (emailContact == null) {
            throw new IllegalArgumentException(Messages.get("email.empty"));
        }

        boolean userExists = userRepository.findByContactsValue(emailContact.getValue()).isPresent();
        if (userExists) {
            throw new IllegalArgumentException(Messages.get("email.exists", emailContact.getValue()));
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        this.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setToken(token);

        LocalDateTime creationTime = LocalDateTime.now();
        confirmationToken.setCreatedTime(creationTime);

        String propExpirationTime = env.getProperty(TokenProperties.EXPIRATION_TIME);
        if (propExpirationTime != null) {
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(Long.parseLong(propExpirationTime));
            confirmationToken.setExpiredTime(expirationTime);
        } else {
            confirmationToken.setExpiredTime(LocalDateTime.now());
        }

        confirmationTokenService.save(confirmationToken);

        // Send confirmation e-mail
        String body = buildEmail(user.getFirstName(), "http://localhost:8087/api/registration/confirm?token=" + token);
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setMean("EMAIL");
        notificationRequest.setFrom("teste@mail.com");
        notificationRequest.addTo(emailContact.getValue());
        notificationRequest.setSubject("Confirm you e-mail");
        notificationRequest.setBody(body);
        Log.info(this, "Sending message to notification server: " + notificationRequest);

        return notifRestService.post("pushNotification", notificationRequest, String.class);
    }

    @Override
    @Transactional
    public void confirmToken(String token) throws SaveDataException, IllegalArgumentException {
        ConfirmationToken confirmationToken;
        try {
            confirmationToken = confirmationTokenService.find(token);
        } catch (NotFoundException e) {
            throw new IllegalStateException(Messages.get("not.found", "token"));
        }

        if (confirmationToken.getConfirmationTime() != null) {
            throw new IllegalStateException(Messages.get("email.confirmed"));
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredTime();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(Messages.get("token.expired"));
        }

        confirmationTokenService.confirm(token);

        UserContact emailContact = confirmationToken.getUser().getContacts().stream()
                .filter(c -> ContactType.EMAIL.matches(c.getName()))
                .findFirst().orElse(null);

        if (emailContact == null) {
            throw new IllegalArgumentException(Messages.get("email.empty"));
        }

        User user = userRepository.findByContactsValue(emailContact.getValue())
                .orElseThrow(() -> new IllegalArgumentException(""));
        user.setEnabled(true);
        this.save(user);
    }

    private String buildEmail(String name, String link) {
        return new HtmlFileParser(FileParser.class.getClassLoader().getResourceAsStream("page/emailBody.html"))
                .addParameter("personName", name)
                .addParameter("confirmationLink", link)
                .addParameter("expireMinutes", env.getProperty(TokenProperties.EXPIRATION_TIME))
                .read();
    }
}
