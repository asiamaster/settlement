package com.dili.settlement.rpc;

import com.dili.settlement.dto.Customer;
import com.dili.settlement.dto.CustomerQuery;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/1/21 14:09
 */
@FeignClient(name = "customer-service")
public interface CustomerRpc {
    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @RequestMapping(value = "/api/customer/list", method = RequestMethod.POST)
    BaseOutput<List<Customer>> list(CustomerQuery customer);

}
