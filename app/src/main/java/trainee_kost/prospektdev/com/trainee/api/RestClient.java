package trainee_kost.prospektdev.com.trainee.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {
    private static final String BASE_URL = "https://api.github.com/";

    private static final RestClient instance = new RestClient();

    public static API getInstance() {
        return instance.service;
    }

    private final API service;

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(API.class);
    }
}