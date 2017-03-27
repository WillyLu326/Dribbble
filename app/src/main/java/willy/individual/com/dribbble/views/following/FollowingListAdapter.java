package willy.individual.com.dribbble.views.following;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.profile.ProfileActivity;


public class FollowingListAdapter extends RecyclerView.Adapter {

    private static final int FOLLOWING_LIST_TYPE = 0;
    private static final int FOLLOWING_SPINNER_TYPE = 1;

    private boolean isShowingSpinner;
    public List<User> followingUsers;
    private FollowingListFragment followingListFragment;
    private OnLoadingMoreListener onLoadingMoreListener;

    public FollowingListAdapter(List<User> followingUsers,
                                FollowingListFragment followingListFragment,
                                OnLoadingMoreListener onLoadingMoreListener) {
        this.followingUsers = followingUsers;
        this.followingListFragment = followingListFragment;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.isShowingSpinner = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOLLOWING_LIST_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item, parent, false);
            FollowingViewHolder viewHolder = new FollowingViewHolder(view);
            return viewHolder;
        } else if (viewType == FOLLOWING_SPINNER_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.following_spinner, parent, false);
            return new FollowingSpinnerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == FOLLOWING_LIST_TYPE) {
            User user = followingUsers.get(position).followee;

            final FollowingViewHolder followingViewHolder = (FollowingViewHolder) holder;
            followingViewHolder.userItemName.setText(user.name);
            followingViewHolder.userItemLocation.setText(user.location);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            followingViewHolder.userItemImage.setController(controller);

            followingViewHolder.userItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(followingListFragment.getActivity(), ProfileActivity.class);
                    followingListFragment.startActivity(intent);
                    Toast.makeText(followingListFragment.getContext(), "Click", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (getItemViewType(position) == FOLLOWING_SPINNER_TYPE) {
            onLoadingMoreListener.onLoadingMore();
        }
    }

    @Override
    public int getItemCount() {
        return isShowingSpinner ? followingUsers.size() + 1 : followingUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == followingUsers.size()) {
            return FOLLOWING_SPINNER_TYPE;
        }
        return FOLLOWING_LIST_TYPE;
    }

    public List<User> getData() {
        return followingUsers;
    }

    public void append(List<User> users) {
        this.followingUsers.addAll(users);
        notifyDataSetChanged();
    }

    public void toggleSpinner(boolean isShowingSpinner) {
        this.isShowingSpinner = isShowingSpinner;
        notifyDataSetChanged();
    }
}
