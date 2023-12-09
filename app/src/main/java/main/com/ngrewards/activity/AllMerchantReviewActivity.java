package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.beanclasses.MerchantTopReview;
import main.com.ngrewards.beanclasses.MerchatReviewBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMerchantReviewActivity extends AppCompatActivity {

    private RecyclerView myreviewlist;
    private RelativeLayout backlay;
    private LinearLayout recentlay, toplay;
    private TextView recent_tv, top_tv;
    private View recentview, topview;
    private CustomMyReviewAdp customMyReviewAdp;
    private String merchant_id = "", user_id = "";
    private ProgressBar progresbar;
    private ArrayList<MerchantTopReview> merchantTopReviewArrayList;
    private int current_offer_pos;
    private MySession mySession;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aal_review);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            merchant_id = bundle.getString("merchant_id");
        }
        mySession = new MySession(AllMerchantReviewActivity.this);
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
        idinit();
        clickevent();
    }


    private void clickevent() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recentview.setBackgroundResource(R.color.darkgrey);
                recent_tv.setTextColor(getResources().getColor(R.color.darkgrey));
                topview.setBackgroundResource(R.color.red);
                top_tv.setTextColor(getResources().getColor(R.color.red));
            }
        });

        recentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recentview.setBackgroundResource(R.color.red);
                recent_tv.setTextColor(getResources().getColor(R.color.red));
                topview.setBackgroundResource(R.color.darkgrey);
                top_tv.setTextColor(getResources().getColor(R.color.darkgrey));
            }
        });

    }

    private void idinit() {
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        recentview = findViewById(R.id.recentview);
        topview = findViewById(R.id.topview);
        top_tv = findViewById(R.id.top_tv);
        toplay = findViewById(R.id.toplay);
        recent_tv = findViewById(R.id.recent_tv);
        recentlay = findViewById(R.id.recentlay);
        myreviewlist = findViewById(R.id.myreviewlist);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(AllMerchantReviewActivity.this, LinearLayoutManager.VERTICAL, false);
        myreviewlist.setLayoutManager(horizontalLayoutManagaer);
        getMerchantsReview(merchant_id);
    }

    private void getMerchantsReview(String merchant_id) {

        progresbar.setVisibility(View.VISIBLE);
        merchantTopReviewArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantReviewList(merchant_id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Merchant All Review >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            MerchatReviewBean successData = new Gson().fromJson(responseData, MerchatReviewBean.class);
                            merchantTopReviewArrayList.addAll(successData.getResult());

                        }
                        customMyReviewAdp = new CustomMyReviewAdp(AllMerchantReviewActivity.this, merchantTopReviewArrayList);
                        myreviewlist.setAdapter(customMyReviewAdp);
                        customMyReviewAdp.notifyDataSetChanged();

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

    public void likedislikemerchantreview_fun(String id) {
        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().likedislikemerchantReview(id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Review Likes >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            if (object.getString("result").equalsIgnoreCase("Review Unlike Successfully")) {
                                MerchantTopReview merchantTopReview = new MerchantTopReview();
                                merchantTopReview.setLikeStatus("dislike");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);
                                int like = Integer.parseInt(merchantTopReviewArrayList.get(current_offer_pos).getLikeCount());
                                if (like > 0) {
                                    int likett = like - 1;
                                    merchantTopReviewArrayList.get(current_offer_pos).setLikeCount(likett + "");
                                }


                                merchantTopReviewArrayList.get(current_offer_pos).setLikeStatus("dislike");
                            } else {
                                MerchantTopReview merchantTopReview = new MerchantTopReview();
                                merchantTopReview.setLikeStatus("like");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);

                                merchantTopReviewArrayList.get(current_offer_pos).setLikeStatus("like");
                                int like = Integer.parseInt(merchantTopReviewArrayList.get(current_offer_pos).getLikeCount());
                                int likett = like + 1;
                                merchantTopReviewArrayList.get(current_offer_pos).setLikeCount(likett + "");

                            }
                            customMyReviewAdp = new CustomMyReviewAdp(AllMerchantReviewActivity.this, merchantTopReviewArrayList);
                            myreviewlist.setAdapter(customMyReviewAdp);
                            customMyReviewAdp.notifyDataSetChanged();
                            myreviewlist.scrollToPosition(current_offer_pos);


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

    class CustomMyReviewAdp extends RecyclerView.Adapter<CustomMyReviewAdp.MyViewHolder> {
        ArrayList<MerchantTopReview> merchantTopReviewArrayList;
        Context context;

        public CustomMyReviewAdp(Activity activity, ArrayList<MerchantTopReview> merchantTopReviewArrayList) {
            this.merchantTopReviewArrayList = merchantTopReviewArrayList;
            this.context = activity;
        }

        @Override
        public CustomMyReviewAdp.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_allmerchant_reviewlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomMyReviewAdp.MyViewHolder holder, final int listPosition) {
            holder.username.setText("" + merchantTopReviewArrayList.get(listPosition).getFullname());
            holder.datetime.setText("" + merchantTopReviewArrayList.get(listPosition).getCreatedDate());
            holder.review.setText("" + merchantTopReviewArrayList.get(listPosition).getReview());
            String image_url = merchantTopReviewArrayList.get(listPosition).getMemberImage();
            holder.likecount.setText("" + merchantTopReviewArrayList.get(listPosition).getLikeCount());
            if (merchantTopReviewArrayList.get(listPosition).getRating() != null && !merchantTopReviewArrayList.get(listPosition).getRating().equalsIgnoreCase("")) {
                holder.rating.setRating(Float.parseFloat(merchantTopReviewArrayList.get(listPosition).getRating()));
            }
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(AllMerchantReviewActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
            }
            if (merchantTopReviewArrayList.get(listPosition).getLikeStatus().equalsIgnoreCase("like")) {
                holder.likeimg.setImageResource(R.drawable.filled_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
                //holder.liketv.setText("" + getResources().getString(R.string.dislike));
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
            }
            holder.likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_offer_pos = listPosition;
                    likedislikemerchantreview_fun(merchantTopReviewArrayList.get(listPosition).getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return merchantTopReviewArrayList == null ? 0 : merchantTopReviewArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView user_img;
            TextView username, datetime, review, liketv, likecount;
            ImageView likeimg;
            LinearLayout likebut;
            RatingBar rating;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.user_img = itemView.findViewById(R.id.user_img);
                this.username = itemView.findViewById(R.id.username);
                this.datetime = itemView.findViewById(R.id.datetime);
                this.review = itemView.findViewById(R.id.review);
                this.likeimg = itemView.findViewById(R.id.likeimg);
                this.liketv = itemView.findViewById(R.id.liketv);
                this.likecount = itemView.findViewById(R.id.likecount);
                this.likebut = itemView.findViewById(R.id.likebut);
                this.rating = itemView.findViewById(R.id.rating);
            }
        }
    }

}
