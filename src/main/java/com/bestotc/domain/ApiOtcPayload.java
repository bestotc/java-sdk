package com.bestotc.domain;

import javax.validation.Payload;

/**
 *
 */
public class ApiOtcPayload {

    public interface MissingParameter extends Payload {

    }

    public interface InvalidParameter extends Payload{

    }

    public interface PrimaryKeyIsNullGroup {
    }
    public interface PrimaryKeyIsNotNullGroup {
    }
}
