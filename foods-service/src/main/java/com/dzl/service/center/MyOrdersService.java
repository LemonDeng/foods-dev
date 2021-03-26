package com.dzl.service.center;

import com.dzl.pojo.Orders;
//import com.dzl.pojo.vo.OrderStatusCountsVO;
import com.dzl.utils.PagedGridResult;

public interface MyOrdersService {

    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize);


    /**
     * @Description: 订单状态 --> 商家发货
     */
    public void updateDeliverOrderStatus(String orderId);
}