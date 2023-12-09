package main.com.ngrewards.marchant.draweractivity;

import static main.com.ngrewards.Utils.Tools.ToolsShowDialog;
import static main.com.ngrewards.constant.MySession.KEY_LANGUAGE;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOffersAct extends AppCompatActivity {

    private final int GALLERY = 1;
    CategoryAdpters categoryAdpters;
    private EditText tital_name_et, offer_desc, offer_price;
    private ImageView uploadimg;
    private Spinner category_spinner;
    private SeekBar seekbar;
    private TextView percantae;
    private ImageView minus, add;
    private double cur_progress = 0;
    private RelativeLayout backlay;
    private ProgressBar progresbar;
    private MySession mySession;
    private Button publish_product;
    private String category_id = "", stripe_account_id = "", after_discount_str = "", user_id = "", tital_name_str = "", offer_desc_str = "", offer_price_str = "", ImagePath = "";
    private ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private Myapisession myapisession;
    private TextView offer_discount_price_tv;
    private File file;
    private String extension;
    private String timeStamp;
    private byte[] imageBytes;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offers);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        String user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    stripe_account_id = jsonObject1.getString("stripe_account_id");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        idinit();
        clickevent();
        getOfferCategory();

     /*   if (myapisession.getKeyOffercate() == null || myapisession.getKeyOffercate().equalsIgnoreCase("")) {
            getOfferCategory();
        } else {
            try {
                categoryBeanListArrayList = new ArrayList<>();

                JSONObject object = new JSONObject(myapisession.getKeyOffercate());
                Log.e("Offer Category >", " >" + myapisession.getKeyOffercate());
                if (object.getString("status").equals("1")) {

                    CategoryBean successData = new Gson().fromJson(myapisession.getKeyOffercate(), CategoryBean.class);
                    categoryBeanListArrayList.addAll(successData.getResult());

                }

                categoryAdpters = new CategoryAdpters(AddOffersAct.this, categoryBeanListArrayList);
                category_spinner.setAdapter(categoryAdpters);
                //  Log.e("category_id >>","dddd "+category_id);
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
                        Log.e("category_id >>", " dd " + categoryBeanListArrayList.get(i).getCategoryId());

                        if (category_id.equalsIgnoreCase(categoryBeanListArrayList.get(i).getCategoryId())) {
                            category_spinner.setSelection(i);
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        publish_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stripe_account_id == null || stripe_account_id.equalsIgnoreCase("")) {
                    Toast.makeText(AddOffersAct.this, getResources().getString(R.string.plseftcrtestractt), Toast.LENGTH_LONG).show();
                } else {
                    tital_name_str = tital_name_et.getText().toString();
                    offer_desc_str = offer_desc.getText().toString();
                    offer_price_str = offer_price.getText().toString();
                    if (tital_name_str == null || tital_name_str.equalsIgnoreCase("")) {
                        Toast.makeText(AddOffersAct.this, getResources().getString(R.string.entertite), Toast.LENGTH_LONG).show();
                    } else if (category_id == null || category_id.equalsIgnoreCase("")) {
                        Toast.makeText(AddOffersAct.this, getResources().getString(R.string.selectoffercate), Toast.LENGTH_LONG).show();

                    } else if (offer_desc_str == null || offer_desc_str.equalsIgnoreCase("")) {
                        Toast.makeText(AddOffersAct.this, getResources().getString(R.string.enterdesc), Toast.LENGTH_LONG).show();

                    } else if (ImagePath == null || ImagePath.equalsIgnoreCase("")) {
                        Toast.makeText(AddOffersAct.this, getResources().getString(R.string.selectimage), Toast.LENGTH_LONG).show();

                    } /*else if (offer_price_str == null || offer_price_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddOffersAct.this, getResources().getString(R.string.enterprice), Toast.LENGTH_LONG).show();

                }*/ else {
                        new AddOfferProdAsc().execute();
                    }
                }


            }
        });
    }

    private void idinit() {
        offer_discount_price_tv = findViewById(R.id.offer_discount_price_tv);
        category_spinner = findViewById(R.id.category_spinner);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    if (categoryBeanListArrayList.get(position).getCategoryId().equalsIgnoreCase("0")) {

                    } else {
                        category_id = categoryBeanListArrayList.get(position).getCategoryId();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        publish_product = findViewById(R.id.publish_product);
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        offer_price = findViewById(R.id.offer_price);
        tital_name_et = findViewById(R.id.tital_name_et);
        offer_desc = findViewById(R.id.offer_desc);
        uploadimg = findViewById(R.id.uploadimg);
        add = findViewById(R.id.add);
        minus = findViewById(R.id.minus);
        percantae = findViewById(R.id.percantae);
        seekbar = findViewById(R.id.seekbar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               /* cur_progress =progress;
                MerchantSignupSlider.mer_reward= String.valueOf(progress);
                percantae.setText(""+cur_progress+" %");*/
                String off_price = offer_price.getText().toString();
                int MIN = 0;
                if (progress <= MIN) {
                    offer_discount_price_tv.setText("");
                    after_discount_str = "";
                    cur_progress = 0;
                    percantae.setText("" + cur_progress + " %");


                } else {
                    cur_progress = progress;
                    if (off_price == null || off_price.equalsIgnoreCase("")) {

                    } else {
                        double price = Double.parseDouble(off_price);
                        double ppp = cur_progress / 100;
                        double discountet_price = price * (1 - ppp);
                        after_discount_str = String.format("%.2f", new BigDecimal(discountet_price));
                        offer_discount_price_tv.setText("" + after_discount_str);


                    }


                    percantae.setText("" + cur_progress + " %");
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        offer_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String off_price = offer_price.getText().toString();
                if (off_price == null || off_price.equalsIgnoreCase("")) {
                    offer_discount_price_tv.setText("");
                    after_discount_str = "";
                } else {
                    if (cur_progress > 0) {
                        double price = Double.parseDouble(off_price);
                        double ppp = cur_progress / 100;
                        double discountet_price = price * (1 - ppp);
                        after_discount_str = String.format("%.2f", new BigDecimal(discountet_price));
                        offer_discount_price_tv.setText("" + after_discount_str);

                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        offer_price.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    final int beforeDecimal = 8;
                    final int afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = offer_price.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });

    }

    private void selectImage() {
        try {
            final Dialog dialogSts = new Dialog(AddOffersAct.this, R.style.DialogSlideAnim);
            dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSts.setCancelable(false);
            dialogSts.setContentView(R.layout.select_img_lay);
            dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button camera = (Button) dialogSts.findViewById(R.id.camera);
            Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
            TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
            gallary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSts.dismiss();
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY);

                }
            });
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSts.dismiss();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);

                }
            });
            cont_find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSts.dismiss();
                }
            });
            dialogSts.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //  Log.e("image_path.===..", "" + path);
        }
        cursor.close();
        return path;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(AddOffersAct.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_" + dateToStr + ".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public void decodeFile(String filePath) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);


            ImagePath = saveToInternalStorage(bitmap);
            Log.e("DECODE PATH", "ff " + ImagePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
   /*     try{
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
            Log.e("DECODE PATH", "ff " + ImagePath);
            uploadimg.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 1:
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (data == null) return;
                        // Get photo picker response for single select.
                        final Uri selectedImage = data.getData();
                        try {
                            assert selectedImage != null;
                            try (final InputStream stream = getContentResolver().openInputStream(selectedImage)) {
                                final Bitmap bitmap = BitmapFactory.decodeStream(stream);
                                uploadimg.setImageBitmap(bitmap);
                                File tempfile = Tools.persistImage(bitmap, getApplicationContext());
                                ImagePath = tempfile.getAbsolutePath();
                                Log.e("ImagePath", "onActivityResult: " + ImagePath);
                            }
                        } catch (IOException e) {
                            ToolsShowDialog(getApplicationContext(), e.getLocalizedMessage());
                        }
                    } else {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        uploadimg.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                        Log.e("picturePath", picturePath);

                        file = new File(picturePath);
                        String fileName = file.getName();
                        Log.e("file", fileName);

                        Log.e("file", fileName);
                        String[] filenameArray = fileName.split("\\.");
                        extension = filenameArray[filenameArray.length - 1];
                        Log.e("fileName", extension);
                        Long tsLong = System.currentTimeMillis() / 1000;
                        timeStamp = tsLong.toString();
                        imageBytes = convertImageToByte(selectedImage, extension);
                        Log.e("fileName", "" + imageBytes.length);
                        decodeFile(picturePath);
                    }
                    break;

                case 2:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String cameraPath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + cameraPath);
                    //  String ImagePath = getPath(selectedImage);
                    decodeFile(cameraPath);
                    break;

            }
        }

    }

    public byte[] convertImageToByte(Uri uri, String extension) {
        byte[] data = null;
        try {
            ContentResolver cr = AddOffersAct.this.getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (extension.equals("jpg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            } else if (extension.equals("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            }
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void getOfferCategory() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        categoryBeanListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getOfferCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyOffercate(responseData);

                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
                            categoryBeanListArrayList.addAll(successData.getResult());

                        }

                        categoryAdpters = new CategoryAdpters(AddOffersAct.this, categoryBeanListArrayList);
                        category_spinner.setAdapter(categoryAdpters);
                        if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                            for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
/*
                                if (category_id.equalsIgnoreCase(categoryBeanListArrayList.get(i).getCategoryId())){
                                    category_spinner.setSelection(i);
                                }
*/
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();


                Log.e("TAG", t.toString());
            }
        });


    }
   /* private void getCategoryType() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        Log.e("loginCall >", " > FIRST");
        progresbar.setVisibility(View.VISIBLE);
        categoryBeanListArrayList = new ArrayList<>();
        CategoryBeanList categoryBeanList = new CategoryBeanList();
        categoryBeanList.setCategoryId("0");
        categoryBeanList.setCategoryName(getString(R.string.selectcat));
categoryBeanList.setCategory_name_spanish(getString(R.string.selectcat));
categoryBeanList.setCategory_name_hindi(getString(R.string.selectcat));
        categoryBeanListArrayList.add(categoryBeanList);
        Call<ResponseBody> call = ApiClient.getApiInterface().getBusnessCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("loginCall >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setMerchantcat(responseData);

                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
                            categoryBeanListArrayList.addAll(successData.getResult());

                        }

                        categoryAdpters = new CategoryAdpters(AddOffersAct.this, categoryBeanListArrayList);
                        category_spinner.setAdapter(categoryAdpters);
                        if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                            for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
*//*
                                if (category_id.equalsIgnoreCase(categoryBeanListArrayList.get(i).getCategoryId())){
                                    category_spinner.setSelection(i);
                                }
*//*
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }*/

    public class AddOfferProdAsc extends AsyncTask<String, String, String> {
        String Jsondata;

        protected void onPreExecute() {
            try {
                super.onPreExecute();
                progresbar.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "add_offer.php?";
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("user_id", user_id);
                multipart.addFormField("offer_name", tital_name_str);
                multipart.addFormField("offer_description", offer_desc_str);
                multipart.addFormField("offer_price", offer_price_str);
                multipart.addFormField("category_id", category_id);
                multipart.addFormField("offer_discount", "" + cur_progress);
                multipart.addFormField("offer_discount_price", "" + after_discount_str.trim());
                if (ImagePath == null || ImagePath.equalsIgnoreCase("")) {

                } else {
                    File ImageFile = new File(ImagePath);
                    multipart.addFilePart("offer_image", ImageFile);
                }
                List<String> response = multipart.finish();
                for (String line : response) {
                    Jsondata = line;
                }
                JSONObject object = new JSONObject(Jsondata);
                return Jsondata;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progresbar.setVisibility(View.GONE);
            Log.e("Add Product ", " >> " + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                        Toast.makeText(AddOffersAct.this, getResources().getString(R.string.offeradded), Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public class CategoryAdpters extends BaseAdapter {
        private final ArrayList<CategoryBeanList> categoryBeanLists;
        Context context;
        LayoutInflater inflter;

        public CategoryAdpters(Context applicationContext, ArrayList<CategoryBeanList> categoryBeanLists) {
            this.context = applicationContext;
            this.categoryBeanLists = categoryBeanLists;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return categoryBeanLists == null ? 0 : categoryBeanLists.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinner_layout, null);
            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);


            if (mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase("es")) {
                names.setText(categoryBeanLists.get(i).getCategory_name_spanish());
            } else if (mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase("hi")) {

                Log.e("TAG", "getView:9090909090 " + categoryBeanLists.get(i).getCategory_name_hindi());
                names.setText(categoryBeanLists.get(i).getCategory_name_hindi());
            } else {
                names.setText(categoryBeanLists.get(i).getCategoryName());
            }
            return view;
        }
    }

}
