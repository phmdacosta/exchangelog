package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.auth.user.registration.ConfirmationMail;
import com.pedrocosta.exchangelog.auth.user.registration.token.ConfirmationToken;
import com.pedrocosta.exchangelog.auth.user.registration.token.ConfirmationTokenService;
import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.role.RoleRepository;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.utils.ContactType;
import com.pedrocosta.exchangelog.auth.utils.TokenProperties;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.AppProperties;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import com.pedrocosta.utils.mailsender.FileParser;
import com.pedrocosta.utils.mailsender.HtmlFileParser;
import com.pedrocosta.utils.mailsender.MimeEmailSender;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
//    private final MimeEmailSender mimeEmailSender;
    private final RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService,
//                           MimeEmailSender mimeEmailSender, RestTemplate restTemplate) {
                           RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
//        this.mimeEmailSender = mimeEmailSender;
        this.restTemplate = restTemplate;
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

        String propExpirationTime = AppProperties.get(TokenProperties.EXPIRATION_TIME);
        if (propExpirationTime != null) {
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(Long.parseLong(propExpirationTime));
            confirmationToken.setExpiredTime(expirationTime);
        } else {
            confirmationToken.setExpiredTime(LocalDateTime.now());
        }

        confirmationTokenService.save(confirmationToken);

        //TODO: send token to e-mail
        String body = buildEmail(user.getPerson().getName(), "http://localhost:8087/api/registration/confirm?token=" + token);
//        mimeEmailSender.subject("Confirm you e-mail")
//                .to(emailContact.getValue()).body(body)
//                .send();
        try {
            ConfirmationMail confirmMail = new ConfirmationMail();
            confirmMail.setMean("EMAIL");
            confirmMail.setFrom("teste@mail.com");
            confirmMail.addTo(emailContact.getValue());
            confirmMail.setSubject("Confirm you e-mail");
            confirmMail.setBody(body);
            URI uri = new URI("http://localhost:8089/pushNotification");
            ResponseEntity<String> response = restTemplate.postForEntity(uri, confirmMail, String.class);
            System.out.println(response);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return token;
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
                .read();

//        InputStream stream = this.getClass().getResourceAsStream("classpath*:emailBody.html");
//        System.out.println(stream != null);
//        stream = this.getClass().getClassLoader().getResourceAsStream("page/emailBody.html");
//        System.out.println(stream != null);
//
//        Scanner s = new Scanner(stream).useDelimiter("\\A");
//        String result = s.hasNext() ? s.next() : "";
//
//        return result
//                .replace("${personName}", name)
//                .replace("${confirmationLink}", link);

//        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
//                "\n" +
//                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
//                "\n" +
//                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
//                "        \n" +
//                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
//                "          <tbody><tr>\n" +
//                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
//                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
//                "                  <tbody><tr>\n" +
//                "                    <td style=\"padding-left:10px\">\n" +
//                "                  \n" +
//                "                    </td>\n" +
//                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
//                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
//                "                    </td>\n" +
//                "                  </tr>\n" +
//                "                </tbody></table>\n" +
//                "              </a>\n" +
//                "            </td>\n" +
//                "          </tr>\n" +
//                "        </tbody></table>\n" +
//                "        \n" +
//                "      </td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table>\n" +
//                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
//                "      <td>\n" +
//                "        \n" +
//                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
//                "                  <tbody><tr>\n" +
//                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
//                "                  </tr>\n" +
//                "                </tbody></table>\n" +
//                "        \n" +
//                "      </td>\n" +
//                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table>\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td height=\"30\"><br></td>\n" +
//                "    </tr>\n" +
//                "    <tr>\n" +
//                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
//                "        \n" +
//                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
//                "        \n" +
//                "      </td>\n" +
//                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//                "    </tr>\n" +
//                "    <tr>\n" +
//                "      <td height=\"30\"><br></td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
//                "\n" +
//                "</div></div>";
    }
}
