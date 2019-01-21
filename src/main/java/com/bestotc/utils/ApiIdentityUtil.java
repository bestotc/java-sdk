package com.bestotc.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 签名工具类
 * @see <a href="https://github.com/bestotc/APIDOC/wiki/%E7%AD%BE%E5%90%8D%E6%9C%BA%E5%88%B6"
 * target="_top">签名机制</a>
 */
public class ApiIdentityUtil {

    private static final Logger log = LoggerFactory.getLogger(ApiIdentityUtil.class);

    private static class ApiIdentityParams {
        private static final String ACCESS_KEY_ID = "AccessKeyId";
        private static final String SIGNATURE_VERSION = "SignatureVersion";
        private static final String SIGNATURE_METHOD = "SignatureMethod";
        private static final String TIMESTAMP = "Timestamp";
        private static final String SIGNATURE = "Signature";
    }

    public static Object sign(String accessKeyId, String accessKeySecret, String signatureVersion,String timestamp, String method, String host, String uri, Map<String, String> params) {
        params.remove(ApiIdentityParams.SIGNATURE);
        params.put(ApiIdentityParams.ACCESS_KEY_ID, accessKeyId);
        params.put(ApiIdentityParams.SIGNATURE_VERSION, signatureVersion);
        params.put(ApiIdentityParams.SIGNATURE_METHOD, HmacAlgorithms.HMAC_SHA_256.getName());
        params.put(ApiIdentityParams.TIMESTAMP, timestamp);

        TreeSet<String> keys = new TreeSet<>(params.keySet());
        StringBuilder paramsStr = new StringBuilder();
        for (String k : keys) {
            paramsStr.append(k).append('=').append(urlEncode(params.get(k))).append('&');
        }
        String strToSign = StringUtils.join(new String[] {method.toUpperCase(), host.toLowerCase(),uri, paramsStr.deleteCharAt(paramsStr.length() - 1).toString()}, "\n");
        // JDK 1.8 可用如下拉姆达表达式替换上段代码
        //List<String> strings = params.keySet().stream().sorted().map(k -> k + "=" + urlEncode(params.get(k)
        // .toString())).collect(Collectors.toList());//changed!!
        //String strToSign = String.join("\n", method.toUpperCase(), host.toLowerCase(), uri, String.join("&",
        // strings));
        String sign = sign(accessKeySecret, strToSign);
        params.put(ApiIdentityParams.SIGNATURE, sign);
        return sign;
    }

    public static String sign(String accessKeySecret, String strToSign) {
        return Base64.encodeBase64String(new HmacUtils(HmacAlgorithms.HMAC_SHA_256, accessKeySecret).hmac(strToSign));
    }

    public static String gmtNow() {
        //JDK 1.8 可以直接使用Instant
        //return Instant.ofEpochSecond(Instant.now().getEpochSecond()).atZone(ZoneId.of("Z")).format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss"));

        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Z"));
        return format.format(new Date());
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     */
    public static final Map<String, String> toMap(Object bean) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result.toString());
                    }
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();    // failed to call setters
        } catch (IntrospectionException e) {
            e.printStackTrace();    // failed to get class fields
        } catch (IllegalAccessException e) {
            e.printStackTrace();    // failed to instant JavaBean
        }

        return returnMap;
    }

}
