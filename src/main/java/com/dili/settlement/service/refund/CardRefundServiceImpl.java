package com.dili.settlement.service.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.RefundService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 园区卡退款处理
 */
@Service
public class CardRefundServiceImpl extends RefundServiceImpl implements RefundService {


    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        if (!StrUtil.isBlank(settleOrderDto.getIds())) {
            settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            BaseOutput<SettleOrder> baseOutput = settleRpc.getById(settleOrderDto.getIdList().get(0));
            modelMap.addAttribute("settleOrder", baseOutput.getData());
        }
        return "refund/special_card";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getTradeCardNo())) {
            throw new BusinessException("", "交易卡号为空");
        }
        if (settleOrderDto.getTradeFundAccountId() == null) {
            throw new BusinessException("", "交易资金账户ID为空");
        }
        if (settleOrderDto.getTradeAccountId() == null) {
            throw new BusinessException("", "交易账户ID为空");
        }
        if (settleOrderDto.getTradeCustomerId() == null) {
            throw new BusinessException("", "交易客户ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getTradeCustomerName())) {
            throw new BusinessException("", "交易客户姓名为空");
        }
    }

    @Override
    public Integer support() {
        return SettleWayEnum.CARD.getCode();
    }
}
