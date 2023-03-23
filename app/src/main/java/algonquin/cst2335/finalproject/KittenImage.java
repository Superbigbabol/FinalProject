package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityKittenImageBinding;
import algonquin.cst2335.finalproject.databinding.FavouriteImageBinding;

public class KittenImage extends AppCompatActivity {

    ActivityKittenImageBinding binding;
    RecyclerView.Adapter myAdapter;
    Bitmap kittenPic;
    RequestQueue queue;

//    ArrayList<FavouritePic> myFavourites = new ArrayList<>();
// todo : create FavouritePic class and implement ViewModelProvider(this).get(XXXViewModel.class);

    // a collection of row objects shown in RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView widthText;
        TextView heightText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            widthText = itemView.findViewById(R.id.width);
            heightText = itemView.findViewById(R.id.height);
            timeText = itemView.findViewById(R.id.time);
        }
    }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(KittenImage.this);
                builder.setTitle("Help")
                        .setMessage("Enter any width and height and fetch image if you like then save it in my favourite list")
                        .setPositiveButton("Ok", (dialogInterface, i) -> {})
                        .create().show();
                break;
            // TODO: implement other menu items
            case R.id.nasa:
                Toast.makeText(KittenImage.this, "Welcome to Nasa Mars Rover Photos", Toast.LENGTH_LONG).show();
                Intent nasaIntent = new Intent(KittenImage.this, MarsPhotoActivity.class);
                startActivity(nasaIntent);
                break;
            case R.id.weather:
                Toast.makeText(KittenImage.this, "Welcome to WeatherStack", Toast.LENGTH_LONG).show();
                Intent weatherIntent = new Intent(KittenImage.this, Weather.class);
                startActivity(weatherIntent);
                break;
            case R.id.nytimes:
                Snackbar.make(binding.myToolbar, "Welcome to New York Times", Snackbar.LENGTH_LONG)
                        .setAction("Back to Home Page", click -> {
                            Intent i = new Intent(KittenImage.this, MainActivity.class);
                            startActivity(i);
                        })
                        .show();
            //    Intent nytIntent = new Intent(KittenImage.this, NewYorkTimes.class);
            //    startActivity(nytIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityKittenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instantiate request queue
        queue = Volley.newRequestQueue(KittenImage.this);

        // toolbar
        setSupportActionBar(binding.myToolbar);

        // adapter for the RecyclerView
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                FavouriteImageBinding fiBinding = FavouriteImageBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(fiBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // ensure MyRowHolder is initialized with correct value
                holder.thumbnail.setImageBitmap(null);
                holder.widthText.setText("");
                holder.heightText.setText("");
                holder.timeText.setText("");

                // todo : temporary code here
                File file = new File(getFilesDir(), "Kitten_600450.png");
                kittenPic = BitmapFactory.decodeFile(file.getPath());
                holder.thumbnail.setImageBitmap(kittenPic);
//                String s = msg.get(position);
                holder.widthText.setText(binding.imgWidth.getText().toString());
                holder.heightText.setText(binding.imgHeight.getText().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                holder.timeText.setText(currentDateandTime);

            }

            @Override
            public int getItemCount() {
                return 1;
//                return msg.size();
            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }
        };
        binding.imgRecyclerView.setAdapter(myAdapter);
        binding.imgRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // shared preferences to save width and height that was entered in last time
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        binding.imgWidth.setText(prefs.getString("ImgWidth", ""));
        binding.imgHeight.setText(prefs.getString("ImgHeight",""));


        // fetch image from web
        binding.retrieveBtn.setOnClickListener(click -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String width = binding.imgWidth.getText().toString();
            String height = binding.imgHeight.getText().toString();
            editor.putString("ImgWidth", width);
            editor.putString("ImgHeight", height);
            editor.apply();
            String url = "https://placekitten.com/"+width+"/"+height;
            ImageRequest imgReq = new ImageRequest(
                    url,
                    bitmap -> {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.imageView.setImageBitmap(bitmap);
                    },
                    1024,
                    1024,
                    ImageView.ScaleType.CENTER,
                    null,
                    error -> Toast.makeText(KittenImage.this, "failed to get response "+error, Toast.LENGTH_SHORT).show()
            );
            queue.add(imgReq);
        });

        // click on save image, the image should be saved to disk, and the width, height, and date & time of when the image was saved should be stored on the database
        binding.saveBtn.setOnClickListener(click -> {
            String fileName = "Kitten_"+ binding.imgWidth.getText().toString() + binding.imgHeight.getText().toString() + ".png";

            //check if kitten img exists
            File file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                // if exists, then use it and do something
                kittenPic = BitmapFactory.decodeFile(file.getPath());
            } else {
                // not exist, create one and save on the disk
                try (FileOutputStream fOut = openFileOutput(fileName, Context.MODE_PRIVATE);) {
                    kittenPic.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // todo : notify RecyclerView
            myAdapter.notifyItemInserted(1);

            // after save the img, the EditText should be cleaned, so other width and height can be entered
            binding.imageView.setImageBitmap(null);
            binding.imgWidth.setText("");
            binding.imgHeight.setText("");
        });
    }
}