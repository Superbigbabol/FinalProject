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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.data.MarsPhoto;
import algonquin.cst2335.finalproject.databinding.ActivityMarsPhotoBinding;
import algonquin.cst2335.finalproject.databinding.ResultImageBinding;

public class MarsPhotoActivity extends AppCompatActivity {


    private static final String API_KEY = "LjA7bPstC59frg4qGHOJZ82NgforWzwuezT4eJKp";
    private static final String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";


    ActivityMarsPhotoBinding binding;
    RecyclerView.Adapter myAdapter;
    Bitmap marsPic;

//    ArrayList<FavouritePic> myFavourites = new ArrayList<>();
// todo : create FavouritePic class and implement ViewModelProvider(this).get(XXXViewModel.class);

    // a collection of row objects shown in RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView roverName;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            roverName = itemView.findViewById(R.id.roverName);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MarsPhotoActivity.this);
                builder.setTitle("About").setMessage("Version 1.0, created by Xiangwu Dai").setPositiveButton("Ok", (dialogInterface, i) -> {}).create().show();
                break;
            // TODO: implement other menu items
            case R.id.kittenImg:
                Toast.makeText(MarsPhotoActivity.this, "Welcome to Nasa Mars Rover Photos", Toast.LENGTH_LONG).show();
                Intent kittenIntent = new Intent(MarsPhotoActivity.this, KittenImage.class);
                startActivity(kittenIntent);
                break;
            case R.id.weather:
                Toast.makeText(MarsPhotoActivity.this, "Welcome to WeatherStack", Toast.LENGTH_LONG).show();
                //    Intent weatherIntent = new Intent(MarsPhotoActivity.this, Weather.class);
                //    startActivity(weatherIntent);
                break;
            case R.id.nytimes:
                Snackbar.make(binding.myToolbar, "Welcome to New York Times", Snackbar.LENGTH_LONG)
                        .setAction("Back to Home Page", click -> {
                            Intent i = new Intent(MarsPhotoActivity.this, MainActivity.class);
                            startActivity(i);
                        })
                        .show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMarsPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // toolbar
        setSupportActionBar(binding.myToolbar);

        // adapter for the RecyclerView
        myAdapter = new RecyclerView.Adapter<MarsPhotoActivity.MyRowHolder>() {
            @NonNull
            @Override
            public MarsPhotoActivity.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ResultImageBinding reBinding = ResultImageBinding.inflate(getLayoutInflater(), parent, false);
                return new MarsPhotoActivity.MyRowHolder(reBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MarsPhotoActivity.MyRowHolder holder, int position) {
                // ensure MyRowHolder is initialized with correct value
                holder.thumbnail.setImageBitmap(null);
                holder.roverName.setText("");

                // todo : temporary code here

                ;

                File file = new File(getFilesDir(), "search0.bmp");
                marsPic  = BitmapFactory.decodeFile(file.getPath());
                holder.thumbnail.setImageBitmap(marsPic);
                holder.roverName.setText("sol");


                /*


                File file = new File(getFilesDir(), "Kitten_600450.png");
                kittenPic = BitmapFactory.decodeFile(file.getPath());
                holder.thumbnail.setImageBitmap(kittenPic);
//                String s = msg.get(position);
                holder.widthText.setText(binding.imgWidth.getText().toString());
                holder.heightText.setText(binding.imgHeight.getText().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateandTime = sdf.format(new Date());
                holder.timeText.setText(currentDateandTime);

                 */

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
        binding.sol.setText(prefs.getString("solarDayOnMars", ""));



        // fetch image from web
        binding.retrieveBtn.setOnClickListener(click -> {
            String sol = binding.sol.getText().toString();
            editor.putString("solarDayOnMars", sol);

            editor.apply();

            if (!sol.isEmpty()) {
                retrievePhotos(sol);
            }


            /*
            String url = "https://placekitten.com/"+width+"/"+height;

            // todo : retrieve image by Executor, but in the instruction not using Executor or AsyncTask, so this part needs to be changed to Volley
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                try(InputStream inputStream = new URL(url).openStream();) {
                    kittenPic = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> binding.imageView.setImageBitmap(kittenPic));
            });

             */

            myAdapter.notifyItemInserted(1);

            // after save the img, the EditText should be cleaned, so other width and height can be entered
            binding.imageView.setImageBitmap(null);
            binding.sol.setText("");

        });

        // click on save image, the image should be saved to disk, and the width, height, and date & time of when the image was saved should be stored on the database
        binding.saveBtn.setOnClickListener(click -> {

            /*String fileName = "Kitten_"+ binding.imgWidth.getText().toString() + binding.imgHeight.getText().toString() + ".png";

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

             */
        });
    }


    private void retrievePhotos(String sol) {
        String url = String.format("%s?sol=%s&api_key=%s", BASE_URL, sol, API_KEY);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<MarsPhoto> photos = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String id = object.getString("id");
                                String imgSrc = object.getString("img_src");
                                String roverName = object.getString("rover__name");
                                String cameraName = object.getString("camera__name");
                                photos.add(new MarsPhoto(id, imgSrc, roverName, cameraName));
                            }
                          //  myAdapter.updatePhotos(photos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MarsPhotoActivity.this, "Error retrieving photos", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}