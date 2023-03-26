package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.data.FavouritePic;
import algonquin.cst2335.finalproject.data.FavouritePicDAO;
import algonquin.cst2335.finalproject.data.KittenImageViewModel;
import algonquin.cst2335.finalproject.data.PicDatabase;
import algonquin.cst2335.finalproject.databinding.ActivityKittenImageBinding;
import algonquin.cst2335.finalproject.databinding.FavouriteImageBinding;

public class KittenImage extends AppCompatActivity {

    ActivityKittenImageBinding binding;
    RecyclerView.Adapter myAdapter;
    FavouritePicDAO fpDAO;
    Bitmap kittenPic;
    RequestQueue queue;
    KittenImageViewModel favModel;
    ArrayList<FavouritePic> myFavourites;


    // inner class - a collection of row objects shown in RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView widthText;
        TextView heightText;
        TextView timeText;
        Button delBtn;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( click ->{
                int position = getAbsoluteAdapterPosition();
                FavouritePic selected = myFavourites.get(position);
                favModel.selectedPic.postValue(selected);
            });
            thumbnail = itemView.findViewById(R.id.thumbnail);
            widthText = itemView.findViewById(R.id.width);
            heightText = itemView.findViewById(R.id.height);
            timeText = itemView.findViewById(R.id.time);
            delBtn = itemView.findViewById(R.id.delBtn);
            delBtn.setOnClickListener( click -> {
                int position = getAbsoluteAdapterPosition();
                FavouritePic clickedFP = myFavourites.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(KittenImage.this);
                builder.setMessage("Are you sure to delete this favourite record?")
                        .setTitle("Caution!")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            File file = new File(getFilesDir(), "Kitten_"+clickedFP.getWidth()+clickedFP.getHeight()+".png");
                            kittenPic = BitmapFactory.decodeFile(file.getPath());
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() ->
                            {
                                fpDAO.deletePic(clickedFP);//delete from db
                                myFavourites.remove(position);//also remove from ArrayList
                                file.delete();//delete file in disk
                                runOnUiThread( () -> {
                                    myAdapter.notifyItemRemoved(position);//update the recycler view
                                    Snackbar.make(timeText, "You deleted favourite #"+position, Snackbar.LENGTH_LONG)
                                            .setAction("Undo", clk -> {
                                                Executor thread_2 = Executors.newSingleThreadExecutor();
                                                thread_2.execute(() -> {
                                                    long id = fpDAO.insertPic(clickedFP);
                                                    clickedFP.id = id;
                                                    myFavourites.add(position, clickedFP);
                                                    runOnUiThread(()->{
                                                        try (FileOutputStream fOut = openFileOutput("Kitten_"+clickedFP.getWidth()+clickedFP.getHeight()+".png", Context.MODE_PRIVATE);) {
                                                            kittenPic.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                                            fOut.flush();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        myAdapter.notifyItemInserted(position);
                                                    });
                                                });
                                            })
                                            .show();
                                });
                            });
                        })
                        .setNegativeButton("No",(dialogInterface, i) -> {
                            // do nothing
                        })
                        .create().show();
            });
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

            case R.id.nasa:
                Toast.makeText(KittenImage.this, "Welcome to Nasa Mars Rover Photos", Toast.LENGTH_SHORT).show();
                Intent nasaIntent = new Intent(KittenImage.this, MarsPhotoActivity.class);
                startActivity(nasaIntent);
                break;
            case R.id.weather:
                Toast.makeText(KittenImage.this, "Welcome to WeatherStack", Toast.LENGTH_SHORT).show();
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

                FavouritePic fp = myFavourites.get(position);
                String width = ""+fp.getWidth();
                String height = ""+fp.getHeight();
                String savedTime = fp.getSavedTime();
                File file = new File(getFilesDir(), "Kitten_"+width+height+".png");
                holder.thumbnail.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                holder.widthText.setText(width);
                holder.heightText.setText(height);
                holder.timeText.setText(savedTime);
            }

            @Override
            public int getItemCount() {
                return myFavourites.size();
            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }
        };

        // create db
        PicDatabase db = Room.databaseBuilder(getApplicationContext(), PicDatabase.class, "KittenImages").build();
        fpDAO = db.fpDAO();

        // instantiate ViewModel so that app can survive from rotation
        favModel = new ViewModelProvider(this).get(KittenImageViewModel.class);
        myFavourites = favModel.favPic.getValue();
        if (myFavourites == null){
            myFavourites = new ArrayList<FavouritePic>();
            // get kitten images from db
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
                myFavourites.addAll(fpDAO.getAllPic());
            });
            favModel.favPic.postValue(myFavourites);
        }
        binding.imgRecyclerView.setAdapter(myAdapter);
        binding.imgRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // shared preferences to save width and height that was entered in last time
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        binding.imgWidth.setText(prefs.getString("ImgWidth", ""));
        binding.imgHeight.setText(prefs.getString("ImgHeight",""));

        //instantiate request queue
        queue = Volley.newRequestQueue(KittenImage.this);

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
                        kittenPic = bitmap;
                    },
                    1024,
                    1024,
                    ImageView.ScaleType.CENTER,
                    null,
                    error -> Toast.makeText(KittenImage.this, "failed to get response "+error, Toast.LENGTH_LONG).show()
            );
            queue.add(imgReq);
        });

        // click on save image, the image should be saved to disk, and the width, height, and date & time of when the image was saved should be stored on the database
        binding.saveBtn.setOnClickListener(click -> {

            String width = binding.imgWidth.getText().toString();
            String height = binding.imgHeight.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            // prevent app from crash due to keeping to click savebtn
            if ( kittenPic != null )
            {
                String fileName = "Kitten_" + width + height + ".png";
                //check if kitten img exists
                File file = new File(getFilesDir(), fileName);
                if (file.exists()) {
                    // if exists, not going to save in db and disk
                    Toast.makeText(this, "You have it, check list", Toast.LENGTH_SHORT).show();
                } else {
                    // not exist, create one and save on the disk
                    try (FileOutputStream fOut = openFileOutput(fileName, Context.MODE_PRIVATE);) {
                        kittenPic.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FavouritePic fp = new FavouritePic(Integer.parseInt(width), Integer.parseInt(height), currentDateandTime);
                    myFavourites.add(fp);
                    // insert into db
                    Executor thread1 = Executors.newSingleThreadExecutor();
                    thread1.execute(()->{
                        long id = fpDAO.insertPic(fp);
                        fp.id = id;//assign id returned from db to object.id, so that they can match when doing transcation
                    });

                    //notify RecyclerView a new RowHolder is inserted
                    myAdapter.notifyItemInserted(myFavourites.size() - 1);

                    // after save the img, things should be cleaned, so other width and height can be entered
                    binding.imageView.setImageBitmap(null);
                    binding.imgWidth.setText("");
                    binding.imgHeight.setText("");
                    kittenPic = null;
                }
            } else {
                Toast.makeText(this,"Nothing to save", Toast.LENGTH_SHORT).show();
            }
        });

        favModel.selectedPic.observe(this, (selectedPic) -> {
            FavouriteDetailFragment detailFragment = new FavouriteDetailFragment(selectedPic);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLocation, detailFragment).addToBackStack("").commit();
        });

    }
}