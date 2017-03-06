package com.james.coolweather.gson;

/**
 * Created by James on 2017/3/6.
 */

public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String qlty;
        public String so2;
    }
}
