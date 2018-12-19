package com.itheima;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QueueController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @RequestMapping("/send")
    public void send(String text){
        jmsMessagingTemplate.convertAndSend("itcast", text);
    }
    @RequestMapping("/sendmap")
    public void sendMap(){
        Map map=new HashMap<>();
        map.get("mobile");
        map.get("template_code");
        map.get("sign_name");
        map.get("param");
        jmsMessagingTemplate.convertAndSend("sms",map);
    }
}
