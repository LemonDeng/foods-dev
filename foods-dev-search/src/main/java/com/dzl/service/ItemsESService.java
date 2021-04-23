package com.dzl.service;

import com.dzl.utils.PagedGridResult;

public interface ItemsESService {
    public PagedGridResult searhItems(String keywords,
                                      String sort,
                                      Integer page,
                                      Integer pageSize);

}
