package trainee_kost.prospektdev.com.trainee.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import trainee_kost.prospektdev.com.trainee.api.model.GithubUserModel;

@Database(exportSchema = false, entities = {GithubUserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}