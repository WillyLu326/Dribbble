package willy.individual.com.dribbble.views.shot_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import willy.individual.com.dribbble.models.Comment;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class ShotFragment extends Fragment {

    public static final String SHOT_KEY = "shot_key";

    private Shot shot;

    private ShotAdapter adapter;

    @BindView(R.id.shot_detail_recycler_view) RecyclerView recyclerView;

    public static Fragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shot_detail_recycle, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shot = ModelUtils.convertToObject(getArguments().getString(SHOT_KEY), new TypeToken<Shot>(){});
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShotAdapter(shot, this, new ArrayList<Comment>(), new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new CommentLoadTask(shot.comments_url));
            }
        });
        recyclerView.setAdapter(adapter);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOT_KEY, ModelUtils.convertToString(shot, new TypeToken<Shot>(){}));
        this.getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }


    public void like(int id) {
        AsyncTaskCompat.executeParallel(new LikeShotTask(id));
    }

    public void unlike(int id) {
        AsyncTaskCompat.executeParallel(new UnlikeShotTask(id));
    }

    private class LikeShotTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public LikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Dribbble.likeShot(id);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class UnlikeShotTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public UnlikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Dribbble.unlikeShot(id);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class CommentLoadTask extends AsyncTask<Void, Void, List<Comment>> {

        private String comments_url;
        private int page;

        public CommentLoadTask(String comments_url) {
            this.comments_url = comments_url;
            this.page = adapter.getCommentsData().size() / 12 + 1;
        }

        @Override
        protected List<Comment> doInBackground(Void... params) {
            return Dribbble.getComments(comments_url, page);
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            adapter.append(comments);
            adapter.toggleSpinner(adapter.getCommentsData().size() / 12 >= page);
        }
    }
}
