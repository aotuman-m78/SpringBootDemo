package com.example.serialization;

/**
 * Created by fangrui on 2018/5/31.
 */
public interface Serialization {
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] b);
}
