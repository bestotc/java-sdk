package com.bestotc.service

import com.bestotc.domain.request.ApiOtcAppealDealRequest
import com.bestotc.domain.request.ApiOtcAppealRequest
import com.bestotc.domain.request.ApiOtcConfirmPayRequest
import com.bestotc.domain.request.ApiOtcCreateBuyOrderRequest
import com.bestotc.domain.request.ApiOtcCreateBuyOrderRequestV2
import com.bestotc.domain.request.ApiOtcCreateSellOrderRequest
import com.bestotc.domain.request.ApiOtcCreateSellOrderRequestV2
import com.bestotc.domain.request.ApiOtcGetAppealItemsRequest
import com.bestotc.domain.request.ApiOtcGetBatchOrderListRequest
import com.bestotc.domain.request.ApiOtcGetOrderListRequest
import com.bestotc.domain.request.ApiOtcGetPriceRequest
import com.bestotc.domain.request.ApiOtcOrderRequest
import com.bestotc.domain.request.ApiOtcSendAppealMessageRequest
import spock.lang.Specification
/**
 * 订单相关操作测试类
 */
class ApiOtcOrderServiceTest extends Specification {

    private static ApiOtcOrderService apiOtcOrderService

    void setup() {
        //otcDomain 测试或者生产环境的 服务器域名
        apiOtcOrderService = new ApiOtcOrderService("ivpdru4c-qpdqswld-yx9atr8t-t7763","89j2t90o-76fhxpc2-3vz0fh18-j2471","")
    }



    /**
     * 买币下单V1
     */
    def "createBuyOrderV1"() {
        def request = new ApiOtcCreateBuyOrderRequest()
        request.outOrderNo = System.currentTimeMillis()
        request.variety = "usdt"
        request.currency = "CNY"
        request.totalAmount = "10"
        request.paymentType = 3
        def response = apiOtcOrderService.createBuyOrder(request)
        expect: response.status == "success"
    }

    /**
     * 买币下单V2
     */
    def "createBuyOrderV2"() {
        def request = new ApiOtcCreateBuyOrderRequestV2()
        request.outOrderNo = System.currentTimeMillis()
        request.variety = "usdt"
        request.currency = "CNY"
        request.totalAmount = "10"
        request.name = "乔欣欣"
        request.idNumber = "411328199002221330"
        def response = apiOtcOrderService.createBuyOrderV2(request)
        expect: response.status == "success"
    }

    /**
     * 卖币下单
     */
    def "createSellOrder"() {
        def request = new ApiOtcCreateSellOrderRequest()
        request.outOrderNo = System.currentTimeMillis()
        request.variety = "usdt"
        request.currency = "CNY"
        request.totalAmount = "10"
        request.payOptionName = "张三"
        request.payOptionNumber = "6222602910029698556"
        request.payOptionBank = "交通银行"
        request.payOptionBankName = "中国交通银行西三旗支行"
        def response = apiOtcOrderService.createSellOrder(request)
        expect: response.status == "success"
    }

    /**
     * 卖币下单V2
     */
    def "createSellOrderV2"() {
        def request = new ApiOtcCreateSellOrderRequestV2()
        request.outOrderNo = System.currentTimeMillis()
        request.variety = "usdt"
        request.currency = "CNY"
        request.totalAmount = "10"
        request.paymentType = 3
        request.payOptionName = "张三"
        request.payOptionNumber = "6222602910029698556"
        request.payOptionBank = "交通银行"
        request.payOptionBankName = "中国交通银行西三旗支行"
        def response = apiOtcOrderService.createSellOrderV2(request)
        expect: response.status == "success"
    }

    /**
     * 取消订单
     */
    def "cancelOrder"() {
        def request = new ApiOtcOrderRequest()
        request.outOrderNo = commonCreateBuyOrder()
        def response = apiOtcOrderService.cancelOrder(request)
        expect: response.status == "success"
    }

    /**
     * 买币订单确认付款
     */
    def "confirmPay"() {
        def request = new ApiOtcConfirmPayRequest()
        request.outOrderNo = commonCreateBuyOrder()
        request.name = "张三"
        request.idNumber = "411328199002221330"
        def response = apiOtcOrderService.confirmPay(request)
        expect: response.status == "success"
    }

