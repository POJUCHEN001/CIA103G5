package com.cia103g5.user.admin.controller;

import com.cia103g5.user.admin.model.AdminService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**############################
 #                            #
 #       管理員 Controller     #
 #                            #
 ############################ */

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/findAllAdmin")
    public String findAllAdmin(){
        return adminService.findAll().toString();
    }




}
