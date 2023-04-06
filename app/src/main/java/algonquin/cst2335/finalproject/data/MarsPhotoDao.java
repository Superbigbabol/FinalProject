package algonquin.cst2335.finalproject.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarsPhotoDao {
    @Insert
    public void insertPhoto(MarsPhoto photo);
    @Query("Select * from MarsPhoto")
    public List<MarsPhoto> getAllPhotos();
}
