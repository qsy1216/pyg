package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private WeixinPayService weixinPayService;
    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map createNative(){
//        IdWorker idworker=new IdWorker();
//        return weixinPayService.createNative(idworker.nextId()+"","1");
        // 获取登录人
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 从缓存中查询支付日志
        TbPayLog payLog = orderService.searchPayLogFromRedis(username);
        // 判断 pagLog 的值
        if (payLog!=null){
            return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");
        }else {
            return new HashMap();
        }


    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result=null;
        int x=0;
        while(true){
            //调用查询接口
            Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
            if(map==null){//出错
                result=new Result(false, "支付出错");
                break;
            }
            if(map.get("trade_state").equals("SUCCESS")){//如果成功
                result=new Result(true, "支付成功");
                break;
            }
            try {
                Thread.sleep(3000);//间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为 5 分钟
            x++;
            if(x>=4){
                result=new Result(false, "二维码超时");
                break;
            }
        }
        return result;
    }


}
