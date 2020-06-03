package com.dili.settlement.service;

import com.dili.settlement.domain.SettleConfig;

import java.util.List;

/**
 * 加载结算方式service接口
 */
public interface SettleWayService {

    /**
     * 查询结算方式 仅用于选择项
     * @param multi
     * @return
     */
    List<SettleConfig> payChooseList(boolean multi);

    /**
     * 查询结算方式 仅用于组合支付表单项
     * @return
     */
    List<SettleConfig> payFormList();
}
