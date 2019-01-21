package com.bestotc.domain.response;

import java.util.Date;

import com.bestotc.domain.AdvertPaymentVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiOtcCreateBuyOrderResponse extends ApiBaseResponse {
    /**
     * 币数量
     */
    private String amount;
    /**
     * 法币金额
     */
    private String totalAmount;
    /**
     * 付款方式
     */
    private AdvertPaymentVo payOption;
    /**
     * 商家接单时间
     */
    private Date applyTime;

}
