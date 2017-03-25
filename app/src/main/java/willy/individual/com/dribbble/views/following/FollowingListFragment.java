package willy.individual.com.dribbble.views.following;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;

/**
 * Created by zhenglu on 3/25/17.
 */

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
        adapter = new FollowingListAdapter(fakeData());
        recyclerView.setAdapter(adapter);
    }

    private List<User> fakeData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            users.add(new User("User " + i));
        }
        return users;
    }
}
