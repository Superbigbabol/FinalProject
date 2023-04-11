package algonquin.cst2335.finalproject.data;

import java.io.Serializable;
import java.util.List;

public class WeatherHttpResponse implements Serializable {
    /**
     * request : {"type":"City","query":"Vancouver, Canada","language":"en","unit":"m"}
     * location : {"name":"Vancouver","country":"Canada","region":"British Columbia","lat":"49.250","lon":"-123.133","timezone_id":"America/Vancouver","localtime":"2023-04-06 06:42","localtime_epoch":1680763320,"utc_offset":"-7.0"}
     * current : {"observation_time":"01:42 PM","temperature":7,"weather_code":296,"weather_icons":["https://cdn.worldweatheronline.com/images/wsymbols01_png_64/wsymbol_0017_cloudy_with_light_rain.png"],"weather_descriptions":["Light Rain"],"wind_speed":24,"wind_degree":70,"wind_dir":"ENE","pressure":1013,"precip":0,"humidity":87,"cloudcover":100,"feelslike":5,"uv_index":1,"visibility":24,"is_day":"yes"}
     */

    private LocationEntity location;
    private CurrentEntity current;

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public CurrentEntity getCurrent() {
        return current;
    }

    public void setCurrent(CurrentEntity current) {
        this.current = current;
    }

    public class RequestEntity implements Serializable {
        /**
         * type : City
         * query : Vancouver, Canada
         * language : en
         * unit : m
         */

        private String type;
        private String query;
        private String language;
        private String unit;
    }

    public class LocationEntity implements Serializable {
        /**
         * name : Vancouver
         * country : Canada
         * region : British Columbia
         * lat : 49.250
         * lon : -123.133
         * timezone_id : America/Vancouver
         * localtime : 2023-04-06 06:42
         * localtime_epoch : 1680763320
         * utc_offset : -7.0
         */

        private String localtime;

        public String getLocaltime() {
            return localtime;
        }

        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }
    }

    public class CurrentEntity implements Serializable {
        /**
         * observation_time : 01:42 PM
         * temperature : 7
         * weather_code : 296
         * weather_icons : ["https://cdn.worldweatheronline.com/images/wsymbols01_png_64/wsymbol_0017_cloudy_with_light_rain.png"]
         * weather_descriptions : ["Light Rain"]
         * wind_speed : 24
         * wind_degree : 70
         * wind_dir : ENE
         * pressure : 1013
         * precip : 0
         * humidity : 87
         * cloudcover : 100
         * feelslike : 5
         * uv_index : 1
         * visibility : 24
         * is_day : yes
         */

        private int temperature;
        private int wind_speed;
        private List<String> weather_icons;
        private List<String> weather_descriptions;

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(int wind_speed) {
            this.wind_speed = wind_speed;
        }

        public List<String> getWeather_icons() {
            return weather_icons;
        }

        public void setWeather_icons(List<String> weather_icons) {
            this.weather_icons = weather_icons;
        }

        public List<String> getWeather_descriptions() {
            return weather_descriptions;
        }

        public void setWeather_descriptions(List<String> weather_descriptions) {
            this.weather_descriptions = weather_descriptions;
        }
    }
}
