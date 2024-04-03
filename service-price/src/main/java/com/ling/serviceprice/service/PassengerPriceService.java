package com.ling.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.dto.PriceRule;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.LocationInformationDTO;
import com.ling.internalcommon.response.DirectionResponse;
import com.ling.internalcommon.response.ForecastPriceResponse;
import com.ling.internalcommon.util.BigDecimalUtils;
import com.ling.serviceprice.mapper.PriceRuleMapper;
import com.ling.serviceprice.remote.ServiceMapClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PassengerPriceService {

    @Autowired
    ServiceMapClient serviceMapClient;

    @Autowired
    PriceRuleMapper priceRuleMapper;
    /**
     * 根据出发地和目的地预估打车价格
     * @param depLongitude
     * @param depLatitude
     * @param destLongitude
     * @param destLatitude
     * @return
     */
    public ResponseResult forecastPrice(String depLongitude, String depLatitude, String destLongitude, String destLatitude,
                                        String cityCode, String vehicleType){

        LocationInformationDTO location = new LocationInformationDTO();
        location.setDepLongitude(depLongitude);
        location.setDepLatitude(depLatitude);
        location.setDestLongitude(destLongitude);
        location.setDestLatitude(destLatitude);
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(location);
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();
        log.info("调用到的距离和时间"+ distance + "," + duration);

        log.info("读取计价规则");
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.isEmpty()){
            log.info("计价规则不存在");
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }

        PriceRule priceRule = priceRules.get(0);
        //根据计价规则计算出真正的价格
        double price = getPrice(distance, duration, priceRule);
        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(price);
        forecastPriceResponse.setCityCode(cityCode);
        forecastPriceResponse.setVehicleType(vehicleType);
        forecastPriceResponse.setFareType(priceRule.getFareType());
        forecastPriceResponse.setFareVersion(priceRule.getFareVersion());
        return ResponseResult.success(forecastPriceResponse);
    }

    /**
     * 根据距离，时长和计价规则来预计算最终价格
     * @param distance
     * @param duration
     * @param priceRule
     * @return
     */
    private double getPrice(Integer distance, Integer duration,PriceRule priceRule){
        //BigDecimal price = new BigDecimal(0);
        double price = 0;
        //起步价
        double startFare = priceRule.getStartFare();
        price = BigDecimalUtils.add(price,startFare);

        //里程费
        //总里程m
        double distanceKMile = BigDecimalUtils.divide(distance,1000);
        //起步里程 km
        double startKMile = priceRule.getStartMile();

        double distanceSubtract = BigDecimalUtils.substract(distanceKMile,startKMile);

        //最终超出基本里程的额外公里 km
        double extraKMile = distanceSubtract <0?0:distanceSubtract;

        double unitPricePerMile = priceRule.getUnitPricePerMile();

        //最终超出的里程价格
        double extraKMileFare = BigDecimalUtils.multiply(extraKMile,unitPricePerMile);

        price = BigDecimalUtils.add(price,extraKMileFare);

        //时长费
        //总时长 min
        double timeMin = BigDecimalUtils.divide(duration,60);
        //每分钟单价
        double unitPricePerMinute = priceRule.getUnitPricePerMinute();

        //时长方面的费用
        double timeFare = BigDecimalUtils.multiply(timeMin,unitPricePerMinute);

        price = BigDecimalUtils.add(price,timeFare);

        //对最后的价格再次精确到后两位
        BigDecimal priceBigDecimal = BigDecimal.valueOf(price);
        priceBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);

        return priceBigDecimal.doubleValue();
    }

    /**
     * 根据距离，时长和计价规则来计算最终实际价格
     * @param distance
     * @param duration
     * @param cityCode
     * @param vehicleType
     * @return
     */
    public ResponseResult<Double> calculatePrice(Integer distance, Integer duration, String cityCode, String vehicleType){
        log.info("读取计价规则");
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");
        List<PriceRule> priceRules = priceRuleMapper.selectList(queryWrapper);
        if (priceRules.isEmpty()){
            log.info("计价规则不存在");
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
        PriceRule priceRule = priceRules.get(0);
        //根据计价规则计算出真正的价格
        double price = getPrice(distance, duration, priceRule);

        return ResponseResult.success(price);
    }
}
