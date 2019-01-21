package com.bestotc.domain.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 卖币下单
 */
@Getter
@Setter
@ToString
public class ApiOtcCreateSellOrderRequest {
    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;
    /**
     * 币种
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "variety")
    private String variety;
    /**
     * 法币类型:CNY
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "currency")
    private String currency;
    /**
     * 售卖金额
     */
    @DecimalMin(value = "0", payload = ApiOtcPayload.InvalidParameter.class, inclusive = false,message = "must be grater than zero")
    @DecimalMax(value = "50000", payload = ApiOtcPayload.InvalidParameter.class,message = "must be less than 50000")
    private String totalAmount;

    /**
     * 售卖数量
     */
    @DecimalMin(value = "0", payload = ApiOtcPayload.InvalidParameter.class, inclusive = false,message = "must be grater than zero")
    private String amount;
    /**
     *收款人银行卡账号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "payOptionNumber")
    private String payOptionNumber;
    /**
     * 收款人姓名
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "payOptionName")
    private String payOptionName;
    /**
     * 收款人银行名称
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "payOptionBank")
    private String payOptionBank;
    /**
     * 收款人开户行
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "payOptionBankName")
    private String payOptionBankName;
    /**
     * 收款用户姓名
     */
    private String name;

    /**
     * 收款用户身份证号
     */
    private String idNumber;

}
