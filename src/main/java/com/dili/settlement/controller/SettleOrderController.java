package com.dili.settlement.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */

@Controller
@RequestMapping("/settleOrder")
public class SettleOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderController.class);

    @Resource
    private SettleRpc settleRpc;

    /**
     * 跳转到支付页面
     * @return
     */
    @RequestMapping(value = "/forwardPayIndex.html")
    public String forwardPayIndex() {
        return "pay/index";
    }

    /**
     * 跳转到退款页面
     * @return
     */
    @RequestMapping(value = "/forwardRefundIndex.html")
    public String forwardRefundIndex() {
        return "refund/index";
    }

    /**
     * 页面支付接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/pay.action")
    @ResponseBody
    public BaseOutput<SettleResultDto> pay(SettleOrderDto settleOrderDto) {
        try {
            validPayParameters(settleOrderDto);
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setOperatorId(userTicket.getId());
            settleOrderDto.setOperatorName(userTicket.getRealName());
            return settleRpc.pay(settleOrderDto);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method pay", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 页面退款接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/refund.action")
    @ResponseBody
    public BaseOutput<SettleResultDto> refund(SettleOrderDto settleOrderDto) {
        try {
            validRefundParameters(settleOrderDto);
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setOperatorId(userTicket.getId());
            settleOrderDto.setOperatorName(userTicket.getRealName());
            return settleRpc.refund(settleOrderDto);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method refund", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 验证支付参数
     * @param settleOrderDto
     */
    private void validPayParameters(SettleOrderDto settleOrderDto) {
        if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
            throw new BusinessException("", "ID列表为空");
        }
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        SettleWayEnum settleWayEnum = SettleWayEnum.getByCode(settleOrderDto.getWay());
        switch (settleWayEnum) {
            case CASH : break;
            case POS:
            case BANK:
            case ALI_PAY:
            case WECHAT_PAY:
                if (StrUtil.isBlank(settleOrderDto.getSerialNumber())) {
                    throw new BusinessException("", "流水号为空");
                }
                break;
            default: throw new BusinessException("", "不支持该方式");
        }
    }

    /**
     * 验证退款参数
     * @param settleOrderDto
     */
    private void validRefundParameters(SettleOrderDto settleOrderDto) {
        if (CollUtil.isEmpty(settleOrderDto.getIdList())) {
            throw new BusinessException("", "ID列表为空");
        }
        if (settleOrderDto.getWay() == null) {
            throw new BusinessException("", "结算方式为空");
        }
        SettleWayEnum settleWayEnum = SettleWayEnum.getByCode(settleOrderDto.getWay());
        switch (settleWayEnum) {
            case CASH : break;
            case BANK:
                if (StrUtil.isBlank(settleOrderDto.getAccountNumber())) {
                    throw new BusinessException("", "银行卡号为空");
                }
                if (StrUtil.isBlank(settleOrderDto.getBankName())) {
                    throw new BusinessException("", "银行名称为空");
                }
                if (StrUtil.isBlank(settleOrderDto.getBankCardHolder())) {
                    throw new BusinessException("", "银行卡主为空");
                }
                if (StrUtil.isBlank(settleOrderDto.getSerialNumber())) {
                    throw new BusinessException("", "流水号为空");
                }
                break;
            default: throw new BusinessException("", "不支持该方式");
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