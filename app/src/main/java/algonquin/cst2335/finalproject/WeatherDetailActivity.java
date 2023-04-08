package algonquin.cst2335.finalproject;

import algonquin.cst2335.finalproject.data.WeatherBeen;
import algonquin.cst2335.finalproject.databinding.ActivityWeatherDetailBinding;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class WeatherDetailActivity extends AppCompatActivity {

    ActivityWeatherDetailBinding binding;

    private WeatherBeen weather;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherDetailActivity.this);
                builder.setTitle("About").setMessage("Version 1.0, created by Junqi Hong").setPositiveButton("Ok", (dialogInterface, i) -> {
                }).create().show();
                break;
            // TODO: implement other menu items
            case R.id.nasa:
                Toast.makeText(WeatherDetailActivity.this, "Welcome to Nasa Mars Rover Photos", Toast.LENGTH_LONG).show();
                Intent nasaIntent = new Intent(WeatherDetailActivity.this, MarsPhotoActivity.class);
                startActivity(nasaIntent);
                break;
            case R.id.kittenimage:
                Toast.makeText(WeatherDetailActivity.this, "Welcome to Kitten Placeholder Images", Toast.LENGTH_LONG).show();
                Intent weatherIntent = new Intent(WeatherDetailActivity.this, KittenImage.class);
                startActivity(weatherIntent);
                break;
            case R.id.newyorktimes:
                Snackbar.make(binding.myToolbar, "Welcome to New York Times", Snackbar.LENGTH_LONG)
                        .setAction("Back to Home Page", click -> {
                            Intent i = new Intent(WeatherDetailActivity.this, MainActivity.class);
                            startActivity(i);
                        }).show();
                //    Intent nytIntent = new Intent(WeatherDetailActivity.this, NewYorkTimes.class);
                //    startActivity(nytIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWeatherDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // toolbar
        setSupportActionBar(binding.myToolbar);

        weather = (WeatherBeen) getIntent().getSerializableExtra("detail");

        Glide.with(WeatherDetailActivity.this).load(weather.getImage()).into(binding.ivWeather);
        binding.tvCity.setText(weather.getCity());
        binding.tvTem.setText(weather.getTemp() + "°");
        binding.tvWind.setText("wind:" + weather.getWind() + "kmph");
        binding.tvWeather.setText(weather.getWeather() + "(" + weather.getTimeStr() + ")");

    }
}