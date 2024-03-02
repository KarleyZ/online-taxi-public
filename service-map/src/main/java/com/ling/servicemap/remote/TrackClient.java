package com.ling.servicemap.remote;

import com.ling.internalcommon.constant.AmapConfigConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTrackResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TrackClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String amapSid;

    @Autowired
    RestTemplate restTemplate;

    public ResponseResult<MapTrackResponse> add(String tid){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.Trace_ADD_URL);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + amapSid);
        url.append("&");
        url.append("tid="+tid);
        log.info("创建轨迹请求的url"+ url.toString());

        ResponseEntity<String> postForEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = postForEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");

        String trid = data.getString("trid");
        String trname = "";
        if(data.has("trname")){
            trname = data.getString("trname");
        }

        MapTrackResponse trackResponse = new MapTrackResponse();
        trackResponse.setTrid(trid);
        trackResponse.setTrname(trname);

        return ResponseResult.success(trackResponse);

    }
}
