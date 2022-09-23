package com.pedrocosta.exchangelog.api;

import com.pedrocosta.exchangelog.exceptions.ExternalServiceException;
import com.pedrocosta.springutils.output.Log;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
@Service
public class RestServiceRequest<RESP> implements EnvironmentAware, GetServiceRequest<RESP>, PostServiceRequest<RESP> {
	
	protected final String REQUEST_URL_REF = "request.url";
	protected final String REQUEST_APIKEY_REF = "request.apikey";
	protected final String REQUEST_APIKEY_NAME_REF = "request.apikey.name";
	protected final String REQUEST_APIKEY_PARAM_REF = "request.apikey.param";

	private final RestTemplate restTemplate;
	private final String type;
	private Environment env;
	private Map<String, String> mapProp;

	public RestServiceRequest(RestTemplate restTemplate) {
		this(restTemplate, null);
	}
	
	public RestServiceRequest(RestTemplate restTemplate, String type) {
		this.restTemplate = restTemplate;
		this.type = type;
		load();
	}

	/**
	 * Load environment properties
	 */
	public void load() {
		if (env != null) {
			getMapProp().put(REQUEST_URL_REF, env.getProperty(getPropName(REQUEST_URL_REF, type, 1), ""));
			getMapProp().put(REQUEST_APIKEY_REF, env.getProperty(getPropName(REQUEST_APIKEY_REF, type, 1), ""));
			getMapProp().put(REQUEST_APIKEY_NAME_REF, env.getProperty(getPropName(REQUEST_APIKEY_NAME_REF, type, 1), ""));
			getMapProp().put(REQUEST_APIKEY_PARAM_REF, env.getProperty(getPropName(REQUEST_APIKEY_PARAM_REF, type, 1), ""));
		}
	}

	@Override
	public void setEnvironment(final @org.jetbrains.annotations.NotNull Environment env) {
		this.env = env;
	}

	public Map<String, String> getMapProp() {
		if (mapProp == null) {
			mapProp = new HashMap<>();
		}
		return mapProp;
	}

	@Deprecated
	public void generate() {
		getMapProp().put(REQUEST_URL_REF, "http://data.fixer.io/api/");
		getMapProp().put(REQUEST_APIKEY_REF, "7a902ff2b94ec3916524bd4aa57e654d");
		getMapProp().put(REQUEST_APIKEY_NAME_REF, "access_key");
		getMapProp().put(REQUEST_APIKEY_PARAM_REF, "access_key=7a902ff2b94ec3916524bd4aa57e654d"); //access_key=7a902ff2b94ec3916524bd4aa57e654d
	}

	public boolean isSuccess() {
		return false;
	}

	public String getType() {
		return null;
	}

	/**
	 * Execute request without parameters.
	 *
	 * @param function Name of API's function to call
	 *
	 * @return {@link RESP} object.
	 */
	@Override
	public RESP get(String function, final Class<RESP> respClass) throws ExternalServiceException {
		return get(function, null, respClass);
	}

	/**
	 * Execute request with parameters.
	 *
	 * @param function	Name of API's function to call
	 * @param params	Parameters of request
	 * @param respClass	Class of response object
	 *
	 * @return {@link RESP} object.
	 */
	@Override
	@Async
	public RESP get(@NotNull final String function, final String params, final Class<RESP> respClass) throws ExternalServiceException {
		String url = getFullUrl(function, params);
		Log.info(this, "Calling GET " + url);
		try {
			ResponseEntity<RESP> respEntity = restTemplate.getForEntity(url, respClass);
			Log.info(this, "Response: " + respEntity);
			return respEntity.getBody();
		} catch (Exception e) {
			throw new ExternalServiceException(e);
		}
	}

	/**
	 * Execute POST request with parameters.
	 *
	 * @param function	Name of API's function to call
	 * @param body		Body of request
	 *
	 * @return {@link RESP} object.
	 */
	@Override
	public RESP post(String function, Object body, Class<RESP> respClass) throws ExternalServiceException {
		return post(function, null, body, respClass);
	}

	/**
	 * Execute request with parameters.
	 *
	 * @param function	Name of API's function to call
	 * @param params	Parameters of request
	 * @param body		Body of request
	 * @param respClass	Class of response object
	 *
	 * @return {@link RESP} object.
	 */
	@Override
	@Async
	public RESP post(String function, String params, Object body, Class<RESP> respClass) throws ExternalServiceException {
		String url = getFullUrl(function, params);
		Log.info(this, "Calling POST " + url);
		try {
			ResponseEntity<RESP> respEntity = restTemplate.postForEntity(url, body, respClass);
			Log.info(this, "Response: " + respEntity);
			return respEntity.getBody();
		} catch (Exception e) {
			throw new ExternalServiceException(e);
		}
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
		if (params == null)
			params = "";

		if (mapProp == null || mapProp.isEmpty())
			load();

		String baseUrl = mapProp.get(REQUEST_URL_REF);
		// If base URL not defined, throws a exception.
		if (baseUrl.isEmpty())
			throw new NullPointerException("Request properties are not defined.");
		
		// Be sure base URL ends with '/'
		if (!baseUrl.endsWith("/"))
			baseUrl = baseUrl + "/";
		
		String apiKeyParam = mapProp.get(REQUEST_APIKEY_PARAM_REF);
		
		if (StringUtils.isEmpty(apiKeyParam)
			&& !StringUtils.isEmpty(mapProp.get(REQUEST_APIKEY_NAME_REF))
			&& !StringUtils.isEmpty(mapProp.get(REQUEST_APIKEY_REF))) {
			apiKeyParam = mapProp.get(REQUEST_APIKEY_NAME_REF) + "=" + mapProp.get(REQUEST_APIKEY_REF);
		}
		
		if (!StringUtils.isEmpty(apiKeyParam))
			params = apiKeyParam + "&" + params;

		if (!StringUtils.isEmpty(params))
			return baseUrl + function + "?" + params;

		return baseUrl + function;
	}
}
