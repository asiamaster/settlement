package com.dili.settlement.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.PayDispatchHandler;
import com.dili.settlement.component.RefundDispatchHandler;
import com.dili.settlement.component.SettleTypeDispatchHandler;
import com.dili.settlement.component.TokenHandler;
import com.dili.settlement.domain.MarketApplication;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.ApplicationConfigDto;
import com.dili.settlement.dto.PrintDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.SettleResultDto;
import com.dili.settlement.enums.ConfigStateEnum;
import com.dili.settlement.enums.SettleGroupCodeEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.rpc.BusinessRpc;
import com.dili.settlement.rpc.SettleRpc;
import com.dili.settlement.service.SettleWayService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
public class SettleOrderController implements IBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderController.class);

    @Resource
    private SettleRpc settleRpc;

    @Resource
    private PayDispatchHandler payDispatchHandler;

    @Resource
    private RefundDispatchHandler refundDispatchHandler;

    @Resource
    private BusinessRpc businessRpc;

    @Resource
    private TokenHandler tokenHandler;

    @Resource
    private SettleTypeDispatchHandler settleTypeDispatchHandler;

    @Resource
    private SettleWayService settleWayService;

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
                return new EasyuiPageOutput(0L, new ArrayList<SettleOrder>(0)).toString();
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
                return new EasyuiPageOutput((long) baseOutput.getData().size(), baseOutput.getData()).toString();
            }
            return new EasyuiPageOutput(0L, new ArrayList<SettleOrder>(0)).toString();
        } catch (Exception e) {
            LOGGER.error("method listPayOrders", e);
            return new EasyuiPageOutput(0L, new ArrayList<SettleOrder>(0)).toString();
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
                return new EasyuiPageOutput(0L, new ArrayList<SettleOrder>(0)).toString();
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
                return new EasyuiPageOutput((long)baseOutput.getData().size(), baseOutput.getData()).toString();
            }
            return new EasyuiPageOutput(0L, new ArrayList<SettleOrder>(0)).toString();
        } catch (Exception e) {
            LOGGER.error("method listRefundOrders", e);
            return new EasyuiPageOutput(0L, new ArrayList<SettleOrder>(0)).toString();
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
                modelMap.addAttribute("totalAmount", amountBaseOutput.getData());
                modelMap.addAttribute("totalAmountView", MoneyUtils.centToYuan(amountBaseOutput.getData()));
            }
            UserTicket userTicket = getUserTicket();
            List<SettleConfig> wayList = settleWayService.payChooseList(userTicket.getFirmId(), settleOrderDto.getIdList().size() > 1);
            modelMap.addAttribute("wayList", wayList);
            modelMap.addAttribute("token", tokenHandler.generate(createTokenStr(userTicket, settleOrderDto)));
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
            settleOrderDto.setMarketId(getUserTicket().getFirmId());
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
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setOperatorId(userTicket.getId());
            settleOrderDto.setOperatorName(userTicket.getRealName());
            if (!tokenHandler.valid(createTokenStr(userTicket, settleOrderDto), settleOrderDto.getToken())) {
                return BaseOutput.failure("非法请求");
            }
            return payDispatchHandler.pay(settleOrderDto);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
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
            settleConfig.setGroupCode(SettleGroupCodeEnum.SETTLE_WAY_REFUND.getCode());
            settleConfig.setState(ConfigStateEnum.ENABLE.getCode());
            BaseOutput<List<SettleConfig>> configBaseOutput = settleRpc.listSettleConfig(settleConfig);
            if (configBaseOutput.isSuccess()) {
                modelMap.addAttribute("wayList", configBaseOutput.getData());
            }
            BaseOutput<SettleOrder> settleOrderBaseOutput = settleRpc.getById(settleOrderDto.getIdList().get(0));
            if (settleOrderBaseOutput.isSuccess()){
                modelMap.addAttribute("settleOrder", settleOrderBaseOutput.getData());
            }
            modelMap.addAttribute("token", tokenHandler.generate(createTokenStr(userTicket, settleOrderDto)));
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
            settleOrderDto.setMarketId(getUserTicket().getFirmId());
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
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setOperatorId(userTicket.getId());
            settleOrderDto.setOperatorName(userTicket.getRealName());
            if (!tokenHandler.valid(createTokenStr(userTicket, settleOrderDto), settleOrderDto.getToken())) {
                return BaseOutput.failure("非法请求");
            }
            return refundDispatchHandler.refund(settleOrderDto);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("method refund", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 跳转到业务详情页面
     * @param response
     */
    @RequestMapping(value = "/showDetail.html")
    public void showDetail(ApplicationConfigDto query, HttpServletResponse response) {
        try {
            String url = settleTypeDispatchHandler.getDetailUrl(query);
            response.setStatus(302);
            response.setHeader("Location", url);
        } catch (BusinessException e) {
            LOGGER.error("method showBusinessDetail", e);
        } catch (Exception e) {
            LOGGER.error("method showBusinessDetail", e);
        }
    }

    /**
     * 加载业务打印数据
     * @param query
     * @return
     */
    @RequestMapping(value = "/loadPrintData.action")
    @ResponseBody
    public BaseOutput<PrintDto> loadPrintData(ApplicationConfigDto query) {
        try {
            String url = settleTypeDispatchHandler.getPrintUrl(query);
            return businessRpc.loadPrintData(url);
        } catch (BusinessException e) {
            LOGGER.error("method loadPrintData", e.getMessage());
            return BaseOutput.failure(e.getMessage());
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
    public String forwardList(ModelMap modelMap) {
        try {
            UserTicket userTicket = getUserTicket();
            MarketApplication marketApplication = new MarketApplication();
            marketApplication.setMarketId(userTicket.getFirmId());
            //marketApplication.setState(ConfigStateEnum.ENABLE.getCode());
            BaseOutput<List<MarketApplication>> appBaseOutput = settleRpc.listMarketApplication(marketApplication);
            if (appBaseOutput.isSuccess()) {
                modelMap.addAttribute("appList", appBaseOutput.getData());
            }
            modelMap.put("marketId", userTicket.getFirmId());
            LocalDate date = DateUtil.nowDate();
            String operateTimeStart = DateUtil.formatDate(date.minus(2L, ChronoUnit.DAYS), "yyyy-MM-dd") + " 00:00:00";
            String operateTimeEnd = DateUtil.formatDate(date, "yyyy-MM-dd") + " 23:59:59";
            modelMap.addAttribute("operateTimeStart", operateTimeStart);
            modelMap.addAttribute("operateTimeEnd", operateTimeEnd);
        } catch (Exception e) {
            LOGGER.error("method forwardList", e);
        }
        return "settleOrder/index";
    }

    /**
     * 分页查询列表数据
     * @return
     */
    @RequestMapping(value = "/listPage.action")
    @ResponseBody
    public String listPage(SettleOrderDto settleOrderDto) {
        try {
            if (CollUtil.isEmpty(settleOrderDto.getBusinessTypeList())) {
                return new EasyuiPageOutput(0L, new ArrayList(0)).toString();
            }
            UserTicket userTicket = getUserTicket();
            settleOrderDto.setMarketId(userTicket.getFirmId());
            settleOrderDto.setConvert(true);
            PageOutput<List<SettleOrder>> pageOutput = settleRpc.listPage(settleOrderDto);
            if (pageOutput.isSuccess()) {
                //List itemList = ValueProviderUtils.buildDataByProvider(settleOrderDto, pageOutput.getData());
                return new EasyuiPageOutput(pageOutput.getTotal(), pageOutput.getData()).toString();
            }
        } catch (Exception e) {
            LOGGER.error("method listPage", e);
        }
        return new EasyuiPageOutput(0L, new ArrayList(0)).toString();
    }

    /**
     * 生成用于签名的字符串
     * @param userTicket
     * @param settleOrderDto
     * @return
     */
    private String createTokenStr(UserTicket userTicket, SettleOrderDto settleOrderDto) {
        StringBuilder builder = new StringBuilder();
        builder.append(settleOrderDto.getIds());
        builder.append(userTicket.getId());
        builder.append(userTicket.getFirmId());
        return builder.toString();
    }
}