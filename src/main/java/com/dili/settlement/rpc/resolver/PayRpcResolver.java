package com.dili.settlement.rpc.resolver;

import com.dili.settlement.dto.pay.*;
import com.dili.settlement.rpc.PayRpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 14:02
 */
@Component("payRpcResolver")
public class PayRpcResolver {

    private static final String PAY_SERVICE_NAME = "pay-service";

    @Autowired
    private PayRpc payRpc;

    /**
     *  提交交易
     * @author miaoguoxin
     * @date 2020/7/1
     */
    public TradeResponseDto trade(TradeRequestDto withdrawRequest) {
        BaseOutput<TradeResponseDto> tradeResponseDtoBaseOutput = payRpc.commitTrade(withdrawRequest);
        return GenericRpcResolver.resolver(tradeResponseDtoBaseOutput, PAY_SERVICE_NAME);
    }

    /**
     * 创建交易
     * @author miaoguoxin
     * @date 2020/7/1
     */
    public CreateTradeResponseDto prePay(CreateTradeRequestDto createTradeRequest) {
        return GenericRpcResolver.resolver(payRpc.preparePay(createTradeRequest), PAY_SERVICE_NAME);
    }

    /**
     *  查询余额
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public BalanceResponseDto findBalanceByFundAccountId(Long fundAccountId) {
        CreateTradeRequestDto requestDto = new CreateTradeRequestDto();
        requestDto.setAccountId(fundAccountId);
        return GenericRpcResolver.resolver(payRpc.getAccountBalance(requestDto), PAY_SERVICE_NAME);
    }
}

