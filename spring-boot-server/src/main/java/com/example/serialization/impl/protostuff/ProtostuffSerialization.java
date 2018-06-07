package com.example.serialization.impl.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.collect.Maps;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;

/**
 * Created by fangrui on 2018/5/31.
 */
public class ProtostuffSerialization {

    private static Objenesis objenesis = new ObjenesisStd(true);

    private static Map<Class<?>, Schema<?>> map = Maps.newConcurrentMap();

    public static Schema getSchema(Class<?> clazz) {
        Schema schema = map.get(clazz);

        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);

            if (schema != null) {
                map.put(clazz, schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj) {
        Class<?> clazz = obj.getClass();
        Schema schema = getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    }

    public static <T> T deserialize(byte[] b, Class<T> clazz) {
        T message = (T) objenesis.newInstance(clazz);
        Schema<T> schema = getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(b, message, schema);
        return message;
    }
}
