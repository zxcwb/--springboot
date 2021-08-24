package com.zxc.o2o.exceptions;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/3 11:30
 * @Version 1.0
 *
 */
public class LocalAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = 6458249813042613985L;

    public LocalAuthOperationException(String message) {
        super(message);
    }
}
