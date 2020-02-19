package com.dili.settlement.provider;

import com.dili.settlement.enums.SettleStateEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 结算状态provider
 */
@Component
public class SettleStateProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER = new ArrayList<>();

    static {
        BUFFER.addAll(Stream.of(SettleStateEnum.values()).map(temp -> new ValuePairImpl<>(temp.getName(), temp.getCode())).collect(Collectors.toList()));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        if (val == null) {
            return null;
        }
        ValuePair<?> valuePair = BUFFER.stream().filter(temp -> val.toString().equals(temp.getValue())).findFirst().orElse(null);
        if (valuePair != null) {
            return valuePair.getText();
        }
        return null;
    }
}
