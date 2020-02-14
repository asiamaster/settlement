package com.dili.settlement.rpc;

import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.UrlConfig;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 用于结算的rpc
 */

@FeignClient(name = "settlement-service")
public interface SettleRpc {

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
     *根据业务类型 和 路径类型 获取访问路径
     * @param urlConfig
     * @return
     */
    @RequestMapping(value = "/api/urlConfig/getUrl", method = RequestMethod.POST)
    BaseOutput<String> getUrl(UrlConfig urlConfig);

    /**
     * 查询结算配置列表
     * @param settleConfig
     * @return
     */
    @RequestMapping(value = "/api/settleConfig/list", method = RequestMethod.POST)
    BaseOutput<List<SettleConfig>> listSettleConfig(SettleConfig settleConfig);
}
