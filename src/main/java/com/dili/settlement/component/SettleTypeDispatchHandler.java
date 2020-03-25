package com.dili.settlement.component;

import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.service.SettleTypeService;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结算类型相关处理器
 */
@Component
public class SettleTypeDispatchHandler {

    @Resource
    private List<SettleTypeService> serviceList;
    private Map<Integer, SettleTypeService> serviceMap = new ConcurrentHashMap<>();

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        for (SettleTypeService service : serviceList) {
            serviceMap.put(service.support(), service);
        }
    }

    /**
     * 获取详情页面
     * @return
     */
    public String getDetailUrl(ApplicationConfigDto query) {
        if (query.getSettleType() == null) {
            throw new BusinessException("", "结算类型为空");
        }
        SettleTypeService service = serviceMap.get(query.getSettleType());
        if (service == null) {
            return "";
        }
        return service.getDetailUrl(query);
    }

    /**
     * 获取打印数据url
     * @param query
     * @return
     */
    public String getPrintUrl(ApplicationConfigDto query) {
        if (query.getSettleType() == null) {
            throw new BusinessException("", "结算类型为空");
        }
        SettleTypeService service = serviceMap.get(query.getSettleType());
        if (service == null) {
            throw new BusinessException("", "不支持该结算方式");
        }
        return service.getPrintUrl(query);
    }
}
