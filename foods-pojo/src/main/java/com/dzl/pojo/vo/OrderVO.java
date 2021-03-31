package com.dzl.pojo.vo;

import com.dzl.pojo.bo.ShopcartBO;

import java.util.List;

public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }

    private List<ShopcartBO> toBeRemovedShopcatdList;

    public List<ShopcartBO> getToBeRemovedShopcatdList() {
        return toBeRemovedShopcatdList;
    }
}