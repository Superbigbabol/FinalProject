package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO: integrate other Activities into this MainActivity
        binding.nasaImgBtn.setOnClickListener( click -> {
            Intent nasaIntent = new Intent(MainActivity.this, MarsPhotoActivity.class);
            startActivity(nasaIntent);
            Toast.makeText(MainActivity.this, "You are in Nasa Rover", Toast.LENGTH_SHORT).show();
        });

        binding.kittenImgBtn.setOnClickListener( click -> {
            Intent kittenIntent = new Intent(MainActivity.this, KittenImage.class);
            startActivity(kittenIntent);
            Toast.makeText(MainActivity.this, "You are in Kitten Image", Toast.LENGTH_SHORT).show();
        });

//        binding.nytImgBtn.setOnClickListener( click -> {
//            Intent nytIntent = new Intent(MainActivity.this, NewYorkTimes.class);
//            startActivity(nytIntent);
//            Toast.makeText(MainActivity.this, "You are in New York Times", Toast.LENGTH_SHORT).show();
//        });

//        binding.weatherImgBtn.setOnClickListener( click -> {
//            Intent weatherIntent = new Intent(MainActivity.this, Weather.class);
//            startActivity(weatherIntent);
//            Toast.makeText(MainActivity.this, "You are in WeatherStack", Toast.LENGTH_SHORT).show();
//        });
    }
}