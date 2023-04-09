package algonquin.cst2335.finalproject.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.data.MarsPhoto;
import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;
import algonquin.cst2335.finalproject.R;

public class PhotoFragment extends Fragment {

    MarsPhoto selected;
    public PhotoFragment(MarsPhoto m) {selected = m;}

    private TextView urlTextview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.camerName.setText(selected.getCameraName());
        String imageUrl = selected.getImgSrc();
        binding.imgSrc.setText(imageUrl);
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(binding.op);


//
//        new Thread(() -> {
//            try {
//                // Load the image from the URL using Picasso
//                Bitmap bitmap = Picasso.get().load(imageUrl).get();
//                // Update the UI on the main thread with the loaded image
//                getActivity().runOnUiThread(() -> binding.op.setImageBitmap(bitmap));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread(() -> {
//            try {
//                URL url = new URL(imageUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                byte[] buffer = new byte[8192];
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
//                int bytesRead;
//                while ((bytesRead = input.read(buffer)) != -1) {
//                    output.write(buffer, 0, bytesRead);
//                }
//                input.close();
//                output.flush();
//                byte[] imageData = output.toByteArray();
//                output.close();
//
//                // Load the image data into the ImageView on the UI thread
//                getActivity().runOnUiThread(() -> {
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//                    binding.op.setImageBitmap(bitmap);
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();

        binding.imgSrc.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(selected.getImgSrc()));
                startActivity(intent);
        });


//       binding.op.setImageBitmap();
        return binding.getRoot();





    }
}
