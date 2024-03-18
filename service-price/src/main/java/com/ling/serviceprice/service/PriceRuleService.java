package com.ling.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.dto.PriceRule;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.serviceprice.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ling
 * @since 2024-03-12
 */
@Service
public class PriceRuleService {

    @Autowired
    PriceRuleMapper priceRuleMapper;

    public ResponseResult add(PriceRule priceRule){
        //拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + vehicleType;
        priceRule.setFareType(fareType);

        //添加版本号
        //先查询是否有旧的版本号
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if(priceRules.size()>0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EXISTS.getCode(),CommonStatusEnum.PRICE_RULE_EXISTS.getValue());
        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }

    public ResponseResult edit(PriceRule priceRule){
        //拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode + vehicleType;
        priceRule.setFareType(fareType);

        //添加版本号
        //先查询是否有旧的版本号
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        Integer fareVersion = 0;
        if(priceRules.size()>0){
            PriceRule lastPriceRule = priceRules.get(0);
            Double unitPricePerMinute = lastPriceRule.getUnitPricePerMinute();
            Double unitPricePerMile = lastPriceRule.getUnitPricePerMile();
            Double startFare = lastPriceRule.getStartFare();
            Integer startMile = lastPriceRule.getStartMile();

            if(unitPricePerMile.doubleValue()==priceRule.getUnitPricePerMile().doubleValue()
            && unitPricePerMinute.doubleValue() == priceRule.getUnitPricePerMinute().doubleValue()
            &&startFare.doubleValue() == priceRule.getStartFare().doubleValue()
            &&startMile.intValue() == priceRule.getStartMile().intValue()){
                return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_NOT_EDIT.getCode(),CommonStatusEnum.PRICE_RULE_NOT_EDIT.getValue());
            }

            fareVersion = lastPriceRule.getFareVersion();

        }
        priceRule.setFareVersion(++fareVersion);

        priceRuleMapper.insert(priceRule);
        return ResponseResult.success();
    }
}
