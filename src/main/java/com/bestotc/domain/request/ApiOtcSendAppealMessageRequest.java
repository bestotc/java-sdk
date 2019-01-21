package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

/**
 * 发送消息
 */
@Getter
@Setter
@ToString
public class ApiOtcSendAppealMessageRequest {
    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;

    /**
     * 消息发送者类型 0:客服,1:用户
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "fromType")
    @Range(payload = ApiOtcPayload.InvalidParameter.class, min = 0, max = 1, message = "must be range between 1 and 2")
    private Integer fromType;

    /**
     * 内容
     */
    private String content;
    /**
     * 附件图片url
     */
    private String attach;

}
