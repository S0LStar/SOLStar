package com.shinhan.solstar.global.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HttpResponseUtil {

    public ResponseEntity<Map<String, Object>> createResponse(HttpMethodCode method, Object o) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", method.getCode());
        data.put("result", o);

        return ResponseEntity.status(HttpStatus.valueOf(method.getCode())).body(data);
    }
}