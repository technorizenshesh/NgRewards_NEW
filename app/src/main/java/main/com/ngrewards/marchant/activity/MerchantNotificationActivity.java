package main.com.ngrewards.marchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import cz.msebera.android.httpclient.extras.Base64;
import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.Models.NotificationModel;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.EMIManualActivity;
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

public class MerchantNotificationActivity extends AppCompatActivity {
    private RecyclerView notificationlist;
    private RelativeLayout backlay;
    private NotificationAdpter notificationAdpter;
    private MySession mySession;
    private String user_id = "", time_zone = "";
    private SwipeRefreshLayout swipeToRefresh;
    private ArrayList<NotificationModel.Result> notificationModels;
    private TextView nonotiavb;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                type = null;
            } else {
                type = extras.getString("type");
            }
        } else {
            type = (String) savedInstanceState.getSerializable("type");
        }


        //   Log.e("sagar>>>>>", type);
        //  Toast.makeText(this, type, Toast.LENGTH_SHORT).show();

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
        UpdateStatus();
    }

    private void UpdateStatus() {
        new UpdateStatus().execute();
    }

    private void clickevent() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class UpdateStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://myngrewards.com/demo/wp-content/plugins/webservice/add_member_card_details.php?member_id=1&card_name=ks&card_number=122334455&expiry_date=12/08&expiry_year=2020
            try {
                String postReceiverUrl = BaseUrl.baseurl + "update_chat_status.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("reciever_id", user_id);
                params.put("type", type);

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
                Log.e("Json Add Response", ">>>>>>>>>>>>" + response);
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
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                      //  Toast.makeText(MerchantNotificationActivity.this, getResources().getString(R.string.status), Toast.LENGTH_LONG).show();

                    } else {
                    //    Toast.makeText(MerchantNotificationActivity.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private void idinit() {
        nonotiavb = findViewById(R.id.nonotiavb);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        backlay = findViewById(R.id.backlay);
        notificationlist = findViewById(R.id.notificationlist);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MerchantNotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        notificationlist.setLayoutManager(horizontalLayoutManagaer);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyNotification();
              //  new MyNotification().execute();
                // swipeToRefresh.setRefreshing(false);
            }
        });

        //getMyNotification();
        getMyNotification();

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  new MyNotification().execute();
    }

    public class NotificationAdpter extends RecyclerView.Adapter<NotificationAdpter.MyViewHolder> {
        Context context;
        ArrayList<NotificationModel.Result> notificationBeanNewArrayList;

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


        public NotificationAdpter(Activity myContacts,
                                  ArrayList<NotificationModel.Result> notificationBeanNewArrayList) {
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

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            NotificationModel.Result result = notificationBeanNewArrayList.get(position);
            NotificationModel.Result.Payload paylode = notificationBeanNewArrayList.get(position).getPayload();
                      if (paylode.getDueDate()!=null){
                          try {
                              int number_of_emi = Integer.parseInt(paylode.getNumberOfEmi());
                              String str = "th";
                              if (number_of_emi == 1) str = "st";
                              if (number_of_emi == 2) str = "nd";
                              if (number_of_emi == 3) str = "rd";
                              holder.message_tv.setText("Reminder for " + number_of_emi + str +
                                      " Payment " + paylode.getSplitAmountX() + "  Due on " + paylode.getDueDate());

                          }catch (Exception e){
                              holder.message_tv.setText("Reminder for EMI"  + " Payment " + paylode.getSplitAmountX() + "Due on " + paylode.getDueDate());

                          }

                          holder.itemView.setOnClickListener(v -> {
                              Log.e("TAG",
                                      "onBindViewHolder: paylode.toString()---"+paylode.toString() );
                              Intent intentw=new Intent(getApplicationContext(), EMIManualActivity.class);
                              intentw.putExtra("object",paylode.toString());
                              context.startActivity(intentw);
                          });
                      }else {
                          holder.message_tv.setText("" + result.getChatMessage());
                      }
                      holder.time_tv.setText("" +result.getDateTime());

         }

        @Override
        public int getItemCount() {
            // return 6;
            return notificationBeanNewArrayList == null ? 0 : notificationBeanNewArrayList.size();
        }
    }

    private void getMyNotification() {
        swipeToRefresh.setRefreshing(true);
        notificationModels = new ArrayList<>();
        Call<NotificationModel> call =
                ApiClient.getApiInterface().admin_notification_list_new(user_id,type);
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                //progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getStatus().equalsIgnoreCase("1")) {
                            Log.e("TAG",
                                    "onResponse: response.body().getStatus()----"+response.body().getStatus() );
                            notificationModels=response.body().getResult();
                            notificationAdpter = new NotificationAdpter(MerchantNotificationActivity.this, notificationModels);
                            notificationlist.setAdapter(notificationAdpter);
                            notificationAdpter.notifyDataSetChanged();
                        } else {
                            nonotiavb.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        nonotiavb.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    nonotiavb.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                t.printStackTrace();
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
                Log.e("TAG", "onFailure: "+t.getMessage() );
                Log.e("TAG", "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }

/*    private class MyNotification extends AsyncTask<String, String, String> {
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
//https://myngrewards.com/demo/wp-content/plugins/webservice/member_notification_msg_lists.php?user_id=990&type=Member
            try {
//                String postReceiverUrl = BaseUrl.baseurl + "admin_notification_list.php?";
                String postReceiverUrl = BaseUrl.baseurl + "admin_notification_list_new.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl>>", " .." + postReceiverUrl + "user_id=" + user_id + "&type="+type);
                params.put("user_id", user_id);
                params.put("type", type);
                //params.put("timezone", time_zone);

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
                            notificationBeanNew.setChatMesssage(jsonObject2.getString("chat_message"));
                            notificationBeanNew.setCreated_date(jsonObject2.getString("date_time"));
                            if (jsonObject2.has("payload")){
                                if (jsonObject2.getString("chat_message").equalsIgnoreCase("You " +
                                        "have emi pending please pay now")){
                                Log.e("TAG", "onPostExecute:datadatadatadata " );
                                 if (!jsonObject2.getJSONObject("payload").equals(null)){
                                JSONObject data = jsonObject2.getJSONObject("payload");
                                Log.e("TAG", "onPostExecute:datadatadatadata "+data.toString() );
                                if (data.isNull("")) {}else {notificationBeanNew.setData(data.toString());}}
                                 }
                            }

                            Log.e("chat_message>>>>", jsonObject2.getString("chat_message"));
                            Log.e("date_time>>>>", jsonObject2.getString("date_time"));

                            notificationBeanNewArrayList.add(notificationBeanNew);

                        }


                    } else {
                        nonotiavb.setVisibility(View.VISIBLE);
                    }

                    if (notificationBeanNewArrayList == null || notificationBeanNewArrayList.isEmpty() || notificationBeanNewArrayList.size() == 0) {
                        nonotiavb.setVisibility(View.VISIBLE);
                    } else {
                        notificationAdpter = new NotificationAdpter(MerchantNotificationActivity.this, notificationBeanNewArrayList);
                        notificationlist.setAdapter(notificationAdpter);
                        notificationAdpter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }*/

    public static String fromBase64(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);

    }
}

