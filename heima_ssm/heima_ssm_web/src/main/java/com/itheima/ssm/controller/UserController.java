package com.itheima.ssm.controller;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller()
@RequestMapping("/user")
public class UserController {

    /**
     * 查询所有用户
     */

    @Autowired
   private UserService userService;
    @RequestMapping("/findAll")
    public ModelAndView findAll(ModelAndView mv) throws Exception {

        List<UserInfo> list = userService.findAll();
        mv.addObject("userList",list);
        mv.setViewName("user-list");
        return mv;
    }

    /**
     * 插入一个用户
     * @param userInfo
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(UserInfo userInfo) throws Exception {
        userService.save(userInfo);

        return "redirect:findAll";
    }

    /**
     * 根据用户id查询用户信息和角色
     * @param mv
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("findById")
    public ModelAndView findById(ModelAndView mv,String id) throws Exception {

        UserInfo userInfo = userService.findById(id);
        mv.addObject("user",userInfo);
        mv.setViewName("user-show");
        return mv;
    }

    /**
     * 用户可以添加的角色
     * @param mv
     * @param userid
     * @return
     * @throws Exception
     */
    @RequestMapping("/findUserByIdAndAllRole")
    public ModelAndView findUserByIdAndAllRole(ModelAndView mv,@RequestParam(name = "id" ,required = true) String userid ) throws Exception {

        // 根据id查询用户
        UserInfo userInfo = userService.findById(userid);
        //根据id查询还可以添加的角色
        List<Role> roleList = userService.findOtherRoles(userid);

        mv.addObject("user",userInfo);
        mv.addObject("roleList",roleList);
        mv.setViewName("user-role-add");

        return mv;

    }

    @RequestMapping("/addRoleToUser")
    public String addRoleToUser(@RequestParam(name = "userId" ,required = true) String userId,
                                      @RequestParam(name = "ids",required = true) String[] roleIds){

        userService.addRoleToUser(userId,roleIds);

        return "redirect:findAll";
    }

}
