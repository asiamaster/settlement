package com.dili.settlement.controller;

import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * 基础controller
 */
public interface IBaseController {

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    default UserTicket getUserTicket() {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return userTicket != null ? userTicket : DTOUtils.newInstance(UserTicket.class);
    }
}
