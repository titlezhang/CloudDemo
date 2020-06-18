package com.crawler.open;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("open")
public class PublicCrawlController {
    @RequestMapping("/crawl")
    @ResponseBody
    public String crawl(@RequestParam String keyWord){
        System.out.println(keyWord);
        return keyWord;
    }
}
