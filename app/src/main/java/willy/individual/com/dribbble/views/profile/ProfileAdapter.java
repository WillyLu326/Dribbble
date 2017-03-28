package willy.individual.com.dribbble.views.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
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

            Bitmap myImage = getBitmapFromURL(user.avatar_url);
            Drawable dr = new BitmapDrawable(profileFragment.getResources(), myImage);
            profileInfoViewHolder.profileContent.setBackground(dr);

            Blurry.with(profileFragment.getActivity())
                    .radius(25)
                    .sampling(2)
                    .color(Color.argb(66, 255, 255, 0))
                    .async()
                    .animate(500)
                    .onto(profileInfoViewHolder.profileContent);

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


    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openStream());
            return image;
        } catch(IOException e) {
            System.out.println(e);
            return null;
        }
    }
}
