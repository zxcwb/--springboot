package com.zxc.o2o.exceptions;

public class ProductOperationException extends RuntimeException {
    private static final long serialVersionUID = -5147547983724815030L;

    public ProductOperationException(String message) {
        super(message);
    }
}
