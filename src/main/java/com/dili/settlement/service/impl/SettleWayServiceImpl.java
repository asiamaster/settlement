package com.dili.settlement.service.impl;

import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.enums.ConfigStateEnum;
import com.dili.settlement.enums.SettleGroupCodeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.service.SettleWayService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 结算方式相关实现类
 */
@Service
public class SettleWayServiceImpl implements SettleWayService {

    @Resource
    private SettleRpc settleRpc;

    @Override
    public List<SettleConfig> payChooseList(boolean multi) {
        List<SettleConfig> itemList = list(SettleGroupCodeEnum.SETTLE_WAY_PAY.getCode());
        if (multi) {
            itemList.removeIf(settleConfig -> settleConfig.getCode().equals(SettleWayEnum.MIXED_PAY.getCode()));
        }
        return itemList;
    }

    @Override
    public List<SettleConfig> payFormList() {
        List<SettleConfig> itemList = list(SettleGroupCodeEnum.SETTLE_WAY_PAY.getCode());
        itemList.removeIf(settleConfig -> settleConfig.getCode().equals(SettleWayEnum.MIXED_PAY.getCode()) || settleConfig.getCode().equals(SettleWayEnum.VIRTUAL_PAY.getCode()));
        return itemList;
    }

    /**
     * 公共查询方法
     * @return
     */
    private List<SettleConfig> list(int groupCode) {
        SettleConfig settleConfig = new SettleConfig();
        settleConfig.setGroupCode(groupCode);
        settleConfig.setState(ConfigStateEnum.ENABLE.getCode());
        BaseOutput<List<SettleConfig>> configBaseOutput = settleRpc.listSettleConfig(settleConfig);
        if (!configBaseOutput.isSuccess()) {
            return new ArrayList<>(0);
        }
        return configBaseOutput.getData();
    }
}
