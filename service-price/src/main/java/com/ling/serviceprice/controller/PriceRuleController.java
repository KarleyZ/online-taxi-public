package com.ling.serviceprice.controller;


import com.ling.internalcommon.dto.PriceRule;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.serviceprice.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ling
 * @since 2024-03-12
 */
@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {

    @Autowired
    PriceRuleService priceRuleService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody PriceRule priceRule){

        return priceRuleService.add(priceRule);
    }
    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule){

        return priceRuleService.edit(priceRule);
    }

    @GetMapping("/get-lastest-version")
    public ResponseResult<PriceRule> getNewestVersion(@RequestParam String fareType){
         return priceRuleService.getNewestVersion(fareType);
    }

    @GetMapping("/is-new")
    public ResponseResult isNewRule(@RequestParam String fareType, @RequestParam Integer fareVersion){
        return priceRuleService.isNew(fareType,fareVersion);
    }

    @GetMapping("/if-exist")
    public ResponseResult<Boolean> ifExists(@RequestParam String cityCode, @RequestParam String vehicleType){
        return priceRuleService.ifExists(cityCode,vehicleType);
    }
}
