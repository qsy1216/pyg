package com.itheima.ssm.controller;


import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询所有角色
     * @param mv
     * @return
     * @throws Exception
     */

    @RequestMapping("/findAll")
    public ModelAndView findAll(ModelAndView mv) throws Exception {
        List<Role> list = roleService.findAll();
        mv.addObject("roleList",list);
        mv.setViewName("role-list");
        return mv;
    }

    /**
     * 添加一个角色
     * @param role
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(Role role) throws Exception {

        roleService.save(role);

        return "redirect:findAll";
    }

    /**
     * 查看角色详情
     * @param mv
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("findById")
    public ModelAndView findById(ModelAndView mv,String id) throws Exception {

        Role role = roleService.findById(id);
        mv.addObject("role",role);
        mv.setViewName("role-show");
        return mv;
    }

    /**
     * 根据id查询角色可以添加的权限
     * @return
     */
    @RequestMapping("/findRoleByIdAndAllPermission")
    public ModelAndView findRoleByIdAndAllPermission(ModelAndView mv, @RequestParam(name = "id" ,required = true)String roleId) throws Exception {

        Role role = roleService.findById(roleId);
        List<Permission> permissionList = roleService.findOtherPermission(roleId);
        mv.addObject("role",role);
        mv.addObject("permissionList",permissionList);
        mv.setViewName("role-permission-add");

        return mv;

    }


    @RequestMapping("/addPermissionToRole")
    public String addPermissionToRole(@RequestParam(name = "roleId" ,required = true) String roleId,
                                      @RequestParam(name = "ids",required = true) String[] permissionIds){

        roleService.addPermissionToRole(roleId,permissionIds);

        return "redirect:findAll";
    }
}
