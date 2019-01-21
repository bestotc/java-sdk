package com.bestotc.service

import com.bestotc.domain.ImNotifyMessage
import com.google.common.base.Function
import spock.lang.Specification
/**
 * IM回调测试
 * @author qxx on 2019/1/4.
 */
class ApiImOrderNotifyServiceTest extends Specification {

    private static ApiImNotifyService imNotifyMessage
    //im回调的字符串
    private static String messgeStr="""{"orderNo": "201810291009150NOLpBHPBkwQqhfX16","messageType": "0","fromType": 2,"content": "已确认放行","attach": "http://www.sample.com/image/2019","timestamp": 1541067368}"""

    void setup() {
        imNotifyMessage = new ApiImNotifyService()
    }

    def "ImNotifyHandel"() {

        def function = new Function<ImNotifyMessage, Boolean>(){
            @Override
            Boolean apply(ImNotifyMessage imNotifyMessage) {
                println imNotifyMessage
                return Boolean.TRUE
            }
        }

        imNotifyMessage.imNotifyHandel(messgeStr, function)

        expect: true
    }
}
