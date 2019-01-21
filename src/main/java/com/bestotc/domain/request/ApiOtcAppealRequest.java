package com.bestotc.domain.request;

import javax.validation.constraints.NotNull;

import com.bestotc.domain.ApiOtcPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;




@Getter
@Setter
@ToString
public class ApiOtcAppealRequest {

    /**
     * 第三方订单号
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "outOrderNo")
    private String outOrderNo;
    /**
     * 内容
     */
    @NotNull(payload = ApiOtcPayload.MissingParameter.class, message = "content")
    private String content;
    /**
     * 附件图片url
     */
    private String attach;

    /**
     * 消息发送者类型 0:客服,1:用户,2:承兑商,3:OTC客服
     */
    @Range(payload = ApiOtcPayload.InvalidParameter.class, min = 1, max = 2, message = "must be range between 1 and 2")
    private Integer fromType = 1;

}
