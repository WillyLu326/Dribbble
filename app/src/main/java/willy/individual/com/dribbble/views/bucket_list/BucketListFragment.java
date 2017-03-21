package willy.individual.com.dribbble.views.bucket_list;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
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
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.views.base.BucketListSpaceItemDecoration;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;


public class BucketListFragment extends Fragment {

    @BindView(R.id.bucket_list_recycler_view) RecyclerView bucketRecyclerView;

    private BucketAdapter bucketAdapter;

    public static BucketListFragment newInstance() {
        return new BucketListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bucket_recycle_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Handler uiThreadHandler = new Handler();
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bucketRecyclerView.addItemDecoration(new BucketListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));
        bucketAdapter = new BucketAdapter(mockData(), new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            uiThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    bucketAdapter.append(mockData());
                                    bucketAdapter.toggleBucketSpinner(bucketAdapter.getItemCount() < 21);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        bucketRecyclerView.setAdapter(bucketAdapter);
    }

    private List<Bucket> mockData() {
        List<Bucket> buckets = new ArrayList<>();

        return buckets;
    }
}
