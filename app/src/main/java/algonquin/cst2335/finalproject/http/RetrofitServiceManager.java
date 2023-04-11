package algonquin.cst2335.finalproject.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static final int DEFAULT_CONNECT_TIME = 20;
    private static final int DEFAULT_WRITE_TIME = 30;
    private static final int DEFAULT_READ_TIME = 30;
    private final OkHttpClient okHttpClient;
    public static final String REQUEST_PATH = "http://api.weatherstack.com/";
    private final Retrofit retrofit;

    private RetrofitServiceManager() {

        HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor();
        LoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)// set connect time
                .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)//set default time for write
                .addInterceptor(LoginInterceptor)
                .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)//set read time
                .build();


        retrofit = new Retrofit.Builder()
                .client(okHttpClient)// set http requirement
                .baseUrl(REQUEST_PATH)//set server path
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

//    private static class SingletonHolder {
//        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
//    }

    static RetrofitServiceManager retrofitServiceManager;

    /*
     * get RetrofitServiceManager
     **/
    public static RetrofitServiceManager getInstance() {

        if (retrofitServiceManager == null) {
            retrofitServiceManager = new RetrofitServiceManager();
        }


        return retrofitServiceManager;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}
