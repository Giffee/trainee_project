package trainee_kost.prospektdev.com.trainee.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import trainee_kost.prospektdev.com.trainee.api.model.GithubUserModel;
import trainee_kost.prospektdev.com.trainee.R;
import trainee_kost.prospektdev.com.trainee.SecondActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<GithubUserModel> usersList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void addItem(GithubUserModel user) {
        usersList.add(user);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, final int position) {
        final String login = usersList.get(position).getLogin();
        final String avatarUrl = usersList.get(position).getAvatarUrl();

        holder.login.setText(login);
        Picasso.with(context).load(avatarUrl).into(holder.avatar);

        onPictureClick(holder);
        onShareClick(holder);
    }

    private void onShareClick(@NonNull final RecyclerViewAdapter.ViewHolder holder) {
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL_TO_SHARE = "You can see Github user profile of \'" +
                        usersList.get(holder.getAdapterPosition()).getLogin() + "\' by pressing on next link:\n" +
                        usersList.get(holder.getAdapterPosition()).getHtmlUrl();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, URL_TO_SHARE);
                context.startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }

    private void onPictureClick(@NonNull final RecyclerViewAdapter.ViewHolder holder) {
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("id", usersList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("login", usersList.get(holder.getAdapterPosition()).getLogin());
                intent.putExtra("avatarUrl", usersList.get(holder.getAdapterPosition()).getAvatarUrl());
                intent.putExtra("htmlUrl", usersList.get(holder.getAdapterPosition()).getHtmlUrl());
                intent.putExtra("type", usersList.get(holder.getAdapterPosition()).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.login)
        TextView login;
        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.share)
        Button share;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
