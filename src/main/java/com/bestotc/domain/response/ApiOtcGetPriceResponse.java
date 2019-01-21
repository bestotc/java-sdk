package com.bestotc.domain.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取币种单价
 */
@Getter
@Setter
@ToString
public class ApiOtcGetPriceResponse {

    /**
     * 币种
     */
    private String variety;
    /**
     * 法币类型
     */
    private String currency;
    /**
     * 买币价格
     */
    private String buy;
    /**
     * 卖币价格
     */
    private String sell;

}
