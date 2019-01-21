package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取币种单价
 */
@Getter
@Setter
@ToString
public class ApiOtcGetPriceRequest{

    /**
     * 币种:btc,usdt...
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "variety")
    private String variety;

    /**
     * 法币类型:CNY
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "currency")
    private String currency;

}
