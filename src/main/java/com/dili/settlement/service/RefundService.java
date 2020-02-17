package com.dili.settlement.service;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.ui.ModelMap;

/**
 * 用于处理退款的service接口
 */
public interface RefundService {

    /**
     * 选择退款方式后页面变动
     * @return
     */
    String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap);

    /**
     * 通用验证参数
     * @param settleOrderDto
     */
    void validParameters(SettleOrderDto settleOrderDto);

    /**
     * 个性化验证参数
     * @param settleOrderDto
     */
    void validParametersSpecial(SettleOrderDto settleOrderDto);

    /**
     * 退款
     * @param settleOrderDto
     * @return
     */
    BaseOutput<SettleResultDto> refund(SettleOrderDto settleOrderDto);

    /**
     * 支持哪种退款类型
     * @return
     */
    Integer support();
}
