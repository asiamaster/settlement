package com.dili.settlement.service;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.ui.ModelMap;

/**
 * 用于处理支付的service接口
 */
public interface PayService {

    /**
     * 选择支付方式后页面变动
     * @return
     */
    String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap);

    /**
     * 通用验证参数
     * @param settleOrderDto
     */
    void validParams(SettleOrderDto settleOrderDto);

    /**
     * 个性化验证参数
     * @param settleOrderDto
     */
    void validParamsSpecial(SettleOrderDto settleOrderDto);

    /**
     * 支付
     * @param settleOrderDto
     * @return
     */
    BaseOutput<SettleResultDto> pay(SettleOrderDto settleOrderDto);

    /**
     * 支持哪种支付类型
     * @return
     */
    Integer support();
}
