package se.umu.maka0437.ou3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    //Get all movies
    @Query("SELECT * FROM movie")
    List<Movie> getAll();

    @Query("SELECT * FROM movie ORDER BY RANDOM() LIMIT 1")
    Movie getRandomMovie();

    //Get all movies by specific years, genres and country
    @Query("SELECT * FROM movie WHERE releaseYear > :year AND genre LIKE :genre")
    List<Movie> findByAll(int year, String genre);

    //Get all movies by country
    @Query("SELECT * FROM movie WHERE country LIKE :country")
    List<Movie> findByCountry(String country);

    //Get all movies by genre
    @Query("SELECT * FROM movie WHERE genre LIKE :genre")
    List<Movie> findByGenre(String genre);

    //Get all movies by year
    @Query("SELECT * FROM movie WHERE releaseYear > :year")
    List<Movie> findByYear(int year);

    @Query("SELECT * FROM movie WHERE title LIKE :title")
    Movie[] findByTitle(String title);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Insert
    void insertAll(Movie... movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

}
