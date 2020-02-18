package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.PayDispatchHandler;
import com.dili.settlement.component.RefundDispatchHandler;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.UrlConfig;
import com.dili.settlement.dto.PrintDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.enums.*;
import com.dili.settlement.rpc.BusinessRpc;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Resource
    private PayDispatchHandler payDispatchHandler;

    @Resource
    private RefundDispatchHandler refundDispatchHandler;

    @Resource
    private BusinessRpc businessRpc;

    /**
     * 跳转到支付页面
     * @return
     */
    @RequestMapping(value = "/forwardPayIndex.html")
    public String forwardPayIndex() {
        return "pay/index";
    }

    /**
     * 根据客户id查询缴费单
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/listPayOrders.action")
    @ResponseBody
    public String listPayOrders(Long customerId) {
        try {
            if (customerId == null) {
                return new EasyuiPageOutput(0, new ArrayList<SettleOrder>(0)).toString();
            }
            UserTicket userTicket = getUserTicket();
            SettleOrderDto query = new SettleOrderDto();
            query.setType(SettleTypeEnum.PAY.getCode());
            query.setState(SettleStateEnum.WAIT_DEAL.getCode());
            query.setCustomerId(customerId);
            query.setMarketId(userTicket.getFirmId());
            query.setConvert(true);
            BaseOutput<List<SettleOrder>> baseOutput = settleRpc.list(query);
            if (baseOutput.isSuccess()) {
                return new EasyuiPageOutput(baseOutput.getData().size(), baseOutput.getData()).toString();
            }
            return new EasyuiPageOutput(0, new ArrayList<SettleOrder>(0)).toString();
        } catch (Exception e) {
            LOGGER.error("method listPayOrders", e);
            return new EasyuiPageOutput(0, new ArrayList<SettleOrder>(0)).toString();
        }
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
     * 根据客户id查询退款单
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/listRefundOrders.action")
    @ResponseBody
    public String listRefundOrders(Long customerId) {
        try {
            if (customerId == null) {
                return new EasyuiPageOutput(0, new ArrayList<SettleOrder>(0)).toString();
            }
            UserTicket userTicket = getUserTicket();
            SettleOrderDto query = new SettleOrderDto();
            query.setType(SettleTypeEnum.REFUND.getCode());
            query.setState(SettleStateEnum.WAIT_DEAL.getCode());
            query.setCustomerId(customerId);
            query.setMarketId(userTicket.getFirmId());
            query.setConvert(true);
            BaseOutput<List<SettleOrder>> baseOutput = settleRpc.list(query);
            if (baseOutput.isSuccess()) {
                return new EasyuiPageOutput(baseOutput.getData().size(), baseOutput.getData()).toString();
            }
            return new EasyuiPageOutput(0, new ArrayList<SettleOrder>(0)).toString();
        } catch (Exception e) {
            LOGGER.error("method listRefundOrders", e);
            return new EasyuiPageOutput(0, new ArrayList<SettleOrder>(0)).toString();
        }
    }

    /**
     * 跳转到支付页面
     * @param settleOrderDto
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/forwardPay.html")
    public String forwardPay(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        try {
            if (StrUtil.isBlank(settleOrderDto.getIds())) {
                return "pay/pay";
            }
            settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            BaseOutput<Long> amountBaseOutput = settleRpc.queryTotalAmount(settleOrderDto);
            if (amountBaseOutput.isSuccess()) {
                modelMap.addAttribute("totalAmount", MoneyUtils.centToYuan(amountBaseOutput.getData()));
            }
            UserTicket userTicket = getUserTicket();
            SettleConfig settleConfig = new SettleConfig();
            settleConfig.setMarketId(userTicket.getFirmId());
            settleConfig.setGroupCode(GroupCodeEnum.SETTLE_WAY_PAY.getCode());
            settleConfig.setState(ConfigStateEnum.ENABLE.getCode());
            BaseOutput<List<SettleConfig>> configBaseOutput = settleRpc.listSettleConfig(settleConfig);
            if (configBaseOutput.isSuccess()) {
                modelMap.addAttribute("wayList", configBaseOutput.getData());
            }
            modelMap.addAttribute("ids", settleOrderDto.getIds());
            return "pay/pay";
        } catch (Exception e) {
            LOGGER.error("method forwardPay", e);
        }
        return "pay/pay";
    }

    /**
     * 跳转到支付个性化页面
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/forwardPaySpecial.html")
    public String forwardPaySpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        try {
            return payDispatchHandler.forwardSpecial(settleOrderDto, modelMap);
        } catch (Exception e) {
            LOGGER.error("method forwardPaySpecial", e);
        }
        return "";
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
            if (settleOrderDto.getWay() == null) {
                return BaseOutput.failure("结算方式为空");
            }
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setOperatorId(userTicket.getId());
            settleOrderDto.setOperatorName(userTicket.getRealName());
            return payDispatchHandler.pay(settleOrderDto);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method pay", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 跳转到退款页面
     * @param settleOrderDto
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/forwardRefund.html")
    public String forwardRefund(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        try {
            if (StrUtil.isBlank(settleOrderDto.getIds())) {
                return "refund/refund";
            }
            settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            BaseOutput<Long> amountBaseOutput = settleRpc.queryTotalAmount(settleOrderDto);
            if (amountBaseOutput.isSuccess()) {
                modelMap.addAttribute("totalAmount", MoneyUtils.centToYuan(amountBaseOutput.getData()));
            }
            UserTicket userTicket = getUserTicket();
            SettleConfig settleConfig = new SettleConfig();
            settleConfig.setMarketId(userTicket.getFirmId());
            settleConfig.setGroupCode(GroupCodeEnum.SETTLE_WAY_REFUND.getCode());
            settleConfig.setState(ConfigStateEnum.ENABLE.getCode());
            BaseOutput<List<SettleConfig>> configBaseOutput = settleRpc.listSettleConfig(settleConfig);
            if (configBaseOutput.isSuccess()) {
                modelMap.addAttribute("wayList", configBaseOutput.getData());
            }
            BaseOutput<SettleOrder> settleOrderBaseOutput = settleRpc.getById(settleOrderDto.getIdList().get(0));
            if (settleOrderBaseOutput.isSuccess()){
                modelMap.addAttribute("settleOrder", settleOrderBaseOutput.getData());
            }
            modelMap.addAttribute("ids", settleOrderDto.getIds());
            return "refund/refund";
        } catch (Exception e) {
            LOGGER.error("method forwardRefund", e);
        }
        return "refund/refund";
    }

    /**
     * 跳转到退款个性化页面
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/forwardRefundSpecial.html")
    public String forwardRefundSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        try {
            return refundDispatchHandler.forwardSpecial(settleOrderDto, modelMap);
        } catch (Exception e) {
            LOGGER.error("method forwardRefundSpecial", e);
        }
        return "";
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
            if (settleOrderDto.getWay() == null) {
                return BaseOutput.failure("结算方式为空");
            }
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setOperatorId(userTicket.getId());
            settleOrderDto.setOperatorName(userTicket.getRealName());
            return refundDispatchHandler.refund(settleOrderDto);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method refund", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 加载业务打印数据
     * @param businessType
     * @param businessCode
     * @param reprint
     * @return
     */
    @RequestMapping(value = "/loadPrintData.action")
    public BaseOutput<PrintDto> loadPrintData(Integer businessType, String businessCode, Integer reprint) {
        try {
            if (businessType == null) {
                return BaseOutput.failure("业务类型为空");
            }
            if (StrUtil.isBlank(businessCode)) {
                return BaseOutput.failure("业务单号为空");
            }
            UrlConfig query = new UrlConfig();
            query.setBusinessType(businessType);
            query.setType(UrlTypeEnum.PRINT_DATA.getCode());
            BaseOutput<String> baseOutput = settleRpc.getUrl(query);
            if (!baseOutput.isSuccess()) {
                return BaseOutput.failure(baseOutput.getMessage());
            }
            StringBuilder builder = new StringBuilder(StrUtil.isBlank(baseOutput.getData()) ? "" : baseOutput.getData());
            builder.append("?businessType=").append(businessType)
                    .append("&businessCode=").append(businessCode)
                    .append("&reprint=").append(reprint);
            return businessRpc.loadPrintData(builder.toString());
        } catch (BusinessException e) {
            LOGGER.error("method loadPrintData", e.getErrorMsg());
            return BaseOutput.failure(e.getErrorMsg());
        } catch (Exception e) {
            LOGGER.error("method loadPrintData", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 跳转到结算单列表页面
     * @return
     */
    @RequestMapping(value = "/forwardList.html")
    public String forwardList() {
        return "settleOrder/list";
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