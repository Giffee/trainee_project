package trainee_kost.prospektdev.com.trainee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.profileLink)
    TextView profileLink;
    @BindView(R.id.loginSecond)
    TextView loginSecond;
    @BindView(R.id.avatarSecond)
    ImageView avatarSecond;
    @BindView(R.id.share)
    Button share;

    int id;
    String login;
    String avatarUrl;
    String htmlUrl;
    String type;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        init();
        displayInfoAboutUser();
    }

    private void init() {
        Intent intent = getIntent();

        id = intent.getExtras().getInt("id");
        login = intent.getExtras().getString("login");
        avatarUrl = intent.getExtras().getString("avatarUrl");
        htmlUrl = intent.getExtras().getString("htmlUrl");
        type = intent.getExtras().getString("type");
        sb = new StringBuilder();

        sb.append("\nID: ").append(id)
                .append("\nLogin: ").append(login)
                .append("\nAccount type: ").append(type)
                .append("\nAnd you also can see more info in his profile by clicking on next link:");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL_TO_SHARE = "You can see Github user profile of \'" +
                        login + "\' by pressing on next link:\n" + htmlUrl;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, URL_TO_SHARE);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }

    private void displayInfoAboutUser() {
        info.setText(sb);
        profileLink.setText(htmlUrl);
        loginSecond.setText(login);
        Picasso.with(this).load(avatarUrl).into(avatarSecond);
    }
}
