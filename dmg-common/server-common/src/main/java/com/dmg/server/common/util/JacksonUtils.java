package com.dmg.server.common.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @Author liubo
 * @Description //TODO JacksonUtils
 * @Date 10:28 2020/2/12
 **/
public final class JacksonUtils {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private JacksonUtils() {
	}

	/**
	 * 对象转换成json字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String toJson(Object value) {
		if (null == value) {
			return null;
		}

		try {
			return OBJECT_MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * json字符串转化为对象
	 * 
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> valueType) {
		if (StringUtils.isBlank(json) || null == valueType) {
			return null;
		}

		try {
			return OBJECT_MAPPER.readValue(json, valueType);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * json字符串转化为对象
	 * 
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T toObject(String json, TypeReference<?> typeReference) {
		if (StringUtils.isBlank(json) || null == typeReference) {
			return null;
		}

		try {
			return OBJECT_MAPPER.readValue(json, typeReference);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * json字符串转化为对象
	 * 
	 * @param json
	 * @param javaType
	 * @return
	 */
	public static <T> T toObject(String json, JavaType javaType) {
		if (StringUtils.isBlank(json) || null == javaType) {
			return null;
		}

		try {
			return OBJECT_MAPPER.readValue(json, javaType);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * json字符串转化为JsonNode
	 * 
	 * @param json
	 * @return
	 */
	public static JsonNode toJsonNode(String json) {
		if (StringUtils.isBlank(json)) {
			return null;
		}

		try {
			return OBJECT_MAPPER.readTree(json);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}