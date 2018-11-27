package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * 品牌接口
 */
public interface BrandService {
    List<TbBrand> findAll() throws Exception;
}
