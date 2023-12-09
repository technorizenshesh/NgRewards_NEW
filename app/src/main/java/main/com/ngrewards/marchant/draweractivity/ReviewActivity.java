package main.com.ngrewards.marchant.draweractivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.MemberChatAct;
import main.com.ngrewards.beanclasses.AllTopReview;
import main.com.ngrewards.beanclasses.MerchantTopReview;
import main.com.ngrewards.beanclasses.ReviewMainBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView myreviewlist;
    private RelativeLayout backlay;
    private LinearLayout recentlay, toplay;
    private TextView recent_tv, top_tv;
    private View recentview, topview;
    private CustomMyReviewAdp customMyReviewAdp;
    private ProgressBar progresbar;
    private ArrayList<MerchantTopReview> reviewTopRecentArrayList;
    private ArrayList<AllTopReview> allTopReviewArrayList;
    private MySession mySession;
    private String user_id = "";
    private CustomMyTopReviewAdp customMyTopReviewAdp;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mySession = new MySession(this);
        Tools.reupdateResources(this);
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
        getMerchantsReview(user_id);
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
                Log.e("allTopReview >>", ">" + allTopReviewArrayList.size());
                customMyTopReviewAdp = new CustomMyTopReviewAdp(ReviewActivity.this, allTopReviewArrayList);
                myreviewlist.setAdapter(customMyTopReviewAdp);
                customMyReviewAdp.notifyDataSetChanged();
            }
        });
        recentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recentview.setBackgroundResource(R.color.red);
                recent_tv.setTextColor(getResources().getColor(R.color.red));
                topview.setBackgroundResource(R.color.darkgrey);
                top_tv.setTextColor(getResources().getColor(R.color.darkgrey));
                Log.e("allTopReview REC>>", ">" + reviewTopRecentArrayList.size());
                customMyReviewAdp = new CustomMyReviewAdp(ReviewActivity.this, reviewTopRecentArrayList);
                myreviewlist.setAdapter(customMyReviewAdp);
                customMyReviewAdp.notifyDataSetChanged();
            }
        });
    }

    private void idinit() {
        backlay = findViewById(R.id.backlay);
        progresbar = findViewById(R.id.progresbar);
        recentview = findViewById(R.id.recentview);
        topview = findViewById(R.id.topview);
        top_tv = findViewById(R.id.top_tv);
        toplay = findViewById(R.id.toplay);
        recent_tv = findViewById(R.id.recent_tv);
        recentlay = findViewById(R.id.recentlay);
        myreviewlist = findViewById(R.id.myreviewlist);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(ReviewActivity.this, LinearLayoutManager.VERTICAL, false);
        myreviewlist.setLayoutManager(horizontalLayoutManagaer);

    }

    private void getMerchantsReview(String merchant_id) {

        progresbar.setVisibility(View.VISIBLE);
        reviewTopRecentArrayList = new ArrayList<>();
        allTopReviewArrayList = new ArrayList<>();
        Log.e("TAG", "getMerchantsReview: " + merchant_id);
        Call<ResponseBody> call = ApiClient.getApiInterface().getMytReviewList(merchant_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e(" >", " getMytReviewListgetgetMytReviewList>" + responseData);
                        if (object.getString("status").equals("1")) {

                            ReviewMainBean successData = new Gson().fromJson(responseData, ReviewMainBean.class);
                            reviewTopRecentArrayList.addAll(successData.getResult().getAllRecentReviews());
                            allTopReviewArrayList.addAll(successData.getResult().getAllTopReviews());

                        }
                        Log.e("SIZE >>", " >> " + reviewTopRecentArrayList.size());
                        Log.e("SIZE >>", " >> " + allTopReviewArrayList.size());
                        customMyReviewAdp = new CustomMyReviewAdp(ReviewActivity.this, reviewTopRecentArrayList);
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

    class CustomMyReviewAdp extends RecyclerView.Adapter<CustomMyReviewAdp.MyViewHolder> {
        ArrayList<MerchantTopReview> reviewTopRecentArrayList;
        ArrayList<AllTopReview> allTopReviewArrayList;
        Context context;


        public CustomMyReviewAdp(Activity activity, ArrayList<MerchantTopReview> reviewTopRecentArrayList) {
            this.context = activity;
            this.reviewTopRecentArrayList = reviewTopRecentArrayList;
        }

        @Override
        public CustomMyReviewAdp.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_my_reviewlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomMyReviewAdp.MyViewHolder holder, final int listPosition) {
            holder.username.setText("" + reviewTopRecentArrayList.get(listPosition).getFullname());
            holder.date_tv.setText("" + reviewTopRecentArrayList.get(listPosition).getCreatedDate());
            holder.review_tv.setText("" + reviewTopRecentArrayList.get(listPosition).getReview());
            String image_url = reviewTopRecentArrayList.get(listPosition).getMemberImage();
            if (reviewTopRecentArrayList.get(listPosition).getRating() != null && !reviewTopRecentArrayList.get(listPosition).getRating().equalsIgnoreCase("")) {
                holder.rating.setRating(Float.parseFloat(reviewTopRecentArrayList.get(listPosition).getRating()));
            }
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(ReviewActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
            }
            holder.commentlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ReviewActivity.this, MemberChatAct.class);
                    i.putExtra("receiver_id", reviewTopRecentArrayList.get(listPosition).getMemberId());
                    i.putExtra("type", "Merchant");
                    // i.putExtra("receiver_fullname", converSessionArrayList.get(position).getFullname());
                    i.putExtra("receiver_fullname", reviewTopRecentArrayList.get(listPosition).getFullname());
                    i.putExtra("receiver_type", "Member");
                    i.putExtra("receiver_img", reviewTopRecentArrayList.get(listPosition).getMemberImage());
                    i.putExtra("receiver_name", reviewTopRecentArrayList.get(listPosition).getUsername());
                    startActivity(i);
                }
            });


            holder.abuse_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", BaseUrl.admin_email, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });

        }

        @Override
        public int getItemCount() {
            //return 2;
            return reviewTopRecentArrayList == null ? 0 : reviewTopRecentArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView user_img;
            TextView commentlay, date_tv, review_tv, username, abuse_tv;
            RatingBar rating;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.username = itemView.findViewById(R.id.username);
                this.user_img = itemView.findViewById(R.id.user_img);
                this.commentlay = itemView.findViewById(R.id.commentlay);
                this.date_tv = itemView.findViewById(R.id.date_tv);
                this.review_tv = itemView.findViewById(R.id.review_tv);
                this.rating = itemView.findViewById(R.id.rating);
                this.abuse_tv = itemView.findViewById(R.id.abuse_tv);


            }
        }
    }

    class CustomMyTopReviewAdp extends RecyclerView.Adapter<CustomMyTopReviewAdp.MyViewHolder> {
        ArrayList<AllTopReview> allTopReviewArrayList;
        Context context;

        public CustomMyTopReviewAdp(Activity activity, ArrayList<AllTopReview> allTopReviewArrayList) {
            this.context = activity;
            this.allTopReviewArrayList = allTopReviewArrayList;
        }

        @Override
        public CustomMyTopReviewAdp.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_my_reviewlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomMyTopReviewAdp.MyViewHolder holder, final int listPosition) {
            holder.username.setText("" + allTopReviewArrayList.get(listPosition).getFullname());
            holder.date_tv.setText("" + allTopReviewArrayList.get(listPosition).getCreatedDate());
            holder.review_tv.setText("" + allTopReviewArrayList.get(listPosition).getReview());
            String image_url = allTopReviewArrayList.get(listPosition).getMemberImage();
            if (reviewTopRecentArrayList.get(listPosition).getRating() != null && !reviewTopRecentArrayList.get(listPosition).getRating().equalsIgnoreCase("")) {
                holder.rating.setRating(Float.parseFloat(reviewTopRecentArrayList.get(listPosition).getRating()));
            }

            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(ReviewActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
            }
            holder.commentlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ReviewActivity.this, MemberChatAct.class);
                    i.putExtra("receiver_id", reviewTopRecentArrayList.get(listPosition).getMemberId());
                    i.putExtra("type", "Merchant");
                    // i.putExtra("receiver_fullname", converSessionArrayList.get(position).getFullname());
                    i.putExtra("receiver_fullname", reviewTopRecentArrayList.get(listPosition).getFullname());
                    i.putExtra("receiver_type", "Member");
                    i.putExtra("receiver_img", reviewTopRecentArrayList.get(listPosition).getMemberImage());
                    i.putExtra("receiver_name", reviewTopRecentArrayList.get(listPosition).getUsername());
                    startActivity(i);
                }
            });
            holder.abuse_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", BaseUrl.admin_email, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }

        @Override
        public int getItemCount() {
            //return 2;
            return allTopReviewArrayList == null ? 0 : allTopReviewArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView user_img;
            TextView commentlay, date_tv, review_tv, username, abuse_tv;
            RatingBar rating;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.username = itemView.findViewById(R.id.username);
                this.user_img = itemView.findViewById(R.id.user_img);
                this.commentlay = itemView.findViewById(R.id.commentlay);
                this.date_tv = itemView.findViewById(R.id.date_tv);
                this.review_tv = itemView.findViewById(R.id.review_tv);
                this.rating = itemView.findViewById(R.id.rating);
                this.abuse_tv = itemView.findViewById(R.id.abuse_tv);

            }
        }
    }

}
