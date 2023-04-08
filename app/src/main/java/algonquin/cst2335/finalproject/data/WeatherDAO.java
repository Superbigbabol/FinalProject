package algonquin.cst2335.finalproject.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WeatherDAO {

    @Insert
    public long insertWeather(WeatherBeen weatherBeen);

    @Query("Select * from WeatherBeen")
    public List<WeatherBeen> getAllWeathers();

    @Delete
    public void deleteWeather(WeatherBeen weatherBeen);
}
