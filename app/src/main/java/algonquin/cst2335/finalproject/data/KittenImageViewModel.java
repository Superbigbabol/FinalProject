package algonquin.cst2335.finalproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class KittenImageViewModel extends ViewModel {
    public MutableLiveData<ArrayList<FavouritePic>> favPic = new MutableLiveData<>();
    public MutableLiveData<FavouritePic> selectedPic = new MutableLiveData<>();

}
