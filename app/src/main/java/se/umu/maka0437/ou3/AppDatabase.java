package se.umu.maka0437.ou3;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, User.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
    public abstract UserDao userDao();
}
