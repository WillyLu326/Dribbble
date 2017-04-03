package willy.individual.com.dribbbow.views.shot_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Bucket;
import willy.individual.com.dribbbow.models.Comment;
import willy.individual.com.dribbbow.models.Shot;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbbow.views.bucket_list.BucketListActivity;
import willy.individual.com.dribbbow.views.bucket_list.BucketListFragment;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;


public class ShotFragment extends Fragment {

    public static final String SHOT_KEY = "shot_key";
    public static final String COLLECTED_BUCKET_IDS_KEY = "collected bucket ids key";

    private Shot shot;
    private ShotAdapter adapter;
    private ArrayList<Integer> collectedIds;

    @BindView(R.id.shot_detail_recycler_view) RecyclerView recyclerView;

    public static Fragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShotActivity.CHOOSEN_BUCKET_ID_REQ && resultCode == Activity.RESULT_OK) {
            List<Integer> newCollectedBucketIds = ModelUtils.convertToObject(data.getStringExtra(BucketListFragment.CHOOSEN_BUCKET_IDS_KEY), new TypeToken<List<Integer>>(){});
            // 新的有 旧的没有 add
            List<Integer> addedIds = new ArrayList<>(newCollectedBucketIds);
            addedIds.removeAll(collectedIds);

            // 新的没有 旧的有 remove
            List<Integer> removeIds = new ArrayList<>(collectedIds);
            removeIds.removeAll(newCollectedBucketIds);

            AsyncTaskCompat.executeParallel(new UpdateShotBucketTask(addedIds, removeIds));
        }
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

        AsyncTaskCompat.executeParallel(new GetCollectBucketId());

        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOT_KEY, ModelUtils.convertToString(shot, new TypeToken<Shot>(){}));
        this.getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }


    public void bucket() {
        if (collectedIds != null) {
            Intent intent = new Intent(getActivity(), BucketListActivity.class);
            intent.putExtra(ShotAdapter.BUCKET_KEY, MainActivity.CHOOSE_BUCKET_TYPE);

            intent.putIntegerArrayListExtra(COLLECTED_BUCKET_IDS_KEY, collectedIds);
            startActivityForResult(intent, ShotActivity.CHOOSEN_BUCKET_ID_REQ);
        } else {
            Toast.makeText(getContext(), "Loading your buckets", Toast.LENGTH_SHORT).show();
        }
    }

    public void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shot.title + " " + shot.html_url);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_shot)));
    }

    public void like(int id) {
        AsyncTaskCompat.executeParallel(new LikeShotTask(id));
    }

    public void unlike(int id) {
        AsyncTaskCompat.executeParallel(new UnlikeShotTask(id));
    }

    private class LikeShotTask extends DribbbleTask<Void, Void, Void> {

        private int id;

        public LikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.likeShot(id);
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {

        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class UnlikeShotTask extends DribbbleTask<Void, Void, Void> {

        private int id;

        public UnlikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.unlikeShot(id);
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {

        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class CommentLoadTask extends DribbbleTask<Void, Void, List<Comment>> {

        private String comments_url;
        private int page;

        public CommentLoadTask(String comments_url) {
            this.comments_url = comments_url;
            this.page = adapter.getCommentsData().size() / 12 + 1;
        }

        @Override
        protected List<Comment> doJob(Void... params) throws DribbbleException {
            return Dribbble.getComments(comments_url, page);
        }

        @Override
        protected void onSuccess(List<Comment> comments) {
            adapter.append(comments);
            adapter.toggleSpinner(adapter.getCommentsData().size() / 12 >= page);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class UpdateShotBucketTask extends DribbbleTask<Void, Void, Void> {

        private List<Integer> addedIds;
        private List<Integer> removeIds;

        public UpdateShotBucketTask(List<Integer> addedIds, List<Integer> removeIds) {
            this.addedIds = addedIds;
            this.removeIds = removeIds;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            for (Integer addedId : addedIds) {
                Dribbble.updateShotBucket(addedId, shot.id);
            }
            for (Integer removeId : removeIds) {
                Dribbble.deleteShotBucket(removeId, shot.id);
            }
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {
            collectedIds.addAll(addedIds);
            collectedIds.removeAll(removeIds);

            shot.bucketed = !collectedIds.isEmpty();
            shot.buckets_count = shot.buckets_count + addedIds.size() - removeIds.size();

            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class GetCollectBucketId extends DribbbleTask<Void, Void, ArrayList<Integer>> {

        @Override
        protected ArrayList<Integer> doJob(Void... params) throws DribbbleException {
            List<Bucket> buckets = Dribbble.getAllBuckets(shot.buckets_url);
            List<Bucket> userBuckets = Dribbble.getAllUserBuckets();

            Set<Integer> userBucketIds = new HashSet<>();
            for (Bucket userBucket : userBuckets) {
                userBucketIds.add(userBucket.id);
            }

            ArrayList<Integer> collectedIds = new ArrayList<>();

            for (Bucket bucket : buckets) {
                if (userBucketIds.contains(bucket.id)) {
                    collectedIds.add(bucket.id);
                }
            }
            return collectedIds;
        }

        @Override
        protected void onSuccess(ArrayList<Integer> ids) {
            collectedIds = new ArrayList<>(ids);

            if (collectedIds.size() > 0) {
                shot.bucketed = true;
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}
