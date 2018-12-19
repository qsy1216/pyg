package com.itheima;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QuickStartController {
    @RequestMapping("/quick")
    @ResponseBody
    public String quick(){
        return "springboot修改了代码不重新启动哈哈 访问成功!";
    }

}
