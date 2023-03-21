package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.util.NetUtil;
import algonquin.cst2335.finalproject.databinding.ActivityWeatherBinding;



public class Weather extends AppCompatActivity {

    ActivityWeatherBinding binding;

    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private  String[] mCities;

    private TextView tvWeather, tvTem,tvTemLowHigh,tvWind;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(Weather.this);
                builder.setTitle("About").setMessage("Version 1.0, created by Bo Shu").setPositiveButton("Ok", (dialogInterface, i) -> {}).create().show();
                break;
            // TODO: implement other menu items
            case R.id.nasa:
                Toast.makeText(Weather.this, "Welcome to Nasa Mars Rover Photos", Toast.LENGTH_LONG).show();
                //    Intent nasaIntent = new Intent(KittenImage.this, RoverPhotos.class);
                //    startActivity(nasaIntent);
                break;
            case R.id.weather:
                Toast.makeText(Weather.this, "Welcome to WeatherStack", Toast.LENGTH_LONG).show();
                //    Intent weatherIntent = new Intent(KittenImage.this, Weather.class);
                //    startActivity(weatherIntent);
                break;
            case R.id.nytimes:
                Snackbar.make(binding.myToolbar, "Welcome to New York Times", Snackbar.LENGTH_LONG)
                        .setAction("Back to Home Page", click -> {
                            Intent i = new Intent(Weather.this, MainActivity.class);
                            startActivity(i);
                        })
                        .show();
                //    Intent nytIntent = new Intent(KittenImage.this, NewYorkTimes.class);
                //    startActivity(nytIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                String weather = (String) msg.obj;
                Log.d("fan","--receive data of weather--" + weather);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // toolbar
        setSupportActionBar(binding.myToolbar);
        setContentView(R.layout.activity_weather);

        initView();

    }

    private void initView() {
        mSpinner = findViewById(R.id.sp_city);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<>(this,R.layout.sp_item_layout,mCities);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCity = mCities[position];

                getWeatherOfCity(selectedCity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvWeather = findViewById(R.id.tv_weather);
        tvTem = findViewById(R.id.tv_tem);
        tvTemLowHigh = findViewById((R.id.tv_tem_low_high));
        tvWind = findViewById(R.id.tv_wind);
        ivWeather = findViewById(R.id.iv_weather);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);
    }

    private void getWeatherOfCity(String selectedCity) {
        //connect weather api
        new Thread(new Runnable() {
            @Override
            public void run() {
                //connect
                String weatherOfCity = NetUtil.getWeatherOfCity(selectedCity);
                //use handler to transfer data
                Message message = Message.obtain();
                message.what=0;
                message.obj=weatherOfCity;
                mHandler.sendMessage(message);



            }
        }).start();
    }
}