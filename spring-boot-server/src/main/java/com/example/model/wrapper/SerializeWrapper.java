package com.example.model.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by fangrui on 2018/6/11.
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class SerializeWrapper<T> {
    @NonNull
    private T data;

    public static <T> SerializeWrapper<T> build(T data) {
        return new SerializeWrapper<>(data);
    }
}
