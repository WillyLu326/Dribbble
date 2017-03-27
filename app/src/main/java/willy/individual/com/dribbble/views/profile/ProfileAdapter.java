package willy.individual.com.dribbble.views.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.User;

/**
 * Created by zhenglu on 3/26/17.
 */

public class ProfileAdapter extends RecyclerView.Adapter {

    private static final int PROFILE_INFO_TYPE = 0;
    private User user = new User("Willy Lu");

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PROFILE_INFO_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_info, parent, false);
            return new ProfileInfoViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == PROFILE_INFO_TYPE) {
            ProfileInfoViewHolder profileInfoViewHolder = (ProfileInfoViewHolder) holder;
            profileInfoViewHolder.profileUsername.setText(user.name);
            profileInfoViewHolder.profileLocation.setText(user.location);
            profileInfoViewHolder.profileDescription.setText(user.bio);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            profileInfoViewHolder.profileAvatar.setController(controller);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PROFILE_INFO_TYPE;
        }
        return 1;
    }
}
