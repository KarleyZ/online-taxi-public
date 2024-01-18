package com.ling.servicemap.controller;

import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.LocationInformationDTO;
import com.ling.servicemap.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direction")
public class DriectionController {

    @Autowired
    DirectionService directionService;

    @GetMapping("/driving")
    public ResponseResult driving(@RequestBody LocationInformationDTO locationInformationDTO){
        String depLongitude = locationInformationDTO.getDepLongitude();
        String depLatitude = locationInformationDTO.getDepLatitude();
        String destLongitude = locationInformationDTO.getDestLongitude();
        String destLatitude = locationInformationDTO.getDestLatitude();


        return directionService.driving(depLongitude,depLatitude,destLongitude,destLatitude);
    }
}
