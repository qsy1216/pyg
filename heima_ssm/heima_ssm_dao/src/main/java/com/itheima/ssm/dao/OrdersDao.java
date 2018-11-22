package com.itheima.ssm.dao;



import com.itheima.ssm.domain.Member;
import com.itheima.ssm.domain.Orders;
import com.itheima.ssm.domain.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrdersDao {
    @Select("select * from orders")
    /**
     * 实体类的属性和数据库的字段一直的时候注释部分是可以省略的
     */
    @Results({
//            @Result(id=true,property = "id",column = "id"),
//            @Result(property = "orderNum",column = "orderNum"),
//            @Result(property = "orderTime",column = "orderTime"),
//            @Result(property = "orderStatus",column = "orderStatus"),
//            @Result(property = "peopleCount",column = "peopleCount"),
//            @Result(property = "payType",column = "payType"),
//            @Result(property = "orderDesc",column = "orderDesc"),
            @Result(property = "product",column = "productId",javaType = Product.class,one = @One(select = "com.itheima.ssm.dao.ProductDao.findById")),
    })
    public List<Orders> finAll() throws Exception;




    @Select("select * from orders where id=#{ordersId}")
    @Results({
//            @Result(id = true, property = "id", column = "id"),
//            @Result(property = "orderNum", column = "orderNum"),
//            @Result(property = "orderTime", column = "orderTime"),
//            @Result(property = "orderStatus", column = "orderStatus"),
//            @Result(property = "peopleCount", column = "peopleCount"),
//            @Result(property = "payType", column = "payType"),
//            @Result(property = "orderDesc", column = "orderDesc"),
            @Result(property = "product", column = "productId", javaType = Product.class, one = @One(select = "com.itheima.ssm.dao.ProductDao.findById")),
            @Result(property = "member",column = "memberId",javaType = Member.class,one = @One(select = "com.itheima.ssm.dao.MemberDao.findById")),
            @Result(property = "travellers",column = "id",javaType =java.util.List.class,many = @Many(select = "com.itheima.ssm.dao.TravellerDao.findByOrdersId"))
    })
    public Orders findById(String ordersId);
}
