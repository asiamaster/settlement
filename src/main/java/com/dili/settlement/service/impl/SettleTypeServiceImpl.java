package com.dili.settlement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.rpc.resolver.GenericRpcResolver;
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
        if (StrUtil.isBlank(query.getOrderCode())) {
            throw new BusinessException("", "订单号为空");
        }
        if (query.getReverse() == null) {
            throw new BusinessException("", "红冲标记为空");
        }
        if (Integer.valueOf(ReverseEnum.YES.getCode()).equals(query.getReverse())) {
            //按照现有逻辑，如果是红冲单则order_code字段存储的是结算单号，所以通过获取原单来order_code值来构建查看详情
            SettleOrder settleOrder = GenericRpcResolver.resolver(settleRpc.getByCode(query.getOrderCode()), "settlement-service");
            query.setOrderCode(settleOrder.getOrderCode());
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
        if (StrUtil.isBlank(query.getOrderCode())) {
            throw new BusinessException("", "订单号为空");
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
        builder.append("?businessType=").append(query.getBusinessType())
                .append("&orderCode=").append(query.getOrderCode());
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
                .append("&orderCode=").append(query.getOrderCode())
                .append("&reprint=").append(query.getReprint());
        return builder.toString();
    }

}
