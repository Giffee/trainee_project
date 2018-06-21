package trainee_kost.prospektdev.com.trainee.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import trainee_kost.prospektdev.com.trainee.api.model.GithubUserModel;

@Dao
public interface UserDao {
    @Query("select * from githubusermodel")
    List<GithubUserModel> getAll();

    @Query("SELECT * FROM githubusermodel WHERE id = :id")
    GithubUserModel getById(long id);

    @Insert
    void insert(GithubUserModel githubUserModel);
}
