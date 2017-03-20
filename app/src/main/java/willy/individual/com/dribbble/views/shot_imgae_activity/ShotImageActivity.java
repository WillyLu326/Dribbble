package willy.individual.com.dribbble.views.shot_imgae_activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

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

    private PopupWindow popupWindow;
    private boolean showPopupWindow = false;

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
                //finish();
            }
        });

        shotImageLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.popup_window, null, false);
                popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(shotImageLinearLayout, Gravity.CENTER | Gravity.BOTTOM, 0 ,0 );
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shot_placeholder, null));

                popupWindow.getContentView().findViewById(R.id.popup_window_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });
    }

}
