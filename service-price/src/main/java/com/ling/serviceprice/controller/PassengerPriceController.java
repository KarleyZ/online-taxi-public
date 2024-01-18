package com.ling.serviceprice.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.LocationInformationDTO;
import com.ling.internalcommon.response.ForecastPriceResponse;
import com.ling.serviceprice.service.PassengerPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PassengerPriceController {

    @Autowired
    PassengerPriceService priceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody LocationInformationDTO locationInformationDTO){

        log.info("出发地位置"+ locationInformationDTO.getDepLongitude() + "," + locationInformationDTO.getDepLatitude());
        log.info("目的地位置"+ locationInformationDTO.getDestLongitude() + "," + locationInformationDTO.getDestLatitude());

        return priceService.forecastPrice(locationInformationDTO.getDepLongitude(), locationInformationDTO.getDepLatitude(), locationInformationDTO.getDestLongitude(), locationInformationDTO.getDestLatitude());


    }
}
