package com.dzl.pojo.vo;

import java.util.List;

/**
 * 二级分类VO
 * BO与数据层打交道的实体类
 *
 */
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    // 把三级分类作为一个List封装到二级分类里面··········三级分类vo list
    private List<SubCategoryVO> subCatList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public List<SubCategoryVO> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(List<SubCategoryVO> subCatList) {
        this.subCatList = subCatList;
    }
}
