package com.bestotc.exception;


/**
 * Created by jack on 16/5/6.
 */
public class MissingParameterException extends ApiOtcException {

    private static final long serialVersionUID = -4530275043528852633L;

    public MissingParameterException(String field){
        super(400,"MissingParameter",String.format("The input parameter '%s' " +
                "that is mandatory for processing this request is not supplied",field));
    }

}
