package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.UrlConfig;
import com.dili.settlement.enums.UrlTypeEnum;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 路劲配置url
 */
@Controller
@RequestMapping(value = "/urlConfig")
public class UrlConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlConfigController.class);

    @Resource
    private SettleRpc settleRpc;
    /**
     * 跳转到业务详情页面
     * @param businessType
     * @param businessCode
     * @param response
     */
    @RequestMapping(value = "/showBusinessDetail.html")
    public void showBusinessDetail(Integer businessType, String businessCode, HttpServletResponse response) {
        try {
            UrlConfig query = new UrlConfig();
            query.setBusinessType(businessType);
            query.setType(UrlTypeEnum.DETAIL.getCode());
            BaseOutput<String> baseOutput = settleRpc.getUrl(query);
            if (!baseOutput.isSuccess()) {
                return;
            }
            StringBuilder builder = new StringBuilder(StrUtil.isBlank(baseOutput.getData()) ? "" : baseOutput.getData());
            builder.append("?businessType=").append(businessType).append("&businessCode=").append(businessCode);
            response.setStatus(302);
            response.setHeader("Location", builder.toString());
        } catch (Exception e) {
            LOGGER.error("method showBusinessDetail", e);
        }
    }
}
