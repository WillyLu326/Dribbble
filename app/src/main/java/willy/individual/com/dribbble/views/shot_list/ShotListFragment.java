package willy.individual.com.dribbble.views.shot_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbble.views.dribbble.Dribbble;
import willy.individual.com.dribbble.views.shot_detail.ShotActivity;
import willy.individual.com.dribbble.views.shot_detail.ShotFragment;


public class ShotListFragment extends Fragment{

    public static final int SHOTLIST_FRAGMENT_REQ_CODE = 100;

    private static final int COUNT_PER_PAGE = 12;

    @BindView(R.id.shot_list_recycler_view) RecyclerView shotListRecyclerView;

    private ShotListAdapter adapter;
    private Shot updateShot;

    public static ShotListFragment newInstance() {
        return new ShotListFragment();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("=======================");
        System.out.println("=======================");
        System.out.println("=======================");
        System.out.println("=======================");
        System.out.println("=======================");
        if (requestCode == ShotListFragment.SHOTLIST_FRAGMENT_REQ_CODE && resultCode == Activity.RESULT_OK) {
            Shot updateShot = ModelUtils.convertToObject(data.getStringExtra(ShotFragment.SHOT_KEY), new TypeToken<Shot>(){});
            this.updateShot = updateShot;

        }
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
        adapter = new ShotListAdapter(new ArrayList<Shot>(), updateShot, this, new OnLoadingMoreListener() {
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

    public void startAcivity(Shot shot) {
        Intent intent = new Intent(getContext(), ShotActivity.class);
        intent.putExtra(ShotFragment.SHOT_KEY,
                ModelUtils.convertToString(shot, new TypeToken<Shot>(){}));
        getActivity().startActivityForResult(intent, ShotListFragment.SHOTLIST_FRAGMENT_REQ_CODE);

    }
}
