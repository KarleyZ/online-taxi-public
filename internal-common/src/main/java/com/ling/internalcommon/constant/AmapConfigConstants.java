package com.ling.internalcommon.constant;

public class AmapConfigConstants {

    //高德地图API请求url通用部分
    public static final String DIRECTION_URL = "https://restapi.amap.com/v3/direction/driving";

    //高德地图API行政区域的url通用部分
    public static final String DISTRICT_URL = "https://restapi.amap.com/v3/config/district";

    //高德地图中猎鹰系统的url通用部分：服务的创建
    public static final String SERVICE_ADD_URL = "https://tsapi.amap.com/v1/track/service/add";

    //高德地图中猎鹰系统的url通用部分：terminal的创建
    public static final String TERMINAL_ADD_URL = "https://tsapi.amap.com/v1/track/terminal/add";

    //高德地图中猎鹰系统的url通用部分：trace的创建
    public static final String Trace_ADD_URL =  "https://tsapi.amap.com/v1/track/trace/add";

    //高德地图中猎鹰系统的url通用部分：轨迹的上传
    public static final String POINT_UPLOAD_URL = "https://tsapi.amap.com/v1/track/point/upload";

    //高德地图中猎鹰系统的url通用部分：终端的周边搜索
    public static final String TERMINAL_AROUND_SEARCH_URL = "https://tsapi.amap.com/v1/track/terminal/aroundsearch";

    //高德地图中猎鹰系统url通用部分：轨迹查询
    public static final String TERMINAL_TRSEARCH = "https://tsapi.amap.com/v1/track/terminal/trsearch";

    /**
     * 路径规划 json key值
     */
    public static final String Status = "status";
    public static final String ROUTE = "route";
    public static final String PATHS = "paths";
    public static final String DISTANCE = "distance";
    public static final String DURATION = "duration";

    /**
     * 行政区域请求的josn中的key值
     */
    public static final String DISTRICTS = "districts";
    public static final String ADCODE = "adcode";
    public static final String ADNAME = "name";
    public static final String LEVEL = "level";
    public static final String STREET = "street";

    /**
     * 猎鹰轨迹上传时可上传的参数名(URL)
     */
    public static final String TRACE_LOCATION = "%22location%22";
    public static final String TRACE_LOCATION_TIME = "%22locatetime%22";
}
