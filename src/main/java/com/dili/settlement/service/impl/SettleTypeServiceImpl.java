package com.dili.settlement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.service.SettleTypeService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

import javax.annotation.Resource;

/**
 * 结算类型基础实现类
 */
public abstract class SettleTypeServiceImpl implements SettleTypeService {

    @Resource
    protected SettleRpc settleRpc;

    @Override
    public void validDetailParams(ApplicationConfigDto query) {
        if (query.getAppId() == null) {
            throw new BusinessException("", "应用ID为空");
        }
        if (query.getBusinessType() == null) {
            throw new BusinessException("", "业务类型为空");
        }
        if (StrUtil.isBlank(query.getBusinessCode())) {
            throw new BusinessException("", "业务单号为空");
        }
    }

    @Override
    public void validPrintParams(ApplicationConfigDto query) {
        if (query.getAppId() == null) {
            throw new BusinessException("", "应用ID为空");
        }
        if (query.getBusinessType() == null) {
            throw new BusinessException("", "业务类型为空");
        }
        if (StrUtil.isBlank(query.getBusinessCode())) {
            throw new BusinessException("", "业务单号为空");
        }
        if (query.getReprint() == null) {
            throw new BusinessException("", "打印标记为空");
        }
    }

    @Override
    public String getDetailUrl(ApplicationConfigDto query) {
        validDetailParams(query);
        buildDetailParams(query);
        query.setCode(query.getBusinessType());
        BaseOutput<String> baseOutput = settleRpc.getAppConfigVal(query);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", baseOutput.getMessage());
        }
        StringBuilder builder = new StringBuilder(StrUtil.isBlank(baseOutput.getData()) ? "" : baseOutput.getData());
        builder.append("?businessType=").append(query.getBusinessType()).append("&businessCode=").append(query.getBusinessCode());
        return builder.toString();
    }

    @Override
    public String getPrintUrl(ApplicationConfigDto query) {
        validPrintParams(query);
        buildPrintParams(query);
        query.setCode(query.getBusinessType());
        BaseOutput<String> baseOutput = settleRpc.getAppConfigVal(query);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", baseOutput.getMessage());
        }
        StringBuilder builder = new StringBuilder(StrUtil.isBlank(baseOutput.getData()) ? "" : baseOutput.getData());
        builder.append("?businessType=").append(query.getBusinessType())
                .append("&businessCode=").append(query.getBusinessCode())
                .append("&reprint=").append(query.getReprint());
        return builder.toString();
    }

}
