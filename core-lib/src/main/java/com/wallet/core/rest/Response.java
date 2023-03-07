package com.wallet.core.rest;

import lombok.Getter;

import java.io.Serializable;

@Getter
public final class Response<T> implements Serializable {

    private T payload;
    private ResponseError error;


    private Response(T payload) {
        this.payload = payload;
    }

    private Response(ResponseError error) {
        this.error = error;
    }


    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> error(ResponseError error) {
        return new Response<>(error);
    }


}
