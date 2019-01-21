package com.bestotc.domain.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取账户信息
 */
@Getter
@Setter
@ToString
public class ApiOtcAccountResponse {

    /**
     * 各币种资产列表
     */
    private List<ApiOtcAccountAsset> assetList;

    @Getter
    @Setter
    @ToString
    public static class ApiOtcAccountAsset {

        /**
         * 币种 如 usdt
         */
        private String variety;
        /**
         * 余额
         */
        private String balance;

        /**
         * 可用
         */
        private String available;

        /**
         * 冻结
         */
        private String freeze;

    }

}
