package com.dili.settlement.rpc;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用于结算的rpc
 */

@FeignClient(name = "settlement-service")
public interface SettleRpc {

    /**
     * 支付服务调用
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/pay", method = RequestMethod.POST)
    BaseOutput<SettleResultDto> pay(SettleOrderDto settleOrderDto);

    /**
     *
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/api/settleOrder/refund", method = RequestMethod.POST)
    BaseOutput<SettleResultDto> refund(SettleOrderDto settleOrderDto);
}
