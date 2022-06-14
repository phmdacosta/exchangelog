package com.pedrocosta.exchangelog.exceptions;

import com.pedrocosta.exchangelog.RestResponse;
import com.pedrocosta.exchangelog.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Base controller advice for common exceptions handles.
 *
 * @author Pedro H. M. da Costa
 * @since 1.0
 */
@RestControllerAdvice
@ResponseBody
public class BaseControllerAdvice {

    private final JsonUtils jsonUtils;

    public BaseControllerAdvice(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    /**
     * Handle any exception that is not handled by specific implementation.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        RestResponse<?> response =  RestResponse.createError(status, exception.getMessage());
        return new ResponseEntity<>(jsonUtils.toJson(response), status);
    }

    /**
     * @return JSON
     * {
     *     "timestamp":"<i>Error generation date (YYYY-MM-DDTHH:MI:SS.XXXXXXXXX)</i>",
     *     "success":false,
     *     "code":400,
     *     "message":"Missing body."
     * }
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException() {
        RestResponse<?> response =  RestResponse.createError(
                HttpStatus.BAD_REQUEST, "Required request body is missing.");
        return new ResponseEntity<>(jsonUtils.toJson(response), HttpStatus.BAD_REQUEST);
    }

    /**
     * @return JSON
     * {
     *     "timestamp":"<i>Error generation date (YYYY-MM-DDTHH:MI:SS.XXXXXXXXX)</i>",
     *     "success":false,
     *     "code":400,
     *     "message":"Wrong parameters."
     * }
     */
    @ExceptionHandler(WrongParametersException.class)
    public ResponseEntity<String> handleWrongParametersException(HttpServletResponse servletResponse) {
        RestResponse<?> response =  RestResponse.createError(
                HttpStatus.BAD_REQUEST, "Wrong parameters.");
        servletResponse.setStatus(response.getCode().value());
        return new ResponseEntity<>(jsonUtils.toJson(response), HttpStatus.BAD_REQUEST);
    }

    /**
     * @return JSON
     * {
     *     "timestamp":"<i>Error generation date (YYYY-MM-DDTHH:MI:SS.XXXXXXXXX)</i>",
     *     "success":false,
     *     "code":<i>HTTP status code of exception</i>,
     *     "message":"<i>Message of exception</i>"
     * }
     */
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<String> handleRestApiException(RestApiException exception) {
        RestResponse<?> response =  RestResponse.createError(
                exception.getResponseMessage().getCode(), exception.getMessage());
        return new ResponseEntity<>(jsonUtils.toJson(response), response.getCode());
    }
}
