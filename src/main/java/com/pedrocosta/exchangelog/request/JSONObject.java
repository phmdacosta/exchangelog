package com.pedrocosta.exchangelog.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Deprecated
//public class JSONObject {
//
//	private Map<Object, Object> map;
//
//	public JSONObject() {
//		this(null);
//	}
//
//	public JSONObject(String json) {
//		map = new LinkedHashMap<Object, Object>();
//
//		if (json != null) {
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				this.map = (mapper.readValue(json, JSONObject.class)).map;
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@JsonAnySetter
//	public void setMap(Object key, Object value) {
//		if (value instanceof LinkedHashMap) {
//			Set<Entry> entries = ((LinkedHashMap) value).entrySet();
//			JSONObject jsonObj = new JSONObject();
//			for (Entry entry : entries) {
//				jsonObj.setMap(entry.getKey(), entry.getValue());
//				map.put(key, jsonObj);
//			}
//		} else {
//			map.put(key, value);
//		}
//	}
//
//	public Set<Object> getKeys() {
//		return map.keySet();
//	}
//
//	public Object get(String key) {
//		return map.get(key);
//	}
//
//	public Boolean getBoolean(String key) {
//		return (Boolean) get(key);
//	}
//
//	public String getString(String key) {
//		return (String) get(key);
//	}
//
//	public Integer getInt(String key) {
//		return (Integer) get(key);
//	}
//
//	public Double getDouble(String key) {
//		return (Double) get(key);
//	}
//
//	public Date getDate(String key) {
//		Date date = null;
//		try {
//			String dateStr = getString(key);
//			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} finally {
//			return date;
//		}
//	}
//
//	public JSONObject getJSONObject(String key) {
//		return (JSONObject) get(key);
//	}
//
//	public JSONObject put(String key, Object value) {
//		map.put(key, value);
//		return this;
//	}
//
//	public static final String toJson(Object obj) {
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonResult = "";
//
//		try {
//			jsonResult = mapper.writeValueAsString(obj);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//		return jsonResult;
//	}
//
//	public void print() {
//		Set<Entry<Object, Object>> entries = map.entrySet();
//
//		for (Entry<Object, Object> entry : entries) {
//
//			System.out.println("---> " + entry.getValue().getClass().getTypeName() + " " + entry.getKey() + " : " + entry.getValue());
//		}
//	}
//
//	@Override
//	public String toString() {
//		return toString(false);
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public String toString(boolean format) {
//		StringBuffer sb = new StringBuffer();
//
//		if (!map.isEmpty()) {
//
//			sb.append("{");
//			if (format)
//				sb.append(lineBreak(3));
//
//			Set<Entry<Object, Object>> entries = map.entrySet();
//			Iterator<Entry<Object, Object>> it = entries.iterator();
//
//			while (it.hasNext()) {
//				Entry<Object, Object> entry = it.next();
//				sb.append("\"");
//				sb.append(entry.getKey());
//				sb.append("\":");
//
//				if (entry.getValue() instanceof JSONObject) {
//					sb.append(((JSONObject) entry.getValue()).toString(format));
//				} else if (entry.getValue() instanceof String) {
//					sb.append("\"");
//					sb.append(entry.getValue());
//					sb.append("\"");
//				} else if (entry.getValue() instanceof ArrayList) {
//					sb.append(arrayToString((ArrayList)entry.getValue(), format));
//				} else {
//					sb.append(entry.getValue());
//				}
//
//				if (it.hasNext()) {
//					sb.append(",");
//					if (format)
//						sb.append(lineBreak(3));
//				}
//			}
//
//			if (format)
//				sb.append(lineBreak(0));
//
//			sb.append("}");
//		}
//
//		return sb.toString();
//	}
//
//	private String arrayToString(ArrayList<Object> list, boolean format) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("[");
//
//		Iterator<Object> it = list.iterator();
//		while (it.hasNext()) {
//			Object o = it.next();
//			if (o instanceof String) {
//				sb.append("\"");
//				sb.append(o);
//				sb.append("\"");
//			} else if (o instanceof JSONObject) {
//				sb.append(((JSONObject)o).toString(format));
//			} else {
//				sb.append(o);
//			}
//
//			if (it.hasNext()) {
//				sb.append(",");
//			}
//		}
//
//		sb.append("]");
//
//		return sb.toString();
//	}
//
//	private String lineBreak(int spaceNum) {
//		String s = "\n";
//		for (int i = 0; i < spaceNum; i++) {
//			s = s.concat(" ");
//		}
//		return s;
//	}
//}