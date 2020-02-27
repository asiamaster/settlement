package com.dili.settlement.service.pay;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.service.PayService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 支付接口基础实现类
 */
public abstract class PayServiceImpl implements PayService {

    @Resource
    protected SettleRpc settleRpc;
    /**
     * 通用验证参数
     * @param settleOrderDto
     */
    @Override
    public void validParameters(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getIds())) {
            throw new BusinessException("", "ID列表为空");
        }
        validParametersSpecial(settleOrderDto);
    }

    /**
     * 个性化验证参数
     * @param settleOrderDto
     */
    @Override
    public void validParametersSpecial(SettleOrderDto settleOrderDto) {
        //根据PRD暂时屏蔽流水号验证
        /*if (StrUtil.isBlank(settleOrderDto.getSerialNumber())) {
            throw new BusinessException("", "流水号为空");
        }*/
    }

    /**
     * 支付
     * @param settleOrderDto
     * @return
     */
    @Override
    public BaseOutput<SettleResultDto> pay(SettleOrderDto settleOrderDto) {
        validParameters(settleOrderDto);
        settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        return settleRpc.pay(settleOrderDto);
    }

    /**
     * 跳转到个性化页面
     * @return
     */
    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "pay/special";
    }

    /**
     * 支持的付款方式
     * @return
     */
    @Override
    public abstract Integer support();
}
