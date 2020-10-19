package com.dili.settlement.component;

/**
 * 市场ID持有
 */
public class FirmIdHolder {

    private static final ThreadLocal<Long> HOLDER = new ThreadLocal<>();

    /**
     * 添加
     * @param firmId
     */
    public static void set(Long firmId) {
        HOLDER.set(firmId);
    }

    /**
     * 获取
     * @return
     */
    public static Long get() {
        return HOLDER.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        HOLDER.remove();
    }
}
