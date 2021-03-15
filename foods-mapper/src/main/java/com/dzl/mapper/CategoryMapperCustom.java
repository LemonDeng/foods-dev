package com.dzl.mapper;

//import com.dzl.pojo.vo.CategoryVO;
//import com.dzl.pojo.vo.NewItemsVO;
import com.dzl.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    public List<CategoryVO> getSubCatList(Integer rootCatId);

   // public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}