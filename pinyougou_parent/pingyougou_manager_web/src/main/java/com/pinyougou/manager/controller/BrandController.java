package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询全表
     * @return
     * @throws Exception
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() throws Exception {
        return brandService.findAll();
    }
    
    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page,int size) throws Exception {
    	
        return brandService.findPage(page, size);
    }
    
    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
		try {
			brandService.add(brand);
			return new Result(true,"增加成功");
		} catch (Exception e) {
		
			e.printStackTrace();
			return new Result(false,"增加失败");
		}
    	
    	
    }
    
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
    	return brandService.findOne(id);
    }
    
    /**
     * 修改
     * @param brand
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
    	try {
			brandService.update(brand);
			return new Result(true,"修改成功");
		} catch (Exception e) {
		
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
    }
    
    
    /**
     * 删除多选
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
    	try {
			brandService.delete(ids);
			return new Result(true,"删除成功");
		} catch (Exception e) {
		
			e.printStackTrace();
			return new Result(false,"删除失败");
		}
    }
    
	/**
	 * 条件分页查询
	 * @param brand
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand,int page,int size){
		return brandService.findPage(brand, page, size);		
	}
	
	
	
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
	return brandService.selectOptionList();
	}
}