    /**
     * 卖币订单确认收款
     */
    def "confirmReceive"() {
        def request = new ApiOtcOrderRequest()
        //该订单必须 为已付款状态
        request.outOrderNo = "1546594007213"
        def response = apiOtcOrderService.confirmReceive(request)
        expect: response.status == "success"
    }

    /**
     * 获取账号信息
     */
    def "getAccount"() {
        def response = apiOtcOrderService.getAccount()
        expect:response.assetList != null
    }

    /**
     * 获取订单列表
     */
    def "getOrderList"() {
        def request = new ApiOtcGetOrderListRequest()
        def response = apiOtcOrderService.getOrderList(request)
        expect: response.total != null
    }

    /**
     * 获取订单列表
     */
    def "getPrice"() {
        def request = new ApiOtcGetPriceRequest()
        request.currency="CNY"
        request.variety="usdt"
        def response = apiOtcOrderService.getPrice(request)
        expect:response.variety != null
    }

    /**
     * 获取订单详情
     */
    def "getOrderDetail"() {
        def request = new ApiOtcOrderRequest()
        request.outOrderNo = "123"
       apiOtcOrderService.getOrderDetail(request)
        expect: true
    }

    /**
     * 批量查询订单列表
     */
    def "getOrderListDetail"() {
        def request = new ApiOtcGetBatchOrderListRequest()
        request.outOrderIds = commonCreateBuyOrder() + ',' + commonCreateBuyOrder()
        def response = apiOtcOrderService.getOrderListDetail(request)
        expect:response.variety != null
    }

    /**
     * 发起申诉
     */
    def "appealSubmit"() {
        def request = new ApiOtcAppealRequest()
        request.outOrderNo = "1546594007213"
        request.content = "没有收到款"
        request.attach = "https://avatars2.githubusercontent.com/u/46237497?s=400&v=4"
        request.fromType = 1
        def response = apiOtcOrderService.appealSubmit(request)
        expect:response.status != null
    }

    /**
     * 发起申诉
     */
    def "sendAppealMessage"() {
        def request = new ApiOtcSendAppealMessageRequest()
        request.outOrderNo = "1546594007213"
        request.content = "确实没有收到款"
        request.attach = "https://avatars2.githubusercontent.com/u/46237497?s=400&v=4"
        request.fromType = 1
        def response = apiOtcOrderService.sendAppealMessage(request)
        expect:response.status != null
    }

    /**
     * 获取申诉消息列表
     */
    def "getAppealMessageList"() {
        def request = new ApiOtcGetAppealItemsRequest()
        request.outOrderNo = "1546594007213"
        def response = apiOtcOrderService.getAppealMessageList(request)
        expect:response.items != null
    }

    /**
     * 申诉详情
     */
    def "getAppealDetail"() {
        def request = new ApiOtcOrderRequest()
        request.outOrderNo = "1546594007213"
        def response = apiOtcOrderService.getAppealDetail(request)
        expect:response.status != null
    }

    /**
     * 申诉处理
     */
    def "appealDeal"() {
        def request = new ApiOtcAppealDealRequest()
        request.outOrderNo = "1546594007213"
        request.dealType = 0
        request.remark = "继续交易"
        def response = apiOtcOrderService.appealDeal(request)
        expect:response.status == "success"
    }

    /**
     * 创建一个买币订单
     * @return
     */
    def commonCreateBuyOrder() {
        def request = new ApiOtcCreateBuyOrderRequest()
        request.outOrderNo = System.currentTimeMillis()
        request.variety = "usdt"
        request.currency = "CNY"
        request.totalAmount = "10"
        request.paymentType = 3
        apiOtcOrderService.createBuyOrder(request)
        return request.outOrderNo
    }
    /**
     * 创建一个卖币订单
     * @return
     */
    def commonCreateSellOrder() {
        def request = new ApiOtcCreateSellOrderRequest()
        request.outOrderNo = System.currentTimeMillis()
        request.variety = "usdt"
        request.currency = "CNY"
        request.totalAmount = "10"
        request.payOptionName = "张三"
        request.payOptionNumber = "6222602910029698556"
        request.payOptionBank = "交通银行"
        request.payOptionBankName = "中国交通银行西三旗支行"
        apiOtcOrderService.createSellOrder(request)
        return request.outOrderNo
    }
}
