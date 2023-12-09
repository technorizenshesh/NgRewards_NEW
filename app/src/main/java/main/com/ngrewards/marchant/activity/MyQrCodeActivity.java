package main.com.ngrewards.marchant.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.BuildConfig;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;

public class MyQrCodeActivity extends AppCompatActivity {

    public final static int QRcodeWidth = 500;
    MySession mySession;
    Uri bmpUri;
    private RelativeLayout backlay;
    private Bitmap bitmap;
    private ImageView myqrview;
    private String user_id = "", merchant_number = "", murchant_name = "", merchant_img_url;
    private CircleImageView merchant_img;
    private TextView merchant_name, merchant_number_tv;
    private LinearLayout share_my_code, savegallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    String business_name = jsonObject1.getString("business_name");
                    merchant_number = jsonObject1.getString("business_no");
                    String contact_name = jsonObject1.getString("contact_name");
                    if (business_name == null || business_name.equalsIgnoreCase("")) {

                        murchant_name = contact_name;

                    } else {
                        murchant_name = business_name;
                    }

                    merchant_img_url = jsonObject1.getString("merchant_image");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinit();
        clickevet();

    }

    private void clickevet() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share_my_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (bitmap != null) {

                        Drawable mDrawable = myqrview.getDrawable();
                        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image I want to share", null);
                        Uri uri = Uri.parse(path);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "" + "This Is " + murchant_name + "QR Code.");
                        shareIntent.setType("image/*");
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        savegallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (bitmap != null) {

                        SaveImage(bitmap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/NgRewards");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "QrCode-" + n + ".jpg";
        File file = new File(myDir, fname);

        Toast.makeText(this, "" + file, Toast.LENGTH_SHORT).show();
        MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap, "", "");

        try {

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            bmpUri = FileProvider.getUriForFile(MyQrCodeActivity.this.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

            Toast.makeText(this, "" + bmpUri, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            Uri U = FileProvider.getUriForFile(MyQrCodeActivity.this.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            bmpUri = U;
            //  bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void idinit() {

        savegallery = findViewById(R.id.savegallery);
        share_my_code = findViewById(R.id.share_my_code);
        merchant_name = findViewById(R.id.merchant_name);
        merchant_number_tv = findViewById(R.id.merchant_number);
        merchant_name.setText("" + murchant_name);
        merchant_number_tv.setText("" + merchant_number);
        merchant_img = findViewById(R.id.merchant_img);
        if (merchant_img_url != null && !merchant_img_url.equalsIgnoreCase("") && !merchant_img_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
            Glide.with(MyQrCodeActivity.this).load(merchant_img_url).placeholder(R.drawable.user_propf).into(merchant_img);
        }
        backlay = findViewById(R.id.backlay);
        myqrview = findViewById(R.id.myqrview);
        try {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("murchant_name", "" + murchant_name);
                jsonObject.put("merchant_number", "" + merchant_number);
                jsonObject.put("merchant_id", "" + user_id);
                String s = murchant_name + "," + merchant_number + "," + user_id;
                bitmap = TextToImageEncode(s);
                myqrview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


}

