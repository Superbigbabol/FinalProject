package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.data.MarsPhoto;
import algonquin.cst2335.finalproject.data.MarsPhotoDao;
import algonquin.cst2335.finalproject.data.MarsPhotoDatabase;
import algonquin.cst2335.finalproject.data.MarsPhotoViewModel;
import algonquin.cst2335.finalproject.data.PhotoFragment;
import algonquin.cst2335.finalproject.databinding.ActivityMarsPhotoBinding;
import algonquin.cst2335.finalproject.databinding.ResultImageBinding;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class MarsPhotoActivity extends AppCompatActivity {

    private ArrayList<MarsPhoto> photoList;
    private boolean isSavedList = true;


    private static final String API_KEY = "CrXmeT8aWrb0WFvtkfJDwf2ue6BVIn4LsDJRScxV";
    private static final String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";


    ActivityMarsPhotoBinding binding;
    RecyclerView.Adapter myAdapter;

    Bitmap marsPic;
    String url0 = "http://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044630890503649E02_DXXX.jpg";
    String url1 = "https://placekitten.com/100/100";
    ArrayList<Bitmap> bitmapList = new ArrayList<>();

    MarsPhotoDao mDao;
    public MarsPhotoViewModel mvm;
    int position;
    PhotoFragment prevFragment;


//    ArrayList<FavouritePic> myFavourites = new ArrayList<>();
// todo : create FavouritePic class and implement ViewModelProvider(this).get(XXXViewModel.class);

    // a collection of row objects shown in RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView roverName;
        TextView photoID;
        public TextView aView;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            roverName = itemView.findViewById(R.id.roverName);
            photoID = itemView.findViewById(R.id.photoID);

            itemView.setOnClickListener(click -> {
                position = getAbsoluteAdapterPosition();
                MarsPhoto selected = photoList.get(position);
                mvm.selectedPhoto.postValue(selected);

                if(isSavedList){

                    aView = binding.snackbar;

                    AlertDialog.Builder builder = new AlertDialog.Builder(MarsPhotoActivity.this);
                    builder.setMessage("Do you want to delete this photo?");
                    builder.setTitle("Warning!!");
                    builder.setPositiveButton("OK", (dialog, which)->{
                        Executor thread_1 = Executors.newSingleThreadExecutor();
                        thread_1.execute(()->{

                            mDao.deletePhoto(selected);
                            photoList.remove(position);
                            runOnUiThread(()->{
                                myAdapter.notifyItemRemoved(position);
                                Snackbar.make(aView, "Item deleted", Snackbar.LENGTH_LONG)
                                        .setAction("Undo", clk ->{
                                            Executor thread_2 = Executors.newSingleThreadExecutor();
                                            thread_2.execute(()->{

                                                mDao.insertPhoto(selected);
                                                photoList.add(position,selected);
                                                runOnUiThread(()->{
                                                    myAdapter.notifyItemInserted(position);
                                                });


                                            });
                                        })
                                        .show();

                            });

                        });
                    });
                    builder.setNegativeButton("Cancel", (dialog, which)->{

                    });
                    builder.create().show();

                }



            });


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
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MarsPhotoActivity.this);
                builder.setTitle("About").setMessage("Version 1.0, created by Xiangwu Dai").setPositiveButton("Ok", (dialogInterface, i) -> {
                }).create().show();
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

        mvm = new ViewModelProvider(this).get(MarsPhotoViewModel.class);

        //   photoList = mvm.photos.getValue();

        MarsPhotoDatabase db = Room.databaseBuilder(getApplicationContext(), MarsPhotoDatabase.class, "database-name").fallbackToDestructiveMigration().build();
        mDao = db.mpDao();

        mvm.selectedPhoto.observe(this, (newValue) -> {

            PhotoFragment pFragment = new PhotoFragment(newValue);
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            if (prevFragment != null) {
                tx.remove(prevFragment);
            }

            tx.add(R.id.fragmentLocation, pFragment);
            prevFragment = pFragment;
            tx.commit();
            tx.addToBackStack("");
        });


        if (photoList == null) {
            mvm.photoList.setValue(photoList = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                photoList.addAll(mDao.getAllPhotos()); //Once you get the data from database

                runOnUiThread(() -> binding.imgRecyclerView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
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
                String imageUrl = photoList.get(position).getImgSrc();
                String roverName = photoList.get(position).getRoverName();
                String photoID = photoList.get(position).getId();


/*
                ImageRequest imageRequest = new ImageRequest(
                        imageUrl,
                        bitmap ->{

//                            public void onResponse(Bitmap response) {
                                // Use Glide to display the bitmap in the ImageView
                                Glide.with( holder.itemView.getContext())
                                        .load(bitmap)
                                        .centerCrop()
                                        .placeholder(R.drawable.placeholder_image)
                                        .error(R.drawable.error_image)
                                        .into(holder.thumbnail);
//                            }
                        },
                        0, 0,
                        ImageView.ScaleType.CENTER_CROP,
                        Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                );
                Volley.newRequestQueue(holder.itemView.getContext()).add(imageRequest);

 */


//                Picasso.get().load(imgUrl).into(holder.thumbnail);
//                holder.thumbnail.setImageBitmap(bitmapList.get(position));


//                new Thread(() -> {
//                    try {
//                        // Load the image from the URL using Picasso
//                        Bitmap bitmap = Picasso.get().load(imageUrl).get();
//                        // Update the UI on the main thread with the loaded image
//                        runOnUiThread(() -> holder.thumbnail.setImageBitmap(bitmap));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }).start();

//                Glide.with(holder.thumbnail.getContext())

                RequestBuilder<Drawable> requestBuilder =
                        Glide.with(holder.itemView.getContext())
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.placeholder);
                requestBuilder.into(holder.thumbnail);


                holder.roverName.setText(roverName);
                holder.photoID.setText(photoID);
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

            isSavedList = false;
            String sol = binding.sol.getText().toString();
            editor.putString("solarDayOnMars", sol);
            editor.apply();
            String searchList = getString(R.string.searchList);
            binding.myFavourite.setText(searchList);

            photoList = new ArrayList<>();
            mvm.photoList.setValue(photoList = new ArrayList<>());
            binding.imgRecyclerView.setAdapter(myAdapter);

 //           bitmapList = new ArrayList<>();

//            Glide.with(this)
//                    .load(url1)
//                    .thumbnail(0.5f)
//                    .into(binding.imageView);


            RequestQueue queue = Volley.newRequestQueue(this);

            if (!sol.isEmpty()) {
                String url = String.format("%s?sol=%s&api_key=%s", BASE_URL, sol, API_KEY);


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, (response) -> {
                    try {
                        JSONArray photosArray = response.getJSONArray("photos");
                        for (int i = 0; i < Math.min(photosArray.length(), 5); i++) {
//                        for (int i = 0; i < 1; i++) {
                            JSONObject photoObject = photosArray.getJSONObject(i);
                            String id = photoObject.getString("id");
                            String imageUrl = photoObject.getString("img_src");
                            JSONObject cameraObject = photoObject.getJSONObject("camera");
                            String cameraName = cameraObject.getString("full_name");
                            JSONObject roverObject = photoObject.getJSONObject("rover");
                            String roverName = roverObject.getString("name");

                            MarsPhoto photo = new MarsPhoto(id, imageUrl, roverName, cameraName);
                            photoList.add(photo);


//                            ImageRequest imgReq = new ImageRequest(url1, (bitmap) -> {
//                                try {
//                                    // Do something with loaded bitmap...
//                                    Bitmap image = bitmap;
//
//
//                                    image.compress(Bitmap.CompressFormat.PNG, 100, MarsPhotoActivity.this.openFileOutput(id + ".jpg", Activity.MODE_PRIVATE));
//                                    bitmapList.add(image);
//
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                            }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
//                            });
//                            queue.add(imgReq);


                        }

                        mvm.photoList.postValue(photoList);
//                        if(photoList.size() == bitmapList.size()){
                        myAdapter.notifyItemInserted(photoList.size() - 1);
//                    }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                        (error) -> {

                        });

                queue.add(request);

            }


//              mvm.photos.postValue(photoList);
//            myAdapter.notifyItemInserted(photoList.size() );


        });

        // click on save image, the image should be saved to disk, and the width, height, and date & time of when the image was saved should be stored on the database
        binding.saveBtn.setOnClickListener(click -> {

            isSavedList = true;

            String savedList = getString(R.string.savedList);
            binding.myFavourite.setText(savedList);


            MarsPhoto newPhoto = mvm.selectedPhoto.getValue();



            Executor thread_1 = Executors.newSingleThreadExecutor();
            thread_1.execute(()->{

                mDao.insertPhoto(newPhoto);

            });

            photoList = new ArrayList<>();


                mvm.photoList.setValue(photoList = new ArrayList<>());
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    photoList.addAll(mDao.getAllPhotos()); //Once you get the data from database

                    runOnUiThread(() -> binding.imgRecyclerView.setAdapter(myAdapter)); //You can then load the RecyclerView
                });


        });


    }
}