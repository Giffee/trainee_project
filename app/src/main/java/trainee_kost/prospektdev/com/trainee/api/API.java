package trainee_kost.prospektdev.com.trainee.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import trainee_kost.prospektdev.com.trainee.api.model.GithubUserModel;

public interface API {

    @GET("users")
    Call<List<GithubUserModel>> getAllUsers(@Query("per_page") int per_page);
}