package com.dili.settlement.controller;

import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.settlement.dto.CustomerDto;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 客户相关controller
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController implements IBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    //客户姓名查询
    private static final String QUERY_TYPE_1 = "1";
    //客户证件号查询
    private static final String QUERY_TYPE_2 = "2";
    //客户卡号查询
    private static final String QUERY_TYPE_3 = "3";

    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private AccountQueryRpc accountQueryRpc;

    /**
     * 查询客户列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/list.action")
    @ResponseBody
    public BaseOutput<List<Customer>> list(CustomerDto param) {
        try {
            UserTicket userTicket = getUserTicket();
            CustomerQueryInput query = createQuery(param, userTicket);
            query.setMarketId(userTicket.getFirmId());
            return customerRpc.list(query);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("method", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 构建客户查询条件
     * @param param
     * @return
     */
    private CustomerQueryInput createQuery(CustomerDto param, UserTicket userTicket) {
        CustomerQueryInput query = new CustomerQueryInput();
        if (QUERY_TYPE_1.equals(param.getQueryType())) {
            query.setName(param.getKeyword());
            return query;
        }
        if (QUERY_TYPE_2.equals(param.getQueryType())) {
            query.setCertificateNumber(param.getKeyword());
            return query;
        }
        if (QUERY_TYPE_3.equals(param.getQueryType())) {
            UserAccountSingleQueryDto userAccountSingleQueryDto = new UserAccountSingleQueryDto();
            userAccountSingleQueryDto.setCardNo(param.getKeyword());
            userAccountSingleQueryDto.setFirmId(userTicket.getFirmId());
            BaseOutput<UserAccountCardResponseDto> baseOutput = accountQueryRpc.findSingle(userAccountSingleQueryDto);
            if (!baseOutput.isSuccess()) {
                throw new BusinessException("", baseOutput.getMessage());
            }
            query.setId(baseOutput.getData().getCustomerId());
            return query;
        }
        throw new BusinessException("", "不支持该查询方式");
    }

}
