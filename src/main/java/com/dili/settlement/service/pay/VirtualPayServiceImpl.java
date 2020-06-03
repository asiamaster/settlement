package com.dili.settlement.service.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.PayService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 支付宝支付方式
 */
@Service
public class VirtualPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special_virtual";
    }

    @Override
    public Integer support() {
        return SettleWayEnum.VIRTUAL_PAY.getCode();
    }
}
