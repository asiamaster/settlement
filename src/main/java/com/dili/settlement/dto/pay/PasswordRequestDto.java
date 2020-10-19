package com.dili.settlement.dto.pay;

/**
 * 密码校验参数dto
 */
public class PasswordRequestDto {

    /** 资金账户ID */
    private Long accountId;
    /** 密码 */
    private String password;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
