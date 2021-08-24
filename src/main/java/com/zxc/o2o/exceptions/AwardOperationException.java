package com.zxc.o2o.exceptions;

/*
 * @Author: zxc of Russell
 * @Date: 2021/8/15 10:19
 * @Version 1.0
 *
 */
public class AwardOperationException extends RuntimeException {
    private static final long serialVersionUID = -1452400902755260083L;

    public AwardOperationException(String message) {
        super(message);
    }
}
