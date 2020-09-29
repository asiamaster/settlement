package com.dili.settlement.service.pay;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.PayService;
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
    public Integer support() {
        return SettleWayEnum.CARD.getCode();
    }
}
