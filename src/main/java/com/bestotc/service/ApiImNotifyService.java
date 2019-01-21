package com.bestotc.service;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

import com.bestotc.domain.ImNotifyMessage;
import com.bestotc.exception.InvalidParameterException;
import com.google.common.base.Function;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * im异步通知
 *
 * @see <a href="https://github.com/bestotc/APIDOC/wiki/%E7%94%B3%E8%AF%89%E6%B6%88%E6%81%AF%E5%9B%9E%E8%B0%83"
 * target="_top">IM回调文档</a>
 */
@Slf4j
public class ApiImNotifyService {

    /**
     * IM异步回调处理
     * @param messageStr 从 HttpServletRequest的 body中获取: String messageStr = IOUtils.toString(request.getInputStream(),"UTF-8");
     *      或者Controller这样定义:
     *     public void orderStatus(@RequestBody String messageJson){...}
     * @param function 回调Function
     * @throws IOException
     */
    public ImNotifyMessage imNotifyHandel(@NonNull String messageStr,Function<ImNotifyMessage, Boolean> function){

        log.info("order im notify message received:{}", messageStr);
        ImNotifyMessage message = null;
        try {
            message = JSON.parseObject(messageStr, ImNotifyMessage.class);
        } catch (Exception e) {
            log.info("message format error messageStr:{}", messageStr);
            throw new InvalidParameterException("message format error.");
        }
        if (function != null) {
            function.apply(message);
        }
        return message;
    }

}
