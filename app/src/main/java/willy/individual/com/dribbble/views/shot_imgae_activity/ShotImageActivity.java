package willy.individual.com.dribbble.views.shot_imgae_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.views.shot_detail.ShotAdapter;


public class ShotImageActivity extends AppCompatActivity {

    @BindView(R.id.shot_image_linear_layout) LinearLayout shotImageLinearLayout;
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

        shotImageLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(ShotImageActivity.this, shotImage);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setGravity(Gravity.CENTER);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_save :
                                Toast.makeText(getApplicationContext(), "Save Image", Toast.LENGTH_SHORT).show();
                            case R.id.popup_cancel :
                                finish();
                        }
                        return false;
                    }
                });

                popup.show();
                return true;
            }
        });
    }
}
