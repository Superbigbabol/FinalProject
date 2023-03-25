package algonquin.cst2335.finalproject.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouritePicDAO {

    @Insert
    public void insertPic(FavouritePic fp);

    @Query("Select * from FavouritePic")
    public List<FavouritePic> getAllPic();

    @Delete
    public void deletePic(FavouritePic fp);
}
