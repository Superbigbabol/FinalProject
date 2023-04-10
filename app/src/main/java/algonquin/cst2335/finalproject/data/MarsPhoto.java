package algonquin.cst2335.finalproject.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MarsPhoto {


     @PrimaryKey(autoGenerate = true)
     @ColumnInfo(name="dataID")
     public long dataID;

    @ColumnInfo(name="id")
    private String id;
    @ColumnInfo(name="imgSrc")
    private String imgSrc;
    @ColumnInfo(name="roverName")
    private String roverName;
    @ColumnInfo(name="cameraName")
    private String cameraName;

    public MarsPhoto(String id, String imgSrc, String roverName, String cameraName) {
        this.id = id;
        this.imgSrc = imgSrc;
        this.roverName = roverName;
        this.cameraName = cameraName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
