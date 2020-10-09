package com.dili.settlement.service.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.service.RefundService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 园区卡退款处理
 */
@Service
public class CardRefundServiceImpl extends RefundServiceImpl implements RefundService {


    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        if (!StrUtil.isBlank(settleOrderDto.getIds())) {
            settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            BaseOutput<SettleOrder> baseOutput = settleRpc.getById(settleOrderDto.getIdList().get(0));
            modelMap.addAttribute("settleOrder", baseOutput.getData());
        }
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
