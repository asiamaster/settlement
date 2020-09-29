package com.dili.settlement.dto;

/**
 * 用于客户查询
 */
public class CustomerDto {
    //查询关键字
    private String keyword;
    //查询类型
    private String queryType;

    /**
     * getter
     * @return
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * setter
     * @return
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * getter
     * @return
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * setter
     * @return
     */
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
