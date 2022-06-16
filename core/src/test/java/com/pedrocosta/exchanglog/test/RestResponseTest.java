package com.pedrocosta.exchanglog.test;

import com.pedrocosta.exchangelog.api.RestResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestResponseTest {
    @Test
    public void test_successCreation() {
        RestResponse<?> response = RestResponse.createSuccess();
        assert response.isSuccess();
        assertEquals(HttpStatus.OK, response.getCode());
    }

    @Test
    public void test_successCreation_withCustomCode() {
        RestResponse<?> response = RestResponse.createSuccess(HttpStatus.CREATED);
        assert response.isSuccess();
        assertEquals(HttpStatus.CREATED, response.getCode());
    }

    @Test
    public void test_errorCreation() {
        RestResponse<?> response = RestResponse.createError();
        assert !response.isSuccess();
        assertEquals(HttpStatus.BAD_REQUEST, response.getCode());
    }

    @Test
    public void test_errorCreation_withCustomCode() {
        RestResponse<?> response = RestResponse.createError(HttpStatus.NOT_FOUND);
        assert !response.isSuccess();
        assertEquals(HttpStatus.NOT_FOUND, response.getCode());
    }

    @Test
    public void test_errorCreation_withCustomMessage() {
        RestResponse<?> response = RestResponse.createError(HttpStatus.BAD_REQUEST,
                "Error message for test");
        assert !response.isSuccess();
        assertEquals("Error message for test", response.getMessage());
    }
}
