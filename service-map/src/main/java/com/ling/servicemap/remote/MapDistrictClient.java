package com.ling.servicemap.remote;

import com.ling.internalcommon.constant.AmapConfigConstants;
import com.ling.internalcommon.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapDistrictClient {

    @Value("${amap.key}")
    private String amapKey;

    @Autowired
    RestTemplate restTemplate;
    /**
     * 请求高德行政区域API
     * @param keywords 要查询的行政区域范围，暂时使用中国
     * @return
     */
    public String dicDistrict(String keywords){
        //https://restapi.amap.com/v3/config/district?keywords=北京&subdistrict=2&key=<用户的key>
        //拼装并请求URL
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.DISTRICT_URL);
        url.append("?");
        url.append("keywords=" + keywords);
        url.append("&");
        url.append("subdistrict=3");
        url.append("&");
        url.append("key=" + amapKey);
        log.info("高德地图行政区域请求url" + url);

        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);

        return forEntity.getBody();
    }
}
