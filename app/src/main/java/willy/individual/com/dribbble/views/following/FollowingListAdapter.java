package willy.individual.com.dribbble.views.following;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.List;
import java.util.zip.Inflater;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.User;

/**
 * Created by zhenglu on 3/25/17.
 */

public class FollowingListAdapter extends RecyclerView.Adapter {

    List<User> followingUsers;

    public FollowingListAdapter(List<User> followingUsers) {
        this.followingUsers = followingUsers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        FollowingViewHolder viewHolder = new FollowingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = followingUsers.get(position);

        FollowingViewHolder followingViewHolder = (FollowingViewHolder) holder;
        followingViewHolder.userItemName.setText(user.name);
        followingViewHolder.userItemLocation.setText(user.location);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(user.avatar_url)
                .setAutoPlayAnimations(true)
                .build();
        followingViewHolder.userItemImage.setController(controller);
    }

    @Override
    public int getItemCount() {
        return followingUsers.size();
    }
}
