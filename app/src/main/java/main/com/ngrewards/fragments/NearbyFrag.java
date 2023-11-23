package main.com.ngrewards.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.ManualActivity;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.activity.OrderActivity;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.beanclasses.MarchantBean;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 14/6/18.
 */

public class NearbyFrag extends Fragment {
    View v;
    private ListView near_marchant;
    private RecyclerView near_marchant_rec;

    private CustomMarchantAdp customMarchantAdp;
    private DistanceAdapter distanceAdapter;
    ProgressBar progresbar;
    private double latitude = 0, longitude = 0;
    ArrayList<MerchantListBean> merchantListBeanArrayList;
    GPSTracker gpsTracker;
    private SwipeRefreshLayout swipeToRefresh;
    private TextView filter_tv, nomerchanttv;
    private MySession mySession;
    private String user_id = "", like_filter_str = "", rating_filter_str = "", distance_filter_str = "", country_id = "", fill_category_id = "", fill_category_id_loc = "";
    private Myapisession myapisession;
    ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private int current_offer_pos;
    private EditText search_et_home;
    private ArrayList<String> distance_filter_list;
    private Bitmap mBitmap;
    private String openingtime;
    private String closingtime;

    private String result="";

    public NearbyFrag()
    {

    }

    @SuppressLint("ValidFragment")
    public NearbyFrag(String result) {
        this.result = result;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.nearby_frag_lay, container, false);

        mySession = new MySession(getActivity());
        myapisession = new Myapisession(getActivity());
        distance_filter_list = new ArrayList<>();
        distance_filter_list.add("Any Distance");
        distance_filter_list.add("5.0");
        distance_filter_list.add("10.0");
        distance_filter_list.add("20.0");
        distance_filter_list.add("50.0");

        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    country_id = jsonObject1.getString("country_id");
                    Log.e("country_id >>", " >" + country_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        checkGps();
        idinit();
        getCategoryType();
        clickevent();
        return v;
    }

