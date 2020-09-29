package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.ApplicationConfig;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.enums.AppGroupCodeEnum;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 路劲配置url
 */
@Controller
@RequestMapping(value = "/applicationConfig")
public class ApplicationConfigController implements IBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigController.class);

    @Resource
    private SettleRpc settleRpc;

    /**
     * 列出业务类型
     * @param query
     * @return
     */
    @RequestMapping(value = "/listBusinessType.action")
    @ResponseBody
    public BaseOutput<List<ApplicationConfig>> listBusinessType(ApplicationConfigDto query) {
        try {
            if (!StrUtil.isBlank(query.getAppIds())) {
                query.setAppIdList(Stream.of(query.getAppIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            }
            query.setGroupCode(AppGroupCodeEnum.APP_BUSINESS_TYPE.getCode());
            //query.setState(ConfigStateEnum.ENABLE.getCode());
            return settleRpc.listAppConfig(query);
        } catch (Exception e) {
            LOGGER.error("method listBusinessType", e);
            return BaseOutput.failure();
        }
    }
}
