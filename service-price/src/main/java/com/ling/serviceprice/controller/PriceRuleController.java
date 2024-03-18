package com.ling.serviceprice.controller;


import com.ling.internalcommon.dto.PriceRule;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.serviceprice.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
}
