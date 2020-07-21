package com.admin;

import com.admin.remote.service.ICrawlerService;
import com.common.utils.QRCodeUtils;
import com.google.zxing.WriterException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
@RequestMapping("test")
public class TestController {
    @Resource
    ICrawlerService crawlerService;
    @RequestMapping("qrcode")
    @ResponseBody
    public String qrCodeTest(@RequestParam("url") String url) {
        try {
            byte[] qrCodeBytes = QRCodeUtils.createQRCodeToBytes(url);
            String qrCode = "data:image/jpg;base64," + Base64.encodeBase64String(qrCodeBytes);
            return qrCode;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return "生成失败";
        }
        return "生成失败";
    }

    @RequestMapping("crawl")
    @ResponseBody
    public String testCrawler(@RequestParam(value="keyWord",defaultValue ="你好") String keyWord){
        return crawlerService.crawl(keyWord);
    }

}
