package com.crawler.remote.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@FeignClient("admin")
@Service
public interface IAdminService {
    @RequestMapping(method = RequestMethod.POST,value = "admin/openTest")
    String openTest(@RequestParam(value="inParams") String inParams);
}
