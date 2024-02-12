package com.ling.servicemap.service;

import com.ling.internalcommon.constant.AmapConfigConstants;
import com.ling.internalcommon.constant.CommonStatusEnum;
import com.ling.internalcommon.dto.DicDistrict;
import com.ling.internalcommon.dto.ResponseResult;
import com.ling.servicemap.mapper.DicDistrictMapper;
import com.ling.servicemap.remote.MapDistrictClient;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DicDistrictService {

    @Autowired
    MapDistrictClient mapDistrictClient;

    @Autowired
    DicDistrictMapper dicDistrictMapper;

    /**
     * 行政区域数据库初始化
     * @param keywords 要查询的行政区域范围，暂时使用中国
     * @return
     */
    public ResponseResult initDicDistrict(String keywords){

        //拼装并请求URL
        String dicDistrictResult = mapDistrictClient.dicDistrict(keywords);

        //解析结果
        JSONObject dicDistrictJsonObject = JSONObject.fromObject(dicDistrictResult);
        int status = dicDistrictJsonObject.getInt(AmapConfigConstants.Status);
        if(status != 1){
            return ResponseResult.fail(CommonStatusEnum.MAP_DISTRICT_ERROR.getCode(),CommonStatusEnum.MAP_DISTRICT_ERROR.getValue());
        }
        JSONArray countryJsonArray = dicDistrictJsonObject.getJSONArray(AmapConfigConstants.DISTRICTS);

        for (int i = 0; i < countryJsonArray.size(); i++) {
            JSONObject countryJsonObject = countryJsonArray.getJSONObject(i);
            String countryAddressCode = countryJsonObject.getString(AmapConfigConstants.ADCODE);
            String countryAddressName = countryJsonObject.getString(AmapConfigConstants.ADNAME);
            String countryLevel = countryJsonObject.getString(AmapConfigConstants.LEVEL);
            int countryLevelInt = generateLevel(countryLevel);
            String countryParentAddressCode = "0";
            //组装成对象并插入数据库
            insertDistrict(countryAddressCode,countryAddressName,countryLevelInt,countryParentAddressCode);

            JSONArray provinceJsonArray = countryJsonObject.getJSONArray(AmapConfigConstants.DISTRICTS);

            for (int j = 0; j < provinceJsonArray.size(); j++) {
                JSONObject provinceJsonObject = provinceJsonArray.getJSONObject(j);
                String provinceAddressCode = provinceJsonObject.getString(AmapConfigConstants.ADCODE);
                String provinceAddressName = provinceJsonObject.getString(AmapConfigConstants.ADNAME);
                String provinceLevel = provinceJsonObject.getString(AmapConfigConstants.LEVEL);
                int provinceLevelInt = generateLevel(provinceLevel);
                String provinceParentAddressCode = countryAddressCode;// districtJsonObject.getString(AmapConfigConstants.ADCODE);

                insertDistrict(provinceAddressCode,provinceAddressName,provinceLevelInt,provinceParentAddressCode);

                JSONArray cityJsonArray = provinceJsonObject.getJSONArray(AmapConfigConstants.DISTRICTS);

                for (int k = 0; k < cityJsonArray.size(); k++) {
                    JSONObject cityJsonObject = cityJsonArray.getJSONObject(k);
                    String cityAddressCode = cityJsonObject.getString(AmapConfigConstants.ADCODE);
                    String cityAddressName = cityJsonObject.getString(AmapConfigConstants.ADNAME);
                    String cityLevel = cityJsonObject.getString(AmapConfigConstants.LEVEL);

                    if(cityLevel.equals(AmapConfigConstants.STREET)){
                        continue;
                    }
                    int cityLevelInt = generateLevel(cityLevel);
                    String cityParentAddressCode = provinceAddressCode;// provinceJsonObject.getString(AmapConfigConstants.ADCODE);

                    insertDistrict(cityAddressCode,cityAddressName,cityLevelInt,cityParentAddressCode);

                    JSONArray distritsJsonArray = cityJsonObject.getJSONArray(AmapConfigConstants.DISTRICTS);

                    for (int l = 0; l < distritsJsonArray.size(); l++) {
                        JSONObject distritsJsonObject = distritsJsonArray.getJSONObject(l);
                        String distritsAddressCode = distritsJsonObject.getString(AmapConfigConstants.ADCODE);
                        String distritsAddressName = distritsJsonObject.getString(AmapConfigConstants.ADNAME);
                        String distritsLevel = distritsJsonObject.getString(AmapConfigConstants.LEVEL);
                        if(distritsLevel.equals(AmapConfigConstants.STREET)){
                            continue;
                        }
                        int distritsLevelInt = generateLevel(distritsLevel);
                        String distritsParentAddressCode = cityAddressCode;// cityJsonObject.getString(AmapConfigConstants.ADCODE);

                        insertDistrict(distritsAddressCode,distritsAddressName,distritsLevelInt,distritsParentAddressCode);

                    }

                }


            }
        }



        return ResponseResult.success();
    }

    public void insertDistrict(String addressCode,String addressName,int levelInt,String parentAddressCode){
        DicDistrict dicDistrict = new DicDistrict();
        dicDistrict.setAddressCode(addressCode);
        dicDistrict.setAddressName(addressName);
        dicDistrict.setLevel(levelInt);
        dicDistrict.setParentAddressCode(parentAddressCode);

        //插入数据库
        dicDistrictMapper.insert(dicDistrict);
    }

    public void generateDistrict(JSONArray districtsJsonArray,String parentAddressCode){
        for (int i = 0; i <= districtsJsonArray.size() ; i++) {
            JSONObject districtJsonObject = districtsJsonArray.getJSONObject(i);
            String addressCode = districtJsonObject.getString(AmapConfigConstants.ADCODE);
            String addressName = districtJsonObject.getString(AmapConfigConstants.ADNAME);
            String level = districtJsonObject.getString(AmapConfigConstants.LEVEL);
            int levelInt = generateLevel(level);

            //组装成对象并插入数据库
            insertDistrict(addressCode,addressName,levelInt,parentAddressCode);
        }
    }


    public int generateLevel(String level){
        int levelInt = 0;
        if(level.trim().equals("country")){
            levelInt = 0;
        } else if (level.trim().equals("province")) {
            levelInt = 1;
        } else if (level.trim().equals("city")) {
            levelInt = 2;
        } else if (level.trim().equals("district")) {
            levelInt = 3;
        }

        return levelInt;
    }
}
