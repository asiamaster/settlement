package com.dili.settlement.dto.pay;

import java.util.List;

/**
 * 提现
 * @author xuliang
 */
public class TradeRequestDto {

    /** 交易号*/
    private String tradeId;
    /** 账户ID*/
    private Long accountId;
    /** 交易渠道*/
    private Integer channelId;
    /** 密码*/
    private String password;
    /** 费用项*/
    private List<FeeItemDto> fees;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FeeItemDto> getFees() {
        return fees;
    }

    public void setFees(List<FeeItemDto> fees) {
        this.fees = fees;
    }

    /**
     * 创建提交交易参数
     * @return
     */
    public static TradeRequestDto build(String tradeId, Long fundAccountId, Integer channelId, String password, List<FeeItemDto> fees) {
        TradeRequestDto tradeRequest = new TradeRequestDto();
        tradeRequest.setTradeId(tradeId);
        tradeRequest.setAccountId(fundAccountId);
        tradeRequest.setChannelId(channelId);
        tradeRequest.setPassword(password);
        tradeRequest.setFees(fees);
        return tradeRequest;
    }

    /**
     * 创建提交交易参数
     * @return
     */
    public static TradeRequestDto build(String tradeId, Long fundAccountId, Integer channelId, List<FeeItemDto> fees) {
        TradeRequestDto tradeRequest = new TradeRequestDto();
        tradeRequest.setTradeId(tradeId);
        tradeRequest.setAccountId(fundAccountId);
        tradeRequest.setChannelId(channelId);
        tradeRequest.setFees(fees);
        return tradeRequest;
    }
}
