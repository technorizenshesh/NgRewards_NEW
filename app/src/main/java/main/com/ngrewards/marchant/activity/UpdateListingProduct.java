package main.com.ngrewards.marchant.activity;

import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.beanclasses.ProductImage;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.marchant.merchantbottum.MultiPhotoSelectActivity;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateListingProduct extends AppCompatActivity {
    private static final String TAG = "UpdateListingProduct";
    //public static ArrayList<String> ImagePathArrayList;
    private RelativeLayout backlay;
    private ImageView uploadimg;
    HorizontalAdapter horizontalAdapter;
    private RecyclerView add_product_list;
    private Spinner category_spinner;
    ProgressBar progresbar;
    private ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private CategoryAdpters categoryAdpters;
    private String category_id = "";
    private EditText shipping_price_et,stock_et, tital_name_et, description_et, price_et, shipping_et, sizes_et, colors_et;
    private String user_id = "", stock_str = "",shipping_price_str="", tital_name_str = "", description_str = "", price_str = "", sizes_str = "", colors_str = "", shipping_str = "";
    private TextView update_item_tv;
    File[] filearray;
    MySession mySession;
    Myapisession myapisession;
    private int remove_pos;
    public static ArrayList<ProductImage> ImagePathArrayList;
    public static ArrayList<String> ImagePathArrayList_str;
    private String   split_amount= "";
    private String  split_payments= "";
    boolean IsSplited  = false;
    LinearLayout split_lay;
    CheckBox split_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_your_listing);
        Log.e(TAG, "onCreate: "+TAG );
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        filearray = new File[0];
        ImagePathArrayList = new ArrayList<>();
        ImagePathArrayList_str = new ArrayList<>();
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinti();

        clickevent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {

        } else {


            //  ImagePathArrayList.add(bundle.getString("Imagepath"));
            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
            add_product_list.setAdapter(horizontalAdapter);
            horizontalAdapter.notifyDataSetChanged();
        }


        if (myapisession.getProductdata() == null || myapisession.getProductdata().equalsIgnoreCase("")) {
            getCategoryType();
        } else {
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

                categoryAdpters = new CategoryAdpters(UpdateListingProduct.this, categoryBeanListArrayList);
                category_spinner.setAdapter(categoryAdpters);
                Log.e("category_id >> ", ">> " + category_id);
                for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
                    String cityname = categoryBeanListArrayList.get(i).getCategoryId();
                    Log.e("cityname >> ", ">> " + cityname);
                    {
                        if (cityname.equalsIgnoreCase(category_id))//for default selection
                        {
                            category_spinner.setSelection(i, true);
                            break;
                        }





                    }
                }

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
                    Toast.makeText(UpdateListingProduct.this, "Only 10 images Uploaded", Toast.LENGTH_LONG).show();
                } else if (ImagePathArrayList.size() < 10) {
                    selectImage();
                }
            }
        });
        update_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tital_name_str = tital_name_et.getText().toString();
                description_str = description_et.getText().toString();
                price_str = price_et.getText().toString();
                shipping_str = shipping_et.getText().toString();
                colors_str = colors_et.getText().toString();
                sizes_str = sizes_et.getText().toString();
                stock_str = stock_et.getText().toString();
                shipping_price_str = shipping_price_et.getText().toString();
                if (tital_name_str == null || tital_name_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.entertite), Toast.LENGTH_LONG).show();
                } else if (category_id == null || category_id.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.selectcat), Toast.LENGTH_LONG).show();
                } else if (description_str == null || description_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.enterdesc), Toast.LENGTH_LONG).show();

                } else if (stock_str == null || stock_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.enteritemstockquantity), Toast.LENGTH_LONG).show();

                } else if (price_str == null || price_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.enterprice), Toast.LENGTH_LONG).show();

                } else if (shipping_str == null || shipping_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.entershiping), Toast.LENGTH_LONG).show();

                } else if (ImagePathArrayList == null || ImagePathArrayList.isEmpty() || ImagePathArrayList.size() == 0) {
                    Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.selectphoto), Toast.LENGTH_LONG).show();

                } else {
                    Log.e("ImagePathArrayList size", " > " + ImagePathArrayList.size());
                    filearray = new File[ImagePathArrayList.size()];
                    Log.e("filearray size", " > " + filearray.length);


                    filearray = new File[ImagePathArrayList_str.size()];
                    Log.e("filearray size", " > " + filearray.length);

                    for (int i = 0; i < ImagePathArrayList_str.size(); i++) {
                        Log.e("Image", " > " + ImagePathArrayList_str.get(i));
                        if (i < 8) {

                        }
                        File ImageFile = new File(ImagePathArrayList_str.get(i));
                        filearray[i] = ImageFile;
                    }


                    /*for (int i = 0; i < ImagePathArrayList.size(); i++) {
                        Log.e("Image", " > " + ImagePathArrayList.get(i));

                        File ImageFile = new File(ImagePathArrayList.get(i));
                        filearray[i] = ImageFile;
                    }*/
//Toast.makeText(UpdateListingProduct.this,getResources().getString(R.string.inworking),Toast.LENGTH_LONG).show();
                    new UpdateProductsAsc().execute();
                }


            }
        });
    }

    private void idinti() {

        shipping_price_et = findViewById(R.id.shipping_price_et);
        stock_et = findViewById(R.id.stock_et);
        colors_et = findViewById(R.id.colors_et);
        sizes_et = findViewById(R.id.sizes_et);
        update_item_tv = findViewById(R.id.update_item_tv);
        shipping_et = findViewById(R.id.shipping_et);
        price_et = findViewById(R.id.price_et);
        description_et = findViewById(R.id.description_et);
        tital_name_et = findViewById(R.id.tital_name_et);
        category_spinner = findViewById(R.id.category_spinner);
        progresbar = findViewById(R.id.progresbar);
        add_product_list = findViewById(R.id.add_product_list);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(UpdateListingProduct.this, LinearLayoutManager.HORIZONTAL, false);
        add_product_list.setLayoutManager(horizontalLayoutManagaer);
        backlay = findViewById(R.id.backlay);
        uploadimg = findViewById(R.id.uploadimg);
        split_lay = findViewById(R.id.split_lay);
        split_check = findViewById(R.id.split_check);


        split_lay.setOnClickListener(v -> {

            if (IsSplited){
                split_amount ="";
                IsSplited = false;
                split_check.setChecked(false);
                Toast.makeText(UpdateListingProduct.this, "Split Payments Removed",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if (price_et.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(UpdateListingProduct.this, "Please Enter Amount First",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    enterNoOfSplits(price_et.getText().toString());
                }}

        });


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

        price_et.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 8, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = price_et.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.toString().indexOf(".") == -1) {
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
        if (ActiveProductsAct.product_item_detail != null) {
            Log.e(TAG, "idinti: ActiveProductsAct.product_item_detail.getSplit_payments()"+ActiveProductsAct.product_item_detail.getSplit_payments() );
if (ActiveProductsAct.product_item_detail.getSplit_payments()!=
        null&&!ActiveProductsAct.product_item_detail.getSplit_payments()
        .equalsIgnoreCase("")){

    split_lay.setVisibility(View.VISIBLE);
}else {
    split_lay.setVisibility(View.VISIBLE);

}


            tital_name_et.setText("" + ActiveProductsAct.product_item_detail.getProductName());
            category_id = ActiveProductsAct.product_item_detail.getCategoryId();
            description_et.setText("" + ActiveProductsAct.product_item_detail.getProductDescription());
            sizes_et.setText("" + ActiveProductsAct.product_item_detail.getSize());
            colors_et.setText("" + ActiveProductsAct.product_item_detail.getColor());
            price_et.setText("" + ActiveProductsAct.product_item_detail.getPrice());
            stock_et.setText("" + ActiveProductsAct.product_item_detail.getStock());
            shipping_et.setText("" + ActiveProductsAct.product_item_detail.getShipping_time());
            if (ActiveProductsAct.product_item_detail.getShipping_price()!=null&&!ActiveProductsAct.product_item_detail.getShipping_price().equalsIgnoreCase("")){
                shipping_price_et.setText("" + ActiveProductsAct.product_item_detail.getShipping_price());
            }
            ImagePathArrayList.addAll(ActiveProductsAct.product_item_detail.getProductImages());
            if (ImagePathArrayList != null && !ImagePathArrayList.isEmpty()) {
                add_product_list.setVisibility(View.VISIBLE);
                horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                add_product_list.setAdapter(horizontalAdapter);
                horizontalAdapter.notifyDataSetChanged();
            }

        }


    }

    private void selectImage() {
        final Dialog dialogSts = new Dialog(UpdateListingProduct.this, R.style.DialogSlideAnim);
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
                Intent i = new Intent(UpdateListingProduct.this, MultiPhotoSelectActivity.class);
                startActivity(i);

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
    private void enterNoOfSplits(String amount) {
        try {
            final Dialog dialogSts = new Dialog( this, R.style.DialogSlideAnim);
            dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSts.setCancelable(false);
            dialogSts.setContentView(R.layout.enter_no_of_split_item);
            dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button submitt = (Button) dialogSts.findViewById(R.id.submitt);
            Button cancel = (Button) dialogSts.findViewById(R.id.cancel);
            EditText no_of_pay = (EditText) dialogSts.findViewById(R.id.no_of_pay);
            submitt.setOnClickListener(v -> {
                try {
                    if (no_of_pay.getText().toString().equalsIgnoreCase("")) {
                        no_of_pay.setError("Empty");
                    } else {

                        if (shipping_price_et.getText().toString().equalsIgnoreCase("")) {
                            dialogSts.dismiss();
                            listSplits(Double.parseDouble(amount), Double.parseDouble(no_of_pay.getText().toString()));
                        } else {
                            dialogSts.dismiss();
                            double ship = Double.parseDouble(shipping_price_et.getText().toString());
                            double price = Double.parseDouble(amount);
                            double price2 = ship + price;
                            listSplits(price2, Double.parseDouble(no_of_pay.getText().toString()));
                        }


                    }
                } catch (Exception e) {
                    Toast.makeText( this, e.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                    dialogSts.dismiss();
                }

            });
            cancel.setOnClickListener(v -> dialogSts.dismiss());
            dialogSts.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void listSplits(double price, double noofemi) {
        try {
            Log.e("TAG", "listSplits:priceprice " + price);
            Log.e("TAG", "listSplits: noofemi " + noofemi);
            Log.e("TAG", "listSplits: noofemi " + price / noofemi);
            double data = price / noofemi;
            ArrayList<String> peopleList = new ArrayList<>();
            for (int i = 0; i < noofemi; i++) {
                DecimalFormat f = new DecimalFormat("##.00");

                peopleList.add("" + f.format(data));
            }
             FinalPuzzelAdapter finalPuzzelAdapter = new FinalPuzzelAdapter(peopleList);
            final Dialog dialogSts = new Dialog( this, R.style.DialogSlideAnim);
            dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSts.setCancelable(false);
            dialogSts.setContentView(R.layout.split__list_item);
            dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RecyclerView recy_list = dialogSts.findViewById(R.id.recy_list);
            Button submitt = (Button) dialogSts.findViewById(R.id.submitt);
            TextView cancel = (TextView) dialogSts.findViewById(R.id.cancel);
            recy_list.hasFixedSize();
            recy_list.setLayoutManager(new LinearLayoutManager(this));
            recy_list.setAdapter(finalPuzzelAdapter);
            submitt.setOnClickListener(v -> {
                split_amount   =     TextUtils.join(", ", peopleList);
                Log.e("TAG", "listSplits:split_amountsplit_amount "+split_amount );
                IsSplited = true ;
                split_check.setChecked(true);
                dialogSts.dismiss();
            });
            cancel.setOnClickListener(v -> dialogSts.dismiss());
            dialogSts.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "listSplits: " + e.getMessage());
            Log.e("TAG", "listSplits: " + e.getLocalizedMessage());
        }

    }

    public class FinalPuzzelAdapter extends RecyclerView.Adapter< FinalPuzzelAdapter.SelectTimeViewHolder> {
        private ArrayList<String> peopleList;

        public FinalPuzzelAdapter(ArrayList<String> peopleList) {
            this.peopleList = peopleList;
        }

        @NonNull
        @Override
        public  FinalPuzzelAdapter.SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.list_split_item_item, parent, false);
             FinalPuzzelAdapter.SelectTimeViewHolder viewHolder = new  FinalPuzzelAdapter.SelectTimeViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull  FinalPuzzelAdapter.SelectTimeViewHolder holder, @SuppressLint("RecyclerView") int position) {
            TextView ivFinalImage = holder.itemView.findViewById(R.id.emi_item);
            int prop = position + 1;
            ivFinalImage.setText("Payment " + prop + " - $ " + peopleList.get(position));

        }

        @Override
        public int getItemCount() {
            return peopleList.size();
        }

        public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
            public SelectTimeViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
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
                    ProductImage productImage = new ProductImage();
                    productImage.setImageId("0");
                    productImage.setProductImage(MultiPhotoSelectActivity.image.get(i));
                    ImagePathArrayList.add(productImage);
                    ImagePathArrayList_str.add(MultiPhotoSelectActivity.image.get(i));
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
        private ArrayList<ProductImage> ImagePathArray;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView ProductImageImagevies, remove_images;
            //   RelativeLayout RLRemovePhoto;

            public MyViewHolder(View view) {
                super(view);
                remove_images = (ImageView) view.findViewById(R.id.remove_images);
                ProductImageImagevies = (ImageView) view.findViewById(R.id.productimage);
                //    RLRemovePhoto = (RelativeLayout) view.findViewById(R.id.RLRemovePhoto);

            }
        }


        public HorizontalAdapter(ArrayList<ProductImage> ImagePathArrayList) {
            this.horizontalList = horizontalList;
            this.ImagePathArray = ImagePathArrayList;
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.business_lay_img, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder,  int position) {
            if (ImagePathArray.get(position) != null) {

                if (ImagePathArray.get(position).getImageId().equalsIgnoreCase("0")) {
                    holder.ProductImageImagevies.setImageURI(Uri.fromFile(new File(ImagePathArray.get(position).getProductImage())));
                } else {
                    String product_img = ImagePathArray.get(position).getProductImage();
                    if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

                    } else {

                        Picasso.with(UpdateListingProduct.this).load(product_img).placeholder(R.drawable.placeholder).into(holder.ProductImageImagevies);


/*
                        Glide.with(UpdateListingProduct.this)
                                .load(product_img)
                                .thumbnail(0.5f)
                                .override(150, 150)
                                .centerCrop()
                                .crossFade()
                                .placeholder(R.drawable.placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;

                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                        return false;
                                    }
                                })
                                .into(holder.ProductImageImagevies);
*/

                    }
                }


            }
            holder.remove_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImagePathArray.get(position).getImageId().equalsIgnoreCase("0")) {
                        ImagePathArrayList.remove(position);
                        horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                        add_product_list.setAdapter(horizontalAdapter);
                        horizontalAdapter.notifyDataSetChanged();
                    } else {
                        remove_pos = position;
                        removeImages(remove_pos, ImagePathArray.get(position).getImageId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ImagePathArrayList == null ? 0 : ImagePathArrayList.size();

        }
    }

    private void removeImages(final int remove_pos, String id) {
        //http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        Log.e("remove image id >"," >> "+id);
        Log.e("remove product id >"," >> "+ActiveProductsAct.product_item_detail.getId());
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().removeProductImages(ActiveProductsAct.product_item_detail.getId(),id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Remove Images >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            ImagePathArrayList.remove(remove_pos);
                            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                            add_product_list.setAdapter(horizontalAdapter);
                            horizontalAdapter.notifyDataSetChanged();

                        }
                        else {
                            Toast.makeText(UpdateListingProduct.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri selectedImage = data.getData();
                    //getPath(selectedImage);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String FinalPath = cursor.getString(columnIndex);
                    cursor.close();
                    String ImagePath = getPath(selectedImage);

                    decodeFile(ImagePath);

                    break;
                case 2:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String cameraPath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + cameraPath);
                    //  String ImagePath = getPath(selectedImage);

                    decodeFile(cameraPath);
                    break;
                case 3:
                    Bitmap photo1 = (Bitmap) data.getExtras().get("data");
                    String cameraPath1 = saveToInternalStorage(photo1);
                    Log.e("PATH Camera", "" + cameraPath1);
                    //  String ImagePath = getPath(selectedImage);
                    ProductImage productImage = new ProductImage();
                    productImage.setImageId("0");
                    productImage.setProductImage(cameraPath1);
                    ImagePathArrayList.add(productImage);
                    ImagePathArrayList_str.add(cameraPath1);
                    add_product_list.setVisibility(View.VISIBLE);
                    horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                    add_product_list.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();
                    //  decodeFile(cameraPath);
                    break;


            }
        }

    }

    @SuppressLint("Range")
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
        ContextWrapper cw = new ContextWrapper(UpdateListingProduct.this);
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

                        categoryAdpters = new CategoryAdpters(UpdateListingProduct.this, categoryBeanListArrayList);
                        category_spinner.setAdapter(categoryAdpters);
                        for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
                            String cityname = categoryBeanListArrayList.get(i).getCategoryId();
                            {
                                if (cityname.equalsIgnoreCase(category_id))//for default selection
                                    category_spinner.setSelection(i, true);
                                break;


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

    public class UpdateProductsAsc extends AsyncTask<String, String, String> {
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
//https://myngrewards.com/demo/wp-content/plugins/webservice/update_product.php?user_id=539&product_id=56300&product_name=Color%20and%20Size%202%20New%20Test%20Product%20Attr&offer_description=TestAttProductDescription&product_price=2&color=Red&size=Small
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "update_product.php?";
            Log.e("requestURL >>", requestURL);
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("user_id", user_id);
                multipart.addFormField("product_id", ActiveProductsAct.product_item_detail.getId());
                multipart.addFormField("product_name", tital_name_str);
                multipart.addFormField("product_description", description_str);
                multipart.addFormField("product_price", price_str);
                multipart.addFormField("shipping_time", shipping_str);
                multipart.addFormField("category_id", category_id);
                multipart.addFormField("color", colors_str);
                multipart.addFormField("size", sizes_str);
                multipart.addFormField("stock", stock_str);
                multipart.addFormField("split_amount",    split_amount);
                multipart.addFormField("split_payments", split_payments);

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
                        if (k < 7) {
                            multipart.addFilePart("product_image[]", filearray[k]);
                        }

                    }

                    // multipart.addFilePart("member_image[]", filearray);
                }
                List<String> response = multipart.finish();

                for (String line : response) {
                    Jsondata = line;
                }
                JSONObject object = new JSONObject(Jsondata);
                return Jsondata;

            } catch (Exception e) {
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
                        Toast.makeText(UpdateListingProduct.this, getResources().getString(R.string.productaddedsucc), Toast.LENGTH_LONG).show();
                        if (ImagePathArrayList != null) {
                            ImagePathArrayList.clear();
                        }
                        if (ImagePathArrayList_str != null) {
                            ImagePathArrayList_str.clear();
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }

}
