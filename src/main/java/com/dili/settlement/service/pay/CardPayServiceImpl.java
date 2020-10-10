package com.dili.settlement.service.pay;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.PayService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 园区卡支付方式
 */
@Service
public class CardPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special_card";
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
        if (StrUtil.isBlank(settleOrderDto.getTradePassword())) {
            throw new BusinessException("", "交易密码为空");
        }
    }

    @Override
    public Integer support() {
        return SettleWayEnum.CARD.getCode();
    }
}
