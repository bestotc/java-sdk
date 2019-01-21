package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

/**
 * 申诉订单处理
 */
@Getter
@Setter
@ToString
public class ApiOtcAppealDealRequest {

    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;

    /**
     * 处理结果:0继续交易 1 取消订单
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "dealType")
    @Range(payload = ApiOtcPayload.InvalidParameter.class, min = 0, max = 1, message = "must be range between 0 and 1")
    private Integer dealType;

    /**
     * 备注
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "remark")
    private String remark;
}
