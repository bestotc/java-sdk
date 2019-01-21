package com.bestotc.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.alibaba.fastjson.JSON;

import com.bestotc.domain.Message;
import com.bestotc.domain.OrderNotifyMessage;
import com.bestotc.exception.InvalidParameterException;
import com.bestotc.utils.ApiIdentityUtil;
import com.google.common.base.Function;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * 订单状态改变后的异步通知
 *
 * @see <a href="https://github.com/bestotc/APIDOC/wiki/%E8%AE%A2%E5%8D%95%E5%BC%82%E6%AD%A5%E9%80%9A%E7%9F%A5"
 * target="_top">订单回调文档</a>
 */
@Slf4j
public class ApiOrderNotifyService {

    /**
     * 秘钥: 渠道商配置的回调签名秘钥
     */
    private final String SECRET_KEY;

    /**
     * 构造方法
     * @param secretKey
     */
    public ApiOrderNotifyService(@NonNull String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    /**
     * 订单状态变化处理
     * 注意:如果业务处理失败抛出异常可重复消费
     * @param messageStr  从 HttpServletRequest的 body中获取: String messageStr = IOUtils.toString(request.getInputStream(), "UTF-8");
     *      或者Controller这样定义:
     *     public void orderStatusNotify(@RequestBody String messageJson){...}
     * @param function 回调的 Function
     * @throws IOException
     */
    public OrderNotifyMessage orderStatusNotifyHandle(@NonNull String messageStr,Function<OrderNotifyMessage, Boolean> function)
        throws IOException {
        log.info("order Status notify message received:{}", messageStr);
        //获取消息体
        Message message;
        try {
            message = JSON.parseObject(messageStr, Message.class);
        } catch (Exception e) {
            throw new InvalidParameterException("message format error.");
        }

        if (message == null || message.getType() == null) {
            log.info("message type not exist");
            throw new InvalidParameterException("Unexpected signature version. Unable to verify signature.");
        }

        // 验证通知队列签名
        if (!message.getSignatureVersion().equals("1") || !isMessageSignatureValid(message)) {
            throw new SecurityException("Signature verification failed.");
        }
        OrderNotifyMessage orderNotifyMessage = null;
        //处理不同消息类型
        if (message.getType().equals("Notification")) {
            // 验证业务签名
            if (!ApiIdentityUtil.sign(SECRET_KEY, message.getMessage()).equals(message.getSubject())) {
                log.info("Business signature verification failed.");
                throw new SecurityException("Unexpected signature version. Unable to verify signature.");
            }
            orderNotifyMessage = JSON.parseObject(message.getMessage(), OrderNotifyMessage.class);
            //具体业务逻辑逻辑
            if (function != null) {
                function.apply(orderNotifyMessage);
            }
        } else if (message.getType().equals("SubscriptionConfirmation")) {
            //确认订阅 请求SubscribeURL即可
            subscription(message);
        } else if (message.getType().equals("UnsubscribeConfirmation")) {
            //用户取消订阅三天后生效 (取消方法 builder.toString() 中获取UnSubscribeURL 请求即可)
            //防止误删 再次发起订阅 可根据具体情况选择注释下边代码
            subscription(message);
        } else {
            log.info("Unknown message type.");
        }
        log.info("Done processing message: " + message.getMessageId());
        return orderNotifyMessage;
    }

    /**
     * 确认订阅
     * @param msg
     * @throws IOException
     */
    private void subscription(Message msg) throws IOException {
        //请求订阅的SubscribeURL 确认订阅
        InputStream inputStream = new URL(msg.getSubscribeURL()).openStream();
        String response = IOUtils.toString(inputStream, "UTF-8");
        log.info("Subscription confirmation (" + msg.getSubscribeURL() + ") Return value: " + response);
    }

    /****************以下为亚马逊队列验证验签方法********************/
    private static boolean isMessageSignatureValid(Message msg) {
        try {
            URL url = new URL(msg.getSigningCertURL());
            InputStream inStream = url.openStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
            inStream.close();

            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(cert.getPublicKey());
            sig.update(getMessageBytesToSign(msg));
            return sig.verify(Base64.decodeBase64(msg.getSignature()));
        } catch (Exception e) {
            throw new SecurityException("Verify method failed.", e);
        }
    }

    private static byte[] getMessageBytesToSign(Message msg) {
        byte[] bytesToSign = null;
        if (msg.getType().equals("Notification")) {
            bytesToSign = buildNotificationStringToSign(msg).getBytes();
        } else if (msg.getType().equals("SubscriptionConfirmation") || msg.getType().equals("UnsubscribeConfirmation")) {
            bytesToSign = buildSubscriptionStringToSign(msg).getBytes();
        }
        return bytesToSign;
    }

    private static String buildNotificationStringToSign(Message msg) {
        String stringToSign = null;
        stringToSign = "Message\n";
        stringToSign += msg.getMessage() + "\n";
        stringToSign += "MessageId\n";
        stringToSign += msg.getMessageId() + "\n";
        if (msg.getSubject() != null) {
            stringToSign += "Subject\n";
            stringToSign += msg.getSubject() + "\n";
        }
        stringToSign += "Timestamp\n";
        stringToSign += msg.getTimestamp() + "\n";
        stringToSign += "TopicArn\n";
        stringToSign += msg.getTopicArn() + "\n";
        stringToSign += "Type\n";
        stringToSign += msg.getType() + "\n";
        return stringToSign;
    }

    private static String buildSubscriptionStringToSign(Message msg) {
        String stringToSign = null;
        stringToSign = "Message\n";
        stringToSign += msg.getMessage() + "\n";
        stringToSign += "MessageId\n";
        stringToSign += msg.getMessageId() + "\n";
        stringToSign += "SubscribeURL\n";
        stringToSign += msg.getSubscribeURL() + "\n";
        stringToSign += "Timestamp\n";
        stringToSign += msg.getTimestamp() + "\n";
        stringToSign += "Token\n";
        stringToSign += msg.getToken() + "\n";
        stringToSign += "TopicArn\n";
        stringToSign += msg.getTopicArn() + "\n";
        stringToSign += "Type\n";
        stringToSign += msg.getType() + "\n";
        return stringToSign;
    }

}
