package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.AccountSimpleResponseDto;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 账户查询
 */
@Controller
@RequestMapping(value = "/accountQuery")
public class AccountQueryController implements IBaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountQueryController.class);

    @Autowired
    private AccountQueryRpc accountQueryRpc;

    /**
     * 查询账户信息
     * @param cardQuery
     * @return
     */
    @RequestMapping(value = "/findSingle.action")
    @ResponseBody
    public BaseOutput<UserAccountCardResponseDto> findSingle(UserAccountSingleQueryDto cardQuery) {
        try {
            cardQuery.setFirmId(getUserTicket().getFirmId());
            return accountQueryRpc.findSingle(cardQuery);
        } catch (Exception e) {
            LOGGER.error("findSingle", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 账户信息，包含余额
     * @param cardNo
     * @return
     */
    @RequestMapping(value = "/simpleInfo.action")
    @ResponseBody
    public BaseOutput<AccountSimpleResponseDto> getInfoByCardNo(String cardNo) {
        try {
            if (StrUtil.isBlank(cardNo)) {
                return BaseOutput.failure("园区卡号为空");
            }
            return accountQueryRpc.getInfoByCardNo(cardNo, getUserTicket().getFirmId());
        } catch (Exception e) {
            LOGGER.error("getInfoByCardNo", e);
            return BaseOutput.failure();
        }
    }
}
