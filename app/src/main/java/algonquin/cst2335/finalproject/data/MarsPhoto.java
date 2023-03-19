package algonquin.cst2335.finalproject.data;


public class MarsPhoto {


    private String id;
    private String imgSrc;
    private String roverName;
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
