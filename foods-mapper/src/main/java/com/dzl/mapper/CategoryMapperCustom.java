package com.dzl.mapper;

import com.dzl.my.mapper.MyMapper;
import com.dzl.pojo.Category;
import com.dzl.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom  {

    //对应xml里面的sql查询
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}