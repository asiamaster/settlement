package com.dili.settlement.service.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.service.RefundService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 退款接口基础实现类
 */
public abstract class RefundServiceImpl implements RefundService {

    @Resource
    protected SettleRpc settleRpc;

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        if (!StrUtil.isBlank(settleOrderDto.getIds())) {
            settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            BaseOutput<SettleOrder> baseOutput = settleRpc.getById(settleOrderDto.getIdList().get(0));
            modelMap.addAttribute("settleOrder", baseOutput.getData());
        }
        return "refund/special";
    }

    @Override
    public void validParameters(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getIds())) {
            throw new BusinessException("", "ID列表为空");
        }
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        validParametersSpecial(settleOrderDto);
    }

    @Override
    public void validParametersSpecial(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getAccountNumber())) {
            throw new BusinessException("", "银行卡号为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getBankName())) {
            throw new BusinessException("", "银行名称为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getBankCardHolder())) {
            throw new BusinessException("", "银行卡主为空");
        }
        //根据PRD暂时屏蔽流水号验证
        /*if (StrUtil.isBlank(settleOrderDto.getSerialNumber())) {
            throw new BusinessException("", "流水号为空");
        }*/
    }

    @Override
    public BaseOutput<SettleResultDto> refund(SettleOrderDto settleOrderDto) {
        validParameters(settleOrderDto);
        settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        return settleRpc.refund(settleOrderDto);
    }

    @Override
    public abstract Integer support();
}
