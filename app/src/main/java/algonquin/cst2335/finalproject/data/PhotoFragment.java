package algonquin.cst2335.finalproject.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;
import algonquin.cst2335.finalproject.R;

public class PhotoFragment extends Fragment {

    MarsPhoto selected;

    public PhotoFragment(MarsPhoto m) {
        selected = m;
    }

    private TextView urlTextview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.camerName.setText(selected.getCameraName());
        String imageUrl = selected.getImgSrc();
        String id = selected.getId();
        binding.imgSrc.setText(imageUrl);

        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(binding.op);
        binding.imgSrc.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(selected.getImgSrc()));
            startActivity(intent);
        });

        return binding.getRoot();
    }

}
