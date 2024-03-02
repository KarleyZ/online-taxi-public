package com.ling.servicemap.remote;

import com.ling.internalcommon.constant.AmapConfigConstants;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.internalcommon.request.PointDTO;
import com.ling.internalcommon.request.PointRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class MapPointClient {

    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String amapSid;

    @Autowired
    RestTemplate restTemplate;

    public ResponseResult upload(PointRequest pointRequest){

        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstants.POINT_UPLOAD_URL);
        url.append("?");
        url.append("key=" + amapKey);
        url.append("&");
        url.append("sid=" + amapSid);
        url.append("&");
        url.append("tid=" + pointRequest.getTid());
        url.append("&");
        url.append("trid=" + pointRequest.getTrid());
        url.append("&");
        url.append("points=" );
        url.append("%5B");//[
        PointDTO[] points = pointRequest.getPoints();
        for (PointDTO point : points) {
            url.append("%7B");//{
            String location = point.getLocation();
            String locatetime = point.getLocatetime();
            url.append(AmapConfigConstants.TRACE_LOCATION);
            url.append("%3A");
            url.append("%22" + location + "%22");//"
            url.append("%B4");//,
            url.append(AmapConfigConstants.TRACE_LOCATION_TIME);
            url.append("%3A");//:
            url.append("%22" + locatetime + "%22");
            url.append("%7D");//}
        }
        url.append("%5D");//]

        log.info("请求轨迹上传的url:"+ url.toString());

        ResponseEntity<String> postForEntity = restTemplate.postForEntity(URI.create(url.toString()), null, String.class);
        log.info("上传轨迹相应结果："+ postForEntity.getBody());

        return ResponseResult.success();
    }
}
