package com.bestotc.service

import com.alibaba.fastjson.JSON
import com.bestotc.domain.OrderNotifyMessage
import com.google.common.base.Function
import spock.lang.Specification
/**
 * 订单状态变化的回调测试
 * @author qxx on 2019/1/4.
 */
class ApiOrderNotifyServiceTest extends Specification {

    private static ApiOrderNotifyService orderNotifyService

    //确认订阅消息
    private static String subscriptionConfirmation ="""{  "Type" : "SubscriptionConfirmation",  "MessageId" : "6c8cc5a8-f38f-403e-aadf-e84d71465c45",  "Token" : "2336412f37fb687f5d51e6e241dbca52ee34aad6e2ec27e43169b37351e06aa2fdd0dc816a63a445a076eb5f451eee627f1c251c3c80412d506459667496ece3c84cd8c141296a84a16e4d1ec82e2497106eab2972b309c6364bc82f701a9916f3c240e2ad8a96fd65cc7401d5f70a6ecf29b68c926c1d01891058308759f368",  "TopicArn" : "arn:aws:sns:us-east-2:713166642506:T_PAYMENT_CHANNEL_UID_10105",  "Message" : "You have chosen to subscribe to the topic arn:aws:sns:us-east-2:713166642506:T_PAYMENT_CHANNEL_UID_10105.\\nTo confirm the subscription, visit the SubscribeURL included in this message.",  "SubscribeURL" : "https://sns.us-east-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-east-2:713166642506:T_PAYMENT_CHANNEL_UID_10105&Token=2336412f37fb687f5d51e6e241dbca52ee34aad6e2ec27e43169b37351e06aa2fdd0dc816a63a445a076eb5f451eee627f1c251c3c80412d506459667496ece3c84cd8c141296a84a16e4d1ec82e2497106eab2972b309c6364bc82f701a9916f3c240e2ad8a96fd65cc7401d5f70a6ecf29b68c926c1d01891058308759f368",  "Timestamp" : "2019-01-04T07:10:13.446Z",  "SignatureVersion" : "1",  "Signature" : "L0uamOJvg3d9QWBdMEdbcLuS4BQCktrrZhIDhR3ZoEWInAj5Ve9H4Kq+NqJ7PGdeeWTsAgM03AJd/pQOC3YJvXtXiTvak0eTvRyi10jevIFGjJ3c4QF+rAM7eTS+uzAlG0mqY2DkdB1AY/pY2Sh/e3Kso0i8HsPSPmXGPctqi7iJojFk8kXpJVoXDrDIo1QoafO4XHpnDDpMueDPxK9L4pLtxD8qqyVTIsqLmmBBpnC/5+gdkwytN/2p755UOPrntR9zM0ezXKBRDCbQW4t3JugKJ9ClFcv5yWOoSpAAdEgZu9zL3nawW36LrpjkB311sLG7JX5Uaeia0LSA3SRRhQ==",  "SigningCertURL" : "https://sns.us-east-2.amazonaws.com/SimpleNotificationService-ac565b8b1a6c5d002d285f9598aa1d9b.pem"}"""

    //已付款的通知消息
    private static String notification="""{  "Type" : "Notification",  "MessageId" : "bfe3e2b8-99d9-5fd1-abb9-b61193a75128",  "TopicArn" : "arn:aws:sns:us-east-2:713166642506:T_PAYMENT_CHANNEL_UID_10105",  "Subject" : "AjaPSaAp+cAHd/k1SKMJc5C1BIg3ErZs3UanH7C4nAk=",  "Message" : "{\\"amount\\":\\"1.42857142\\",\\"appealStatus\\":1,\\"isReopen\\":false,\\"outOrderNo\\":\\"1546585956527\\",\\"price\\":\\"7.0000\\",\\"status\\":3,\\"timestamp\\":\\"2019-01-04T07:16:12.916Z\\",\\"total\\":\\"10.0000\\",\\"orderType\\":2,\\"type\\":\\"OrderStatusChangeMessage\\"}",  "Timestamp" : "2019-01-04T07:16:14.019Z",  "SignatureVersion" : "1",  "Signature" : "jfrlQa2iGqDwqNbp16+KjXvjiyEsPpjYacMBGixeSHAf4TyZr00Tfp81zqlBRLX6v3JhPy1DwM1F79VoM/DTnwH1ZgpaA0gSheVsjnjeRX6VyKY9sry4mbba4wRceZVH1PMN7SSbqaKH1xFIRndfHEcQIYY/tqbP3cOZk2Z3p/2YG4dXsGoWC72Bg7VbpMXFUhBNwquV2fo6HAAy+cYcAdyiLbNCAUaCW41AsgpqTOuQR1HLzpfsjgFfySUpIUj3UM9uKaRuvmqNDwRf92xwbnxoZvUhefGrrJ3v4zydAUoeMSOiX4YhR+lRd1NbWfBWfKL573hxX3EQbyPJqwgViA==",  "SigningCertURL" : "https://sns.us-east-2.amazonaws.com/SimpleNotificationService-ac565b8b1a6c5d002d285f9598aa1d9b.pem",  "UnsubscribeURL" : "https://sns.us-east-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-2:713166642506:T_PAYMENT_CHANNEL_UID_10105:1ef5c72e-94e6-4cb2-bd7b-915e81cb8e46"}"""

    void setup() {
        orderNotifyService = new ApiOrderNotifyService("QHqNZXZZ2XRm")
    }


    def "notification"() {

        //创建回调的Function 如果业务处理失败可抛出异常 重复消费
        def function = new Function<OrderNotifyMessage, Boolean>(){
            @Override
            Boolean apply(OrderNotifyMessage orderNotifyMessage) {
                println JSON.toJSONString(orderNotifyMessage)
                return Boolean.TRUE
            }
        }

        orderNotifyService.orderStatusNotifyHandle(notification, function)
        expect: true
    }

    def "subscriptionConfirmation"() {
        def function = new Function<OrderNotifyMessage, Boolean>(){
            @Override
            Boolean apply(OrderNotifyMessage orderNotifyMessage) {
                println JSON.toJSONString(orderNotifyMessage)
                return Boolean.TRUE
            }
        }
        orderNotifyService.orderStatusNotifyHandle(subscriptionConfirmation, function)
        expect: true
    }


}
