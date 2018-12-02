package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {
	/**
	 * 查询全表
	 * @return
	 * @throws Exception
	 */
    List<TbBrand> findAll();
    
    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(int pageNum,int pageSize) ;
    
    
    // 增加一个品牌
    public void add(TbBrand brand) ;
    
    //根据id查询
    public TbBrand findOne(Long id) ;
    
    //修改
    public void update(TbBrand brand) ;
    
    //删除
    public void delete(Long[] ids) ;
    
    /**
	 * 条件查询分页
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);
    
    
	/**
	* 品牌下拉框数据
	*/
	List<Map> selectOptionList();
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
