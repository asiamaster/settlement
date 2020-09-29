package com.dili.settlement.service.refund;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.RefundService;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 园区卡退款处理
 */
@Service
public class CardRefundServiceImpl extends RefundServiceImpl implements RefundService {


    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special_card";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {

    }

    @Override
    public Integer support() {
        return SettleWayEnum.CARD.getCode();
    }
}
