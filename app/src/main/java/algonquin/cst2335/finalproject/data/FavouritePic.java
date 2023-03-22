package algonquin.cst2335.finalproject.data;

public class FavouritePic {

    private int width;
    private int height;
    private String savedTime;

    public FavouritePic(){}

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