    private void clickevent() {
        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progresbar.getVisibility() == View.VISIBLE) {
                    Toast.makeText(getActivity(), "Please Wait..", Toast.LENGTH_LONG).show();
                } else {
                    filterlay();
                }
            }
        });
    }

     private void idinit() {
        search_et_home = getActivity().findViewById(R.id.search_et_home);
        filter_tv = v.findViewById(R.id.filter_tv);
        nomerchanttv = v.findViewById(R.id.nomerchanttv);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        near_marchant_rec = v.findViewById(R.id.near_marchant_rec);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        near_marchant_rec.setLayoutManager(horizontalLayoutManagaer);

        near_marchant = v.findViewById(R.id.near_marchant);
        progresbar = v.findViewById(R.id.progresbar);
        getNearMarchant();

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkGps();
                getNearMarchant();
            }
        });

        search_et_home.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s == null) {

                } else {

                    if (s.toString().length() > 0) {

                        if (customMarchantAdp == null) {

                        } else {
                            customMarchantAdp.filter(s.toString());
                        }

                    } else {
                        if (customMarchantAdp == null) {

                        } else {
                            customMarchantAdp.filter("");
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    private void getNearMarchant() {

        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");

        String timeav = formatDate.format(new Date());
        String formattedDate = timeav.replace("a.m.", "AM").replace("p.m.", "PM");
        Log.e("Current Time", " ." + formattedDate);

        Log.e("Near" ,latitude + " , " + longitude + " Cou " + country_id + " D >" + distance_filter_str + " R >" + rating_filter_str + " ORDER " + like_filter_str +"cat_id>"+fill_category_id);
        swipeToRefresh.setRefreshing(true);
        merchantListBeanArrayList = new ArrayList<>();

        Call<ResponseBody> call = ApiClient.getApiInterface().getNearMarchant
                ("" + latitude, "" + longitude, country_id, fill_category_id, user_id, formattedDate, distance_filter_str, rating_filter_str, like_filter_str);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Near Merchant  >", " >" + responseData);

                        if (object.getString("status").equals("1")) {
                            MarchantBean successData = new Gson().fromJson(responseData, MarchantBean.class);
                            merchantListBeanArrayList.addAll(successData.getResult());
                            Log.e("merchantList", "" + merchantListBeanArrayList);
                        }

                        if (merchantListBeanArrayList == null || merchantListBeanArrayList.isEmpty() || merchantListBeanArrayList.size() == 0) {
                            nomerchanttv.setVisibility(View.VISIBLE);
                        } else {
                            nomerchanttv.setVisibility(View.GONE);
                        }

                        customMarchantAdp = new CustomMarchantAdp(merchantListBeanArrayList);
                        near_marchant_rec.setAdapter(customMarchantAdp);

                        if(!result.equalsIgnoreCase(""))
                        {
                            gotoMercent();
                        }

                        customMarchantAdp.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void checkGps() {
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            Log.e("REFRESH_LOCATION ", " >> " + latitude);
            longitude = gpsTracker.getLongitude();
            if (latitude == 0.0) {
                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;
            }
        }
    }

    class CustomMarchantAdp extends RecyclerView.Adapter<CustomMarchantAdp.MyViewHolder> {
        ArrayList<MerchantListBean> merchantListBeanArrayList;
        ArrayList<MerchantListBean> searchmerchantListBeanArrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView marchant_img, likeimg;
            TextView mer_name_tv, liketv;
            TextView number_tv, distance_tv, tv_order;
            TextView locationtv, likecount, mer_openstatus, category_name, rating_count;
            LinearLayout likebut, sharelay, paybilllay;
            RatingBar rating;


            public MyViewHolder(View itemView) {
                super(itemView);
                this.distance_tv = itemView.findViewById(R.id.distance_tv);
                this.marchant_img = itemView.findViewById(R.id.marchant_img);
                this.mer_name_tv = itemView.findViewById(R.id.mer_name_tv);
                this.number_tv = itemView.findViewById(R.id.number_tv);
                this.locationtv = itemView.findViewById(R.id.locationtv);
                this.likebut = itemView.findViewById(R.id.likebut);
                this.likeimg = itemView.findViewById(R.id.likeimg);
                this.liketv = itemView.findViewById(R.id.liketv);
                this.paybilllay = itemView.findViewById(R.id.paybilllay);
                this.likecount = itemView.findViewById(R.id.likecount);
                this.mer_openstatus = itemView.findViewById(R.id.mer_openstatus);
                this.sharelay = itemView.findViewById(R.id.sharelay);
                this.rating = itemView.findViewById(R.id.rating);
                this.category_name = itemView.findViewById(R.id.category_name);
                this.rating_count = itemView.findViewById(R.id.rating_count);
                this.tv_order = itemView.findViewById(R.id.tv_order);
            }
        }

        public CustomMarchantAdp(ArrayList<MerchantListBean> merchantListBeanArrayList) {
            this.merchantListBeanArrayList = merchantListBeanArrayList;
            this.searchmerchantListBeanArrayList = new ArrayList<>();
            searchmerchantListBeanArrayList.addAll(merchantListBeanArrayList);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_new_nearbay_lay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        public void filter(String charText) {

            if (charText != null) {

                merchantListBeanArrayList.clear();

                if (charText.isEmpty()) {

                    merchantListBeanArrayList.addAll(searchmerchantListBeanArrayList);

                } else {

                    charText = charText.toLowerCase(Locale.getDefault());

                    for (MerchantListBean wp : searchmerchantListBeanArrayList) {

                        try {

                            if (wp.getBusinessName().toLowerCase().startsWith(charText)) {
                                merchantListBeanArrayList.add(wp);
                            }

                        } catch (Exception e3) {

                            e3.printStackTrace();
                        }

                        notifyDataSetChanged();
                    }
                }
                notifyDataSetChanged();
            }
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            if (merchantListBeanArrayList.get(listPosition).getBusinessName() == null || merchantListBeanArrayList.get(listPosition).getBusinessName().equalsIgnoreCase("")) {
                holder.mer_name_tv.setText(getResources().getString(R.string.staticmerchantname));
            } else {
                holder.mer_name_tv.setText("" + merchantListBeanArrayList.get(listPosition).getBusinessName());
            }

            if (merchantListBeanArrayList.get(listPosition).getBusinessCategoryName() != null && !merchantListBeanArrayList.get(listPosition).getBusinessCategoryName().equalsIgnoreCase("")) {
                holder.category_name.setText("" + merchantListBeanArrayList.get(listPosition).getBusinessCategoryName() + "  " + merchantListBeanArrayList.get(listPosition).getDistance() + " mi");

            } else {
                holder.category_name.setText("" + merchantListBeanArrayList.get(listPosition).getDistance() + " mi");

            }

            if (merchantListBeanArrayList.get(listPosition).getOpening_time() != null &&
                    merchantListBeanArrayList.get(listPosition).getClosing_time() != null)
            {

                openingtime = merchantListBeanArrayList.get(listPosition).getOpening_time();
                closingtime = merchantListBeanArrayList.get(listPosition).getClosing_time();

                try {

                    Date mToday = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String curTime = sdf.format(mToday);
                    Date start = sdf.parse(openingtime);
                    Date end = sdf.parse(closingtime);
                    Date userDate = sdf.parse(curTime);

                    if (end.before(start)) {
                        Calendar mCal = Calendar.getInstance();
                        mCal.setTime(end);
                        mCal.add(Calendar.DAY_OF_YEAR, 1);
                        end.setTime(mCal.getTimeInMillis());
                    }
                    if (userDate.after(start) && userDate.before(end)) {
                        holder.mer_openstatus.setText(getString(R.string.open));
                        holder.mer_openstatus.setTextColor(R.color.green);
                    } else {
                        holder.mer_openstatus.setText(getString(R.string.close));
                        holder.mer_openstatus.setTextColor(getResources().getColor(R.color.red));
                    }
                } catch (ParseException e) {
                }
            } else {
                holder.mer_openstatus.setTextColor(getResources().getColor(R.color.red));
                holder.mer_openstatus.setText(getString(R.string.closed));
            }

            String rat_str = merchantListBeanArrayList.get(listPosition).getAverageRating();

            if (rat_str != null && !rat_str.equalsIgnoreCase("")) {

                holder.rating.setRating(Float.parseFloat(rat_str));
                holder.rating_count.setText("(" + merchantListBeanArrayList.get(listPosition).getReviewCount() + ")");
            }

            // holder.average_review.setText("" + merchantListBeanArrayList.get(listPosition).getAverageRating()+" out of 5 stars");
            holder.distance_tv.setText("" + merchantListBeanArrayList.get(listPosition).getDistance() + " mi");
            holder.number_tv.setText("" + merchantListBeanArrayList.get(listPosition).getBusinessNo());
            holder.likecount.setText("" + merchantListBeanArrayList.get(listPosition).getLikeCount());
            holder.locationtv.setText("" + merchantListBeanArrayList.get(listPosition).getAddress());
            String image_url = merchantListBeanArrayList.get(listPosition).getMerchantImage();

            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(getActivity()).load(image_url).placeholder(R.drawable.placeholder).into(holder.marchant_img);
            }

            if (merchantListBeanArrayList.get(listPosition).getLikeStatus().equalsIgnoreCase("like")) {
                holder.likeimg.setImageResource(R.drawable.filled_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
                //holder.liketv.setText("" + getResources().getString(R.string.dislike));
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MerchantDetailAct.class);
                    i.putExtra("user_id", user_id);
                    i.putExtra("merchant_id", merchantListBeanArrayList.get(listPosition).getId());
                    i.putExtra("opeaning_time", merchantListBeanArrayList.get(listPosition).getOpening_time());
                    i.putExtra("closing_time", merchantListBeanArrayList.get(listPosition).getClosing_time());
                    i.putExtra("merchant_name", merchantListBeanArrayList.get(listPosition).getBusinessName());
                    i.putExtra("merchant_number", merchantListBeanArrayList.get(listPosition).getBusinessNo());
                    i.putExtra("merchant_contact_name", merchantListBeanArrayList.get(listPosition).getContactName());
                    i.putExtra("merchant_img", merchantListBeanArrayList.get(listPosition).getMerchantImage());
                    i.putExtra("employee_sales_id", merchantListBeanArrayList.get(listPosition).getEmployee_sale_id());
                    i.putExtra("employee_slaes_name", merchantListBeanArrayList.get(listPosition).getEmployee_sale_name());
                    startActivity(i);
                }
            });

            holder.paybilllay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), merchantListBeanArrayList.get(listPosition).getEmployee_sale_name(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), user_id, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), ManualActivity.class);
                    i.putExtra("user_id", user_id);
                    i.putExtra("merchant_id", "" + merchantListBeanArrayList.get(listPosition).getId());
                    i.putExtra("merchant_name", "" + merchantListBeanArrayList.get(listPosition).getBusinessName());
                    i.putExtra("merchant_number", "" + merchantListBeanArrayList.get(listPosition).getBusinessNo());
                    i.putExtra("employee_sales_id", merchantListBeanArrayList.get(listPosition).getEmployee_sale_id());
                    i.putExtra("employee_slaes_name", merchantListBeanArrayList.get(listPosition).getEmployee_sale_name());
                    startActivity(i);
                }
            });


            holder.sharelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri bmpUri = getLocalBitmapUri(holder.marchant_img);
                    // Get access to the URI for the bitmap
                    try {

                        if (bmpUri != null) {

                            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                    .setLink(Uri.parse("https://www.ngrewards.com/data/Merchent?"+merchantListBeanArrayList.get(listPosition).getId()))
                                    .setDynamicLinkDomain("ngtechn.page.link")
                                    // Open links with this app on Android
                                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                                    // Open links with com.example.ios on iOS
                                    .setIosParameters(new DynamicLink.IosParameters.Builder("com.ios.ngreward").build())
                                    .buildDynamicLink();

                            Uri dynamicLinkUri = dynamicLink.getUri();

                            Log.d("TAG", "onCreate: "+dynamicLinkUri);

                            shortenLongLink(dynamicLinkUri.toString());

                        } else {
                            // ...sharing failed, handle error
                        }

                    } catch (Exception e) {
                        Log.e("EXC", " > " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            });

            holder.likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_offer_pos = listPosition;
                    likedislikemerchant_fun(merchantListBeanArrayList.get(listPosition).getId());
                }
            });

            holder.tv_order.setVisibility(merchantListBeanArrayList.get(listPosition).getOrder_status().equalsIgnoreCase("Yes") ? View.VISIBLE : View.GONE);
            holder.tv_order.setOnClickListener(v -> {

                startActivity(new Intent(getActivity(), OrderActivity.class).putExtra("data", merchantListBeanArrayList.get(listPosition)));

            });

        }

        @Override
        public int getItemCount() {
            // return 2;
            return merchantListBeanArrayList == null ? 0 : merchantListBeanArrayList.size();
        }
    }

    public void shortenLongLink(String link)
    {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(link))
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Log.d("TAG", "onComplete: "+shortLink);

                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            //  shareIntent.putExtra(Intent.EXTRA_STREAM, "" + bmpUri);
