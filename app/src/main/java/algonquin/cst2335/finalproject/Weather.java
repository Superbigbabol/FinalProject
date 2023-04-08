package algonquin.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.adapter.WeatherAdapter;
import algonquin.cst2335.finalproject.data.WeatherBeen;
import algonquin.cst2335.finalproject.data.WeatherDAO;
import algonquin.cst2335.finalproject.data.WeatherDatabase;
import algonquin.cst2335.finalproject.data.WeatherHttpResponse;
import algonquin.cst2335.finalproject.databinding.ActivityWeatherBinding;
import algonquin.cst2335.finalproject.http.NetService;
import algonquin.cst2335.finalproject.http.RetrofitServiceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Weather extends AppCompatActivity {
    ActivityWeatherBinding binding;

    private WeatherBeen currentWeather;
    private List<WeatherBeen> weatherBeens = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(Weather.this);
                builder.setTitle("About").setMessage("Version 1.0, created by Junqi Hong").setPositiveButton("Ok", (dialogInterface, i) -> {
                }).create().show();
                break;
            case R.id.nasa:
                Toast.makeText(Weather.this, "Welcome to Nasa Mars Rover Photos", Toast.LENGTH_SHORT).show();
                Intent nasaIntent = new Intent(Weather.this, MarsPhotoActivity.class);
                startActivity(nasaIntent);
                break;
            case R.id.kittenimage:
                Toast.makeText(Weather.this, "Welcome to Kitten Placeholder Images", Toast.LENGTH_SHORT).show();
                Intent weatherIntent = new Intent(Weather.this, KittenImage.class);
                startActivity(weatherIntent);
                break;
            case R.id.newyorktimes:
                Toast.makeText(Weather.this, "Welcome to New York Times", Toast.LENGTH_SHORT).show();
                Intent nytIntent = new Intent(Weather.this, NewYorkTimeActivity.class);
                startActivity(nytIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weather = (String) msg.obj;
                Log.d("fan", "--receive data of weather--" + weather);
            } else if (msg.what == 1) {

                weatherAdapter.notifyDataSetChanged();

            } else if (msg.what == 2) {

                weatherBeens.remove(msg.arg1);
                weatherAdapter.notifyDataSetChanged();

            }
        }
    };

    WeatherDAO weatherDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WeatherDatabase db = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "Weather").build();
        weatherDAO = db.initDAO();

        // toolbar
        setSupportActionBar(binding.myToolbar);

        initView();
    }

    Executor thread = Executors.newSingleThreadExecutor();

    private void getAllWeather() {

        thread.execute(() -> {
            weatherBeens.clear();
            weatherBeens.addAll(weatherDAO.getAllWeathers());
            Log.e("weatherBeens", weatherBeens.size() + "个");

            mHandler.sendEmptyMessage(1);
        });
    }

    private void initView() {

        // shared preferences to save width and height that was entered in last time
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        binding.etCity.setText(prefs.getString("city", ""));

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etCity.getText().toString())) {
                    return;
                }

                editor.putString("city", binding.etCity.getText().toString());
                editor.apply();

                search();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentWeather == null) {
                    return;
                }
                thread.execute(() -> {
                    weatherBeens.add(currentWeather);
                    weatherDAO.insertWeather(currentWeather);

                    getAllWeather();
                    mHandler.sendEmptyMessageDelayed(1, 200);
                });
            }
        });

        weatherAdapter = new WeatherAdapter(this, weatherBeens, new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                thread.execute(() -> {
                    weatherDAO.deleteWeather(weatherBeens.get(position));
                    Message message = new Message();
                    message.arg1 = position;
                    message.what = 2;
                    mHandler.sendMessageDelayed(message, 200);
                });

            }

            @Override
            public void onDetailClicked(int position) {
                Intent intent = new Intent(Weather.this,WeatherDetailActivity.class);
                intent.putExtra("detail",weatherBeens.get(position));
                startActivity(intent);
            }
        });
        binding.rvFutureWeather.setAdapter(weatherAdapter);

        getAllWeather();

    }

    private void search() {

        RetrofitServiceManager.getInstance()
                .create(NetService.class)
                .getWeather("8e99fc29654b0fe11b596a68d6548ac3", binding.etCity.getText().toString())
                .enqueue(new Callback<WeatherHttpResponse>() {
                    @Override
                    public void onResponse(Call<WeatherHttpResponse> call, Response<WeatherHttpResponse> response) {
                        if (response != null) {
                            if (response.body() != null) {

                                WeatherBeen weatherBeen = new WeatherBeen();
                                weatherBeen.setWeather(response.body().getCurrent().getWeather_descriptions().get(0));
                                weatherBeen.setImage(response.body().getCurrent().getWeather_icons().get(0));
                                weatherBeen.setTemp(response.body().getCurrent().getTemperature() + "");
                                weatherBeen.setWind(response.body().getCurrent().getWind_speed() + "");
                                weatherBeen.setTimeStr(response.body().getLocation().getLocaltime());
                                weatherBeen.setCity(binding.etCity.getText().toString());

                                Glide.with(Weather.this).load(weatherBeen.getImage()).into(binding.ivWeather);
                                binding.tvTem.setText(weatherBeen.getTemp() + "°");
                                binding.tvWind.setText("wind:" + weatherBeen.getWind() + "kmph");
                                binding.tvWeather.setText(weatherBeen.getWeather() + "(" + weatherBeen.getTimeStr() + ")");

                                currentWeather = weatherBeen;

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<WeatherHttpResponse> call, Throwable t) {
                        Log.e("getWeather", "onFailure");
                        Toast.makeText(Weather.this, "Search Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}