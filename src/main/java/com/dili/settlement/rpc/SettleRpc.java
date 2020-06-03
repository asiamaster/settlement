package com.dili.settlement.rpc;

import com.dili.settlement.domain.*;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用于结算的rpc
 */

@FeignClient(name = "settlement-service", url = "http://10.28.1.8:8184")
public interface SettleRpc {

    /**
     * 根据ID查询结算单
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/getById", method = RequestMethod.GET)
    BaseOutput<SettleOrder> getById(@RequestParam Long id);

    /**
     * 查询结算单
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/list", method = RequestMethod.POST)
    BaseOutput<List<SettleOrder>> list(SettleOrderDto settleOrderDto);

    /**
     * 分页查询结算单
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/listPage", method = RequestMethod.POST)
    PageOutput<List<SettleOrder>> listPage(SettleOrderDto settleOrderDto);

    /**
     * 支付服务调用
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/pay", method = RequestMethod.POST)
    BaseOutput<SettleResultDto> pay(SettleOrderDto settleOrderDto);

    /**
     * 退款服务调用
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/refund", method = RequestMethod.POST)
    BaseOutput<SettleResultDto> refund(SettleOrderDto settleOrderDto);

    /**
     * 根据id列表查询总额
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/queryTotalAmount", method = RequestMethod.POST)
    BaseOutput<Long> queryTotalAmount(SettleOrderDto settleOrderDto);

    /**
     * 查询应用配置列表 appId 组编码
     * @param applicationConfig
     * @return
     */
    @RequestMapping(value = "/api/applicationConfig/list", method = RequestMethod.POST)
    BaseOutput<List<ApplicationConfig>> listAppConfig(ApplicationConfigDto applicationConfig);

    /**
     * 查询应用配置值 应用ID 组编码   编码
     * @param applicationConfig
     * @return
     */
    @RequestMapping(value = "/api/applicationConfig/getVal", method = RequestMethod.POST)
    BaseOutput<String> getAppConfigVal(ApplicationConfigDto applicationConfig);

    /**
     * 查询结算配置列表
     * @param settleConfig
     * @return
     */
    @RequestMapping(value = "/api/settleConfig/list", method = RequestMethod.POST)
    BaseOutput<List<SettleConfig>> listSettleConfig(SettleConfig settleConfig);

    /**
     * 查询结算配置值 应用ID 组编码   编码
     * @param settleConfig
     * @return
     */
    @RequestMapping(value = "/api/settleConfig/getVal", method = RequestMethod.POST)
    BaseOutput<String> getSettleConfigVal(SettleConfig settleConfig);

    /**
     * 查询接入系统列表
     * @param marketApplication
     * @return
     */
    @RequestMapping(value = "/api/marketApplication/list", method = RequestMethod.POST)
    BaseOutput<List<MarketApplication>> listMarketApplication(MarketApplication marketApplication);

    /**
     * 执行结算回调
     */
    @RequestMapping(value = "/api/retryRecord/executeCallback")
    void executeCallback();

    /**
     * code 根据结算单号查询结算明细列表
     * @return
     */
    @RequestMapping(value = "/api/settleWayDetail/listByCode")
    BaseOutput<List<SettleWayDetail>> listByCode(@RequestParam String code);
}
