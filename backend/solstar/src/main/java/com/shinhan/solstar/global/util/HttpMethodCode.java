package com.shinhan.solstar.global.util;

public enum HttpMethodCode {
    GET(200),
    POST(201),
    PUT(201),
    DELETE(200),
    PATCH(201);

    private int code;

    private HttpMethodCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}