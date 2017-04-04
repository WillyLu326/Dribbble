package willy.individual.com.dribbbow.views.following;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.User;
import willy.individual.com.dribbbow.views.auth.Auth;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbbow.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;


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

        final int followType = getArguments().getInt(FOLLOW_TYPE_KEY);

        setupSwipeContainer(followType);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new ShotListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.xsmall_space)));
        adapter = new FollowingListAdapter(new ArrayList<User>(), followType, this, new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new UserFollowUsersTask(followType, false));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeContainer(final int followType) {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskCompat.executeParallel(new UserFollowUsersTask(followType, true));
            }
        });
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private class UserFollowUsersTask extends DribbbleTask<Void, Void, List<User>> {

        private int page;
        private int followType;
        private boolean refresh;

        public UserFollowUsersTask(int followType, boolean refresh) {
            this.refresh = refresh;
            this.followType = followType;
            this.page = adapter.followingUsers.size() / 12 + 1;
        }

        @Override
        protected List<User> doJob(Void... params) throws DribbbleException {
            User user = Auth.authUser;
            if (followType == MainActivity.FOLLOWING_TYPE) {
                return refresh ? Dribbble.getFollowingUsers(user.following_url, 1)
                        : Dribbble.getFollowingUsers(user.following_url, page);
            } else if (followType == MainActivity.FOLLOWER_TYPE) {
                return refresh ? Dribbble.getFollowerUser(user.followers_url, 1)
                        : Dribbble.getFollowerUser(user.followers_url, page);
            }
            return new ArrayList<>();
        }

        @Override
        protected void onSuccess(List<User> users) {
            if (refresh) {
                adapter.clearAll();
                adapter.append(users);
                swipeContainer.setRefreshing(false);
            } else {
                adapter.append(users);
                adapter.toggleSpinner(adapter.followingUsers.size() / 12 >= page);
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}
