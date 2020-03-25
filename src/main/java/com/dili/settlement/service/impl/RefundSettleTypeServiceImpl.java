package com.dili.settlement.service.impl;

import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.enums.AppGroupCodeEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.service.SettleTypeService;
import org.springframework.stereotype.Service;

/**
 * 退款类型处理类
 */
@Service
public class RefundSettleTypeServiceImpl extends SettleTypeServiceImpl implements SettleTypeService {
    @Override
    public void buildDetailParams(ApplicationConfigDto query) {
        query.setGroupCode(AppGroupCodeEnum.APP_BUSINESS_URL_REFUND_DETAIL.getCode());
    }

    @Override
    public void buildPrintParams(ApplicationConfigDto query) {
        query.setGroupCode(AppGroupCodeEnum.APP_BUSINESS_URL_REFUND_PRINT.getCode());
    }

    @Override
    public Integer support() {
        return SettleTypeEnum.REFUND.getCode();
    }
}
