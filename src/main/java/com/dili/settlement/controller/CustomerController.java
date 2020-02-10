package com.dili.settlement.controller;

import com.dili.settlement.dto.Customer;
import com.dili.settlement.dto.CustomerQuery;
import com.dili.settlement.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
    public BaseOutput<List<Customer>> list(@RequestBody CustomerQuery query) {
        try {
            return customerRpc.list(query);
        } catch (Exception e) {
            LOGGER.error("method", e);
            return BaseOutput.failure();
        }
    }
}
