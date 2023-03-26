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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.data.MarsPhoto;
import algonquin.cst2335.finalproject.data.MarsPhotoViewModel;
import algonquin.cst2335.finalproject.databinding.ActivityMarsPhotoBinding;
import algonquin.cst2335.finalproject.databinding.ResultImageBinding;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MarsPhotoActivity extends AppCompatActivity {

    private ArrayList<MarsPhoto> photoList;



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
                Intent weatherIntent = new Intent(MarsPhotoActivity.this, Weather.class);
                startActivity(weatherIntent);
                break;
            case R.id.nytimes:
                Snackbar.make(binding.myToolbar, "Welcome to New York Times", Snackbar.LENGTH_LONG)
                        .setAction("Back to Home Page", click -> {
                            Intent i = new Intent(MarsPhotoActivity.this, MainActivity.class);
                            startActivity(i);
                        })
                        .show();
                //    Intent nytIntent = new Intent(MarsPhotoActivity.this, NewYorkTimes.class);
                //    startActivity(nytIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMarsPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       MarsPhotoViewModel mvm = new ViewModelProvider(this).get(MarsPhotoViewModel.class);

        photoList = mvm.photos.getValue();

        if(photoList == null){
            mvm.photos.postValue(photoList = new ArrayList<MarsPhoto>());
        }



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
//                holder.thumbnail.setImageBitmap(null);
//                holder.roverName.setText("");
                String imgUrl = photoList.get(position).getImgSrc();
                String roverName = photoList.get(position).getRoverName();
                Glide.with(holder.thumbnail.getContext())
                        .load(imgUrl)
                        .thumbnail(0.5f)
                        .into(holder.thumbnail);
                holder.roverName.setText(roverName);
            }

            @Override
            public int getItemCount() {
                return photoList.size();
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

            mvm.photos.postValue(photoList);


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
        photoList = new ArrayList<MarsPhoto>();





        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON data here

                        try {
                            JSONArray photosArray = response.getJSONArray("photos");
                            for (int i = 0; i < photosArray.length(); i++) {
                                JSONObject photoObject = photosArray.getJSONObject(i);
                                String id = photoObject.getString("id");
                                String imageUrl = photoObject.getString("img_src");
                                JSONObject cameraObject = photoObject.getJSONObject("camera");
                                String cameraName = cameraObject.getString("full_name");
                                JSONObject roverObject = photoObject.getJSONObject("rover");
                                String roverName = roverObject.getString("name");



                                MarsPhoto photo = new MarsPhoto(id, imageUrl, roverName, cameraName);
                                photoList.add(photo);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                    }
                });

        queue.add(jsonObjectRequest);


    }

}