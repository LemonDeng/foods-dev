package com.dzl.controller;

import com.dzl.enums.YesOrNo;
import com.dzl.pojo.*;
import com.dzl.pojo.vo.CategoryVO;
import com.dzl.pojo.vo.CommentLevelCountsVO;
import com.dzl.pojo.vo.ItemInfoVO;
import com.dzl.pojo.vo.NewItemsVO;
import com.dzl.service.CarouselService;
import com.dzl.service.CategoryService;
import com.dzl.service.ItemService;
import com.dzl.utils.DZLJSONResult;
import com.dzl.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dzl.controller.BaseController.COMMON_PAGE_SIZE;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@Api(value = "商品接口",tags = "商品信息展示的相关接口")
@RequestMapping("index")//首页所对应的路由
@RestController   /*默认返回出去的都是json对象*/

public class ItemsController {
    //注入Service，调用实现类里面的对应方法
    @Autowired
    private ItemService itemService;


    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")//rootCatId路径参数，首先需要{}占位符，把路径参数写进去
    public DZLJSONResult subCat(
            //接口文档给前端对接人员观看的
            @ApiParam(name = "itemId", value = "商品id", required = true)
            //路径参数在参数位置要加上一个注解@PathVariable
            @PathVariable String itemId) {
        //判断传进来的父id是否为空，则不做任何的查询直接报错
        if (StringUtils.isBlank(itemId)) {
            return DZLJSONResult.errorMsg(null);
        }
        /**
         * 在一个Controller里面调用四个service方法
         * 构建VO来传递多个对象
         */
        Items item = itemService.queryItemById(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        /**
         * 这些对象都是要传递到前端去的，但是只能返回一个对象出去，所以要构建一个VO（显示层）,然后把这些都丢进去，并且生成get和set方法
         */
        //把VO先New出来
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);
        return DZLJSONResult.ok(itemInfoVO);

    }
    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public DZLJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {//请求参数用注解@RequestParam

        if (StringUtils.isBlank(itemId)) {
            return DZLJSONResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return DZLJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public DZLJSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return DZLJSONResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId,
                level,
                page,
                pageSize);

        return DZLJSONResult.ok(grid);
    }
}



