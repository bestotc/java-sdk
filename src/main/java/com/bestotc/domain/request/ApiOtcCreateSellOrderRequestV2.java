package com.bestotc.domain.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

/**
 * 创建卖币订单V2
 */
@Getter
@Setter
@ToString
public class ApiOtcCreateSellOrderRequestV2{

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
     *法币类型:CNY
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "currency")
    private String currency;

    /**
     *售卖金额
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
     * 付款方式 1 支付宝 2微信 3 银行卡
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "paymentType")
    @Range(payload = ApiOtcPayload.InvalidParameter.class,  min = 1, max = 3, message = "must be range between 1 and 3")
    private Integer paymentType;

    /**
     *账号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "payOptionNumber")
    private String payOptionNumber;

    /**
     *收款人姓名
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "payOptionName")
    private String payOptionName;

    /**
     *银行名称
     */
    private String payOptionBank;

    /**
     *开户行
     */
    private String payOptionBankName;

    /**
     *支付二维码图片
     */
    private String payOptionQr;

    /**
     * 收款人真实姓名
     */
    private String name;

    /**
     * 收款人身份证号
     */
    private String idNumber;

}
