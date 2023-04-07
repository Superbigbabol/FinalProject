package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.data.FavouritePic;
import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

/** A reserved view for showing details of the selected favourite image, zoom in thumbnail to its original size,
 * show width, height, and saved time
 * @author SHUBO
 * @see Fragment
 */
public class FavouriteDetailFragment extends Fragment {

    FavouritePic selected;

    /** Construct picture details
     *
     * @param selected a FavouritePic being clicked
     */
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
        binding.widthText.setText(""+selected.getWidth());
        binding.heightText.setText(""+selected.getHeight());
        binding.savedTimeText.setText(selected.getSavedTime());
        return binding.getRoot();
    }

}
