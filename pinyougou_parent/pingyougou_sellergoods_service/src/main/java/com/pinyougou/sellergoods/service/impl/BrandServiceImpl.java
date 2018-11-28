package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    /**
     * 查询全表
     */
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    /**
     * 分页查询
     */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
		return new PageResult(page.getTotal(),page.getResult());
	}
 
	//添加
	@Override
	public void add(TbBrand brand) {
		tbBrandMapper.insert(brand);
		
	}


	//根据id查询
	@Override
	public TbBrand findOne(Long id)  {
		
		return tbBrandMapper.selectByPrimaryKey(id);
	}

	//修改
	@Override
	public void update(TbBrand brand)  {
		
		tbBrandMapper.updateByPrimaryKey(brand);
	}

	//删除
	@Override
	public void delete(Long[] ids) {
		//循环遍历
		for (Long id : ids) {
			tbBrandMapper.deleteByPrimaryKey(id);
		}
		
	}

	/**
	 * 条件查询
	 */
	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);//分页	
		
		TbBrandExample example=new TbBrandExample();
		
		Criteria criteria = example.createCriteria();
		
		if(brand!=null){
			if(brand.getName()!=null && brand.getName().length()>0){
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0){
				criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
			}			
		}
		
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(example);
		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	
	
	
}
