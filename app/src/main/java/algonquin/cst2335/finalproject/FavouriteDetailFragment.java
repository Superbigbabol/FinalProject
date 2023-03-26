package algonquin.cst2335.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.io.File;

import algonquin.cst2335.finalproject.data.FavouritePic;
import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

public class FavouriteDetailFragment extends Fragment {

    FavouritePic selected;

    public FavouriteDetailFragment(FavouritePic selected){
        this.selected = selected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        // get img from disk and resize it to original size
        String filePath = getContext().getFilesDir().getPath()+"/Kitten_"+selected.getWidth()+selected.getHeight()+".png";
        Bitmap resized = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(filePath),selected.getWidth(),selected.getHeight(),false);
        binding.resizedImage.setImageBitmap(resized);
        binding.widthText.setText("Width : "+selected.getWidth());
        binding.heightText.setText("Height : "+selected.getHeight());
        binding.savedTimeText.setText("Saved Time : "+selected.getSavedTime());
        return binding.getRoot();
    }

}
