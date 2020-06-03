package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 结算方式明细
 */
@Controller
@RequestMapping(value = "/settleWayDetail")
public class SettleWayDetailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleWayDetailController.class);

    @Resource
    private SettleRpc settleRpc;
    /**
     * 根绝结算单编号查询结算方式明细列表
     * @param code
     * @return
     */
    @RequestMapping(value = "/listByCode.action")
    @ResponseBody
    public BaseOutput<List<SettleWayDetail>> listByCode(String code) {
        try {
            if (StrUtil.isBlank(code)) {
                return BaseOutput.failure("结算单号为空");
            }
            return settleRpc.listByCode(code);
        } catch (Exception e) {
            LOGGER.error("listByCode", e);
            return BaseOutput.failure();
        }
    }
}
