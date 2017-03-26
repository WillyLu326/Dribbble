package willy.individual.com.dribbble.views.following;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class FollowingListFragment extends Fragment {

    @BindView(R.id.following_recycler_view) RecyclerView recyclerView;

    private FollowingListAdapter adapter;

    public static FollowingListFragment newInstance() {
        return new FollowingListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.following_recycle_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ShotListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));
        adapter = new FollowingListAdapter(new ArrayList<User>(), new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new UserFollowingTask());
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private class UserFollowingTask extends AsyncTask<Void, Void, List<User>> {

        int page = adapter.followingUsers.size() / 12 + 1;

        @Override
        protected List<User> doInBackground(Void... params) {
            User user = Dribbble.getAuthUser();
            return Dribbble.getFollowingUsers(user.following_url, page);
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            adapter.append(users);
            adapter.toggleSpinner(adapter.followingUsers.size() / 12 >= page);
        }
    }
}
