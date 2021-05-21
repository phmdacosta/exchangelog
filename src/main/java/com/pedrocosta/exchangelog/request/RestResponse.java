package com.pedrocosta.exchangelog.request;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public interface RestResponse {

    /**
     * Verify if request was successful.
     *
     * @return true if it was successful, false otherwise.
     * @throws {@link JSONException}
     */
    boolean isSuccess() throws JSONException;

    /**
     * Get response's json object generated by request.
     *
     * @return {@link JSONObject} of response.
     */
    JSONObject getJsonObject();
}
