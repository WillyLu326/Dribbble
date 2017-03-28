package willy.individual.com.dribbble.views.profile;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.models.User;


public class ProfileAdapter extends RecyclerView.Adapter {

    private static final int PROFILE_INFO_TYPE = 0;
    private static final int PROFILE_SHOT_TYPE = 1;
    private static final int PROFILE_SPINNER = 2;

    private User user;
    private List<Shot> profileShots = new ArrayList<>();
    private ProfileFragment profileFragment;

    public ProfileAdapter(User user, ProfileFragment profileFragment) {
        this.user = user;
        this.profileFragment = profileFragment;
    }

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
            profileInfoViewHolder.profileDescription.setText(Html.fromHtml(user.bio, 0));
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            profileInfoViewHolder.profileAvatar.setController(controller);

            Glide.with(profileFragment.getContext())
                    .load(user.avatar_url)
                    .centerCrop()
                    .bitmapTransform(new BlurTransformation(profileFragment.getContext()))
                    .into(profileInfoViewHolder.profileIv);
        }
    }

    @Override
    public int getItemCount() {
        //return 2 + profileShots.size();
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PROFILE_INFO_TYPE;
        }
//        else if (position == profileShots.size() + 1) {
//            return PROFILE_SPINNER;
//        }
        return PROFILE_SHOT_TYPE;
    }

}
