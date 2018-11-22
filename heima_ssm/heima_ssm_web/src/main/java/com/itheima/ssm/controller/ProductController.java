package com.itheima.ssm.controller;

import com.itheima.ssm.domain.Product;
import com.itheima.ssm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    /**
     * 查询全部
     */
    private ProductService productService;
    @RequestMapping("/findAll")
    public ModelAndView findAll() throws Exception {

        ModelAndView mv = new ModelAndView();
        List<Product> ps = productService.findAll();
        mv.addObject("productList",ps);
        mv.setViewName("productlist1");
        return mv;
    }

    /**
     * 添加产品
     * @param product
     */
    @RequestMapping("/save")
    public String save(Product product) throws Exception {
        productService.save(product);
        //保存完成之后重新查询全部
        return "redirect:findAll";
    }




}
