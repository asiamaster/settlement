package com.dili.settlement.controller;

import com.dili.settlement.dto.Customer;
import com.dili.settlement.dto.CustomerQuery;
import com.dili.settlement.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户相关controller
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Resource
    private CustomerRpc customerRpc;

    /**
     * 查询客户列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list.action")
    @ResponseBody
    public BaseOutput<List<Customer>> list(CustomerQuery query) {
        try {
            UserTicket userTicket = getUserTicket();
            query.setMarketId(userTicket.getFirmId());
            return customerRpc.list(query);
        } catch (Exception e) {
            LOGGER.error("method", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    private UserTicket getUserTicket() {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return userTicket != null ? userTicket : DTOUtils.newInstance(UserTicket.class);
    }
}
