package com.alibaba.datax.plugin.reader.mongodbreader.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XJson {
    static Logger log = LoggerFactory.getLogger(XJson.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public XJson() {
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    private static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static String getObjectFieldValue(String fieldName, Object o) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return String.valueOf(field.get(o));
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> T decodeJson(byte[] src, Class<T> c) {
        try {
            return mapper.readValue(src, c);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> T decodeJson(String src, Class<T> c) {
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(src, c);
        } catch (Exception var3) {
            log.warn("invalid string to decode to json:{}", src);
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> List<T> decodeJsonList(String src, Class<T> c) {
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = getCollectionType(mapper, ArrayList.class, c);
            return (List)mapper.readValue(src, javaType);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static String encodeJson(Object o) {
        if (o == null) {
            return null;
        } else {
            try {
                return mapper.writeValueAsString(o);
            } catch (JsonProcessingException var2) {
                log.error(var2.getMessage(), var2);
                return null;
            }
        }
    }

    public static byte[] encodeJsonBytes(Object o) {
        if (o == null) {
            return null;
        } else {
            try {
                return mapper.writeValueAsBytes(o);
            } catch (JsonProcessingException var2) {
                log.error(var2.getMessage(), var2);
                return null;
            }
        }
    }
}
