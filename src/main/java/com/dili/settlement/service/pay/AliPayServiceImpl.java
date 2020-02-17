package com.dili.settlement.service.pay;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.PayService;
import org.springframework.stereotype.Service;

/**
 * 支付宝支付方式
 */
@Service
public class AliPayServiceImpl extends PayServiceImpl implements PayService {

    @Override
    public Integer support() {
        return SettleWayEnum.ALI_PAY.getCode();
    }
}
