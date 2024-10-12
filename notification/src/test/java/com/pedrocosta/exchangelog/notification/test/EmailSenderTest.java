package com.pedrocosta.exchangelog.notification.test;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations="classpath:application-test.properties",
        properties = {"send.notification=true"}
)
public class EmailSenderTest {
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("tester", "test"))
            .withPerMethodLifecycle(false);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void test_emailSending_success() throws Exception {
        String requestBody = "{" +
                "\"mean\":\"EMAIL\"," +
                "\"from\":\"from@test.com\"," +
                "\"to\":[\"to_01@test.com\",\"to_02@test.com\",\"to_03@test.com\"]," +
                "\"subject\":\"Test subject\"," +
                "\"body\":\"This is a test notification\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = this.testRestTemplate.postForEntity("/api/notification/push", request, String.class);

        assertEquals(200, response.getStatusCodeValue());

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        assertEquals("This is a test notification", GreenMailUtil.getBody(receivedMessage));
        assertEquals("Test subject", receivedMessage.getSubject());
        assertEquals("from@test.com", receivedMessage.getFrom()[0].toString());
        assertEquals(3, receivedMessage.getAllRecipients().length);
        boolean assertionRecipients = true;
        for (Address address : receivedMessage.getAllRecipients()) {
            if (!"to_01@test.com".equals(address.toString())
                    && !"to_02@test.com".equals(address.toString())
                    && !"to_03@test.com".equals(address.toString())) {
                assertionRecipients = false;
                break;
            }
        }
        assert assertionRecipients;
    }
}
