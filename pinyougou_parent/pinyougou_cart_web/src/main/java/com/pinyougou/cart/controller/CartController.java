package com.pinyougou.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference(timeout=6000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 购物车列表
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        String cartListString = util.CookieUtil.getCookieValue(request,
                "cartList","UTF-8");
        if(cartListString==null || cartListString.equals("")){
            cartListString="[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);

        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals("anonymousUser")) {//如果未登录
            //从cookie里面拿购物车列表
            System.out.println("从cookie中提取购物车");
            return cartList_cookie;
        }else {// 如果登录了
            // 从缓存里面获取购物车列表
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            if(cartList_cookie.size()>0){//判断当本地购物车中存在数据
                //得到合并后的购物车
                List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //将合并后的购物车存入redis
                cartService.saveCartListToRedis(username, cartList);
                //本地购物车清除
                util.CookieUtil.deleteCookie(request, response, "cartList");
                System.out.println("执行了合并购物车的逻辑");
                return cartList;
            }
            return cartList_redis;

        }
    }


    /**
     * 添加商品到购物车
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9005")
    public Result addGoodsToCartList(Long itemId, Integer num){

//       response.setHeader("Access-Control-Allow-Origin", "http://localhost:9005");
//       response.setHeader("Access-Control-Allow-Credentials", "true");

        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            List<Cart> cartList =findCartList();//获取购物车列表
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if(username.equals("anonymousUser")){//如果未登录
                //将新的购物车存入cookie
                String cartListString = JSON.toJSONString(cartList);
                util.CookieUtil.setCookie(request, response, "cartList", cartListString, 3600*24, "UTF-8");
                System.out.println("向cookie存储购物车");

            }else{//如果登录
                cartService.saveCartListToRedis(username, cartList);
            }


            util.CookieUtil.setCookie(request, response, "cartList",
                    JSON.toJSONString(cartList),3600*24,"UTF-8");
            return  new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"添加失败");
        }

    }


}
