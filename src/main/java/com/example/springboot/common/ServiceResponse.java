package com.example.springboot.common;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.net.ResponseCache;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceResponse<T> implements Serializable {

    private int status;

    private String message;

    private T data;

    private ServiceResponse(int status) {
        this.status = status;
    }

    private ServiceResponse(int status, T data) {

        this.status = status;
        this.data = data;
    }

    private ServiceResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private ServiceResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ServiceResponse<T> createBySuccess() {
        return new ServiceResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServiceResponse<T> createBySuccessMessage(String message) {
        return new ServiceResponse<>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> ServiceResponse<T> createBySuccess(T data) {
        return new ServiceResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServiceResponse<T> createBySuccess(String message, T data) {
        return new ServiceResponse<>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> ServiceResponse<T> createByError() {
        return new ServiceResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServiceResponse<T> createByErrorMessage(String errorMessage) {
        return new ServiceResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServiceResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServiceResponse<>(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
