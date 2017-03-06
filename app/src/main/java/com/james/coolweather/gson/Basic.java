package com.james.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by James on 2017/3/6.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("cnty")
    public String country;

    @SerializedName("id")
    public String weatherId;

    public String lat;

    @SerializedName("lon")
    public String lag;

    @SerializedName("prov")
    public String province;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

        @SerializedName("utc")
        public String utcTime;
    }
}
