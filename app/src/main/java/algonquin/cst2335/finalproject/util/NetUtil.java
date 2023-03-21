package algonquin.cst2335.finalproject.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {
    public static String apiKey = "8e99fc29654b0fe11b596a68d6548ac3";
    public static final String URL_WEATHER_WITH_FUTURE = "http://api.weatherstack.com/current" + apiKey;

    public static String doGet(String urlStr){

        String result ="";
        //connect to web
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            //read data
            InputStream inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            //binary data to memory
            bufferedReader = new BufferedReader(inputStreamReader);
            //read data from memory
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();


        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
            if(inputStreamReader != null){
                try{
                    inputStreamReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }


        }

        return result;
    }

    public static String getWeatherOfCity(String city){
        String result = "";
        //combine weather data URL
        String weatherUrl = URL_WEATHER_WITH_FUTURE + "&city=" + city;
        Log.d("fan","----weatherUrl----" + weatherUrl);

        String weatherResult =  doGet(weatherUrl);
        Log.d("fan","----weatherUrl----" + weatherResult);
        return  weatherResult;
    }
}
