package com.dili.settlement.api;

import com.dili.settlement.rpc.SettleRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务api
 */
@RestController
@RequestMapping(value = "/api/task")
public class TaskApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskApi.class);

    @Resource
    private SettleRpc settleRpc;

    /**
     * 执行结算回调
     */
    @RequestMapping(value = "/executeCallback")
    public void executeCallback() {
        try {
            settleRpc.executeCallback();
        } catch (Exception e) {
            LOGGER.error("method executeCallback", e);
        }
    }
}
