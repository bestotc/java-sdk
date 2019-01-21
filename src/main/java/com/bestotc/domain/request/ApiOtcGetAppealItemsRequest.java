package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取申诉消息列表
 */
@Getter
@Setter
@ToString
public class ApiOtcGetAppealItemsRequest {

    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;

    /**
     * 取消息列表标识
     * lastKey 首次拉取不需要传，当消息不能一次取完时，接口返回 lastKey，再次取时将该值原样返回请求； 返回的 lastKey == null 表示已取完； 返回的消息列表取最新的消息，再按时间正排序；
     * @see <a href="https://github.com/bestotc/APIDOC/wiki/%E7%94%B3%E8%AF%89%E6%8E%A5%E5%8F%A3" target="_top">API文档</a>
     */
    private String lastKey;

}
