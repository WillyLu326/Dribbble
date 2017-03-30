package willy.individual.com.dribbble.views.following;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.views.auth.Auth;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class FollowingListFragment extends Fragment {

    @BindView(R.id.following_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.following_swipe_container) SwipeRefreshLayout swipeContainer;

    private static final String FOLLOW_TYPE_KEY = "follow_type_key";

    private FollowingListAdapter adapter;

    public static FollowingListFragment newInstance(int followType) {
        Bundle args = new Bundle();
        args.putInt(FOLLOW_TYPE_KEY, followType);
        FollowingListFragment fragment = new FollowingListFragment();
        fragment.setArguments(args);
        return fragment;
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

        setupSwipeContainer();

        final int followType = getArguments().getInt(FOLLOW_TYPE_KEY);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ShotListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.xsmall_space)));
        adapter = new FollowingListAdapter(new ArrayList<User>(), this, new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                if (followType == MainActivity.FOLLOWING_TYPE) {
                    AsyncTaskCompat.executeParallel(new UserFollowingTask(false));
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskCompat.executeParallel(new UserFollowingTask(true));
            }
        });
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, null));
    }

    private class UserFollowingTask extends AsyncTask<Void, Void, List<User>> {

        private int page;
        private boolean refresh;

        public UserFollowingTask(boolean refresh) {
            this.refresh = refresh;
            this.page = adapter.followingUsers.size() / 12 + 1;
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            User user = Auth.loadAuthUser(getContext());
            return refresh ? Dribbble.getFollowingUsers(user.following_url, 1)
                            :Dribbble.getFollowingUsers(user.following_url, page);
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            if (refresh) {
                adapter.clearAll();
                adapter.append(users);
                swipeContainer.setRefreshing(false);
            } else {
                adapter.append(users);
                adapter.toggleSpinner(adapter.followingUsers.size() / 12 >= page);
            }
        }
    }

}
