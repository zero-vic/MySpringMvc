package com.xy.meeting.pojo;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2020/11/27
 * Description:
 * Version:V1.0
 */
public class PageBean<T> {
    private Integer pageCode;//当前页码
    private Integer pageSize;//每页记录数
    private Integer totalRecords;//总记录数
    //private Integer totalPages;//总页数
    private List<T> datas;//当前页的数据

    /**
     * 获取总页数
     *
     * @return
     */
    public Integer getTotalPages() {
        return (totalRecords - 1) / pageSize + 1;
    }

    public Integer getPageCode() {
        return pageCode;
    }

    public void setPageCode(Integer pageCode) {
        this.pageCode = pageCode;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
