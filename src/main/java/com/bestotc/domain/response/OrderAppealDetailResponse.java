package com.bestotc.domain.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单申诉信息
 * @author qxx on 2018/11/1.
 */
@Getter
@Setter
@ToString
public class OrderAppealDetailResponse {

    /**
     * 是否是用户发起的 true:用户发起 false:承兑商发起
     */
    private Boolean isUser;

    /**
     * 申诉状态2-申诉中 3-已处理
     */
    private Integer status;

    /**
     * 处理类型 0 无操作 1 关闭订单
     */
    private Integer dealType;

    /**
     * 处理结果
     */
    private String dealResult;

    /**
     * 处理时间
     */
    private Date dealTime;

    /**
     * 申诉时间
     */
    private Date createTime;
}
