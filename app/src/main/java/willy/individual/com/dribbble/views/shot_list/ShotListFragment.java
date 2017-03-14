package willy.individual.com.dribbble.views.shot_list;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
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
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class ShotListFragment extends Fragment{

    public static final int SHOTLIST_FRAGMENT_REQ_CODE = 100;

    private static final int COUNT_PER_PAGE = 12;

    @BindView(R.id.shot_list_recycler_view) RecyclerView shotListRecyclerView;

    private ShotListAdapter adapter;

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
        adapter = new ShotListAdapter(new ArrayList<Shot>(), new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new LoadShotTask(adapter.getItemCount() / COUNT_PER_PAGE + 1));
            }
        });
        shotListRecyclerView.setAdapter(adapter);
    }


    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>> {

        private int page;

        public LoadShotTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Shot> doInBackground(Void... params) {
            return Dribbble.getShots(page);
        }

        @Override
        protected void onPostExecute(List<Shot> shotList) {
            super.onPostExecute(shotList);
            adapter.append(shotList);
            adapter.toggleSpinner(adapter.getItemCount() / COUNT_PER_PAGE <= page);
        }
    }
}
