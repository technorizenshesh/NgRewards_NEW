package main.com.ngrewards.constant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import main.com.ngrewards.R;


public class CropActivity extends AppCompatActivity {

    private final String language = "";
    CropImageView newCropImageView;
    LinearLayout CropCancelLL, CropDoneLL, RotateDoneLL2;
    Toolbar toolbar;
    Uri fileUri;
    int degree = 0;
    String number;

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/Fixezi");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Hello Camera", "Oops! Failed create Hello Camera directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crop);

        newCropImageView = (CropImageView) findViewById(R.id.newCropImageView);

        CropDoneLL = (LinearLayout) findViewById(R.id.CropDoneLL2);
        CropCancelLL = (LinearLayout) findViewById(R.id.CropCancelLL2);
        RotateDoneLL2 = (LinearLayout) findViewById(R.id.RotateDoneLL2);

        newCropImageView.setAspectRatio(7, 5);

        Bundle extra = getIntent().getExtras();
        String ImagePath = extra.getString("ImagePath");
        number = extra.getString("number");
        if (ImagePath == null) {

        } else {
            newCropImageView.setImageUriAsync(Uri.fromFile(new File(ImagePath)));
        }
        CropCancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RotateDoneLL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = degree + 90;
                newCropImageView.rotateImage(degree);

            }
        });


        CropDoneLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileUri = getOutputMediaFileUri(1);

                Bitmap cropped = newCropImageView.getCroppedImage();

                File f = new File(fileUri.getPath());
                Bitmap bitmap = cropped;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent returnIntent = new Intent();
                returnIntent.putExtra("CroppedImage", fileUri.getPath());
                returnIntent.putExtra("number", number);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

