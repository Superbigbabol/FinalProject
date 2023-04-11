package algonquin.cst2335.finalproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavouritePic.class}, version = 1)
public abstract class PicDatabase extends RoomDatabase {

    public abstract FavouritePicDAO fpDAO();

}
