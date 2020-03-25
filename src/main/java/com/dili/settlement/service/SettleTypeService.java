package com.dili.settlement.service;

import com.dili.settlement.dto.ApplicationConfigDto;

/**
 * 结算类型相关接口类
 */
public interface SettleTypeService {

    /**
     * 详情参数验证
     * @param query
     */
    void validDetailParams(ApplicationConfigDto query);

    /**
     * 打印参数验证
     * @param query
     */
    void validPrintParams(ApplicationConfigDto query);

    /**
     * 构建详情参数
     * @param query
     */
    void buildDetailParams(ApplicationConfigDto query);

    /**
     * 构建打印参数
     * @param query
     */
    void buildPrintParams(ApplicationConfigDto query);

    /**
     * 获取详情url
     * @param query
     * @return
     */
    String getDetailUrl(ApplicationConfigDto query);

    /**
     * 获取打印数据url
     * @param query
     * @return
     */
    String getPrintUrl(ApplicationConfigDto query);

    /**
     * 支持哪种退款方式
     * @return
     */
    Integer support();
}
