package algonquin.cst2335.finalproject.http;

import algonquin.cst2335.finalproject.data.WeatherHttpResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NetService {

    @POST("current")
    Call<WeatherHttpResponse> getWeather(@Query("access_key") String access_key,
                                         @Query("query") String citiName);
}
