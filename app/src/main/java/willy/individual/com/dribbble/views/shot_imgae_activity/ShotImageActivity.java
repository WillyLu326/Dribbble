package willy.individual.com.dribbble.views.shot_imgae_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_image);
        ButterKnife.bind(this);

        setupImageUi();
        registerForContextMenu(shotImageLinearLayout);

        setupShotImageLinearLayout();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an Action");
        menu.add(0, v.getId(), 0, "Save");
        menu.add(0, v.getId(), 0, "Cancel");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (TextUtils.equals(item.getTitle(), "Save")) {
            Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.equals(item.getTitle(), "Cancel")) {
            Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        return true;
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
