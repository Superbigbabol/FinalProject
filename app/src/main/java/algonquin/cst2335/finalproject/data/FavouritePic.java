package algonquin.cst2335.finalproject.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavouritePic {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "width")
    private int width;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "savedTime")
    private String savedTime;

    public FavouritePic(int width, int height, String savedTime){
        this.width = width;
        this.height = height;
        this.savedTime = savedTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSavedTime() {
        return savedTime;
    }

}
