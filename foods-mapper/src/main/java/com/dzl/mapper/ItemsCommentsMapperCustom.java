package com.dzl.mapper;

import com.dzl.my.mapper.MyMapper;
import com.dzl.pojo.ItemsComments;
//import com.dzl.pojo.vo.MyCommentVO;
import com.dzl.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    public void saveComments(Map<String, Object> map);

    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}