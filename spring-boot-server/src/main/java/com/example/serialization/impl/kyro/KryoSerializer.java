package com.example.serialization.impl.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.example.model.wrapper.SerializeWrapper;
import com.example.serialization.impl.base.BaseSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by fangrui on 2018/6/13.
 */
public class KryoSerializer extends BaseSerializer {

    public static <T> byte[] serialize(T obj) {
        Class<?> clazz = obj.getClass();
        Object serializeObj = obj;

        if (set.contains(clazz)) {
            serializeObj = SerializeWrapper.build(obj);
            clazz = SerializeWrapper.class;
        }
        Kryo kryo = new Kryo();
        kryo.register(clazz, new BeanSerializer(kryo, clazz));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeObject(output, serializeObj);
        output.flush();
        output.close();
        byte[] bytes = bos.toByteArray();

        try {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Kryo kryo = new Kryo();

        if (set.contains(clazz)) {
            kryo.register(WRAPPER_CLASS, new BeanSerializer(kryo, WRAPPER_CLASS));

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            Input input = new Input(bais);
            input.close();
            SerializeWrapper<T> wrapper = kryo.readObject(input, WRAPPER_CLASS);
            return wrapper.getData();
        }

        kryo.register(clazz, new BeanSerializer(kryo, clazz));

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Input input = new Input(bais);
        input.close();
        T obj = kryo.readObject(input, clazz);
        return obj;
    }
}
