package com.timesplus.generator.controller;

import com.timesplus.common.utils.PageVo;
import com.timesplus.common.utils.RetResponseData;
import com.timesplus.common.utils.RetResult;
import com.timesplus.generator.service.SysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *  
 *  
 */
@RestController
@RequestMapping("/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */

    @PostMapping("/list")
    public RetResult list(@RequestParam Map<String, Object> params){
        //查询列表数据
        PageVo page=new PageVo(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(params);
        page.setList(list);
        return RetResponseData.makeOKRsp(page);
    }

    /**
     * 生成代码
     */
    @GetMapping("/code/{tables}")
    public void code(@PathVariable String tables, HttpServletResponse response) throws Exception {
        String[] tableNames = new String[]{};
        tableNames=tables.toUpperCase().split(",");
//        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = sysGeneratorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"timesplus.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
