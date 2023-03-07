package com.wallet.core.rest;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST(400, 0),
    UNAUTHORIZED(400, 1),
    INTERNAL_SERVER_ERROR(500, 0),
    BAD_GATEWAY(500, 2);

    private final int series;
    private final int code;

    ErrorCode(int series, int code) {
        this.series = series;
        this.code = code;
    }

}
