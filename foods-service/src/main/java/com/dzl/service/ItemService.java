package com.dzl.service;

import com.dzl.pojo.*;




import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询详情，单个商品的详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表,查询出来的是多个图片所以用List
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格，商品的规格有多个用List
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

}
