package com.crawler;

import com.crawler.remote.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("")
public class FirstController {
    @Resource
    IAdminService adminService;
    @RequestMapping(value="/testMe",method = RequestMethod.GET)
    @ResponseBody
    public String testMe(){
        return adminService.openTest("Crawler");
    }
}
