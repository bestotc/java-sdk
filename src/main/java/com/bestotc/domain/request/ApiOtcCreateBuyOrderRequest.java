package com.bestotc.domain.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

/**
 * 买币下单V1
 * @author qxx on 2019/1/2.
 */
@Getter
@Setter
@ToString
public class ApiOtcCreateBuyOrderRequest {
    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;

    /**
     * 币种: usdt
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "variety")
    private String variety;

    /**
     * 发布类型: CNY
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "currency")
    private String currency;

    /**
     * 购买金额
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "totalAmount")
    @DecimalMin(value = "0", payload = ApiOtcPayload.InvalidParameter.class, inclusive = false,message = "must be grater than zero")
    private String totalAmount;

    /**
     * 付款方式 1 支付宝 2微信 3 银行卡
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "paymentType")
    @Range(payload = ApiOtcPayload.InvalidParameter.class,  min = 1, max = 3, message = "must be range between 1 and 3")
    private Integer paymentType;

    /**
     * 付款用户姓名
     */
    private String name;

    /**
     * 付款用户身份证号
     */
    private String idNumber;

}
