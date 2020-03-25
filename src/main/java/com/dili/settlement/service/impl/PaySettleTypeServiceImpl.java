package com.dili.settlement.service.impl;

import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.enums.AppGroupCodeEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.service.SettleTypeService;
import org.springframework.stereotype.Service;

/**
 * 缴费类型处理类
 */
@Service
public class PaySettleTypeServiceImpl extends SettleTypeServiceImpl implements SettleTypeService {
    @Override
    public void buildDetailParams(ApplicationConfigDto query) {
        query.setGroupCode(AppGroupCodeEnum.APP_BUSINESS_URL_PAY_DETAIL.getCode());
    }

    @Override
    public void buildPrintParams(ApplicationConfigDto query) {
        query.setGroupCode(AppGroupCodeEnum.APP_BUSINESS_URL_PAY_PRINT.getCode());
    }

    @Override
    public Integer support() {
        return SettleTypeEnum.PAY.getCode();
    }
}
