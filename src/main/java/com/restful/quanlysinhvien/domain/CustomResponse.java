package com.restful.quanlysinhvien.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponse<T> {
    private int statusCode;
    private String error;
    // message co the la: String or ArrayList
    private Object message;
    private T data;
}
