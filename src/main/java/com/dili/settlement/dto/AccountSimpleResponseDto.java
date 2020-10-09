package com.dili.settlement.dto;


import java.io.Serializable;

/**
 * 账户信息（包含余额）
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:21
 */
public class AccountSimpleResponseDto implements Serializable {
    /** */
	private static final long serialVersionUID = 965655137183253419L;
	/**账户资金信息*/
    private BalanceResponseDto accountFund;
    /**账户信息*/
    private UserAccountCardResponseDto accountInfo;

    public AccountSimpleResponseDto(BalanceResponseDto accountFund, UserAccountCardResponseDto accountInfo) {
        this.accountFund = accountFund;
        this.accountInfo = accountInfo;
    }

    public BalanceResponseDto getAccountFund() {
        return accountFund;
    }

    public void setAccountFund(BalanceResponseDto accountFund) {
        this.accountFund = accountFund;
    }

    public UserAccountCardResponseDto getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(UserAccountCardResponseDto accountInfo) {
        this.accountInfo = accountInfo;
    }
}

