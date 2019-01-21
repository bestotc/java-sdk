package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 批量查询订单列表
 */
@Getter
@Setter
@ToString
public class ApiOtcGetBatchOrderListRequest {

    /**
     * 	第三方订单编号列表,英文","号分割
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderIds")
    private String outOrderIds;
}
