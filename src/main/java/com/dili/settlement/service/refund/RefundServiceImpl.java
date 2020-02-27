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
    public void validParams(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getIds())) {
            throw new BusinessException("", "ID列表为空");
        }
        validParamsSpecial(settleOrderDto);
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public BaseOutput<SettleResultDto> refund(SettleOrderDto settleOrderDto) {
        validParams(settleOrderDto);
        settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        return settleRpc.refund(settleOrderDto);
    }

    @Override
    public abstract Integer support();
}
