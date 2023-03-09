package main.com.ngrewards.marchant.activity;

import android.app.Dialog;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.merchantbottum.MultiPhotoSelectActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;

public class StartYourListing extends AppCompatActivity {
    public static ArrayList<String> ImagePathArrayList;
    private RelativeLayout backlay;
    private ImageView uploadimg;
    HorizontalAdapter horizontalAdapter;
    private RecyclerView add_product_list;
    private Spinner category_spinner;
    ProgressBar progresbar;
    private ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private CategoryAdpters categoryAdpters;
    private String category_id = "";
    private EditText shipping_price_et,stock_et,tital_name_et, description_et, price_et, shipping_et,sizes_et,colors_et;
    private String user_id = "",stripe_account_id="",time_zone="",shipping_price_str="",stock_str="", tital_name_str = "", description_str = "", price_str = "",sizes_str="",colors_str="", shipping_str = "";
    private TextView list_item_tv;
    File[] filearray;
    MySession mySession;
    Myapisession myapisession;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_your_listing);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        filearray = new File[0];
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
        idinti();
        ImagePathArrayList = new ArrayList<>();
        clickevent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {

        } else {


            //  ImagePathArrayList.add(bundle.getString("Imagepath"));
            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
            add_product_list.setAdapter(horizontalAdapter);
            horizontalAdapter.notifyDataSetChanged();
        }


        if (myapisession.getProductdata()==null||myapisession.getProductdata().equalsIgnoreCase("")){
            getCategoryType();
        }
        else {
            try {
                categoryBeanListArrayList = new ArrayList<>();
                CategoryBeanList categoryBeanList = new CategoryBeanList();
                categoryBeanList.setCategoryId("0");
                categoryBeanList.setCategoryName("Select category");
                categoryBeanListArrayList.add(categoryBeanList);
                JSONObject object = new JSONObject(myapisession.getProductdata());
                Log.e("loginCall >", " >" + myapisession.getProductdata());
                if (object.getString("status").equals("1")) {
                    CategoryBean successData = new Gson().fromJson(myapisession.getProductdata(), CategoryBean.class);
                    categoryBeanListArrayList.addAll(successData.getResult());
                }

                categoryAdpters = new CategoryAdpters(StartYourListing.this, categoryBeanListArrayList);
                category_spinner.setAdapter(categoryAdpters);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
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
                if (ImagePathArrayList.size() == 10) {
                    Toast.makeText(StartYourListing.this, "Only 10 images Uploaded", Toast.LENGTH_LONG).show();
                } else if (ImagePathArrayList.size() < 10) {
                    selectImage();
                }
            }
        });
        list_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stripe_account_id==null||stripe_account_id.equalsIgnoreCase(""))
                {
                   Toast.makeText(StartYourListing.this,getResources().getString(R.string.plseftcrtestractt),Toast.LENGTH_LONG).show();
                }
                else {
                    tital_name_str = tital_name_et.getText().toString();
                    description_str = description_et.getText().toString();
                    price_str = price_et.getText().toString();
                    shipping_str = shipping_et.getText().toString();
                    colors_str = colors_et.getText().toString();
                    sizes_str = sizes_et.getText().toString();
                    stock_str = stock_et.getText().toString();
                    shipping_price_str = shipping_price_et.getText().toString();
                    if (tital_name_str == null || tital_name_str.equalsIgnoreCase("")) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.entertite), Toast.LENGTH_LONG).show();
                    } else if (category_id == null || category_id.equalsIgnoreCase("")) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.selectcat), Toast.LENGTH_LONG).show();
                    } else if (description_str == null || description_str.equalsIgnoreCase("")) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.enterdesc), Toast.LENGTH_LONG).show();

                    }
                    else if (stock_str == null || stock_str.equalsIgnoreCase("")) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.enteritemstockquantity), Toast.LENGTH_LONG).show();

                    }
                    else if (price_str == null || price_str.equalsIgnoreCase("")) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.enterprice), Toast.LENGTH_LONG).show();

                    } else if (shipping_str == null || shipping_str.equalsIgnoreCase("")) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.entershiping), Toast.LENGTH_LONG).show();

                    } else if (ImagePathArrayList == null || ImagePathArrayList.isEmpty() || ImagePathArrayList.size() == 0) {
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.selectphoto), Toast.LENGTH_LONG).show();

                    } else {
                        Log.e("ImagePathArrayList size", " > " + ImagePathArrayList.size());
                        filearray = new File[ImagePathArrayList.size()];
                        Log.e("filearray size", " > " + filearray.length);

                        for (int i = 0; i < ImagePathArrayList.size(); i++) {
                            Log.e("Image", " > " + ImagePathArrayList.get(i));

                            File ImageFile = new File(ImagePathArrayList.get(i));
                            filearray[i] = ImageFile;
                        }

                        new AddProductsAsc().execute();
                    }
                }




            }
        });
    }

    private void idinti() {

        shipping_price_et = findViewById(R.id.shipping_price_et);
        stock_et = findViewById(R.id.stock_et);
        colors_et = findViewById(R.id.colors_et);
        sizes_et = findViewById(R.id.sizes_et);
        list_item_tv = findViewById(R.id.list_item_tv);
        shipping_et = findViewById(R.id.shipping_et);
        price_et = findViewById(R.id.price_et);
        description_et = findViewById(R.id.description_et);
        tital_name_et = findViewById(R.id.tital_name_et);
        category_spinner = findViewById(R.id.category_spinner);
        progresbar = findViewById(R.id.progresbar);
        add_product_list = findViewById(R.id.add_product_list);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(StartYourListing.this, LinearLayoutManager.HORIZONTAL, false);
        add_product_list.setLayoutManager(horizontalLayoutManagaer);
        backlay = findViewById(R.id.backlay);
        uploadimg = findViewById(R.id.uploadimg);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    if (categoryBeanListArrayList.get(position).getCategoryId().equalsIgnoreCase("0")) {
                        category_id = "";
                    } else {
                        category_id = categoryBeanListArrayList.get(position).getCategoryId();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        price_et.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 8, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = price_et.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
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
        final Dialog dialogSts = new Dialog(StartYourListing.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_img_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button camera = (Button) dialogSts.findViewById(R.id.camera);
        Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(StartYourListing.this, MultiPhotoSelectActivity.class);
                startActivity(i);

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
               /* ContextWrapper cw = new ContextWrapper(StartYourListing.this);
              //  File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File file = new File(Environment.getExternalStorageDirectory(), "/imageDir/a" + "/photo_" + timeStamp + ".png");
                imageUri = Uri.fromFile(file);*/


               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);*/

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
             //   cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MultiPhotoSelectActivity.image == null) {

        } else if (MultiPhotoSelectActivity.image.isEmpty()) {

        } else {
            for (int i = 0; i < MultiPhotoSelectActivity.image.size(); i++) {
                if (ImagePathArrayList.size() < 10) {
                    Log.e("Select Photo ", " > " + MultiPhotoSelectActivity.image.get(i));
                    ImagePathArrayList.add(MultiPhotoSelectActivity.image.get(i));
                    Log.e("Select Photo add", " > " + ImagePathArrayList.get(i));

                }

            }
            MultiPhotoSelectActivity.image = null;
            add_product_list.setVisibility(View.VISIBLE);
            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
            add_product_list.setAdapter(horizontalAdapter);
            horizontalAdapter.notifyDataSetChanged();
        }

    }

    private class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private ArrayList<Bitmap> horizontalList;
        private ArrayList<String> ImagePathArrayList_adp;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView ProductImageImagevies,removeimages;
            //   RelativeLayout RLRemovePhoto;

            public MyViewHolder(View view) {
                super(view);

                ProductImageImagevies = (ImageView) view.findViewById(R.id.productimage);
                removeimages = (ImageView) view.findViewById(R.id.removeimages);
                //    RLRemovePhoto = (RelativeLayout) view.findViewById(R.id.RLRemovePhoto);

            }
        }


        public HorizontalAdapter(ArrayList<String> ImagePathArrayList_adp) {
            this.horizontalList = horizontalList;
            this.ImagePathArrayList_adp = ImagePathArrayList_adp;
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.horizontal_list_item, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder, final int position) {
            if (ImagePathArrayList_adp.get(position) != null) {
                holder.ProductImageImagevies.setImageURI(Uri.fromFile(new File(ImagePathArrayList_adp.get(position))));

            }
            holder.removeimages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImagePathArrayList!=null&&!ImagePathArrayList.isEmpty()){
                        ImagePathArrayList.remove(position);

                        horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                        add_product_list.setAdapter(horizontalAdapter);
                        horizontalAdapter.notifyDataSetChanged();
                        if (ImagePathArrayList==null||ImagePathArrayList.isEmpty()){
                            add_product_list.setVisibility(View.GONE);
                        }
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return ImagePathArrayList_adp == null ? 0 : ImagePathArrayList_adp.size();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri selectedImage = data.getData();

                    String ImagePath = getPath(selectedImage);
                    ImagePathArrayList.add(ImagePath);

                    //  decodeFile(ImagePath);
                    add_product_list.setVisibility(View.VISIBLE);

                    horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                    add_product_list.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String cameraPath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + cameraPath);
                    //  String ImagePath = getPath(selectedImage);

                   /* Bitmap thumbnail = null;
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String newcampath = getRealPathFromURI(imageUri);*/
                    ImagePathArrayList.add(cameraPath);

                    add_product_list.setVisibility(View.VISIBLE);
                    horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                    add_product_list.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();
                    //  decodeFile(cameraPath);
                    break;

            }
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(StartYourListing.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_"+dateToStr+".PNG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

           /* int size = bitmapImage.getRowBytes() * bitmapImage.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            bitmapImage.copyPixelsToBuffer(byteBuffer);
            //byteArray = byteBuffer.array();


            fos.write(byteBuffer.array());*/
            Bitmap.createScaledBitmap(bitmapImage, 1000, 1000,true);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            //

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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public void decodeFile(String filePath) {
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
        //ImagePath = saveToInternalStorage(bitmap);
        //  Log.e("DECODE PATH", "ff " + ImagePath);
        //user_img.setImageBitmap(bitmap);
    }

    private void getCategoryType() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        Log.e("loginCall >", " > FIRST");
        progresbar.setVisibility(View.VISIBLE);
        categoryBeanListArrayList = new ArrayList<>();
        CategoryBeanList categoryBeanList = new CategoryBeanList();
        categoryBeanList.setCategoryId("0");
        categoryBeanList.setCategoryName("Select category");
        categoryBeanListArrayList.add(categoryBeanList);
        Call<ResponseBody> call = ApiClient.getApiInterface().getCategory();
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
                            myapisession.setProductdata(responseData);
                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
                            categoryBeanListArrayList.addAll(successData.getResult());

                        }

                        categoryAdpters = new CategoryAdpters(StartYourListing.this, categoryBeanListArrayList);
                        category_spinner.setAdapter(categoryAdpters);
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
    }

    public class CategoryAdpters extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private ArrayList<CategoryBeanList> categoryBeanLists;

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
            names.setText(categoryBeanLists.get(i).getCategoryName());
            return view;
        }
    }

    public class AddProductsAsc extends AsyncTask<String, String, String> {
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
//https://myngrewards.com/demo/wp-content/plugins/webservice/add_product.php?user_id=1&product_name=shdjfgdshjfg&product_description=xcvcxvxv&product_price=500&shipping_time=4-5
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "add_product.php?";
            Log.e("requestURL >>", requestURL);
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("user_id", user_id);
                multipart.addFormField("product_name", tital_name_str);
                multipart.addFormField("product_description", description_str);
                multipart.addFormField("product_price", price_str);
                multipart.addFormField("shipping_time", shipping_str);
                multipart.addFormField("category_id", category_id);
                multipart.addFormField("color", colors_str);
                multipart.addFormField("size", sizes_str);
                multipart.addFormField("stock", stock_str);
                multipart.addFormField("timezone", time_zone);
                if (shipping_price_str==null){
                    multipart.addFormField("shipping_price", "");
                }
                else {
                    multipart.addFormField("shipping_price", shipping_price_str);
                }


                if (ImagePathArrayList == null || ImagePathArrayList.isEmpty()) {
//["+k+"]
                } else {
                    for (int k = 0; k < filearray.length; k++) {
                        multipart.addFilePart("product_image[]", filearray[k]);
                    }

                    // multipart.addFilePart("member_image[]", filearray);
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
                        Toast.makeText(StartYourListing.this, getResources().getString(R.string.productaddedsucc), Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
