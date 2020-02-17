package com.dili.settlement.service.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.PayService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 现金支付
 */
@Service
public class CashPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special_cash";
    }

    @Override
    public void validParametersSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public Integer support() {
        return SettleWayEnum.CASH.getCode();
    }
}
