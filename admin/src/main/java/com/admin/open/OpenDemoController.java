package com.admin.open;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class OpenDemoController {

    @RequestMapping(value="openTest",method = RequestMethod.POST)
    @ResponseBody
    public String openTest(@RequestParam String inParams){
        return "Hello"+inParams+"，I am Admin!";
    }
}
