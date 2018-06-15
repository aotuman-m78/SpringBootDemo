package com.example.serialization.impl.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.example.model.wrapper.SerializeWrapper;
import com.example.serialization.impl.base.BaseSerializer;
import com.google.common.collect.Maps;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;

/**
 * Created by fangrui on 2018/5/31.
 */
public class ProtostuffSerializer extends BaseSerializer {

    private static Objenesis objenesis = new ObjenesisStd(true);

    private final static Map<Class<?>, Schema<?>> map = Maps.newConcurrentMap();

    private final static Schema WRAPPER_SCHEMA = RuntimeSchema.createFrom(SerializeWrapper.class);

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
        Schema schema = WRAPPER_SCHEMA;
        Object serializeObj = obj;

        if (set.contains(clazz)) {
            serializeObj = SerializeWrapper.build(obj);
        } else {
            schema = getSchema(clazz);
        }

        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(serializeObj, schema, buffer);
    }

    public static <T> T deserialize(byte[] b, Class<T> clazz) {

        if (set.contains(clazz)) {
            SerializeWrapper<T> wrapper = objenesis.newInstance(SerializeWrapper.class);
            ProtostuffIOUtil.mergeFrom(b, wrapper, WRAPPER_SCHEMA);
            return wrapper.getData();
        } else {
            T message = (T) objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(b, message, schema);
            return message;
        }
    }
}
