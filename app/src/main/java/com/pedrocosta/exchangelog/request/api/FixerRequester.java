package com.pedrocosta.exchangelog.request.api;

import com.pedrocosta.exchangelog.request.RestRequest;
import com.pedrocosta.exchangelog.request.RestResponse;
import com.pedrocosta.exchangelog.services.api.utils.ApiTypes;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Component;

@Component
public class FixerRequester extends RestRequest {

	public static final String LATEST = "latest";
	public static final String SYMBOLS = "symbols";

	public FixerRequester() {
		super(ApiTypes.FIXER);
	}

	@Override
	public boolean isSuccess() throws JSONException {
		return getJsonObject() != null && getJsonObject().getBoolean("success");
	}

	@Override
	public String getType() {
		return ApiTypes.FIXER;
	}

	public RestResponse latest() throws JSONException {
		return latest(null);
	}
	public RestResponse latest(String params) throws JSONException {
		return get(LATEST, params);
	}

	public RestResponse symbols() throws JSONException {
		return get(SYMBOLS);
	}

	/**
	 * Not supported by current subscription
	 * @param fromCcy
	 * @param toCcy
	 * @param amount
	 * @return {@link FixerRequester} object.
	 */
	@Deprecated
	public FixerRequester convert(String fromCcy, String toCcy, Double amount) throws JSONException {
		String params = "from=" + fromCcy +
				"&to=" + toCcy +
				"&amount=" + amount;
		return (FixerRequester) get("convert", params);
	}


}
