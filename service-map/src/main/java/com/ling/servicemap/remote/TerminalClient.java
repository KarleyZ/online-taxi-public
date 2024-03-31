package com.ling.servicemap.remote;

import com.ling.internalcommon.constant.AmapConfigConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.response.MapTerminalResponse;
import com.ling.internalcommon.response.TrsearchResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TerminalClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String amapSid;

    @Autowired
    RestTemplate restTemplate;

    public ResponseResult<MapTerminalResponse> add(String name, String desc){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_ADD_URL);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + amapSid);
        url.append("&");
        url.append("name=" + name);
        url.append("&");
        url.append("desc=" + desc);

        log.info("请求创建终端的url:"+ url.toString());

        ResponseEntity<String> postForEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = postForEntity.getBody();
        log.info("创建终端的结果"+body);
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String tid = data.getString("tid");
        MapTerminalResponse mapTerminalResponse = new MapTerminalResponse();
        mapTerminalResponse.setTid(tid);

        return ResponseResult.success(mapTerminalResponse);
    }

    public ResponseResult<List<MapTerminalResponse>> aroundSearch(String center,Integer radius){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_AROUND_SEARCH_URL);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + amapSid);
        url.append("&");
        url.append("center=" + center);
        url.append("&");
        url.append("radius=" + radius);

        log.info("请求终端搜索的url:"+ url.toString());

        ResponseEntity<String> postForEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = postForEntity.getBody();
        log.info("终端搜索的响应结果:"+ body);
        
        //解析结果
        JSONObject bodyObject = JSONObject.fromObject(body);
        JSONObject data = bodyObject.getJSONObject("data");

        List<MapTerminalResponse> terminals = new ArrayList<>();

        JSONArray results = data.getJSONArray("results");
        for (int i = 0; i < results.size(); i++) {
            MapTerminalResponse terminalResponse = new MapTerminalResponse();
            JSONObject jsonObject = results.getJSONObject(i);
            String tid = jsonObject.getString("tid");
            //desc存的carId
            //Long carId = jsonObject.getLong("desc");
            String desc = jsonObject.getString("desc");
            Long carId = Long.parseLong(desc);

            JSONObject location = jsonObject.getJSONObject("location");
            String longitude = location.getString("longitude");
            String latitude = location.getString("latitude");

            terminalResponse.setTid(tid);
            terminalResponse.setCarId(carId);
            terminalResponse.setLongitude(longitude);
            terminalResponse.setLatitude(latitude);

            terminals.add(terminalResponse);
        }

        return ResponseResult.success(terminals);

    }

    public ResponseResult<TrsearchResponse> trSearch(String tid,Long startTime,Long endTime){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.TERMINAL_TRSEARCH);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + amapSid);
        url.append("&");
        url.append("tid=" + tid);
        url.append("&");
        url.append("starttime=" + startTime);
        url.append("&");
        url.append("endtime=" + endTime);

        log.info("轨迹搜索的请求url:" + url.toString());
        ResponseEntity<String> postForEntity = restTemplate.getForEntity(url.toString(), String.class);

        JSONObject result = JSONObject.fromObject(postForEntity.getBody());
        log.info("轨迹搜索请求的结果:" + result.toString());

        JSONObject data = result.getJSONObject("data");
        int counts = data.getInt("counts");
        if(counts == 0){
            return null;
        }
        JSONArray tracks = data.getJSONArray("tracks");
        long driverMile = 0L;
        long driverTime = 0L;
        for (int i = 0; i < tracks.size(); i++) {
            JSONObject track = tracks.getJSONObject(i);
            long distance = track.getLong("distance");//米
            long time = track.getLong("time");//毫秒
            time = time/(1000 *60);
            driverMile = driverMile + distance;
            driverTime = driverTime + time;
        }
        TrsearchResponse trsearchResponse = new TrsearchResponse();
        trsearchResponse.setDriverMile(driverMile);
        trsearchResponse.setDriverTime(driverTime);
        return ResponseResult.success(trsearchResponse);
    }
}
