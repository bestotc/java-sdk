package com.bestotc.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

import com.bestotc.domain.JsonResult;
import com.bestotc.domain.OtcApiOrderVO;
import com.bestotc.domain.QueryResponse;
import com.bestotc.domain.request.ApiOtcAppealDealRequest;
import com.bestotc.domain.request.ApiOtcAppealRequest;
import com.bestotc.domain.request.ApiOtcConfirmPayRequest;
import com.bestotc.domain.request.ApiOtcCreateBuyOrderRequest;
import com.bestotc.domain.request.ApiOtcCreateBuyOrderRequestV2;
import com.bestotc.domain.request.ApiOtcCreateSellOrderRequest;
import com.bestotc.domain.request.ApiOtcCreateSellOrderRequestV2;
import com.bestotc.domain.request.ApiOtcGetAppealItemsRequest;
import com.bestotc.domain.request.ApiOtcGetBatchOrderListRequest;
import com.bestotc.domain.request.ApiOtcGetOrderListRequest;
import com.bestotc.domain.request.ApiOtcGetPriceRequest;
import com.bestotc.domain.request.ApiOtcOrderRequest;
import com.bestotc.domain.request.ApiOtcSendAppealMessageRequest;
import com.bestotc.domain.response.ApiBaseResponse;
import com.bestotc.domain.response.ApiOtcAccountResponse;
import com.bestotc.domain.response.ApiOtcCreateBuyOrderResponse;
import com.bestotc.domain.response.ApiOtcCreateBuyOrderResponseV2;
import com.bestotc.domain.response.ApiOtcCreateSellOrderResponse;
import com.bestotc.domain.response.ApiOtcGetAppealItemsResponse;
import com.bestotc.domain.response.ApiOtcGetPriceResponse;
import com.bestotc.domain.response.OrderAppealDetailResponse;
import com.bestotc.exception.ApiOtcException;
import com.bestotc.exception.InvalidParameterException;
import com.bestotc.utils.ApiIdentityUtil;
import com.bestotc.utils.HttpUtil;
import com.bestotc.utils.ValidatorHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * 订单相关
 *
 * @see <a href="https://github.com/bestotc/APIDOC/wiki/%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3" target="_top">API文档</a>
 */
@Slf4j
public class ApiOtcOrderService {
    /**
     * 秘匙
     */
    private final String ACCESS_KEY_ID;

    /**
     * 秘钥
     */
    private final String ACCESS_KEY_SECRET;

    /**
     * 服务器地址
     */
    private final String HOST;
    /**
     * 协议+ HOST
     */
    private final String DOMAIN;

    /**
     * httpClient
     */
    private final HttpClient httpClient;

    /**
     * 构造方法
     *
     * @param accessKeyId     秘匙
     * @param accessKeySecret 秘钥
     * @param otcDomain otc域名全称
     */
    public ApiOtcOrderService(@NonNull String accessKeyId, @NonNull String accessKeySecret, @NonNull String otcDomain) {
        this.ACCESS_KEY_ID = accessKeyId;
        this.ACCESS_KEY_SECRET = accessKeySecret;
        this.httpClient = HttpUtil.getHttpClient();
        this.HOST = getHost(otcDomain);
        this.DOMAIN = otcDomain;
    }

    /**
     * 自带httpClient构造方法
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param httpClient      指定httpClient
     */
    public ApiOtcOrderService(@NonNull String accessKeyId,@NonNull String accessKeySecret,@NonNull String otcDomain,@NonNull HttpClient httpClient) {
        this.ACCESS_KEY_ID = accessKeyId;
        this.ACCESS_KEY_SECRET = accessKeySecret;
        this.httpClient = httpClient;
        this.HOST = getHost(otcDomain);
        this.DOMAIN = otcDomain;
    }

    /**
     * 买币下单V1
     *
     */
    public ApiOtcCreateBuyOrderResponse createBuyOrder(ApiOtcCreateBuyOrderRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/buy";
        log.info("createBuyOrder,request={}", request);
        return call(uri, params, ApiOtcCreateBuyOrderResponse.class);
    }

    /**
     * 买币下单V2
     *
     * @See <a href="https://github.com/bestotc/APIDOC/wiki/%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3" target="_top">API文档</a>
     */
    public ApiOtcCreateBuyOrderResponseV2 createBuyOrderV2(ApiOtcCreateBuyOrderRequestV2 request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v2/api/openotc/order/buy";
        log.info("createBuyOrderV2,request={}", request);
        return call(uri, params, ApiOtcCreateBuyOrderResponseV2.class);
    }

    /**
     * 卖币下单
     */
    public ApiOtcCreateSellOrderResponse createSellOrder(ApiOtcCreateSellOrderRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/sell";
        log.info("createSellOrder,request={}", request);
        return call(uri, params, ApiOtcCreateSellOrderResponse.class);
    }

    /**
     * 卖币下单V2
     * @See <a href="https://github.com/bestotc/APIDOC/wiki/%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3" target="_top">API文档</a>
     */
    public ApiOtcCreateSellOrderResponse createSellOrderV2(ApiOtcCreateSellOrderRequestV2 request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v2/api/openotc/order/sell";
        log.info("createSellOrderV2,request={}", request);
        return call(uri, params, ApiOtcCreateSellOrderResponse.class);
    }

    /**
     * 取消订单
     */
    public ApiBaseResponse cancelOrder(ApiOtcOrderRequest request) {
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/cancel";
        log.info("cancelOrder,request={}", request);
        return call(uri, params, ApiBaseResponse.class);
    }

    /**
     * 确认付款
     */
    public ApiBaseResponse confirmPay(ApiOtcConfirmPayRequest request) {
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/buy/confirmPay";
        log.info("confirmPay,request={}", request);
        return call(uri, params, ApiBaseResponse.class);
    }

