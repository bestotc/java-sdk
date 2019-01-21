package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 确认付款
 */
@Getter
@Setter
@ToString
public class ApiOtcConfirmPayRequest {

    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;

    /**
     * 付款用户姓名
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "name")
    private String name;

    /**
     * 付款用户身份证号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "idNumber")
    private String idNumber;

    /**
     * 付款用户手机
     */
    private String mobile;


}
