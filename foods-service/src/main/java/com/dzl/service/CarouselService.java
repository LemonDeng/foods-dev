package com.dzl.service;

import com.dzl.pojo.Carousel;

import java.util.List;

public interface CarouselService {
   /* *//**
     * 查询所有轮播图
     * @param isShow
     * @return
     * 因为查询的图片有多个所有返回值用List<Carousel>类型为Carousel
     *//*
    public List<Carousel> queryAll(Integer isShow);*/

    /**
     * 查询所有的轮播图，所以用List集合去装
     * @param isShow
     * @return
     */
    public  List<Carousel> queryAll(Integer isShow);
}
