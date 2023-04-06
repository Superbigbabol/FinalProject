package algonquin.cst2335.finalproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MarsPhoto.class}, version=1)
public abstract class MarsPhotoDatabase extends RoomDatabase {

    public abstract MarsPhotoDao mpDao();



}
