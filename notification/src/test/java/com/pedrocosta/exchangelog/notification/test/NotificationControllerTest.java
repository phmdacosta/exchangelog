package com.pedrocosta.exchangelog.notification.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class NotificationControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getNotificationMeans_success() throws Exception {
        String expectedResponse =
                "\"success\":true," +
                "\"code\":200," +
                "\"info\":[" +
                    "\"EMAIL\",\"APP\",\"SMS\"" +
                "]";

        this.mockMvc.perform(get("/notificationMeans")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedResponse)));
    }

    @Test
    public void test_pushNotification_success() throws Exception {
        String expectedResponse = "\"success\":true,\"code\":200";

        String requestBody = "{" +
                    "\"mean\":\"EMAIL\"," +
                    "\"from\":\"from@test.com\"," +
                    "\"to\":[\"to_01@test.com\",\"to_02@test.com\",\"to_03@test.com\"]," +
                    "\"subject\":\"Test subject\"," +
                    "\"body\":\"This is a test notification\"" +
                "}";

        this.mockMvc.perform(post("/pushNotification?mean=e-mail")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedResponse)));
    }

    @Test
    public void test_pushNotification_error_badRequest() throws Exception {
        String requestBody = "{" +
                    "\"mean\":\"EMAIL\"" +
                    "\"from\":\"from@test.com\"," +
                    "\"to\":[\"to_01@test.com\",\"to_02@test.com\",\"to_03@test.com\"]," +
                    "\"subject\":\"Test subject\"," +
                    "\"body\":\"This is a test notification\"" +
                "}";

        this.mockMvc.perform(post("/pushNotification")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string(
                        containsString("Request body JSON wrong format.")));
    }

    @Test
    public void test_pushNotification_error_badRequest_missingBody() throws Exception {
        this.mockMvc.perform(post("/pushNotification")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(""))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string(
                        containsString("Missing body.")));
    }

    @Test
    public void test_pushNotificationList_success() throws Exception {
        String expectedResponse = "\"success\":true,\"code\":200";

        String requestBody = "[" +
                    "{" +
                        "\"mean\":\"EMAIL\"," +
                        "\"from\":\"from@test.com\"," +
                        "\"to\":[\"to_01@test.com\",\"to_02@test.com\",\"to_03@test.com\"]," +
                        "\"subject\":\"Test subject\"," +
                        "\"body\":\"This is a test notification by E-mail\"" +
                    "}" +
                    ",{" +
                        "\"mean\":\"APP\"," +
                        "\"from\":\"from_server_123456\"," +
                        "\"to\":[\"to_app_1216874621\",\"to_app_85249635\"]," +
                        "\"subject\":\"Test subject\"," +
                        "\"body\":\"This is a test notification by App\"" +
                    "}" +
                    ",{" +
                        "\"mean\":\"EMAIL\"," +
                        "\"from\":\"from@test.com\"," +
                        "\"to\":[\"to_01@test.com\"]," +
                        "\"subject\":\"Test subject\"," +
                        "\"body\":\"This is a test notification by E-mail (2)\"" +
                    "}" +
                "]";

        this.mockMvc.perform(post("/pushNotifications")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedResponse)));
    }

    @Test
    public void test_pushNotificationList_error_badRequest_JSONWrongFormat() throws Exception {
        String requestBody = "" +
                "{" +
                "\"mean\":\"EMAIL\"," +
                "\"from\":\"from@test.com\"," +
                "\"to\":[\"to_01@test.com\",\"to_02@test.com\",\"to_03@test.com\"]," +
                "\"subject\":\"Test subject\"," +
                "\"body\":\"This is a test notification by E-mail\"" +
                "}" +
                ",{" +
                "\"mean\":\"APP\"," +
                "\"from\":\"from_server_123456\"," +
                "\"to\":[\"to_app_1216874621\",\"to_app_85249635\"]," +
                "\"subject\":\"Test subject\"," +
                "\"body\":\"This is a test notification by App\"" +
                "}" +
                ",{" +
                "\"mean\":\"EMAIL\"," +
                "\"from\":\"from@test.com\"," +
                "\"to\":[\"to_01@test.com\"]," +
                "\"subject\":\"Test subject\"," +
                "\"body\":\"This is a test notification by E-mail (2)\"" +
                "}" +
                "";

        this.mockMvc.perform(post("/pushNotifications")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string(
                        containsString("Request body JSON wrong format.")));
    }

    @Test
    public void test_pushNotificationList_error_badRequest_singleJSONObjectInsteadOfArray() throws Exception {
        String requestBody = "{" +
                    "\"mean\":\"EMAIL\"," +
                    "\"from\":\"from@test.com\"," +
                    "\"to\":[\"to_01@test.com\",\"to_02@test.com\",\"to_03@test.com\"]," +
                    "\"subject\":\"Test subject\"," +
                    "\"body\":\"This is a test notification by E-mail\"" +
                "}";

        this.mockMvc.perform(post("/pushNotifications")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string(
                        containsString("Request body JSON wrong format.")));
    }

    @Test
    public void test_pushNotificationList_error_badRequest_missingBody() throws Exception {
        this.mockMvc.perform(post("/pushNotifications")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(""))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().string(
                        containsString("Missing body.")));
    }
}
