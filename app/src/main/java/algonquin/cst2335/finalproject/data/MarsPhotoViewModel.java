package algonquin.cst2335.finalproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class MarsPhotoViewModel extends ViewModel {

    public MutableLiveData<ArrayList<MarsPhoto>> photoList = new MutableLiveData< >();

    public MutableLiveData<MarsPhoto> selectedPhoto = new MutableLiveData<>();



}