/*package main.com.ngrewards.marchant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import cz.msebera.android.httpclient.extras.Base64;
import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.Models.NotificationModel;
import main.com.ngrewards.R;
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

public class MerchantNotificationActivity extends AppCompatActivity {
    private RecyclerView notificationlist;
    private RelativeLayout backlay;
    private NotificationAdpter notificationAdpter;
    private MySession mySession;
    private String user_id = "", time_zone = "";
    private SwipeRefreshLayout swipeToRefresh;
    private ArrayList<NotificationListBean> notificationListBeanArrayList;
    private ArrayList<NotificationListBean> notificationListBeanArrayList;
    private ArrayList<NotificationBeanNew> notificationBeanNewArrayList;
    private TextView nonotiavb;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                type = null;
            } else {
                type = extras.getString("type");
            }
        } else {
            type = (String) savedInstanceState.getSerializable("type");
        }


        //   Log.e("sagar>>>>>", type);
        //  Toast.makeText(this, type, Toast.LENGTH_SHORT).show();

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
        UpdateStatus();
    }

    private void UpdateStatus() {
        new UpdateStatus().execute();
    }

    private void clickevent() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class UpdateStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://myngrewards.com/demo/wp-content/plugins/webservice/add_member_card_details.php?member_id=1&card_name=ks&card_number=122334455&expiry_date=12/08&expiry_year=2020
            try {
                String postReceiverUrl = BaseUrl.baseurl + "update_chat_status.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("reciever_id", user_id);
                params.put("type", type);

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
                Log.e("Json Add Response", ">>>>>>>>>>>>" + response);
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
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                      //  Toast.makeText(MerchantNotificationActivity.this, getResources().getString(R.string.status), Toast.LENGTH_LONG).show();

                    } else {
                    //    Toast.makeText(MerchantNotificationActivity.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private void idinit() {
        nonotiavb = findViewById(R.id.nonotiavb);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        backlay = findViewById(R.id.backlay);
        notificationlist = findViewById(R.id.notificationlist);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MerchantNotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        notificationlist.setLayoutManager(horizontalLayoutManagaer);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyNotification();
              //  new MyNotification().execute();
                // swipeToRefresh.setRefreshing(false);
            }
        });
        //getMyNotification();
        getMyNotification();

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  new MyNotification().execute();
    }

    public class NotificationAdpter extends RecyclerView.Adapter<NotificationAdpter.MyViewHolder> {
        Context context;
        ArrayList<NotificationBeanNew> notificationBeanNewArrayList;

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

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (notificationBeanNewArrayList.get(position).getChatMesssage().equalsIgnoreCase("You " +
                    "have emi pending please pay now")){
                    JSONObject data = null;
                    try {
                        Log.e("TAG", "onBindViewHolder: notificationBeanNewArrayList datadata");                        data = new JSONObject( notificationBeanNewArrayList.get(position).getData());
                        if (data!=null){
                            Log.e("TAG", "onBindViewHolder: notificationBeanNewArrayList datadata");                        data = new JSONObject( notificationBeanNewArrayList.get(position).getData());

                        }
                        data = new JSONObject(notificationBeanNewArrayList.get(position).getData());
                        String  split_amount_x=data.getString("split_amount_x");
                        String  message= data.getString("message");
                        String  due_date= data.getString("due_date");
                        int  number_of_emi= Integer.parseInt(data.getString("number_of_emi"));
                        String str = "th";
                        if (number_of_emi == 0) str = "st";
                        if (number_of_emi == 1) str = "nd";
                        if (number_of_emi == 2) str = "rd";
                        holder.message_tv.setText("Reminder for " + number_of_emi + str + " Payment " + split_amount_x + "Due on " + due_date);
                    } catch (JSONException e) {
                        Log.e("TAG", "onBindViewHolder: "+e.getLocalizedMessage() );
                        throw new RuntimeException(e);
                    }
            }else {


            holder.message_tv.setText("" + notificationBeanNewArrayList.get(position).getChatMesssage());
            holder.time_tv.setText("" + notificationBeanNewArrayList.get(position).getCreated_date());
            }
         }

        @Override
        public int getItemCount() {
            // return 6;
            return notificationBeanNewArrayList == null ? 0 : notificationBeanNewArrayList.size();
        }
    }

    private void getMyNotification() {
        swipeToRefresh.setRefreshing(true);
        notificationListBeanArrayList = new ArrayList<>();
        Call<NotificationModel> call = ApiClient.getApiInterface().admin_notification_list_new(user_id);
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                //progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {

                        if (response.body().getStatus().equals("1")) {

                            notificationListBeanArrayList.addAll(response.body().getResult());

                        } else {
                            nonotiavb.setVisibility(View.VISIBLE);
                        }
                        if (notificationListBeanArrayList == null || notificationListBeanArrayList.isEmpty() || notificationListBeanArrayList.size() == 0) {
                            nonotiavb.setVisibility(View.VISIBLE);
                        }


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
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
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
//https://myngrewards.com/demo/wp-content/plugins/webservice/member_notification_msg_lists.php?user_id=990&type=Member
            try {
//                String postReceiverUrl = BaseUrl.baseurl + "admin_notification_list.php?";
                String postReceiverUrl = BaseUrl.baseurl + "admin_notification_list_new.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl>>", " .." + postReceiverUrl + "user_id=" + user_id + "&type="+type);
                params.put("user_id", user_id);
                params.put("type", type);
                //params.put("timezone", time_zone);

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
                            notificationBeanNew.setChatMesssage(jsonObject2.getString("chat_message"));
                            notificationBeanNew.setCreated_date(jsonObject2.getString("date_time"));
                            if (jsonObject2.has("payload")){
                                if (jsonObject2.getString("chat_message").equalsIgnoreCase("You " +
                                        "have emi pending please pay now")){
                                Log.e("TAG", "onPostExecute:datadatadatadata " );
                                 if (!jsonObject2.getJSONObject("payload").equals(null)){
                                JSONObject data = jsonObject2.getJSONObject("payload");
                                Log.e("TAG", "onPostExecute:datadatadatadata "+data.toString() );
                                if (data.isNull("")) {}else {notificationBeanNew.setData(data.toString());}}
                                 }
                            }

                            Log.e("chat_message>>>>", jsonObject2.getString("chat_message"));
                            Log.e("date_time>>>>", jsonObject2.getString("date_time"));

                            notificationBeanNewArrayList.add(notificationBeanNew);

                        }


                    } else {
                        nonotiavb.setVisibility(View.VISIBLE);
                    }

                    if (notificationBeanNewArrayList == null || notificationBeanNewArrayList.isEmpty() || notificationBeanNewArrayList.size() == 0) {
                        nonotiavb.setVisibility(View.VISIBLE);
                    } else {
                        notificationAdpter = new NotificationAdpter(MerchantNotificationActivity.this, notificationBeanNewArrayList);
                        notificationlist.setAdapter(notificationAdpter);
                        notificationAdpter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static String fromBase64(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);

    }
}
*/