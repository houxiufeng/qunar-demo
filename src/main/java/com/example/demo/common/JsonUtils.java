package com.example.demo.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static ObjectMapper getObjectMapperInstance() {
        return objectMapper;
    }

    public static String fromBean(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException var2) {
            log.error("write to json string error:{}", object, var2);
            return null;
        }
    }

    public static <T> T toBean(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }

    public static <T> T toBean(String jsonString, JavaType javaType) throws IOException {
        return objectMapper.readValue(jsonString, javaType);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(jsonString, clazz);
            } catch (IOException var3) {
                log.error("parse json string error:{}", jsonString, var3);
                return null;
            }
        }
    }

    public static <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(jsonString, javaType);
            } catch (IOException var3) {
                log.error("parse json string error:{}", jsonString, var3);
                return null;
            }
        }
    }

    public static JavaType buildType(Type type) {
        return objectMapper.constructType(type);
    }

    public static JavaType buildParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static JavaType buildParametricType(Class<?> parametrized, JavaType... parameterTypes) {
        return objectMapper.getTypeFactory().constructParametricType(parametrized, parameterTypes);
    }

    public static JavaType buildCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public static JavaType buildCollectionType(Class<? extends Collection> collectionClass, JavaType elementType) {
        return objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementType);
    }

    public static JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return objectMapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    public static JavaType buildMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType) {
        return objectMapper.getTypeFactory().constructMapType(mapClass, keyType, valueType);
    }

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.getFactory().enable(com.fasterxml.jackson.core.JsonFactory.Feature.INTERN_FIELD_NAMES);
        objectMapper.getFactory().enable(com.fasterxml.jackson.core.JsonFactory.Feature.CANONICALIZE_FIELD_NAMES);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
