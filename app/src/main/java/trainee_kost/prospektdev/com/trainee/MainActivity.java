package trainee_kost.prospektdev.com.trainee;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trainee_kost.prospektdev.com.trainee.adapter.RecyclerViewAdapter;
import trainee_kost.prospektdev.com.trainee.api.RestClient;
import trainee_kost.prospektdev.com.trainee.api.model.GithubUserModel;
import trainee_kost.prospektdev.com.trainee.database.App;
import trainee_kost.prospektdev.com.trainee.database.UserDao;

public class MainActivity extends AppCompatActivity implements Callback<List<GithubUserModel>> {

    public static final int MAX_PER_PAGE = 50;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<GithubUserModel> usersList = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        if (hasConnection()) {
            RestClient.getInstance().getAllUsers(MAX_PER_PAGE).enqueue(this);
        } else {
            noInternet();
        }
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void noInternet() {
        Toast.makeText(this, R.string.load_from_db, Toast.LENGTH_LONG).show();
        getAllFromDatabase();
    }

    private boolean hasConnection() {
        boolean internetConnection = false;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            internetConnection = true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            internetConnection = true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            internetConnection = true;
        }
        return internetConnection;
    }

    @Override
    public void onResponse(@NonNull Call<List<GithubUserModel>> call, Response<List<GithubUserModel>> response) {
        addUsersToList(response.body());
        insertIntoDatabase();
        for (GithubUserModel user : response.body()) {
            GithubUserModel userModel = response.body() != null ? user : new GithubUserModel();
            adapter.addItem(userModel);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<GithubUserModel>> call, Throwable t) {
        Toast.makeText(context, R.string.cant_connect, Toast.LENGTH_SHORT).show();
    }

    private void addUsersToList(List<GithubUserModel> users) throws NullPointerException {
        usersList.addAll(users);
    }

    private void insertIntoDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao userDao = App.getInstance().getDatabase().userDao();
                for (GithubUserModel u : usersList) {
                    boolean bool = userDao.getById(u.getId()) == null;
                    if (bool) {
                        GithubUserModel user = new GithubUserModel();

                        user.setId(u.getId());
                        user.setLogin(u.getLogin());
                        user.setAvatarUrl(u.getAvatarUrl());
                        user.setHtmlUrl(u.getHtmlUrl());
                        user.setType(u.getType());

                        userDao.insert(user);
                    }
                }
            }
        }).start();
    }

    private void getAllFromDatabase() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                if (App.getInstance().getDatabase().userDao().getAll().size() == 0) {
                    Toast.makeText(context, R.string.no_local_db, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                UserDao userDao = App.getInstance().getDatabase().userDao();

                addUsersToList(userDao.getAll());

                for (GithubUserModel user : userDao.getAll()) {
                    GithubUserModel userModel = userDao.getAll() != null ? user : new GithubUserModel();
                    adapter.addItem(userModel);
                }
                adapter.notifyDataSetChanged();
            }
        }).start();
    }
}