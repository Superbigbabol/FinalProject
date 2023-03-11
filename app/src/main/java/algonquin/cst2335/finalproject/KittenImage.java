package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityKittenImageBinding;

public class KittenImage extends AppCompatActivity {

    ActivityKittenImageBinding binding;
    RecyclerView.Adapter myAdapter;
    Bitmap kittenPic;
    // a collection of row objects shown in RecyclerView
    class MyRowHolder extends RecyclerView.ViewHolder {
        // todo : create xml template for each row
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKittenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // adapter for the RecyclerView
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }
        };
        binding.imgRecyclerView.setAdapter(myAdapter);
        // todo : setLayoutManager for the imgRecyclerView


        // fetch image from web
        binding.retrieveBtn.setOnClickListener(click -> {
//            binding.imgWidth.setText("");
//            binding.imgHeight.setText("");
            String width = binding.imgWidth.getText().toString();
            String height = binding.imgHeight.getText().toString();
            String url = "https://placekitten.com/"+width+"/"+height;
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                try {
                    InputStream inputStream = new URL(url).openStream();
                    kittenPic = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> binding.imageView.setImageBitmap(kittenPic));
            });
        });

        // click on save image, the image should be saved to disk, and the width, height, and date & time of when the image was saved should be stored on the database
        binding.saveBtn.setOnClickListener(click -> {

        });
    }
}