package com.dili.settlement.service.refund;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.RefundService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 现金退款
 */
@Service
public class CashRefundServiceImpl extends RefundServiceImpl implements RefundService {

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special_cash";
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
