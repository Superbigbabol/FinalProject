package algonquin.cst2335.finalproject.data;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.data.MarsPhoto;
import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

public class PhotoFragment extends Fragment {

    MarsPhoto selected;
    public PhotoFragment(MarsPhoto m) {selected = m;}

    private TextView urlTextview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.camerName.setText(selected.getCameraName());
        binding.imgSrc.setText(selected.getImgSrc());
        binding.imgSrc.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(selected.getImgSrc()));
                startActivity(intent);
        });


//       binding.op.setImageBitmap();
        return binding.getRoot();





    }
}
