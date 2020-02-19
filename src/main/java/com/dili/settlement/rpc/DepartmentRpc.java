package com.dili.settlement.rpc;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于调用部门接口
 */
@Component
public class DepartmentRpc {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRpc.class);

    @Value("${uap.contextPath:http://uap.diligrp.com}")
    private String domain;

    /**
     * 远程查询部门列表
     * @param query
     * @return
     */
    public List<Department> list(Department query) {
        String url = domain + "/departmentApi/listByExample.api";
        HttpRequest request = HttpUtil.createPost(url);
        request.header("Content-Type", "application/json;charset=UTF-8");
        String responseBody = request.body(JSON.toJSONString(query)).execute().body();
        if (StrUtil.isBlank(responseBody)) {
            return new ArrayList<>(0);
        }
        BaseOutput<List<Department>> baseOutput = JSON.parseObject(responseBody, new TypeReference<BaseOutput<List<Department>>>(){}.getType());
        if (!baseOutput.isSuccess()) {
            LOGGER.error("method list result: " + baseOutput.getMessage());
            return new ArrayList<>(0);
        }
        return baseOutput.getData();
    }
}
