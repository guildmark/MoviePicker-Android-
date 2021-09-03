package se.umu.maka0437.ou3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    //Get all movies
    @Query("SELECT * FROM movie")
    List<Movie> getAll();

    //Get all movies by country
    @Query("SELECT * FROM movie WHERE country LIKE :country")
    List<Movie> findByCountry(String country);

    //Get all movies by year
    @Query("SELECT * FROM movie WHERE releaseYear LIKE :year")
    List<Movie> findByYear(int year);

    @Insert
    void insertAll(Movie... movies);

    @Delete
    void delete(Movie movie);

}
