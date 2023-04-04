package com.example.springboot.exception;

import lombok.Getter;

/**
 * @author xuyihao
 * @date 2023-01-16
 * @apiNote
 */
@Getter
public class ServiceException extends RuntimeException{

    private String code;

    public ServiceException(String code, String msg){
        super(msg);
        this.code = code;
    }
}
