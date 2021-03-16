package com.dzl.mapper;

import com.dzl.my.mapper.MyMapper;
import com.dzl.pojo.Category;
import com.dzl.pojo.vo.CategoryVO;
import com.dzl.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom  {

    //对应xml里面的sql查询
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 用Map来传参数，可以是任何类型的
     * @param map
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String,Object> map);
}