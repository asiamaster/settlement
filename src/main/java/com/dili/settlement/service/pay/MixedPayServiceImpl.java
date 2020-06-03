package com.dili.settlement.service.pay;

import cn.hutool.core.collection.CollUtil;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.service.PayService;
import com.dili.settlement.service.SettleWayService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.util.List;

/**
 * 支付宝支付方式
 */
@Service
public class MixedPayServiceImpl extends PayServiceImpl implements PayService {

    @Resource
    private SettleWayService settleWayService;

    @Resource
    private SettleRpc settleRpc;

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        List<SettleConfig> wayList = settleWayService.payFormList();
        modelMap.addAttribute("wayList", wayList);
        return "pay/special_mixed";
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getIds().split(",").length > 1) {
            throw new BusinessException("", "组合结算方式仅支持单条记录");
        }
        if (settleOrderDto.getTotalAmount() == null) {
            throw new BusinessException("", "收款金额为空");
        }
        if (CollUtil.isEmpty(settleOrderDto.getSettleWayDetailList())) {
            throw new BusinessException("", "组合结算方式列表为空");
        }
        long tempAmount = 0;
        for (SettleWayDetail detail : settleOrderDto.getSettleWayDetailList()) {
            if (detail.getAmount() == null || detail.getAmount() < 0) {
                throw new BusinessException("", "组合结算方式金额错误");
            }
            tempAmount += detail.getAmount();
        }
        if (!Long.valueOf(tempAmount).equals(settleOrderDto.getTotalAmount())) {
            throw new BusinessException("", "收款金额与结算金额不相等");
        }
    }

    @Override
    public Integer support() {
        return SettleWayEnum.MIXED_PAY.getCode();
    }
}
