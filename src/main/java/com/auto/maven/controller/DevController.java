package com.auto.maven.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DevController {
	
	private static Logger log = LoggerFactory.getLogger(DevController.class);  
    /** 配置文件名称，早conf-sys.properties文件中定义，区分不同打包指令后各环境的配置文件内容    */  
    @Value("${propertiesName}")  
    private String propertiesName;  
    /** 
     * 登录 
     * @return String 
     */  
    @RequestMapping("/login")  
    public String  login(Model model) {  
        log.info(propertiesName + "Login!");  
        model.addAttribute("user", propertiesName);  
        return "index";  
    } 
}
