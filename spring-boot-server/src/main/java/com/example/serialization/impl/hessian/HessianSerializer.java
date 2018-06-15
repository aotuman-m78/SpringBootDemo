package com.example.serialization.impl.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.dyuproject.protostuff.ByteArrayInput;
import com.example.serialization.impl.base.BaseSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by fangrui on 2018/6/13.
 */
public class HessianSerializer extends BaseSerializer {

    public static <T> byte[] serialize(T obj) {
//        Class<?> clazz = obj.getClass();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            HessianOutput out = new HessianOutput(baos);
            try {
                out.writeObject(obj);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return baos.toByteArray();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T deserialize(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            HessianInput input = new HessianInput(bais);
            return (T) input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
