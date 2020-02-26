package com.dili.settlement.component;

import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.service.PayService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付相关处理器
 */
@Component
public class PayDispatchHandler {

    @Resource
    private List<PayService> serviceList;
    private Map<Integer, PayService> serviceMap = new ConcurrentHashMap<>();

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        for (PayService service : serviceList) {
            serviceMap.put(service.support(), service);
        }
    }

    /**
     * 根据支付方式返回个性化页面
     * @return
     */
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        PayService service = serviceMap.get(settleOrderDto.getWay());
        if (service == null) {
            return "";
        }
        return service.forwardSpecial(settleOrderDto, modelMap);
    }

    /**
     * 根据支付方式处理支付
     * @param settleOrderDto
     * @return
     */
    public BaseOutput<SettleResultDto> pay(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        PayService service = serviceMap.get(settleOrderDto.getWay());
        if (service == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        return service.pay(settleOrderDto);
    }
}
