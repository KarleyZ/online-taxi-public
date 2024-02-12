package com.ling.servicemap.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicemap.service.DicDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistrictController {

    @Autowired
    private DicDistrictService dicDistrictService;

    /**
     * 行政区域字典初始化，keywords使用url?keywords=的方式传入参数
     * @param keywords
     * @return
     */
    @GetMapping("/dic-district")
    public ResponseResult initDicDistrict(String keywords){

        return dicDistrictService.initDicDistrict(keywords);
    }
}
