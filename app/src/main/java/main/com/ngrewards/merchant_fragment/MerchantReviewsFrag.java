package main.com.ngrewards.merchant_fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.AllMerchantReviewActivity;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.beanclasses.MarchantBean;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.beanclasses.MerchantTopReview;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 13/6/18.
 */

public class MerchantReviewsFrag extends Fragment {
    public static ArrayList<MerchantListBean> merchantListBeanArrayList;
    View v;
    CustomReviewAdp customReviewAdp;
    private TextView donereview_tv, submit_review, total_review_count, outofstar, seeallreview, oneper, twoper, fivper, forper, theeper;
    private EditText comment_et;
    private ExpandableHeightListView topreviewlist;
    private String comment_str = "", user_id = "";
    private RatingBar rating, allrating, rating_done;
    private float rating_val;
    private ProgressBar progresbar;
    private MySession mySession;
    private ProgressBar onestarProgressbar, twostarProgbar, threestrprogbar, fourstrProgbr, fivestrPrgbar;
    private LinearLayout done_review_lay, post_review_lay;
    private int current_offer_pos;
    private SwipeRefreshLayout swipeToRefresh;

    public MerchantReviewsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.pro_reviewfrag_lay, container, false);
        v = inflater.inflate(R.layout.pro_reviewfrag_lay, container, false);
        mySession = new MySession(getActivity());
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
        idint();
        clickevent();
        getMerchantsDetail(MerchantDetailAct.merchant_id);
        return v;
    }

    private void clickevent() {
        submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_val = rating.getRating();
                comment_str = comment_et.getText().toString();
                if (comment_str == null || comment_str.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.plswricmt), Toast.LENGTH_LONG).show();
                } else {
                    new SubmitReviewAsc().execute();
                }
            }
        });
        seeallreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AllMerchantReviewActivity.class);
                i.putExtra("merchant_id", MerchantDetailAct.merchant_id);
                startActivity(i);
            }
        });
    }

    private void getMerchantsDetail(String merchant_id) {
        swipeToRefresh.setRefreshing(true);
        Log.e("USER ID==" + user_id, " > MERchant Id " + merchant_id);
        //progresbar.setVisibility(View.VISIBLE);
        merchantListBeanArrayList = new ArrayList<>();
        Log.e("TAG", "getMerchantsDetailgetMerchantsDetail: " + user_id);
        Log.e("TAG", "getMerchantsDetailgetMerchantsDetail: " + merchant_id);
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchnantReview(user_id, merchant_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            return;
                        }
                        String responseData = response.body().string();

                        JSONObject object = new JSONObject(responseData);
                        Log.e("Merchant Reviews >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            MarchantBean successData = new Gson().fromJson(responseData, MarchantBean.class);
                            merchantListBeanArrayList.addAll(successData.getResult());

                        }
                        if (merchantListBeanArrayList != null) {
                            if (getActivity() != null) {
                                customReviewAdp = new CustomReviewAdp(getActivity(), merchantListBeanArrayList.get(0).getMerchantTopReview());
                                topreviewlist.setAdapter(customReviewAdp);
                                customReviewAdp.notifyDataSetChanged();
                            }

                            fivestrPrgbar.setProgress(merchantListBeanArrayList.get(0).getFiveStarPercentage());
                            fourstrProgbr.setProgress(merchantListBeanArrayList.get(0).getFourStarPercentage());
                            threestrprogbar.setProgress(merchantListBeanArrayList.get(0).getThreeStarPercentage());
                            twostarProgbar.setProgress(merchantListBeanArrayList.get(0).getTwoStarPercentage());
                            onestarProgressbar.setProgress(merchantListBeanArrayList.get(0).getOneStarPercentage());
                            if (merchantListBeanArrayList.get(0).getReviewCount() != null && !merchantListBeanArrayList.get(0).getReviewCount().equalsIgnoreCase("")) {
                                int ddd = Integer.parseInt(merchantListBeanArrayList.get(0).getReviewCount());
                                if (ddd == 0) {
                                    seeallreview.setText("" + merchantListBeanArrayList.get(0).getReviewCount() + " customer reviews.");

                                } else if (ddd == 1) {
                                    seeallreview.setText("See " + merchantListBeanArrayList.get(0).getReviewCount() + " customer reviews.");

                                } else {
                                    seeallreview.setText("See all " + merchantListBeanArrayList.get(0).getReviewCount() + " customer reviews.");

                                }

                            }
                            outofstar.setText("" + merchantListBeanArrayList.get(0).getAverageRating() + " out of 5 stars");
                            total_review_count.setText("" + merchantListBeanArrayList.get(0).getReviewCount());
                            oneper.setText("" + merchantListBeanArrayList.get(0).getOneStarPercentage() + "%");
                            twoper.setText("" + merchantListBeanArrayList.get(0).getTwoStarPercentage() + "%");
                            theeper.setText("" + merchantListBeanArrayList.get(0).getThreeStarPercentage() + "%");
                            forper.setText("" + merchantListBeanArrayList.get(0).getFourStarPercentage() + "%");
                            fivper.setText("" + merchantListBeanArrayList.get(0).getFiveStarPercentage() + "%");
                            if (merchantListBeanArrayList.get(0).getAverageRating() != null && !merchantListBeanArrayList.get(0).getAverageRating().equalsIgnoreCase("")) {
                                allrating.setRating(Float.parseFloat(merchantListBeanArrayList.get(0).getAverageRating()));
                            }

                            if (merchantListBeanArrayList.get(0).getReviewStatus().equalsIgnoreCase("Yes")) {
                                post_review_lay.setVisibility(View.GONE);
                                done_review_lay.setVisibility(View.VISIBLE);
                                submit_review.setText(getResources().getString(R.string.updatereview));
                                comment_et.setText("" + merchantListBeanArrayList.get(0).getReview());
                                donereview_tv.setText("" + merchantListBeanArrayList.get(0).getReview());
                                if (merchantListBeanArrayList.get(0).getRating() != null && !merchantListBeanArrayList.get(0).getRating().equalsIgnoreCase("")) {
                                    rating.setRating(Float.parseFloat(merchantListBeanArrayList.get(0).getRating()));
                                    rating_done.setRating(Float.parseFloat(merchantListBeanArrayList.get(0).getRating()));
                                }
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
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void idint() {
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMerchantsDetail(MerchantDetailAct.merchant_id);
                //swipeToRefresh.setRefreshing(false);
            }
        });

        rating_done = v.findViewById(R.id.rating_done);
        donereview_tv = v.findViewById(R.id.donereview_tv);
        post_review_lay = v.findViewById(R.id.post_review_lay);
        done_review_lay = v.findViewById(R.id.done_review_lay);
        allrating = v.findViewById(R.id.allrating);
        fivestrPrgbar = v.findViewById(R.id.fivestrPrgbar);
        fourstrProgbr = v.findViewById(R.id.fourstrProgbr);
        threestrprogbar = v.findViewById(R.id.threestrprogbar);
        twostarProgbar = v.findViewById(R.id.twostarProgbar);
        onestarProgressbar = v.findViewById(R.id.onestarProgressbar);
        oneper = v.findViewById(R.id.oneper);
        theeper = v.findViewById(R.id.theeper);
        twoper = v.findViewById(R.id.twoper);
        fivper = v.findViewById(R.id.fivper);
        forper = v.findViewById(R.id.forper);
        seeallreview = v.findViewById(R.id.seeallreview);
        outofstar = v.findViewById(R.id.outofstar);
        total_review_count = v.findViewById(R.id.total_review_count);
        topreviewlist = v.findViewById(R.id.topreviewlist);
        submit_review = v.findViewById(R.id.submit_review);
        comment_et = v.findViewById(R.id.comment_et);
        progresbar = v.findViewById(R.id.progresbar);
        rating = v.findViewById(R.id.rating);
        topreviewlist.setExpanded(true);

    }

    @Override
    public void onResume() {
        super.onResume();
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
                                int like = Integer.parseInt(merchantListBeanArrayList.get(0).getMerchantTopReview().get(current_offer_pos).getLikeCount());
                                if (like > 0) {
                                    int likett = like - 1;
                                    merchantListBeanArrayList.get(0).getMerchantTopReview().get(current_offer_pos).setLikeCount(likett + "");
                                }


                                merchantListBeanArrayList.get(0).getMerchantTopReview().get(current_offer_pos).setLikeStatus("dislike");
                            } else {
                                MerchantTopReview merchantTopReview = new MerchantTopReview();
                                merchantTopReview.setLikeStatus("like");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);

                                merchantListBeanArrayList.get(0).getMerchantTopReview().get(current_offer_pos).setLikeStatus("like");
                                int like = Integer.parseInt(merchantListBeanArrayList.get(0).getMerchantTopReview().get(current_offer_pos).getLikeCount());
                                int likett = like + 1;
                                merchantListBeanArrayList.get(0).getMerchantTopReview().get(current_offer_pos).setLikeCount(likett + "");

                            }
                            if (getActivity() != null) {
                                customReviewAdp = new CustomReviewAdp(getActivity(), merchantListBeanArrayList.get(0).getMerchantTopReview());
                                topreviewlist.setAdapter(customReviewAdp);
                                customReviewAdp.notifyDataSetChanged();
                                topreviewlist.setSelection(current_offer_pos);
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

    public class CustomReviewAdp extends BaseAdapter {
        Context context;
        List<MerchantTopReview> merchantTopReview;
        private LayoutInflater inflater = null;

        public CustomReviewAdp(Context contexts, List<MerchantTopReview> merchantTopReview) {
            this.context = contexts;
            this.merchantTopReview = merchantTopReview;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            // return 2;
            return merchantTopReview == null ? 0 : merchantTopReview.size();

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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.top_customer_review_lay, null);
            CircleImageView user_img = rowView.findViewById(R.id.user_img);
            LinearLayout likebut = rowView.findViewById(R.id.likebut);
            ImageView likeimg = rowView.findViewById(R.id.likeimg);
            TextView liketv = rowView.findViewById(R.id.liketv);
            TextView likecount = rowView.findViewById(R.id.likecount);
            TextView user_name = rowView.findViewById(R.id.user_name);
            TextView datetime = rowView.findViewById(R.id.datetime);
            TextView review_message = rowView.findViewById(R.id.review_message);
            RatingBar rating = rowView.findViewById(R.id.rating);
            review_message.setText("" + merchantTopReview.get(position).getReview());

            try {
                String mytime = merchantTopReview.get(position).getCreatedDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "MM-dd-yyyy hh:mm:ss");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy");
                String finalDate = timeFormat.format(myDate);
                datetime.setText("" + finalDate);


            } catch (Exception e) {
                Log.e("EXC TRUE", " RRR");
                datetime.setText("" + merchantTopReview.get(position).getCreatedDate());

            }

            //   datetime.setText(""+merchantTopReview.get(position).getCreatedDate());
            Log.e("TAG", "getViewgetViewgetView: " + merchantTopReview.get(position).getFullname());
            user_name.setText("" + merchantTopReview.get(position).getFullname());
            String image_url = merchantTopReview.get(position).getMemberImage();
            likecount.setText("" + merchantTopReview.get(position).getLikeCount());

            if (merchantTopReview.get(position).getRating() != null && !merchantTopReview.get(position).getRating().equalsIgnoreCase("")) {
                rating.setRating(Float.parseFloat(merchantTopReview.get(position).getRating()));
            }
            if (merchantTopReview.get(position).getLikeStatus().equalsIgnoreCase("like")) {
                likeimg.setImageResource(R.drawable.filled_like);
                liketv.setText("" + getResources().getString(R.string.like));
                //holder.liketv.setText("" + getResources().getString(R.string.dislike));
            } else {
                likeimg.setImageResource(R.drawable.ic_like);
                liketv.setText("" + getResources().getString(R.string.like));
            }
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(getActivity()).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
            }
            likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_offer_pos = position;
                    likedislikemerchantreview_fun(merchantTopReview.get(position).getId());

                }

            });

            return rowView;
        }

        public class Holder {

        }


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
                String postReceiverUrl = BaseUrl.baseurl + "add_customer_reviews.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
                params.put("merchant_id", MerchantDetailAct.merchant_id);
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.revewupdate), Toast.LENGTH_LONG).show();
                            submit_review.setText("" + getResources().getString(R.string.updatereview));

                        } else {
                            getMerchantsDetail(MerchantDetailAct.merchant_id);
                            Toast.makeText(getActivity(), getResources().getString(R.string.revewsubmit), Toast.LENGTH_LONG).show();
                            /*submit_review.setText("" + getResources().getString(R.string.updatereview));
                            post_review_lay.setVisibility(View.GONE);
                            done_review_lay.setVisibility(View.VISIBLE);
                            rating_done.setRating(rating.getRating());
                            donereview_tv.setText(""+comment_str);
                            if (merchantListBeanArrayList.get(0).getReviewCount()!=null&&!merchantListBeanArrayList.get(0).getReviewCount().equalsIgnoreCase("")){
                                int count = Integer.parseInt(merchantListBeanArrayList.get(0).getReviewCount());
                                int total_count=count+1;
                                total_review_count.setText("" + total_count);

                                if (total_count==0){
                                    seeallreview.setText("" + total_count + " customer reviews.");

                                }
                                else if (total_count==1){
                                    seeallreview.setText("See " + total_count + " customer reviews.");

                                }
                                else {
                                    seeallreview.setText("See all " + total_count + " customer reviews.");

                                }

                            }
                            else {
                                total_review_count.setText("" + 1);
                                seeallreview.setText("See 1 customer reviews.");

                            }*/


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