//                            shareIntent.putExtra(Intent.EXTRA_TEXT, "" + merchantListBeanArrayList.get(listPosition).getMerchantImage()
//                                    + " \n" + " \n " + "Merchant Name - " + merchantListBeanArrayList.get(listPosition).getBusinessName() +
//                                    " \n" + " \n " + "Merchant Number - " + merchantListBeanArrayList.get(listPosition).getBusinessNo()
//                                    + " \n" + " \n " + "Merchant Address - " + merchantListBeanArrayList.get(listPosition).getAddress() +
//                                    " \n " + "\n " + merchantListBeanArrayList.get(listPosition).getShare_link() + BuildConfig.APPLICATION_ID);
                            //shareIntent.setType("image/*");

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

    public class CategoryAdpters extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private final ArrayList<CategoryBeanList> categoryBeanLists;

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
            names.setText(categoryBeanLists.get(i).getCategoryName());
            return view;
        }
    }

    public class DistanceAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflter;
        private final ArrayList<String> distancelist;

        public DistanceAdapter(Context applicationContext, ArrayList<String> distancelist) {
            this.context = applicationContext;
            this.distancelist = distancelist;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return distancelist == null ? 0 : distancelist.size();
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
            if (i == 0) {
                names.setText(distancelist.get(i));
            } else {
                names.setText("Within " + distancelist.get(i) + " mi");
            }

            return view;
        }
    }

    private void filterlay() {

        CategoryAdpters categoryAdpters;
        final Dialog dialogSts = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.filter_nearby_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.back_pop_col)));
        //dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView reset_tv = dialogSts.findViewById(R.id.reset_tv);
        TextView filter_tv = dialogSts.findViewById(R.id.filter_tv);
        final RadioButton lowtohigh = dialogSts.findViewById(R.id.lowtohigh);
        final RadioButton hightolow = dialogSts.findViewById(R.id.hightolow);
        final RatingBar rating = dialogSts.findViewById(R.id.rating);
        Spinner distance_spinner = dialogSts.findViewById(R.id.distance_spinner);
        Spinner category_spinner = dialogSts.findViewById(R.id.category_spinner);
        distanceAdapter = new DistanceAdapter(getActivity(), distance_filter_list);
        distance_spinner.setAdapter(distanceAdapter);

        categoryAdpters = new CategoryAdpters(getActivity(), categoryBeanListArrayList);
        category_spinner.setAdapter(categoryAdpters);


        distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (distance_filter_list != null && !distance_filter_list.isEmpty()) {
                    if (distance_filter_list.get(position).equalsIgnoreCase("Any Distance")) {
                        distance_filter_str = "";
                    } else {
                        distance_filter_str = distance_filter_list.get(position);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    if (categoryBeanListArrayList.get(position).getCategoryId().equalsIgnoreCase("0")) {

                    } else {
                        fill_category_id_loc = categoryBeanListArrayList.get(position).getCategoryId();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rat = rating.getRating();
                if (rat == 0) {
                    rating_filter_str = "";
                } else {
                    rating_filter_str = String.valueOf(rat);
                }

                if (hightolow.isChecked()) {
                    like_filter_str = "HighToLow";
                }

                if (lowtohigh.isChecked()) {
                    like_filter_str = "LowToHigh";
                }

                fill_category_id = fill_category_id_loc;
                Log.e("Filter Cat Id", " >" + fill_category_id);
                if (fill_category_id != null && !fill_category_id.equalsIgnoreCase("") && !fill_category_id.equalsIgnoreCase("0")) {
                    getNearMarchant();
                } else if (rating_filter_str != null && !rating_filter_str.equalsIgnoreCase("")) {
                    getNearMarchant();
                } else if (like_filter_str != null && !like_filter_str.equalsIgnoreCase("")) {
                    getNearMarchant();
                } else if (distance_filter_str != null && !distance_filter_str.equalsIgnoreCase("")) {
                    getNearMarchant();
                }
                dialogSts.dismiss();
            }
        });

        reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fill_category_id != null && !fill_category_id.equalsIgnoreCase("") && !fill_category_id.equalsIgnoreCase("0")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    getNearMarchant();
                } else if (rating_filter_str != null && !rating_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    distance_filter_str = "";
                    getNearMarchant();
                } else if (distance_filter_str != null && !distance_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    distance_filter_str = "";
                    getNearMarchant();
                } else if (like_filter_str != null && !like_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    distance_filter_str = "";
                    getNearMarchant();
                }

                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    public void likedislikemerchant_fun(String id) {
        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().likedislikemerchant(id, user_id);
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
                            if (object.getString("result").equalsIgnoreCase("Merchant Unlike Successfully")) {
                                MerchantListBean merchantListBean = new MerchantListBean();
                                merchantListBean.setLikeStatus("dislike");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);
                                int like = Integer.parseInt(merchantListBeanArrayList.get(current_offer_pos).getLikeCount());
                                if (like > 0) {
                                    int likett = like - 1;
                                    merchantListBeanArrayList.get(current_offer_pos).setLikeCount(likett + "");
                                }

                                merchantListBeanArrayList.get(current_offer_pos).setLikeStatus("dislike");

                            } else {
                                MerchantListBean merchantListBean = new MerchantListBean();
                                merchantListBean.setLikeStatus("like");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);

                                merchantListBeanArrayList.get(current_offer_pos).setLikeStatus("like");
                                int like = Integer.parseInt(merchantListBeanArrayList.get(current_offer_pos).getLikeCount());
                                int likett = like + 1;
                                merchantListBeanArrayList.get(current_offer_pos).setLikeCount(likett + "");

                            }
                            customMarchantAdp = new CustomMarchantAdp(merchantListBeanArrayList);
                            near_marchant_rec.setAdapter(customMarchantAdp);

                            customMarchantAdp.notifyDataSetChanged();
                            near_marchant_rec.scrollToPosition(current_offer_pos);
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

    public void gotoMercent()
    {

        MerchantListBean myMerchant = null;

        for(MerchantListBean merchantListBean:merchantListBeanArrayList)
        {
            if(result.equalsIgnoreCase(merchantListBean.getId()))
            {
                myMerchant = merchantListBean;
            }
        }

        if(myMerchant!=null)
        {
            Intent i = new Intent(getActivity(), MerchantDetailAct.class);
            i.putExtra("user_id", user_id);
            i.putExtra("merchant_id", myMerchant.getId());
            i.putExtra("opeaning_time", myMerchant.getOpening_time());
            i.putExtra("closing_time", myMerchant.getClosing_time());
            i.putExtra("merchant_name", myMerchant.getBusinessName());
            i.putExtra("merchant_number", myMerchant.getBusinessNo());
            i.putExtra("merchant_contact_name", myMerchant.getContactName());
            i.putExtra("merchant_img", myMerchant.getMerchantImage());
            i.putExtra("employee_sales_id", myMerchant.getEmployee_sale_id());
            i.putExtra("employee_slaes_name", myMerchant.getEmployee_sale_name());
            startActivity(i);
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

            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void getCategoryType() {
        Log.e("loginCall >", " > FIRST");
        progresbar.setVisibility(View.VISIBLE);
        categoryBeanListArrayList = new ArrayList<>();
        CategoryBeanList categoryBeanList = new CategoryBeanList();
        categoryBeanList.setCategoryId("0");
        categoryBeanList.setCategoryName("Select category");
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
}
