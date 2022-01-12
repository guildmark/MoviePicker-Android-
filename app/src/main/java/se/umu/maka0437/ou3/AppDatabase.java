package se.umu.maka0437.ou3;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
