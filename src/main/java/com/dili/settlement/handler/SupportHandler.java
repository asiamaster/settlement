package com.dili.settlement.handler;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 功能支持判断处理器
 */
public class SupportHandler {

    /**
     * 是否支持园区卡查询
     * @return
     */
    public static boolean cardQuerySupport() {
        if ("sg".equals(getUserTicket().getFirmCode())) {
            return true;
        }
        return false;
    }

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    private static UserTicket getUserTicket() {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return userTicket != null ? userTicket : DTOUtils.newInstance(UserTicket.class);
    }
}
