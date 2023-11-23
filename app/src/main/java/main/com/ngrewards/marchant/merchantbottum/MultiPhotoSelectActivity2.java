package main.com.ngrewards.marchant.merchantbottum;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.marchant.adapter.ImageAdpterGrid;

public class MultiPhotoSelectActivity2 extends AppCompatActivity {

    private RelativeLayout backrell;
    private TextView done_but;
    private ImageAdpterGrid imageAdpterGrid;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;

    String language ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multi_photo_select);
        done_but = (TextView) findViewById(R.id.done_but);
        backrell = (RelativeLayout) findViewById(R.id.backrell);
        backrell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedItems = imageAdpterGrid.getCheckedItems();

                if (selectedItems!= null && selectedItems.size() > 0) {
                    MultiPhotoSelectActivity.image = imageAdpterGrid.getCheckedItems();
                    Log.e("Images Path",""+MultiPhotoSelectActivity.image.get(0));
                    finish();
                    Log.e(MultiPhotoSelectActivity2.class.getSimpleName(), "Selected Items: " + selectedItems);
                }
                else {
                    Toast.makeText(MultiPhotoSelectActivity2.this, "No image selected" + selectedItems.size(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        populateImagesFromGallery();
    }

    public void btnChoosePhotosClick(View v){


    }

    private void populateImagesFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!mayRequestGalleryImages()) {
                return;
            }
        }

        ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean mayRequestGalleryImages() {

        if (checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(READ_MEDIA_IMAGES)) {
            //promptStoragePermission();
            showPermissionRationaleSnackBar();
        } else {
            requestPermissions(new String[]{READ_MEDIA_IMAGES}, REQUEST_FOR_STORAGE_PERMISSION);
        }

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_FOR_STORAGE_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    populateImagesFromGallery();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_IMAGES)) {
                        showPermissionRationaleSnackBar();
                    } else {
                        Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        ArrayList<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));

            System.out.println("=====> Array path => "+imageUrls.get(i));
        }

        return imageUrls;
    }

    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdpterGrid = new ImageAdpterGrid(this, imageUrls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        GridView recyclerView = (GridView) findViewById(R.id.gridView);
/*
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
*/
        recyclerView.setAdapter(imageAdpterGrid);
    }

    private void showPermissionRationaleSnackBar() {
        Snackbar.make(findViewById(R.id.button1), getString(R.string.permission_rationale),
                Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ok), new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(MultiPhotoSelectActivity2.this,
                        new String[]{READ_MEDIA_IMAGES},
                        REQUEST_FOR_STORAGE_PERMISSION);
            }
        }).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
