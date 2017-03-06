package com.james.coolweather.gson;

/**
 * Created by James on 2017/3/6.
 */

public class Now {

    public String fl;
    public String hum;
    public String pcpn;
    public String pres;
    public String tmp;
    public String vis;

    public Cond cond;

    public Wind wind;

    public class Cond{
        public String code;
        public String txt;
    }

    public class Wind {
        public String deg;
        public String dir;
        public String sc;
        public String spd;
    }
}
