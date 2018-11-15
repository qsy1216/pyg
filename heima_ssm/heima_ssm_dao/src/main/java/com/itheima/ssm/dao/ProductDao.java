package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductDao {
    //查询所有商品的信息
    @Select("select * from product")
    public List<Product> finAll() throws Exception;

    // 插入一条产品信息
    @Insert("insert into product(productNum,productName," +
            "cityName,departureTime,productPrice," +
            "productDesc,productStatus) " +
            "values(#{productNum},#{productName},#{cityName},#{departureTime}," +
            "#{productPrice},#{productDesc},#{productStatus})")
    void save(Product product)throws Exception;

    // 根据id查询产品
    @Select("select * from product where id = #{id}")
    public Product findById(String id)throws Exception;
}
