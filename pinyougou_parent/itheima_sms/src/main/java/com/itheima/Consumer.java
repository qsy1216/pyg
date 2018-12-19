package com.itheima;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Consumer {
    @JmsListener(destination="itcast")
    public void readMessage(String text){
        System.out.println("接收到消息："+text);
    }
    @JmsListener(destination="itcast_map")
    public void readMap(Map map){
        System.out.println("接收到消息："+map);
    }
}
