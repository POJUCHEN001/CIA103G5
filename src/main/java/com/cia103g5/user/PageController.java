package com.cia103g5.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	@RequestMapping("/")
	public String home() {
		return "/index";
	}
	
//	@GetMapping
//	 public String index() {
//        return "index"; // 導向 index.html
//    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login"; // 導向 login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "/register"; // 導向 regist.html
    }

    @GetMapping("/membercenter")
    public String memberCenterPage() {
        return "/membercenter"; // 導向 membercenter.html
    }
    
    @GetMapping("/registermember")
    public String registerMemberPage() {
    	return "/registermember"; 
    }
    
    @GetMapping("/resetpassword")
    public String resetPasswordPage() {
    	return "/resetpassword"; 
    }
    
    @GetMapping("/registerftmember")
    public String registerFtMemberPage() {
    	return "/registerftmember"; 
    }
    
    @GetMapping("/writeftinfo")
    public String writeFtregisterInfoPage() {
    	return "/writeftinfo"; 
    }
    
    @GetMapping("/ftcenter")
    public String ftcenterPage() {
    	return "/ftcenter"; 
    }
    
}
