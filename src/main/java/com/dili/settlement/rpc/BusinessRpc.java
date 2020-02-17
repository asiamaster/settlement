package com.dili.settlement.rpc;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.settlement.dto.PrintDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.stereotype.Component;

/**
 * 主要用于处理各业务系统打印数据
 */
@Component
public class BusinessRpc {

    /**
     * 根据完成的url获取打印数据
     * @param url
     * @return
     */
    public BaseOutput<PrintDto> loadPrintData(String url) {
        HttpRequest request = HttpUtil.createGet(url);
        String responseBody = request.execute().body();
        if (StrUtil.isBlank(responseBody)) {
            return BaseOutput.failure("获取远程打印数据为空");
        }
        return JSON.parseObject(responseBody, new TypeReference<BaseOutput<PrintDto>>(){}.getType());
    }
}
