package com.dzl.pojo.vo;

import com.dzl.pojo.Items;
import com.dzl.pojo.ItemsImg;
import com.dzl.pojo.ItemsParam;
import com.dzl.pojo.ItemsSpec;

import java.util.List;

/**
 * 最新商品VO
 *
 */
public class ItemInfoVO {

    private Items item;
    private List<ItemsSpec> itemSpecList;
    private List<ItemsImg> itemImgList;
    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }



    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}
