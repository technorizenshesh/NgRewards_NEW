package main.com.ngrewards.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.BuildConfig;
import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.DetailList;
import main.com.ngrewards.beanclasses.ProductDetail;
import main.com.ngrewards.beanclasses.ProductImage;
import main.com.ngrewards.beanclasses.ProductTopReview;
import main.com.ngrewards.beanclasses.SimilarProduct;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightGridView;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.showzoomableimages.FullScreenImagesActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragItemDetails extends AppCompatActivity {
    private RelativeLayout backlay;
    private ExpandableHeightListView review_list;
    private ExpandableHeightGridView similar_item_list;
    private CustomReviewAdp customReviewAdp;
    private CustomSimilarItem customSimilarItem;
    private ProgressBar progresbar;
    private TextView colors_tv, description_tv, sizes_tv, merchant_name_sec, real_price, item_name, item_description, merchant_name, feedback_type, shipping_info, discountprice;
    public static ArrayList<DetailList> productDetailArrayList;
    private String product_id = "", product_name = "", product_description = "";
    private ViewPager productimage_pager;
    private CustomProductImgAdp customProductImgAdp;
    private CircleImageView user_img;
    private ImageView like_buton, shareproduct, description_arrow, itemarrow, shipingarrow, proimg;
    private MySession mySession;
    private final String like_count = "0";
    private String EMI = "";
    private String stockcount = "";
    private String time_zone = "";
    private String size_select_str = "";
    private String color_select_str = "";
    private String user_id = "";
    private String comment_str = "";
    private String merchant_name_str = "";
    private String product_price = "";
    private String like_status = "";
    private String share_url_str = "";
    private String price_str;
    private String product_thumbimg;
    private LinearLayout description_lay, shipping_lay, itemspeclay;
    private RelativeLayout description_info, shipinfo_but, item_info, colorlay, sizelay;
    private RatingBar rating, rating_done, averagerating;
    private EditText comment_et;
    private TextView buyon_emi_tv, shipping_price, buynow_tv, addtocart_tv, submit_review,
            donereview_tv,
            rating_count, color_tv_head, sizes_tv_head, price_tv, quant_tv, instock_tv;
    private float rating_val;
    private LinearLayout done_review_lay, post_review_lay;
    CirclePageIndicator fullscreen_indecator;
    private int count = 0, sumcount;
    private Button plusq, minusq;
    private boolean click_sts = false;
    private Myapisession myapisession;
    private List<String> sizelist, colorlist;
    private List<String> sizelist_sel, colorlist_sel;
    private CustomSizeColAdapter customSizeColAdapter;
    ExpandableHeightListView sizelistview;
    private boolean size_sts = false;
    private boolean color_sts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_item_details);
         try {
             buyon_emi_tv = findViewById(R.id.buyon_emi_tv);
             shipping_price = findViewById(R.id.shipping_price);
             sizelay = findViewById(R.id.sizelay);
             colorlay = findViewById(R.id.colorlay);
             buynow_tv = findViewById(R.id.buynow_tv);
             addtocart_tv = findViewById(R.id.addtocart_tv);
             instock_tv = findViewById(R.id.instock_tv);
             quant_tv = findViewById(R.id.quant_tv);
             minusq = findViewById(R.id.minusq);
             plusq = findViewById(R.id.plusq);
             color_tv_head = findViewById(R.id.color_tv_head);
             price_tv = findViewById(R.id.price_tv);
             sizes_tv_head = findViewById(R.id.sizes_tv_head);
             rating_count = findViewById(R.id.rating_count);
             averagerating = findViewById(R.id.averagerating);
             fullscreen_indecator = findViewById(R.id.fullscreen_indecator);
             donereview_tv = findViewById(R.id.donereview_tv);
             rating_done = findViewById(R.id.rating_done);
             post_review_lay = findViewById(R.id.post_review_lay);
             done_review_lay = findViewById(R.id.done_review_lay);
             rating = findViewById(R.id.rating);
             submit_review = findViewById(R.id.submit_review);
             comment_et = findViewById(R.id.comment_et);
             proimg = findViewById(R.id.proimg);
             colors_tv = findViewById(R.id.colors_tv);
             item_info = findViewById(R.id.item_info);
             shipinfo_but = findViewById(R.id.shipinfo_but);
             description_info = findViewById(R.id.description_info);
             itemarrow = findViewById(R.id.itemarrow);
             itemspeclay = findViewById(R.id.itemspeclay);
             shipingarrow = findViewById(R.id.shipingarrow);
             shipping_lay = findViewById(R.id.shipping_lay);
             description_lay = findViewById(R.id.description_lay);
             description_arrow = findViewById(R.id.description_arrow);
             description_tv = findViewById(R.id.description_tv);
             sizes_tv = findViewById(R.id.sizes_tv);
             shareproduct = findViewById(R.id.shareproduct);
             like_buton = findViewById(R.id.like_buton);
             user_img = findViewById(R.id.user_img);
             progresbar = findViewById(R.id.progresbar);
             productimage_pager = findViewById(R.id.productimage_pager);
             merchant_name_sec = findViewById(R.id.merchant_name_sec);
             discountprice = findViewById(R.id.discountprice);
             real_price = findViewById(R.id.real_price);
             shipping_info = findViewById(R.id.shipping_info);
             feedback_type = findViewById(R.id.feedback_type);
             merchant_name = findViewById(R.id.merchant_name);
             item_description = findViewById(R.id.item_description);
             item_name = findViewById(R.id.item_name);
             backlay = findViewById(R.id.backlay);
             review_list = findViewById(R.id.review_list);
             similar_item_list = findViewById(R.id.similar_item_list);
             sizelist = new ArrayList<>();
             colorlist = new ArrayList<>();
             sizelist_sel = new ArrayList<>();
             colorlist_sel = new ArrayList<>();
             Calendar c = Calendar.getInstance();
             TimeZone tz = c.getTimeZone();
             time_zone = tz.getID();
             mySession = new MySession(this);
             myapisession = new Myapisession(this);
             String user_log_data = mySession.getKeyAlldata();

             idinit();
             clickevent();
             Bundle bundle = getIntent().getExtras();
             if (bundle != null) {
                 Log.e("PRO BUNDLE DATA", " >> " + product_id + " >" + product_name + " <>" + product_description);

                 EMI = bundle.getString("EMI");
                 product_id = bundle.getString("product_id");
                 product_name = bundle.getString("product_name");
                 product_thumbimg = bundle.getString("product_thumbimg");
                 product_description = bundle.getString("product_description");
                 product_price = bundle.getString("product_price");
                 share_url_str = bundle.getString("share_link");
                 merchant_name_str = bundle.getString("merchant_name_str");
                 Log.e("share_url_str >>", "dd" + share_url_str);
                 item_name.setText("" + product_name);
                 item_description.setText("" + product_description);
                 description_tv.setText("" + product_description);
                 if (merchant_name_str == null || merchant_name_str.equalsIgnoreCase("")) {
                     merchant_name.setText(getResources().getString(R.string.staticmerchantname));
                     merchant_name_sec.setText(getResources().getString(R.string.staticmerchantname));

                 } else {
                     merchant_name.setText("" + merchant_name_str);
                     merchant_name_sec.setText("" + merchant_name_str);

                 }

                 if (user_log_data == null) {
                 } else {
                     try {
                         Log.e("TAG", "onCreate: user_log_datauser_log_data  "+user_log_data );
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

                 getFeaturedProductsDetail(product_id);
                 Picasso.with(FragItemDetails.this).load(product_thumbimg).placeholder(R.drawable.placeholder).into(proimg);

             }




         }catch (Exception e
         ){e.printStackTrace();
         }
    }

    private void getFeaturedProductsDetail(String product_id) {
        progresbar.setVisibility(View.VISIBLE);
        productDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getProductDetail(product_id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Item Products Detail>", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            ProductDetail successData = new Gson().fromJson(responseData, ProductDetail.class);
                            productDetailArrayList.addAll(successData.getResult());

                        }
                        if (productDetailArrayList != null && !productDetailArrayList.isEmpty()) {
                            Log.e("My Item Cart Status", " >>" + productDetailArrayList.get(0).getCart_status());

                            if (productDetailArrayList.get(0).getCart_status() != null && productDetailArrayList.get(0).getCart_status().equalsIgnoreCase("In Cart")) {
                                addtocart_tv.setText("" + getResources().getString(R.string.gotocart));
                            } else {
                                addtocart_tv.setText("" + getResources().getString(R.string.addtocart));
                            }
                            discountprice.setText(mySession.getValueOf(MySession.CurrencySign)  + productDetailArrayList.get(0).getPrice());
                            price_str = productDetailArrayList.get(0).getPrice();
                            if (productDetailArrayList.get(0).getStock() != null && !productDetailArrayList.get(0).getStock().equalsIgnoreCase("")) {
                                stockcount = productDetailArrayList.get(0).getStock();
                                instock_tv.setText(getResources().getString(R.string.instock) + " " + stockcount);
                            }
                            String rat_str = productDetailArrayList.get(0).getAverageRating();
                            if (rat_str != null && !rat_str.equalsIgnoreCase("")) {
                                averagerating.setRating(Float.parseFloat(rat_str));
                                rating_count.setText("(" + productDetailArrayList.get(0).getReviewCount() + ")");
                            }
                            price_tv.setText(mySession.getValueOf(MySession.CurrencySign)  + productDetailArrayList.get(0).getPrice());
                            real_price.setText(mySession.getValueOf(MySession.CurrencySign)  + productDetailArrayList.get(0).getPrice());
                            shipping_price.setText("" + getResources().getString(R.string.shippriceing) + " "+mySession.getValueOf(MySession.CurrencySign) +  productDetailArrayList.get(0).getShipping_price());
                            shipping_info.setText("" + productDetailArrayList.get(0).getShippingTime());
                            sizes_tv.setText("" + productDetailArrayList.get(0).getSize());
                            sizes_tv_head.setText("" + productDetailArrayList.get(0).getSize());

                            if (productDetailArrayList.get(0).getSize() == null || productDetailArrayList.get(0).getSize().equalsIgnoreCase("")) {
                                sizelay.setVisibility(View.GONE);
                                size_sts = false;


                            } else {
                                size_sts = true;
                                //Remove whitespace and split by comma
                                sizelist = Arrays.asList(productDetailArrayList.get(0).getSize().split("\\s*,\\s*"));
                                if (sizelist != null && !sizelist.isEmpty()) {
                                    for (int i = 0; i < sizelist.size(); i++) {
                                        sizelist_sel.add("false");
                                    }
                                }
                            }
                            if (productDetailArrayList.get(0).getColor() == null || productDetailArrayList.get(0).getColor().equalsIgnoreCase("")) {
                                colorlay.setVisibility(View.GONE);
                                color_sts = false;
                            } else {
                                color_sts = true;
                                //Remove whitespace and split by comma
                                colorlist = Arrays.asList(productDetailArrayList.get(0).getColor().split("\\s*,\\s*"));
                                if (colorlist != null && !colorlist.isEmpty()) {
                                    for (int i = 0; i < colorlist.size(); i++) {
                                        colorlist_sel.add("false");
                                    }
                                }

                            }


                            colors_tv.setText("" + productDetailArrayList.get(0).getColor());
                            color_tv_head.setText("" + productDetailArrayList.get(0).getColor());
                            if (productDetailArrayList.get(0).getUserDetails() != null && !productDetailArrayList.get(0).getUserDetails().isEmpty()) {
                                String name = productDetailArrayList.get(0).getUserDetails().get(0).getBusinessName();

                                String image_url = productDetailArrayList.get(0).getUserDetails().get(0).getMerchantImage();
                                if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                                    Picasso.with(FragItemDetails.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                                }
                            }
                            like_status = productDetailArrayList.get(0).getLikeStatus();
                            if (like_status.equalsIgnoreCase("Like")) {
                                like_buton.setImageResource(R.drawable.ic_like_red);
                            } else {
                                like_buton.setImageResource(R.drawable.ic_like_blk);
                            }
                            //  share_url_str = productDetailArrayList.get(0).getLikeStatus();

                            /* discountprice = findViewById(R.id.discountprice);
                            real_price = findViewById(R.id.real_price);
                            shipping_info = findViewById(R.id.shipping_info);
                            feedback_type = findViewById(R.id.feedback_type);
                            merchant_name = findViewById(R.id.merchant_name);*/

                            if (productDetailArrayList.get(0).getReviewStatus() != null) {
                                if (productDetailArrayList.get(0).getReviewStatus().equalsIgnoreCase("Yes")) {
                                    post_review_lay.setVisibility(View.GONE);
                                    done_review_lay.setVisibility(View.VISIBLE);
                                    submit_review.setText(getResources().getString(R.string.updatereview));
                                    comment_et.setText("" + productDetailArrayList.get(0).getReview());
                                    donereview_tv.setText("" + productDetailArrayList.get(0).getReview());
                                    if (productDetailArrayList.get(0).getRating() != null && !productDetailArrayList.get(0).getRating().equalsIgnoreCase("")) {
                                        rating.setRating(Float.parseFloat(productDetailArrayList.get(0).getRating()));
                                        rating_done.setRating(Float.parseFloat(productDetailArrayList.get(0).getRating()));
                                    }
                                }

                            }


                            customProductImgAdp = new CustomProductImgAdp(FragItemDetails.this, productDetailArrayList.get(0).getProductImages());
                            productimage_pager.setAdapter(customProductImgAdp);
                            fullscreen_indecator.setViewPager(productimage_pager);
                            final float density = getResources().getDisplayMetrics().density;
                            fullscreen_indecator.setRadius(5 * density);


                            customSimilarItem = new CustomSimilarItem(FragItemDetails.this, productDetailArrayList.get(0).getSimilarProducts());
                            similar_item_list.setAdapter(customSimilarItem);
                            customReviewAdp = new CustomReviewAdp(FragItemDetails.this, productDetailArrayList.get(0).getProductTopReview());
                            review_list.setAdapter(customReviewAdp);
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

    private void selectSizeDrop() {
        final Dialog dialogSts = new Dialog(FragItemDetails.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.custom_drop_down_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sizelistview = dialogSts.findViewById(R.id.sizelistview);
        TextView cancel_tv = dialogSts.findViewById(R.id.cancel_tv);
        TextView selecttv = dialogSts.findViewById(R.id.selecttv);
        sizelistview.setExpanded(true);
        customSizeColAdapter = new CustomSizeColAdapter(FragItemDetails.this, sizelist, "size");
        sizelistview.setAdapter(customSizeColAdapter);
        customSizeColAdapter.notifyDataSetChanged();
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                size_select_str = "";
                sizes_tv_head.setText("" + productDetailArrayList.get(0).getSize());
            }
        });
        selecttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size_select_str == null || size_select_str.equalsIgnoreCase("")) {
                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.selectsize), Toast.LENGTH_LONG).show();
                } else {
                    dialogSts.dismiss();
                    sizes_tv_head.setText("" + size_select_str);
                }
                Log.e("SIZE SELECT", " >> " + size_select_str);
            }
        });
        dialogSts.show();
    }

    private void selectColor() {
        final Dialog dialogSts = new Dialog(FragItemDetails.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.custom_drop_down_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sizelistview = dialogSts.findViewById(R.id.sizelistview);
        TextView cancel_tv = dialogSts.findViewById(R.id.cancel_tv);
        TextView heading = dialogSts.findViewById(R.id.heading);
        TextView selecttv = dialogSts.findViewById(R.id.selecttv);
        heading.setText("" + getResources().getString(R.string.selectcolor));
        sizelistview.setExpanded(true);
        customSizeColAdapter = new CustomSizeColAdapter(FragItemDetails.this, colorlist, "color");
        sizelistview.setAdapter(customSizeColAdapter);
        customSizeColAdapter.notifyDataSetChanged();
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                color_select_str = "";
                sizes_tv_head.setText("" + productDetailArrayList.get(0).getColor());
            }
        });
        selecttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (color_select_str == null || color_select_str.equalsIgnoreCase("")) {
                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.selectcolor), Toast.LENGTH_LONG).show();
                } else {
                    dialogSts.dismiss();
                    color_tv_head.setText("" + color_select_str);
                }

                Log.e("COLOR SELECT", " >> " + color_select_str);
            }
        });
        dialogSts.show();
    }

    public class CustomSizeColAdapter extends BaseAdapter {
        Context context;
        private final String type;
        private LayoutInflater inflater = null;
        List<String> strlist;

        public CustomSizeColAdapter(Context contexts, List<String> strlist, String type) {
            this.type = type;
            this.context = contexts;
            this.strlist = strlist;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            // return 3;
            return strlist == null ? 0 : strlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.custom_size_col_selectlay, null);
            ImageView selimg = rowView.findViewById(R.id.selimg);
            if (type.equalsIgnoreCase("size")) {
                if (sizelist_sel != null && !sizelist_sel.isEmpty()) {
                    if (sizelist_sel.get(position).equalsIgnoreCase("true")) {
                        selimg.setImageResource(R.drawable.ic_checked_circle);
                        size_select_str = sizelist.get(position);
                    } else {
                        selimg.setImageResource(R.drawable.ic_circle_border);
                    }
                }

            } else {
                if (colorlist_sel != null && !colorlist_sel.isEmpty()) {
                    if (colorlist_sel.get(position).equalsIgnoreCase("true")) {
                        selimg.setImageResource(R.drawable.ic_checked_circle);
                        color_select_str = colorlist.get(position);
                    } else {
                        selimg.setImageResource(R.drawable.ic_circle_border);
                    }
                }

            }
            TextView name_tv = rowView.findViewById(R.id.name_tv);
            name_tv.setText(strlist.get(position));

            selimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    if (type.equalsIgnoreCase("size")) {

                        if (sizelist_sel != null && !sizelist_sel.isEmpty()) {
                            for (int k = 0; k < sizelist_sel.size(); k++) {
                                if (pos == k) {
                                    Log.e("CLICK TRUE", "SIZE");
                                    if (sizelist_sel.get(k).equalsIgnoreCase("true")) {
                                        sizelist_sel.set(k, "false");
                                        size_select_str = "";

                                    } else {
                                        size_select_str = sizelist.get(k);
                                        sizelist_sel.set(k, "true");

                                    }
                                } else {
                                    sizelist_sel.set(k, "false");
                                }
                            }


                            customSizeColAdapter = new CustomSizeColAdapter(FragItemDetails.this, sizelist, "size");
                            sizelistview.setAdapter(customSizeColAdapter);
                            customSizeColAdapter.notifyDataSetChanged();
                        }

                    } else {
                        if (colorlist_sel != null && !colorlist_sel.isEmpty()) {

                            for (int k = 0; k < colorlist_sel.size(); k++) {
                                if (pos == k) {
                                    if (colorlist_sel.get(k).equalsIgnoreCase("true")) {
                                        colorlist_sel.set(k, "false");
                                        color_select_str = "";

                                    } else {
                                        color_select_str = colorlist.get(k);
                                        colorlist_sel.set(k, "true");

                                    }
                                } else {

                                    colorlist_sel.set(k, "false");
                                }
                            }

                            customSizeColAdapter = new CustomSizeColAdapter(FragItemDetails.this, colorlist, "color");
                            sizelistview.setAdapter(customSizeColAdapter);
                            customSizeColAdapter.notifyDataSetChanged();
                        }

                    }

                }
            });

            return rowView;
        }

    }

    private void clickevent() {
        plusq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lessthen = 0;
                if (stockcount == null) {

                } else if (stockcount.equalsIgnoreCase("")) {

                } else {
                    String checked = stockcount.replace(",", "");
                    lessthen = Integer.parseInt(checked);
                }

                if (count < lessthen) {

                    sumcount = ++count;
                }

                quant_tv.setText("" + sumcount);


            }
        });

        minusq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    sumcount = --count;
                }

                quant_tv.setText("" + sumcount);

            }
        });
        addtocart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_sts = false;
                if (stockcount != null && !stockcount.equalsIgnoreCase("")) {
                    if (sumcount != 0) {
                        if (productDetailArrayList != null && !productDetailArrayList.isEmpty()) {
                            if (productDetailArrayList.get(0).getCart_status() != null && productDetailArrayList.get(0).getCart_status().equalsIgnoreCase("In Cart")) {
                                Intent i = new Intent(FragItemDetails.this, MyCartDetail.class);
                                startActivity(i);
                            } else {
                                if (size_sts && (size_select_str == null || size_select_str.equalsIgnoreCase(""))) {
                                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctsize), Toast.LENGTH_LONG).show();
                                } else if (color_sts && (color_select_str == null || color_select_str.equalsIgnoreCase(""))) {
                                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctcolor), Toast.LENGTH_LONG).show();

                                } else {
                                    new AddTocartAsc().execute();
                                }

                            }
                        } else {

                            if (size_sts && (size_select_str == null || size_select_str.equalsIgnoreCase(""))) {
                                Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctsize), Toast.LENGTH_LONG).show();
                            } else if (color_sts && (color_select_str == null || color_select_str.equalsIgnoreCase(""))) {
                                Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctcolor), Toast.LENGTH_LONG).show();

                            } else {
                                new AddTocartAsc().execute();
                            }

                            //  new AddTocartAsc().execute();
                        }
                        //  new AddTocartAsc().execute();

                    } else {
                        Toast.makeText(FragItemDetails.this, getResources().getString(R.string.selproductquant), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.outstk), Toast.LENGTH_LONG).show();
                }
            }
        });
        buynow_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_sts = true;
                if (stockcount != null && !stockcount.equalsIgnoreCase("")) {
                    if (sumcount != 0) {
                        if (productDetailArrayList != null && !productDetailArrayList.isEmpty()) {
                            if (productDetailArrayList.get(0).getCart_status() != null && productDetailArrayList.get(0).getCart_status().equalsIgnoreCase("In Cart")) {
                                Intent i = new Intent(FragItemDetails.this, MyCartDetail.class);
                                startActivity(i);
                            } else {
                                if (size_sts && (size_select_str == null || size_select_str.equalsIgnoreCase(""))) {
                                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctsize), Toast.LENGTH_LONG).show();
                                } else if (color_sts && (color_select_str == null || color_select_str.equalsIgnoreCase(""))) {
                                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctcolor), Toast.LENGTH_LONG).show();

                                } else {
                                    new AddTocartAsc().execute();
                                }
                                // new AddTocartAsc().execute();
                            }
                        } else {
                            if (size_sts && (size_select_str == null || size_select_str.equalsIgnoreCase(""))) {
                                Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctsize), Toast.LENGTH_LONG).show();
                            } else if (color_sts && (color_select_str == null || color_select_str.equalsIgnoreCase(""))) {
                                Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctcolor), Toast.LENGTH_LONG).show();

                            } else {
                                new AddTocartAsc().execute();
                            }

                            //new AddTocartAsc().execute();
                        }
                        //  new AddTocartAsc().execute();

                    } else {
                        Toast.makeText(FragItemDetails.this, getResources().getString(R.string.selproductquant), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.outstk), Toast.LENGTH_LONG).show();

                }

            }
        });
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        like_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likedislikeproduct_fun(product_id);
            }
        });
        submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_val = rating.getRating();
                comment_str = comment_et.getText().toString();
                if (comment_str == null || comment_str.equalsIgnoreCase("")) {
                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.plswricmt), Toast.LENGTH_LONG).show();
                } else {
                    new SubmitReviewAsc().execute();
                }
            }
        });

        shareproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get access to the URI for the bitmap
                try {

                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                .setLink(Uri.parse("https://www.ngrewards.com/data/Ng?"+productDetailArrayList.get(0).getId()))
                                .setDynamicLinkDomain("ngtechn.page.link")
                                // Open links with this app on Android
                                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                                // Open links with com.example.ios on iOS
                                .setIosParameters(new DynamicLink.IosParameters.Builder("com.ios.ngreward").build())
                                .buildDynamicLink();

                        Uri dynamicLinkUri = dynamicLink.getUri();

                        Log.d("TAG", "onCreate: "+dynamicLinkUri);

                        shortenLongLink(dynamicLinkUri.toString());

                } catch (Exception e) {
                    Log.e("EXC", " > " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
        shipinfo_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shipping_lay.getVisibility() == View.VISIBLE) {
                    shipping_lay.setVisibility(View.GONE);
                    shipingarrow.setImageResource(R.drawable.uparrow);
                } else {
                    shipping_lay.setVisibility(View.VISIBLE);
                    shipingarrow.setImageResource(R.drawable.down_arrow);
                }
            }
        });
        item_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemspeclay.getVisibility() == View.VISIBLE) {
                    itemspeclay.setVisibility(View.GONE);
                    itemarrow.setImageResource(R.drawable.uparrow);
                } else {
                    itemspeclay.setVisibility(View.VISIBLE);
                    itemarrow.setImageResource(R.drawable.down_arrow);
                }
            }
        });
        description_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (description_lay.getVisibility() == View.VISIBLE) {
                    description_lay.setVisibility(View.GONE);
                    description_arrow.setImageResource(R.drawable.uparrow);
                } else {
                    description_lay.setVisibility(View.VISIBLE);
                    description_arrow.setImageResource(R.drawable.down_arrow);
                }
            }
        });
    }

    public void shortenLongLink(String link)
    {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(link))
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(FragItemDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Log.d("TAG", "onComplete: "+shortLink);

                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shortLink+"");
                            shareIntent.setType("text/plain");
                            // Launch sharing dialog for image
                            startActivity(shareIntent);

                        } else {

                            Log.d("TAG", "onComplete: Error"+task.getException());
                            // Error
                            // ...
                        }
                    }
                });

    }

    public void likedislikeproduct_fun(String id) {
        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().likedislikeoffer(id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Offer Products >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            if (object.getString("result").equalsIgnoreCase("Offer Unlike Successfully")) {
                                like_status = "dislike";
                                like_buton.setImageResource(R.drawable.ic_like_blk);
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);
                               /* int like = Integer.parseInt(offerBeanListArrayList.get(current_offer_pos).getLikeCount());
                                if (like>0){
                                    int likett=like-1;
                                    offerBeanListArrayList.get(current_offer_pos).setLikeCount(likett+"");
                                }*/


                            } else {
                                like_status = "Like";
                                like_buton.setImageResource(R.drawable.ic_like_red);
                                 /*int like = Integer.parseInt(offerBeanListArrayList.get(current_offer_pos).getLikeCount());
                                int likett=like+1;
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
                progresbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });


    }

    private class SubmitReviewAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                String postReceiverUrl = BaseUrl.baseurl + "add_product_reviews.php?";
                Log.e("PRODUCT SUBMIT REVIEW", ">" + postReceiverUrl + "product_id=" + product_id + "&member_id=" + user_id + "&review=" + comment_str + "&rating=" + rating_val);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
                params.put("product_id", product_id);
                params.put("review", comment_str);
                params.put("rating", rating_val);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Submit Review", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);

            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        if (jsonObject.getString("result").equalsIgnoreCase("Review Updated Successfully")) {
                            Toast.makeText(FragItemDetails.this, getResources().getString(R.string.revewupdate), Toast.LENGTH_LONG).show();
                            submit_review.setText("" + getResources().getString(R.string.updatereview));

                        } else {
                            getFeaturedProductsDetail(product_id);
                            Toast.makeText(FragItemDetails.this, getResources().getString(R.string.revewsubmit), Toast.LENGTH_LONG).show();


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private void idinit() {

        review_list.setExpanded(true);
        similar_item_list.setExpanded(true);
        sizelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSizeDrop();
            }
        });
        colorlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor();
            }
        });
        Log.e("TAG", "EMIEMIEMIEMI: "+EMI );
        if(EMI.equalsIgnoreCase("YES")){
            buyon_emi_tv.setVisibility(View.VISIBLE);
        }

        buyon_emi_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
                {
                    click_sts = true;
                    if (stockcount != null && !stockcount.equalsIgnoreCase("")) {
                        if (sumcount != 0) {
                            if (productDetailArrayList != null && !productDetailArrayList.isEmpty()) {
                                if (productDetailArrayList.get(0).getCart_status() != null && productDetailArrayList.get(0).getCart_status().equalsIgnoreCase("In Cart")) {
                                    Intent i = new Intent(FragItemDetails.this, MyCartDetail.class);
                                    startActivity(i);
                                } else {
                                    if (size_sts && (size_select_str == null || size_select_str.equalsIgnoreCase(""))) {
                                        Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctsize), Toast.LENGTH_LONG).show();
                                    } else if (color_sts && (color_select_str == null || color_select_str.equalsIgnoreCase(""))) {
                                        Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctcolor), Toast.LENGTH_LONG).show();

                                    } else {
                                        new AddTocartAsc().execute();
                                    }
                                    // new AddTocartAsc().execute();
                                }
                            } else {
                                if (size_sts && (size_select_str == null || size_select_str.equalsIgnoreCase(""))) {
                                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctsize), Toast.LENGTH_LONG).show();
                                } else if (color_sts && (color_select_str == null || color_select_str.equalsIgnoreCase(""))) {
                                    Toast.makeText(FragItemDetails.this, getResources().getString(R.string.pleaseselctcolor), Toast.LENGTH_LONG).show();

                                } else {
                                    new AddTocartAsc().execute();
                                }

                                //new AddTocartAsc().execute();
                            }
                            //  new AddTocartAsc().execute();

                        } else {
                            Toast.makeText(FragItemDetails.this, getResources().getString(R.string.selproductquant), Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(FragItemDetails.this, getResources().getString(R.string.outstk), Toast.LENGTH_LONG).show();

                    }

                }

        });
        merchant_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productDetailArrayList!=null&&!productDetailArrayList.isEmpty())
                {
                    Intent i = new Intent(FragItemDetails.this, MerchantDetailAct.class);
                    i.putExtra("user_id", user_id);
                    i.putExtra("merchant_contact_name",merchant_name_str);

                    i.putExtra("merchant_id", productDetailArrayList.get(0).getUserDetails().get(0).getId());
                    i.putExtra("merchant_name", productDetailArrayList.get(0).getUserDetails().get(0).getBusinessName());
                    i.putExtra("merchant_number", productDetailArrayList.get(0).getUserDetails().get(0).getBusinessNo());
                    i.putExtra("merchant_img", productDetailArrayList.get(0).getUserDetails().get(0).getMerchantImage());
                    startActivity(i);
                }
                else {
                    Toast.makeText(FragItemDetails.this,getResources().getString(R.string.plswaititemdetailisfatching),Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private class AddTocartAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String postReceiverUrl = BaseUrl.baseurl + "add_to_cart.php?";
                Log.e("ADD CART URL", " >> " + postReceiverUrl + "user_id=" + user_id +
                        "&product_id=" + product_id + "&quantity=" + sumcount + "&size=" + size_select_str + "&color=" + color_select_str+"&pay_by_emi="+EMI);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);
                params.put("product_id", product_id);
                params.put("quantity", sumcount);
                params.put("timezone", time_zone);
                params.put("size", size_select_str);
                params.put("color", color_select_str);
                params.put("pay_by_emi", EMI);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Json Add To cart", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                myapisession.setKeyCartitem("");

                if (click_sts) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            Intent i = new Intent(FragItemDetails.this, MyCartDetail.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(FragItemDetails.this, getResources().getString(R.string.someprobaccoured), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            Toast.makeText(FragItemDetails.this, getResources().getString(R.string.itemadded), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(FragItemDetails.this, getResources().getString(R.string.someprobaccoured), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                    }

                }
            }


        }
    }

    public class CustomReviewAdp extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;
        List<ProductTopReview> productTopReview;

        public CustomReviewAdp(Context contexts, List<ProductTopReview> productTopReview) {
            this.context = contexts;
            this.productTopReview = productTopReview;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // return 3;
            return productTopReview == null ? 0 : productTopReview.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.custom_review_product, null);
            CircleImageView user_img = rowView.findViewById(R.id.user_img);
            TextView membername = rowView.findViewById(R.id.membername);
            TextView created_date = rowView.findViewById(R.id.created_date);
            RatingBar rating = rowView.findViewById(R.id.rating);
            TextView review = rowView.findViewById(R.id.review);
            membername.setText("" + productTopReview.get(position).getFullname());

            try {
                String mytime=productTopReview.get(position).getCreatedDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "MM-dd-yyyy hh:mm:ss");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy");
                String finalDate = timeFormat.format(myDate);
                created_date.setText(""+finalDate);


            }catch (Exception e){
                Log.e("EXC TRUE"," RRR");
                created_date.setText(""+productTopReview.get(position).getCreatedDate());

            }


            //created_date.setText("" + productTopReview.get(position).getCreatedDate());
            review.setText("" + productTopReview.get(position).getReview());
            String image_url = productTopReview.get(position).getMemberImage();
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Picasso.with(FragItemDetails.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
            }

            if (productTopReview.get(position).getRating() != null && !productTopReview.get(position).getRating().equalsIgnoreCase("")) {
                rating.setRating(Float.parseFloat(productTopReview.get(position).getRating()));
            }
            return rowView;
        }

    }

    public class CustomSimilarItem extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;
        List<SimilarProduct> similarProducts;

        public CustomSimilarItem(Context contexts, List<SimilarProduct> similarProducts) {
            this.context = contexts;
            this.similarProducts = similarProducts;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return similarProducts == null ? 0 : similarProducts.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.custom_similar_item_lay, null);
            ImageView similar_pro_image = rowView.findViewById(R.id.similar_pro_image);
            TextView price = rowView.findViewById(R.id.price);
            String image_url = similarProducts.get(position).getThumbnailImage();
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Picasso.with(FragItemDetails.this).load(image_url).placeholder(R.drawable.placeholder).into(similar_pro_image);
            }
            price.setText(mySession.getValueOf(MySession.CurrencySign)  + similarProducts.get(position).getPrice());
            return rowView;
        }

    }

    public class CustomProductImgAdp extends PagerAdapter {

        private final Context mContext;
        private final List<ProductImage> productImages;

        public CustomProductImgAdp(Context context, List<ProductImage> productImages) {
            mContext = context;
            this.productImages = productImages;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.custom_product_image_pager, collection,
                    false);
            collection.addView(itemView);
            ImageView productimg = itemView.findViewById(R.id.productimg);
            String image_url = productImages.get(position).getProductImage();
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Picasso.with(FragItemDetails.this).load(image_url).placeholder(R.drawable.placeholder).into(productimg);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(FragItemDetails.this, FullScreenImagesActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("status", "");
                    startActivity(i);
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return productImages == null ? 0 : productImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}
