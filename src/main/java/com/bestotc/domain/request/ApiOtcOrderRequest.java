package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 根据第三方订单请求
 */
@Getter
@Setter
@ToString
public class ApiOtcOrderRequest {

    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;

}
