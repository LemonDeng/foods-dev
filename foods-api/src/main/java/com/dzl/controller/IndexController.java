package com.dzl.controller;

import com.dzl.enums.YesOrNo;
import com.dzl.pojo.Carousel;
import com.dzl.pojo.Category;
import com.dzl.pojo.vo.CategoryVO;
import com.dzl.pojo.vo.NewItemsVO;
import com.dzl.service.CarouselService;
import com.dzl.service.CategoryService;
import com.dzl.utils.DZLJSONResult;
import com.dzl.utils.JsonUtils;
import com.dzl.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
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

    @Autowired
    private RedisOperator redisOperator;
    //@ApiOperation接口文档的规范化，便于前端人员的观看
    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public DZLJSONResult carousel() {

        List<Carousel> list = new ArrayList<>();
        String carouselStr = redisOperator.get("carousel");
        if (StringUtils.isBlank(carouselStr))
        {
            list = carouselService.queryAll(YesOrNo.YES.type);//使用枚举来增加方法的多样性
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }else
        {
            list = JsonUtils.jsonToList(carouselStr,Carousel.class);
        }
        return DZLJSONResult.ok(list);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public DZLJSONResult cats() {

        List<Category> list = new ArrayList<>();
        String catsStr = redisOperator.get("cats");
        if (StringUtils.isBlank(catsStr))
        {
            list = categoryService.queryAllRootLevelCat();
            redisOperator.set("cats",JsonUtils.objectToJson(list));
        }else
        {
            list = JsonUtils.jsonToList(catsStr, Category.class);
        }

        return DZLJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public DZLJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return DZLJSONResult.errorMsg("分类不存在");
        }

        /**
         * 查询的key在redis中不存在，
         * 对应的id在数据库也不存在，
         * 此时被非法用户进行攻击，大量的请求会直接打在db上，
         * 造成宕机，从而影响整个系统，
         * 这种现象称之为缓存穿透。
         * 解决方案：把空的数据也缓存起来，比如空字符串，空对象，空数组或list
         */
        /*if (list != null && list.size() > 0) {
            redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(list));
        } else {
            redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(list), 5*60);
        }*/
        List<CategoryVO> list = new ArrayList<>();
        String subCatStr = redisOperator.get("subCat");
        if (StringUtils.isBlank(subCatStr))
        {
            list = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat:" + rootCatId,JsonUtils.objectToJson(list));
        }else
        {
            list = JsonUtils.jsonToList(subCatStr,CategoryVO.class);
        }

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
