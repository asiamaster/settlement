package com.dili.settlement.service.refund;

import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.RefundService;
import org.springframework.stereotype.Service;

/**
 * 银行卡退款处理
 */
@Service
public class BankRefundServiceImpl extends RefundServiceImpl implements RefundService {
    @Override
    public Integer support() {
        return SettleWayEnum.BANK.getCode();
    }
}
