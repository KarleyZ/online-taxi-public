package com.ling.servicemap.remote;


import com.ling.internalcommon.constant.AmapConfigConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapServiceResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SeriveClient {

    @Value("${amap.key}")
    private String amapKey;

    @Autowired
    RestTemplate restTemplate;

    public ResponseResult add(String name){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.SERVICE_ADD_URL);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("name=" + name);

        ResponseEntity<String> postForEntity = restTemplate.postForEntity(url.toString(), null, String.class);

        String body = postForEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String sid = data.getString("sid");
        MapServiceResponse serviceResponse = new MapServiceResponse();
        serviceResponse.setSid(sid);

        return ResponseResult.success(serviceResponse);

    }
}
