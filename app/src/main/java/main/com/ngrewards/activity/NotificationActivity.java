package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import cz.msebera.android.httpclient.extras.Base64;
import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.NotificationBean;
import main.com.ngrewards.beanclasses.NotificationBeanNew;
import main.com.ngrewards.beanclasses.NotificationListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notificationlist;
    private RelativeLayout backlay;
    private NotificationAdpter notificationAdpter;
    private MySession mySession;
    private String user_id = "", time_zone = "";
    private SwipeRefreshLayout swipeToRefresh;
    private ArrayList<NotificationListBean> notificationListBeanArrayList;
    private ArrayList<NotificationBeanNew> notificationBeanNewArrayList;
    private TextView nonotiavb;

    public static String fromBase64(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_notification);
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();

        Log.d("TAG", "testing: " + " Notification me gaya");

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

        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
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
    }

    private void idinit() {
        nonotiavb = findViewById(R.id.nonotiavb);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        backlay = findViewById(R.id.backlay);
        notificationlist = findViewById(R.id.notificationlist);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        notificationlist.setLayoutManager(horizontalLayoutManagaer);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new MyNotification().execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MyNotification().execute();
    }

    private void getMyNotification() {
        swipeToRefresh.setRefreshing(true);
        notificationListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMemberNotification(user_id);
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

                            NotificationBean successData = new Gson().fromJson(responseData, NotificationBean.class);
                            notificationListBeanArrayList.addAll(successData.getResult());

                        } else {
                            nonotiavb.setVisibility(View.VISIBLE);
                        }
                        if (notificationListBeanArrayList == null || notificationListBeanArrayList.isEmpty() || notificationListBeanArrayList.size() == 0) {
                            nonotiavb.setVisibility(View.VISIBLE);
                        }
                        /*notificationAdpter = new NotificationAdpter(NotificationActivity.this,notificationListBeanArrayList);
                        notificationlist.setAdapter(notificationAdpter);
                        notificationAdpter.notifyDataSetChanged();*/

                    } catch (IOException e) {
                        nonotiavb.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    } catch (JSONException e) {
                        nonotiavb.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    nonotiavb.setVisibility(View.VISIBLE);
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

    public class NotificationAdpter extends RecyclerView.Adapter<NotificationAdpter.MyViewHolder> {
        Context context;
        ArrayList<NotificationListBean> notificationListBeanArrayList;
        ArrayList<NotificationBeanNew> notificationBeanNewArrayList;

        public NotificationAdpter(Activity myContacts, ArrayList<NotificationBeanNew> notificationBeanNewArrayList) {
            this.context = myContacts;
            this.notificationBeanNewArrayList = notificationBeanNewArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_notilay, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);
            return holder;
            // return new MyViewHolder(itemView);
        }
/*
        public NotificationAdpter(Activity myContacts, ArrayList<NotificationListBean> notificationListBeanArrayList) {
            this.context = myContacts;
            this.notificationListBeanArrayList = notificationListBeanArrayList;
        }
*/

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            if (notificationBeanNewArrayList.get(position).getNotification_type().equalsIgnoreCase("Chat")) {
                if (notificationBeanNewArrayList.get(position).getUnseen_count() != null && !notificationBeanNewArrayList.get(position).getUnseen_count().equalsIgnoreCase("") && !notificationBeanNewArrayList.get(position).getUnseen_count().equalsIgnoreCase("0")) {
                    holder.reqcount.setVisibility(View.VISIBLE);
                    holder.reqcount.setText("" + notificationBeanNewArrayList.get(position).getUnseen_count());
                } else {
                    holder.reqcount.setVisibility(View.GONE);
                }

                holder.time_tv.setText("" + notificationBeanNewArrayList.get(position).getCreated_date());
                holder.user_name_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname());
                holder.message_tv.setText("" + notificationBeanNewArrayList.get(position).getMessage_key());
                String image_url = notificationBeanNewArrayList.get(position).getImage();
                if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                    Glide.with(NotificationActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
                }
            } else if (notificationBeanNewArrayList.get(position).getNotification_type().equalsIgnoreCase("Transfer")) {
                holder.reqcount.setVisibility(View.GONE);
                holder.time_tv.setText("" + notificationBeanNewArrayList.get(position).getCreated_date());
                holder.user_name_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname());
                // holder.message_tv.setText(""+notificationBeanNewArrayList.get(position).getMessage_key());
                String text = notificationBeanNewArrayList.get(position).getFullname() + " transferred <font color=green>" + mySession.getValueOf(MySession.CurrencySign) + notificationBeanNewArrayList.get(position).getAmount() + "</font> to you" + " " + notificationBeanNewArrayList.get(position).getTimeago();
                holder.message_tv.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                //holder.message_tv.setText("" + text);
                // holder.message_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname() + " transfer you $"+notificationBeanNewArrayList.get(position).getAmount()+" " + notificationBeanNewArrayList.get(position).getTimeago());

                String image_url = notificationBeanNewArrayList.get(position).getImage();
                if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                    Glide.with(NotificationActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
                }
            } else if (notificationBeanNewArrayList.get(position).getNotification_type().equalsIgnoreCase("Request")) {
                holder.reqcount.setVisibility(View.GONE);
                holder.time_tv.setText("" + notificationBeanNewArrayList.get(position).getCreated_date());
                holder.user_name_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname());
                // holder.message_tv.setText(""+notificationBeanNewArrayList.get(position).getMessage_key());

                holder.message_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname() + getString(R.string.request_for) + mySession.getValueOf(MySession.CurrencySign) + notificationBeanNewArrayList.get(position).getAmount() + " " + notificationBeanNewArrayList.get(position).getTimeago());

                String image_url = notificationBeanNewArrayList.get(position).getImage();
                if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                    Glide.with(NotificationActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
                }
            } else {
                holder.reqcount.setVisibility(View.GONE);
                holder.time_tv.setText("" + notificationBeanNewArrayList.get(position).getCreated_date());
                holder.user_name_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname());
                // holder.message_tv.setText(""+notificationBeanNewArrayList.get(position).getMessage_key());

                holder.message_tv.setText("" + notificationBeanNewArrayList.get(position).getFullname() + getString(R.string.just_signed_up_using_your_username) + notificationBeanNewArrayList.get(position).getTimeago());

                String image_url = notificationBeanNewArrayList.get(position).getImage();
                if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                    Glide.with(NotificationActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(holder.user_img);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notificationBeanNewArrayList.get(position).getNotification_type().equalsIgnoreCase("Chat")) {

                        Intent i = new Intent(NotificationActivity.this, MemberChatAct.class);
                        i.putExtra("receiver_id", notificationBeanNewArrayList.get(position).getSenderid());
                        i.putExtra("type", "Member");
                        i.putExtra("type", "Member");
                        i.putExtra("receiver_fullname", notificationBeanNewArrayList.get(position).getFullname());
                        i.putExtra("receiver_img", "" + notificationBeanNewArrayList.get(position).getSenderimg());
                        i.putExtra("receiver_name", notificationBeanNewArrayList.get(position).getSendername());
                        startActivity(i);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            // return 6;
            return notificationBeanNewArrayList == null ? 0 : notificationBeanNewArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout backlay;
            CircleImageView user_img;
            TextView user_name_tv, message_tv, time_tv, reqcount;

            public MyViewHolder(View view) {
                super(view);
                reqcount = itemView.findViewById(R.id.reqcount);
                user_img = itemView.findViewById(R.id.user_img);
                user_name_tv = itemView.findViewById(R.id.user_name_tv);
                message_tv = itemView.findViewById(R.id.message_tv);
                time_tv = itemView.findViewById(R.id.time_tv);
            }
        }
    }

    private class MyNotification extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            swipeToRefresh.setRefreshing(true);
            notificationBeanNewArrayList = new ArrayList<>();
            super.onPreExecute();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "member_notification_msg_lists.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl >>", " .." + postReceiverUrl + "user_id=" + user_id + "&type=Member");
                params.put("user_id", user_id);
                params.put("type", "Member");
                params.put("timezone", time_zone);

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
            Log.e("Get Notification >>", "" + result);
            swipeToRefresh.setRefreshing(false);
            if (result == null) {

            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int jsonlenth = 0;
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        jsonlenth = jsonArray.length();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            NotificationBeanNew notificationBeanNew = new NotificationBeanNew();
                            if (jsonObject2.getString("notification_type").equalsIgnoreCase("Chat")) {

                                if (jsonObject2.getString("type").equalsIgnoreCase("Merchant")) {
                                    notificationBeanNew.setSenderid(jsonObject2.getString("id"));
                                    notificationBeanNew.setSenderimg(jsonObject2.getString("merchant_image"));
                                    notificationBeanNew.setSendername(jsonObject2.getString("business_name"));
                                    notificationBeanNew.setFullname(jsonObject2.getString("business_name"));
                                    notificationBeanNew.setImage(jsonObject2.getString("merchant_image"));

                                } else {
                                    if (jsonObject2.getString("receiver_type").equalsIgnoreCase("Member")) {
                                        notificationBeanNew.setSenderid(jsonObject2.getString("tid"));
                                        notificationBeanNew.setSenderimg(jsonObject2.getString("member_image"));
                                        notificationBeanNew.setSendername(jsonObject2.getString("affiliate_name"));
                                        notificationBeanNew.setImage(jsonObject2.getString("member_image"));
                                        notificationBeanNew.setFullname(jsonObject2.getString("fullname"));

                                    } else {
                                        notificationBeanNew.setSenderid(jsonObject2.getString("id"));
                                        notificationBeanNew.setSenderimg(jsonObject2.getString("merchant_image"));
                                        notificationBeanNew.setSendername(jsonObject2.getString("business_name"));
                                        notificationBeanNew.setFullname(jsonObject2.getString("business_name"));
                                        notificationBeanNew.setImage(jsonObject2.getString("merchant_image"));
                                    }
                                }


                                notificationBeanNew.setNotification_type(jsonObject2.getString("notification_type"));
                                notificationBeanNew.setUnseen_count(jsonObject2.getString("no_of_message"));

                                //conversession.setId(jsonObject2.getString("id"));
                                notificationBeanNew.setReceiver_type(jsonObject2.getString("receiver_type"));

                                if (jsonObject2.getString("last_message") == null || jsonObject2.getString("last_message").equalsIgnoreCase("")) {
                                    notificationBeanNew.setMessage(jsonObject2.getString("msg_type"));
                                    notificationBeanNew.setMessage_key(jsonObject2.getString("msg_type"));

                                } else {

                                    notificationBeanNew.setMessage(fromBase64(jsonObject2.getString("last_message")));
                                    notificationBeanNew.setMessage_key(fromBase64(jsonObject2.getString("last_message")));
                                    // notificationBeanNew.setMessage_key(jsonObject2.getString("last_message"));
                                }


                                notificationBeanNew.setTimeago("");
                                notificationBeanNew.setDatetime(jsonObject2.getString("date"));

                                try {
                                    String mytime = "";
                                    if (jsonObject2.has("created_date")) {
                                        mytime = jsonObject2.getString("created_date");
                                    } else {
                                        mytime = jsonObject2.getString("date");
                                    }

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date myDate = null;
                                    myDate = dateFormat.parse(mytime);

                                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                                    String finalDate = timeFormat.format(myDate);
                                    // date_tv.setText("Date:- "+finalDate);
                                    notificationBeanNew.setCreated_date(finalDate);
                                    System.out.println(finalDate);
                                } catch (Exception e) {
                                    Log.e("EXC TRUE", " RRR");
                                    notificationBeanNew.setCreated_date(jsonObject2.getString("created_date"));

                                }
                            } else if (jsonObject2.getString("notification_type").equalsIgnoreCase("Transfer") || jsonObject2.getString("notification_type").equalsIgnoreCase("Request")) {

                                if (jsonObject2.getString("type").equalsIgnoreCase("Member")) {
                                    notificationBeanNew.setSenderid(jsonObject2.getString("id"));
                                    notificationBeanNew.setSenderimg(jsonObject2.getString("image"));
                                    notificationBeanNew.setSendername(jsonObject2.getString("business_name"));
                                    notificationBeanNew.setImage(jsonObject2.getString("image"));
                                    notificationBeanNew.setFullname(jsonObject2.getString("fullname"));

                                }

                                notificationBeanNew.setNotification_type(jsonObject2.getString("notification_type"));
                                notificationBeanNew.setAmount(jsonObject2.getString("amount"));

                                //conversession.setId(jsonObject2.getString("id"));

                                notificationBeanNew.setTimeago("");
                                //  notificationBeanNew.setDatetime(jsonObject2.getString("date"));

                                try {
                                    String mytime = jsonObject2.getString("created_date");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                                            "yyyy-MM-dd hh:mm:ss");
                                    Date myDate = null;
                                    myDate = dateFormat.parse(mytime);

                                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                                    String finalDate = timeFormat.format(myDate);
                                    // date_tv.setText("Date:- "+finalDate);
                                    notificationBeanNew.setCreated_date(finalDate);
                                    System.out.println(finalDate);
                                } catch (Exception e) {
                                    Log.e("EXC TRUE", " RRR");
                                    notificationBeanNew.setCreated_date(jsonObject2.getString("created_date"));

                                }
                                // notificationBeanNew.setCreated_date(jsonObject2.getString("date"));
                            } else {
                                notificationBeanNew.setId(jsonObject2.getString("id"));
                                notificationBeanNew.setUser_id(jsonObject2.getString("user_id"));
                                notificationBeanNew.setInvite_user_id(jsonObject2.getString("invite_user_id"));
                                notificationBeanNew.setMessage_key(jsonObject2.getString("message_key"));
                                notificationBeanNew.setMessage(jsonObject2.getString("message"));
                                notificationBeanNew.setType(jsonObject2.getString("type"));
                                notificationBeanNew.setNotification_type(jsonObject2.getString("notification_type"));
                                notificationBeanNew.setStatus(jsonObject2.getString("status"));

                                try {
                                    Log.e("TRY BLOCK", " > " + jsonObject2.getString("created_date"));
                                    String mytime = jsonObject2.getString("created_date");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date myDate = null;
                                    myDate = dateFormat.parse(mytime);

                                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                                    String finalDate = timeFormat.format(myDate);
                                    notificationBeanNew.setCreated_date(finalDate);
                                    System.out.println(finalDate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("Exception BLOCK", " > " + jsonObject2.getString("created_date"));
                                    Log.e("EXC TRUE", " RRR");
                                    notificationBeanNew.setCreated_date(jsonObject2.getString("created_date"));

                                }

                                notificationBeanNew.setImage(jsonObject2.getString("image"));
                                notificationBeanNew.setFullname(jsonObject2.getString("business_name"));
                                notificationBeanNew.setBusiness_name(jsonObject2.getString("business_name"));
                                notificationBeanNew.setTimeago("");
                                notificationBeanNew.setUnseen_count("");

                            }
                            notificationBeanNewArrayList.add(notificationBeanNew);
                        }


                    } else {
                        nonotiavb.setVisibility(View.VISIBLE);
                    }
                    if (notificationBeanNewArrayList == null || notificationBeanNewArrayList.isEmpty() || notificationBeanNewArrayList.size() == 0) {
                        nonotiavb.setVisibility(View.VISIBLE);
                    }
                    notificationAdpter = new NotificationAdpter(NotificationActivity.this, notificationBeanNewArrayList);
                    notificationlist.setAdapter(notificationAdpter);
                    notificationAdpter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
