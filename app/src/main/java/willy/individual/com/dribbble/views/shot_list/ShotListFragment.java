package willy.individual.com.dribbble.views.shot_list;

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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;

/**
 * Created by zhenglu on 3/5/17.
 */

public class ShotListFragment extends Fragment{

    @BindView(R.id.shot_list_recycler_view) RecyclerView shotListRecyclerView;

    public static ShotListFragment newInstance() {
        return new ShotListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shot_recycle_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shotListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotListRecyclerView.addItemDecoration(new ShotListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));
        ShotAdapter adapter = new ShotAdapter(mockData());
        shotListRecyclerView.setAdapter(adapter);
    }

    private List<Shot> mockData() {
        List<Shot> shotList = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 5; ++i) {
            Shot shot = new Shot();
            shot.views_count = random.nextInt(3000);
            shot.likes_count = random.nextInt(100);
            shot.butckets_count = random.nextInt(20);
            shotList.add(shot);
        }

        return shotList;
    }

}
