package com.dili.settlement.provider;

import com.dili.settlement.rpc.DepartmentRpc;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门provider
 */
public class DepartmentProvider implements ValueProvider {

    @Resource
    private DepartmentRpc departmentRpc;
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return new ArrayList<>(0);
        }
        Department query = DTOUtils.newInstance(Department.class);
        query.setFirmCode(userTicket.getFirmCode());
        List<Department> itemList = departmentRpc.list(query);
        return itemList.stream().map(temp -> new ValuePairImpl<>(temp.getName(), temp.getId())).collect(Collectors.toList());
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
