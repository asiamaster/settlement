package com.dili.settlement.service.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.PayService;
import org.springframework.stereotype.Service;

/**
 * POS支付方式
 */
@Service
public class POSPayServiceImpl extends PayServiceImpl implements PayService {
    @Override
    public Integer support() {
        return SettleWayEnum.POS.getCode();
    }
}