    /**
     * 确认收款
     */
    public ApiBaseResponse confirmReceive(ApiOtcOrderRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/sell/confirmReceive";
        log.info("confirmReceive,request={}", request);
        return call(uri, params, ApiBaseResponse.class);
    }

    /**
     * 获取账号信息
     */
    public ApiOtcAccountResponse getAccount() {
        Map<String, String> params = new HashMap<>();
        String uri = "/v1/api/openotc/account";
        log.info("getAccount,request={}");
        return call(uri, params, ApiOtcAccountResponse.class);
    }

    /**
     * 获取订单列表
     */
    public QueryResponse<OtcApiOrderVO> getOrderList(ApiOtcGetOrderListRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/list";
        log.info("getOrderList,request={}", request);
        return call(uri, params, new TypeReference<QueryResponse<OtcApiOrderVO>>() {});
    }

    /**
     * 获取币种单价
     */
    public ApiOtcGetPriceResponse getPrice(ApiOtcGetPriceRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/price";
        log.info("getPrice,request={}", request);
        return call(uri, params, ApiOtcGetPriceResponse.class);
    }

    /**
     * 获取订单详情
     */
    public OtcApiOrderVO getOrderDetail(ApiOtcOrderRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/detail";
        log.info("getOrderDetail,request={}", request);
        return callForDetail(uri, params, OtcApiOrderVO.class);
    }

    /**
     * 批量查询订单列表
     */
    public List<OtcApiOrderVO> getOrderListDetail(ApiOtcGetBatchOrderListRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/list/batch";
        log.info("getOrderListDetail,request={}{}", request);
        return callForList(uri, params, OtcApiOrderVO.class);
    }

    /**
     * 发起申诉
     */
    public ApiBaseResponse appealSubmit(ApiOtcAppealRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/appeal/submit";
        log.info("appealSubmit,request={}{}", request);
        return call(uri, params, ApiBaseResponse.class);
    }

    /**
     * 发送消息
     */
    public ApiBaseResponse sendAppealMessage(ApiOtcSendAppealMessageRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/appeal/message/send";
        log.info("sendAppealMessage,request={}{}", request);
        return call(uri, params, ApiBaseResponse.class);
    }

    /**
     * 获取申诉消息列表
     */
    public ApiOtcGetAppealItemsResponse getAppealMessageList(ApiOtcGetAppealItemsRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/appeal/message/list";
        log.info("getAppealMessageList,request={}", request);
        return call(uri, params, ApiOtcGetAppealItemsResponse.class);
    }

    /**
     * 申诉详情
     */
    public OrderAppealDetailResponse getAppealDetail(ApiOtcOrderRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/appeal/detail";
        log.info("getAppealDetail:{}", request);
        return call(uri, params, OrderAppealDetailResponse.class);
    }

    /**
     * 申诉处理
     */
    public ApiBaseResponse appealDeal(ApiOtcAppealDealRequest request) {
        ValidatorHelper.validator(request);
        Map<String, String> params = ApiIdentityUtil.toMap(request);
        String uri = "/v1/api/openotc/order/appealDeal";
        log.info("appealDeal:{}", request);
        return call(uri, params, ApiBaseResponse.class);
    }

    private <T> T call(String uri, Map<String, String> params, Class<T> clazz) {
        return baseCall(uri, params).getBody().toJavaObject(clazz);
    }

    /**
     * 详情接口会出现null的情况
     */
    private <T> T callForDetail(String uri, Map<String, String> params, Class<T> clazz) {
        JSON jsonBody = baseCall(uri, params).getBody();
        if (jsonBody == null) {
            return null;
        }
        return jsonBody.toJavaObject(clazz);
    }

    private <T> List<T> callForList(String uri, Map<String, String> params, Class<T> clazz) {
        return ((JSONArray)baseCall(uri, params).getBody()).toJavaList(clazz);
    }

    private <T> T call(String uri, Map<String, String> params, TypeReference<T> typeReference) {
        return JSON.parseObject(baseCall(uri, params).getBody().toJSONString(), typeReference);
    }

    /**
     * 生成签名的url并发起请求
     * @param uri 接口uri
     * @param params 参数
     * @return
     */
    private JsonResult<JSON> baseCall(String uri, Map<String, String> params) {
        ApiIdentityUtil.sign(ACCESS_KEY_ID, ACCESS_KEY_SECRET, "1", ApiIdentityUtil.gmtNow(), HttpPost.METHOD_NAME,
            HOST, uri, params);
        String json = JSON.toJSONString(params);
        log.info("requestData={}", json);
        HttpPost httpPost = new HttpPost(DOMAIN + uri);
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        String responseData;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();
            responseData = IOUtils.toString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApiOtcException("HttpClient IOException");
        }
        log.info("responseData={}", responseData);
        JsonResult<JSON> jsonResult = JSON.parseObject(responseData, new TypeReference<JsonResult<JSON>>() {});
        if(200 !=jsonResult.getCode()){
            throw new ApiOtcException("call otc service error");
        }
        return jsonResult;
    }

    /**
     * 获取host (签名需要)
     * @param otcDomain
     * @return
     */
    private String getHost(String otcDomain) {
        String urlPattern = "^((https|http)://)"
            + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
            + "|"
            + "([0-9a-z_!~*'()-]+\\.)*"
            + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
            + "[a-z]{2,6})"
            + "(:[0-9]{1,5})?";
        boolean matches = Pattern.compile(urlPattern).matcher(otcDomain).matches();
        if (!matches) {
            throw new InvalidParameterException("otcDomain");
        }
        String[] split = otcDomain.split("//");
        return split[1];
    }


}
