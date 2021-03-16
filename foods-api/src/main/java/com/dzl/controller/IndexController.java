package com.dzl.controller;

import com.dzl.enums.YesOrNo;
import com.dzl.pojo.Carousel;
import com.dzl.pojo.Category;
import com.dzl.pojo.vo.CategoryVO;
import com.dzl.pojo.vo.NewItemsVO;
import com.dzl.service.CarouselService;
import com.dzl.service.CategoryService;
import com.dzl.utils.DZLJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

//@Controller   SpringMVC里面用的比较多用于页面的跳转
@Api(value = "首页",tags = "首页展示的相关接口")
@RequestMapping("index")//首页所对应的路由
@RestController   /*默认返回出去的都是json对象*/

public class IndexController {
    //注入Service，调用实现类里面的对应方法
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;
//@ApiOperation接口文档的规范化，便于前端人员的观看
    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public DZLJSONResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);//使用枚举来增加方法的多样性
        return DZLJSONResult.ok(list);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
   @GetMapping("/cats")
   @ApiOperation(value = "获取商品分类(一级分类)",notes = "获取商品分类(一级分类)",httpMethod = "GET")
   public DZLJSONResult cats()
   {
       List<Category> list = categoryService.queryAllRootLevelCat();
       return DZLJSONResult.ok(list);
   }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")//rootCatId路径参数，首先需要{}占位符，把路径参数写进去
    public DZLJSONResult subCat(
            //接口文档给前端对接人员观看的
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            //路径参数在参数位置要加上一个注解@PathVariable
            @PathVariable Integer rootCatId) {
        //判断传进来的父id是否为空，则不做任何的查询直接报错
        if (rootCatId == null) {
            return DZLJSONResult.errorMsg("分类不存在");
        }

        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return DZLJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")//rootCatId路径参数，首先需要{}占位符，把路径参数写进去
    public DZLJSONResult getSixNewItemsLazy(
            //接口文档给前端对接人员观看的
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            //路径参数在参数位置要加上一个注解@PathVariable
            @PathVariable Integer rootCatId) {
        //判断传进来的父id是否为空，则不做任何的查询直接报错
        if (rootCatId == null) {
            return DZLJSONResult.errorMsg("分类不存在");
        }

        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return DZLJSONResult.ok(list);
    }
}
