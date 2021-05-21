package com.pedrocosta.exchangelog.request;

import java.util.HashMap;
import java.util.Map;

import com.sun.istack.NotNull;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.codehaus.jettison.json.JSONObject;

/**
 * Request generic class, it has all configuration names to load the API config.
 *
 * To configurate API, use the following names at application.properties file:
 * - request.url
 * - request.apikey
 * - request.apikey.name
 * - request.apikey.param
 *
 * It implements {@link EnvironmentAware} to load properties during its instantiation.
 */
@Component
public class RestRequest implements EnvironmentAware, RestResponse {
	
	private final String REQUEST_URL_REF = "request.url";
	private final String REQUEST_APIKEY_REF = "request.apikey";
	private final String REQUEST_APIKEY_NAME_REF = "request.apikey.name";
	private final String REQUEST_APIKEY_PARAM_REF = "request.apikey.param";

	@Autowired
	private RestTemplate restTemplate;

	private String type;

	private Environment env;

	private JSONObject jsonObject;
	private Map<String, String> mapProp;

	public RestRequest() {
		load();
	}
	
	public RestRequest(String type) {
		this.type = type;
		load();
	}

	/**
	 * Load environment properties
	 */
	public void load() {
		if (env != null) {
			mapProp = new HashMap<>();
			mapProp.put(REQUEST_URL_REF, env.getProperty(getPropName(REQUEST_URL_REF, type, 1), ""));
			mapProp.put(REQUEST_APIKEY_REF, env.getProperty(getPropName(REQUEST_APIKEY_REF, type, 1), ""));
			mapProp.put(REQUEST_APIKEY_NAME_REF, env.getProperty(getPropName(REQUEST_APIKEY_NAME_REF, type, 1), ""));
			mapProp.put(REQUEST_APIKEY_PARAM_REF, env.getProperty(getPropName(REQUEST_APIKEY_PARAM_REF, type, 1), ""));
		}
	}

	@Override
	public void setEnvironment(final Environment env) {
		this.env = env;
	}
	
	@Deprecated
	public void generate() {
		mapProp.put(REQUEST_URL_REF, "http://data.fixer.io/api/");
		mapProp.put(REQUEST_APIKEY_REF, "7a902ff2b94ec3916524bd4aa57e654d");
		mapProp.put(REQUEST_APIKEY_NAME_REF, "access_key");
		mapProp.put(REQUEST_APIKEY_PARAM_REF, "access_key=7a902ff2b94ec3916524bd4aa57e654d"); //access_key=7a902ff2b94ec3916524bd4aa57e654d
	}

	@Override
	public boolean isSuccess() throws JSONException {
		return false;
	}

	@Override
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public String getType() {
		return null;
	}

	/**
	 * Execute request without parameters.
	 *
	 * @param function Name of API's function to call
	 *
	 * @return {@link RestResponse} object.
	 * @throws JSONException
	 */
	public RestResponse get(String function) throws JSONException {
		return get(function, null);
	}

	/**
	 * Execute request with parameters.
	 *
	 * @param function	Name of API's function to call
	 * @param params	Parameters of request
	 *
	 * @return {@link RestResponse} object.
	 * @throws JSONException
	 */
	protected RestResponse get(@NotNull final String function, final String params) throws JSONException {
		String s = restTemplate.getForObject(getFullUrl(function, params), String.class);
		System.out.println(s);
		this.jsonObject = new JSONObject(s);
		return this;
	}

	/**
	 * Search for target property name.
	 * It looks for type at string.
	 *
	 * @param key		Default property name
	 * @param type		Type of API
	 * @param position	Position of {@code type} value at property name
	 *
	 * @return Property value.
	 */
	public String getPropName(String key, String type, int position) {
		if (position < 0) {
			throw new IllegalArgumentException("Position can not be negative.");
		}

		String typePropName = type.toLowerCase();
		String[] arrayKey = key.split("\\.");
		
		if (position >= arrayKey.length) {
			return key + "." + typePropName;
		}
		
		String result = "";
		
		for (int i = 0; i < arrayKey.length; i++) {
			if (i == position) {
				result = position == 0 
						? typePropName : result + "." + typePropName;
			}
			
			result = i == 0 && position > 0 
					? arrayKey[i] 
							: result + "." + arrayKey[i];
		}
		
		return result;
	}

	/**
	 * Build full URL of request without parameters.
	 *
	 * @param function	Name of API's function to call
	 *
	 * @return Request URL.
	 */
	public String getFullUrl(String function) {
		return getFullUrl(function, null);
	}

	/**
	 * Build full URL of request with parameters.
	 *
	 * @param function	Name of API's function to call
	 * @param params	Parameters of request
	 *
	 * @return Request URL.
	 */
	public String getFullUrl(String function, String params) {
		if (params == null) {
			params = "";
		}

		if (mapProp == null || mapProp.isEmpty()) {
			load();
		}

		String baseUrl = mapProp.get(REQUEST_URL_REF);
		// If base URL not defined, throws a exception.
		if (baseUrl.isEmpty()) {
			throw new NullPointerException("Request properties are not defined.");
		}
		
		// Be sure base URL ends with '/'
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		
		String apiKeyParam = mapProp.get(REQUEST_APIKEY_PARAM_REF);
		
		if (apiKeyParam.isEmpty()) {
			apiKeyParam = mapProp.get(REQUEST_APIKEY_NAME_REF) + "=" + mapProp.get(REQUEST_APIKEY_REF);
		}
		
		if (!apiKeyParam.isEmpty()) {
			params = apiKeyParam + "&" + params;
		}
		
		return baseUrl + function + "?" + params;
	}
}
