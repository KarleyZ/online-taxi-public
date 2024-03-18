package com.ling.apipassenger.service;

import com.ling.apipassenger.remote.ServicePriceClient;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.LocationInformationDTO;
import com.ling.internalcommon.response.ForecastPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

    @Autowired
    ServicePriceClient servicePriceClient;
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

        LocationInformationDTO locationInformationDTO = new LocationInformationDTO();
        locationInformationDTO.setDepLongitude(depLongitude);
        locationInformationDTO.setDepLatitude(depLatitude);
        locationInformationDTO.setDestLongitude(destLongitude);
        locationInformationDTO.setDestLatitude(destLatitude);
        locationInformationDTO.setCityCode(cityCode);
        locationInformationDTO.setVehicleType(vehicleType);

        ResponseResult<ForecastPriceResponse> responseResult = servicePriceClient.forecastPrice(locationInformationDTO);
        return ResponseResult.success(responseResult.getData());
    }
}
