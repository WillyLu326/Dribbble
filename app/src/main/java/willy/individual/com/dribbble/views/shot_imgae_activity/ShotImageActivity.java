package willy.individual.com.dribbble.views.shot_imgae_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.views.shot_detail.ShotAdapter;


public class ShotImageActivity extends AppCompatActivity {

    @BindView(R.id.shot_image_linear_layout) View shotImageLinearLayout;
    @BindView(R.id.activity_shot_image) SimpleDraweeView shotImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_image);
        ButterKnife.bind(this);

        setupImageUi();
        setupShotImageLinearLayout();
    }

    private void setupImageUi() {
        String imageUrl = getIntent().getStringExtra(ShotAdapter.IMAGE_URL_KEY);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageUrl)
                .setAutoPlayAnimations(true)
                .build();
        shotImage.setController(controller);
    }

    private void setupShotImageLinearLayout() {
        shotImageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
