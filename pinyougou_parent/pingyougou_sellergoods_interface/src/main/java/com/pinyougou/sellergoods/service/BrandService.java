package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

import java.util.List;

/**
 * 品牌接口
 */
public interface BrandService {
    List<TbBrand> findAll() throws Exception;
    
    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(int pageNum,int pageSize);
}
