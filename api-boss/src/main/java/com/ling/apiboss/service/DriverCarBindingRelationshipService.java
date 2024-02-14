package com.ling.apiboss.service;

import com.ling.apiboss.remote.ServiceUserDriverClient;
import com.ling.internalcommon.dto.DriverCarBindingRelationship;
import com.ling.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverCarBindingRelationshipService {

    @Autowired
    ServiceUserDriverClient serviceUserDriverClient;

    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship){

        return serviceUserDriverClient.bind(driverCarBindingRelationship);
    }

    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship){
        return serviceUserDriverClient.unbind(driverCarBindingRelationship);
    }
}
