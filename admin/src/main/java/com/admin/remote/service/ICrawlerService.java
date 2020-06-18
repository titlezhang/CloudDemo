package com.admin.remote.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("crawler")
@Service
public interface ICrawlerService{
    @RequestMapping(value = "crawler/open/crawl")
    String crawl(@RequestParam(value = "keyWord") String keyWord);
}
