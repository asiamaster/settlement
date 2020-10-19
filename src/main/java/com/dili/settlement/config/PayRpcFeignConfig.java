package com.dili.settlement.config;

import com.dili.settlement.component.FirmIdHolder;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * 支付服务的Feign配置
 * ps：不要在该类上面加诸如 @Configuration，否则会变成全局配置
 * @Date: 2020/6/29 17:01
 */
public class PayRpcFeignConfig {

    private static final String APPID = "1030";
    private static final String TOKEN = "abcd1030";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("appid", APPID);
            template.header("token", TOKEN);
            Long firmId = FirmIdHolder.get();
            if (firmId == null) {
                return;
            }
            template.header("mchid", String.valueOf(firmId));
        };
    }
}
