package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.memberstripe.MemberStripeExpressAcountAct;
import main.com.ngrewards.activity.memberstripe.SeeMemberMyStripeDashBoardAct;
import main.com.ngrewards.beanclasses.CommisionMain;
import main.com.ngrewards.beanclasses.CommissionData;
import main.com.ngrewards.beanclasses.FirstData;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommisionActivity extends AppCompatActivity {

    public static List<CommissionData> commissionDataArrayList;
    MySession mySession;
    ProgressBar progressbar;
    SwipeRefreshLayout swipeToRefresh;
    private RecyclerView commision_list;
    private RelativeLayout menulay, backlay;
    private CommisionAdpter commisionAdpter;
    private String user_id = "", affiliate_number = "", stripe_account_id = "", stripe_account_login_link = "";
    private ArrayList<FirstData> firstDataArrayList;
    private TextView nocommission;
    private RelativeLayout seestripedashboard, genrateloginlinklay, addstripeact;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_commision);
        idinit();
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
                    affiliate_number = jsonObject1.getString("affiliate_number");

                    stripe_account_id = jsonObject1.getString("member_stripe_account_id");
                    stripe_account_login_link = jsonObject1.getString("stripe_account_login_link");

                    if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                        addstripeact.setVisibility(View.GONE);
                        if (stripe_account_login_link != null && !stripe_account_login_link.equalsIgnoreCase("")) {
                            genrateloginlinklay.setVisibility(View.GONE);
                            seestripedashboard.setVisibility(View.VISIBLE);
                        } else {
                            genrateloginlinklay.setVisibility(View.VISIBLE);
                        }

                    }
                    // image_url = jsonObject1.getString("member_image");
                    Log.e("affiliate_number ", " .> " + affiliate_number);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        clickevent();

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menulay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommisionActivity.this, MemberRefListAct.class);
                startActivity(i);
            }
        });

        addstripeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressbar.getVisibility() != View.VISIBLE) {
                    Intent i = new Intent(CommisionActivity.this, MemberStripeExpressAcountAct.class);
                    startActivity(i);
                }
                // Intent i = new Intent(MerSettingActivity.this, AddPaypalEmail.class);

            }
        });

        genrateloginlinklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                    if (progressbar.getVisibility() != View.VISIBLE) {

                        new GenrateLoginLink().execute();
                    }
                }

            }
        });

        seestripedashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressbar.getVisibility() != View.VISIBLE) {
                    Intent i = new Intent(CommisionActivity.this, SeeMemberMyStripeDashBoardAct.class);
                    i.putExtra("stripe_login_url", stripe_account_login_link);
                    startActivity(i);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCommission();
        if (stripe_account_id == null || stripe_account_id.equalsIgnoreCase("") || stripe_account_login_link == null || stripe_account_login_link.equalsIgnoreCase("")) {
            new GetProfile().execute();
        }

    }

    private void idinit() {

        seestripedashboard = findViewById(R.id.seestripedashboard);
        genrateloginlinklay = findViewById(R.id.genrateloginlinklay);

        addstripeact = findViewById(R.id.addstripeact);

        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        nocommission = findViewById(R.id.nocommission);
        progressbar = findViewById(R.id.progressbar);
        commision_list = findViewById(R.id.commision_list);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(CommisionActivity.this, LinearLayoutManager.VERTICAL, false);

        commision_list.setLayoutManager(horizontalLayoutManagaer);
        backlay = findViewById(R.id.backlay);
        menulay = findViewById(R.id.menulay);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMyCommission();


                // swipeToRefresh.setRefreshing(false);
            }
        });
    }

    private void getMyCommission() {

        swipeToRefresh.setRefreshing(true);
        progressbar.setVisibility(View.VISIBLE);
        firstDataArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getCommissionData(affiliate_number);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);

                if (response.isSuccessful()) {

                    try {

                        String responseData = response.body().string();

                        Log.e("affiliate_number!", affiliate_number);
                        Log.e("responseData!!!s", responseData);

                        Log.e("Get Commision Data>", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            CommisionMain successData = new Gson().fromJson(responseData, CommisionMain.class);
                            if (successData.getResult() != null) {

                                firstDataArrayList.addAll(successData.getResult());
                            }
                        }

                        if (firstDataArrayList == null || firstDataArrayList.isEmpty()) {
                            nocommission.setVisibility(View.VISIBLE);
                        } else {
                            nocommission.setVisibility(View.GONE);
                        }

                        commisionAdpter = new CommisionAdpter(CommisionActivity.this, firstDataArrayList);
                        commision_list.setAdapter(commisionAdpter);
                        commisionAdpter.notifyDataSetChanged();

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
                swipeToRefresh.setRefreshing(false);
                progressbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }

    public class CommisionAdpter extends RecyclerView.Adapter<CommisionAdpter.MyViewHolder> {
        Context context;
        ArrayList<FirstData> firstDataArrayList;

        public CommisionAdpter(Activity myContacts, ArrayList<FirstData> firstDataArrayList) {
            this.context = myContacts;
            this.firstDataArrayList = firstDataArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_commission_lay, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);

            return holder;
            // return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.weeknumber_tv.setText(getString(R.string.week) + firstDataArrayList.get(position).getWeek() + "/53");
            holder.week_date_range.setText("" + firstDataArrayList.get(position).getData().get(0).getWeekStart() + "-" + firstDataArrayList.get(position).getData().get(0).getWeekEnd());
            holder.weekearning_amount.setText(mySession.getValueOf(MySession.CurrencySign) + " " + firstDataArrayList.get(position).getWeekComission());
            if (firstDataArrayList.get(position).getWithdraw_status().equalsIgnoreCase("Confirm")) {
                holder.payment_sts.setBackgroundResource(R.color.darkgreen);
                holder.payment_sts.setText(getString(R.string.completed));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commissionDataArrayList = firstDataArrayList.get(position).getData();
                    Intent i = new Intent(CommisionActivity.this, CommissionDetail.class);
                    i.putExtra("week_str", firstDataArrayList.get(position).getWeek());
                    i.putExtra("stripe_account_id", stripe_account_id);
                    i.putExtra("week_year_str", firstDataArrayList.get(position).getYear());
                    i.putExtra("week_start_month_str", firstDataArrayList.get(position).getData().get(0).getWeekStart());
                    i.putExtra("week_end_month_str", firstDataArrayList.get(position).getData().get(0).getWeekEnd());
                    i.putExtra("total_week_count", "53");
                    i.putExtra("total_week_commision_str", "" + firstDataArrayList.get(position).getWeekComission());
                    i.putExtra("availabel_week_withdraw_status", "" + firstDataArrayList.get(position).getAvailabel_week_withdraw_status());
                    i.putExtra("withdraw_status", "" + firstDataArrayList.get(position).getWithdraw_status());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 6;
            return firstDataArrayList == null ? 0 : firstDataArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView payment_sts, weeknumber_tv, week_date_range, weekearning_amount;

            public MyViewHolder(View view) {
                super(view);
                payment_sts = itemView.findViewById(R.id.payment_sts);
                weeknumber_tv = itemView.findViewById(R.id.weeknumber_tv);
                week_date_range = itemView.findViewById(R.id.week_date_range);
                weekearning_amount = itemView.findViewById(R.id.weekearning_amount);
            }
        }
    }

    private class GenrateLoginLink extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "create_stripe_login_link_member.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
                params.put("account_id", stripe_account_id);
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
                Log.e("StripeLogin RES", ">>>>>>>>>>>>" + response);
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
            progressbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        new GetProfile().execute();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "member_profile.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
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
                Log.e("GetProfile_Response", ">>>>>>>>>>>>" + response);
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
            progressbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        mySession.setlogindata(result);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        stripe_account_id = jsonObject1.getString("member_stripe_account_id");
                        stripe_account_login_link = jsonObject1.getString("stripe_account_login_link");
                        if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                            addstripeact.setVisibility(View.GONE);
                            if (stripe_account_login_link != null && !stripe_account_login_link.equalsIgnoreCase("")) {
                                genrateloginlinklay.setVisibility(View.GONE);
                                seestripedashboard.setVisibility(View.VISIBLE);
                            } else {
                                genrateloginlinklay.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

}
