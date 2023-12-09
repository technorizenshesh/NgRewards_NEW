package main.com.ngrewards.marchant.merchantbottum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import co.dift.ui.SwipeToAction;
import cz.msebera.android.httpclient.extras.Base64;
import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.MemberChatAct;
import main.com.ngrewards.beanclasses.ConverSession;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.marchant.draweractivity.MerchantBaseActivity;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerMessageAct extends MerchantBaseActivity {
    MessageRecycladp messageRecycladp;
    RecyclerView mychat;
    FrameLayout contentFrameLayout;
    SwipeToAction swipeToAction;
    private ImageView write_to;
    private SwipeRefreshLayout swipeToRefresh;
    private ProgressBar progresbar;
    private ArrayList<ConverSession> converSessionArrayList;
    private MySession mySession;
    private String user_id = "", image_url = "";
    private int del_item_pos;
    private ArrayList<MemberDetail> memberDetailArrayList;
    private Myapisession myapisession;

    public static String fromBase64(String message) {

        byte[] data = Base64.decode(message, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_mer_message);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_mer_message, contentFrameLayout);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    image_url = jsonObject1.getString("merchant_image");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinti();
        clickevent();


        if (myapisession.getKeyMemberusername() == null || myapisession.getKeyMemberusername().equalsIgnoreCase("")) {
            getUsername();
        } else {
            try {
                memberDetailArrayList = new ArrayList<>();
                JSONObject object = new JSONObject(myapisession.getKeyMemberusername());
                Log.e("Product Category >", " >" + myapisession.getKeyMemberusername());
                if (object.getString("status").equals("1")) {
                    MemberBean successData = new Gson().fromJson(myapisession.getKeyMemberusername(), MemberBean.class);
                    memberDetailArrayList.addAll(successData.getResult());
                } else {
                    getUsername();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    private void clickevent() {
        write_to.setOnClickListener(v -> {
            Intent i = new Intent(MerMessageAct.this, NewMessageActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetChatList().execute();
    }

    private void idinti() {
        mychat = findViewById(R.id.mychat);
        progresbar = findViewById(R.id.progresbar);
        write_to = findViewById(R.id.write_to);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);

        swipeToRefresh.setOnRefreshListener(() -> new GetChatList().execute());

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(MerMessageAct.this, LinearLayoutManager.VERTICAL, false);
        mychat.setLayoutManager(horizontalLayoutManagaer);
        mychat.setHasFixedSize(true);

        messageRecycladp = new MessageRecycladp(MerMessageAct.this, converSessionArrayList);
        mychat.setAdapter(messageRecycladp);
        messageRecycladp.notifyDataSetChanged();

        swipeToAction = new SwipeToAction(mychat, new SwipeToAction.SwipeListener<ConverSession>() {
            @Override
            public boolean swipeLeft(final ConverSession itemData) {
                final int pos = removeBook(itemData);
                displaySnackbar(itemData.getSendername() + " removed", "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBook(pos, itemData);
                    }
                });
                return true;
            }

            @Override
            public boolean swipeRight(ConverSession itemData) {
                displaySnackbar(itemData.getSendername() + " loved", null, null);
                return true;
            }

            @Override
            public void onClick(ConverSession itemData) {
                displaySnackbar(itemData.getSendername() + " clicked", null, null);
            }

            @Override
            public void onLongClick(ConverSession itemData) {
                displaySnackbar(itemData.getSendername() + " long clicked", null, null);
            }
        });

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeToAction!=null){
                    swipeToAction.swipeRight(2);
                }

            }
        }, 3000);
*/
    }

    private int removeBook(ConverSession book) {
        int pos = converSessionArrayList.indexOf(book);
        converSessionArrayList.remove(book);
        messageRecycladp.notifyItemRemoved(pos);
        return pos;
    }

    private void addBook(int pos, ConverSession book) {
        converSessionArrayList.add(pos, book);
        messageRecycladp.notifyItemInserted(pos);
    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.darkgrey));
        ((TextView) v.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(R.id.snackbar_action)).setTextColor(Color.BLACK);

        snack.show();
    }

    private void getUsername() {
        Log.e("User name list>", " >GET NAME");

        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername(user_id, mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("User name list>", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyMemberusername(responseData);
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);
                            memberDetailArrayList.addAll(successData.getResult());
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

    private class GetChatList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            converSessionArrayList = new ArrayList<>();
            // progresbar.setVisibility(View.VISIBLE);
            swipeToRefresh.setRefreshing(true);
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

                String postReceiverUrl = BaseUrl.baseurl + "get_conversation.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl >>", " .." + postReceiverUrl + "user_id=" + user_id + "&type=Merchant");

                params.put("user_id", user_id);
                params.put("type", "Merchant");

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
            Log.e("Chat List >>", "" + result);
            // progresbar.setVisibility(View.GONE);
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
                        converSessionArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if (jsonObject2.has("id") | jsonObject2.has("tid")) {
                                ConverSession conversession = new ConverSession();
                                //conversession.setId(jsonObject2.getString("id"));
                                conversession.setChat_id(jsonObject2.getString("chat_id"));
                                conversession.setNo_of_message(jsonObject2.getString("no_of_message"));
                                conversession.setReceiver_type(jsonObject2.getString("receiver_type"));

                                if (jsonObject2.getString("last_message") == null || jsonObject2.getString("last_message").equalsIgnoreCase("")) {
                                    conversession.setMessage(jsonObject2.getString("msg_type"));
                                } else {
                                    // conversession.setMessage(fromBase64(jsonObject2.getString("last_message")));

                                    conversession.setMessage(jsonObject2.getString("last_message"));
                                }


                                if (jsonObject2.getString("type").equalsIgnoreCase("Member")) {
                                    conversession.setSenderid(jsonObject2.getString("tid"));
                                    conversession.setSenderimg(jsonObject2.getString("member_image"));
                                    conversession.setSendername(jsonObject2.getString("username"));
                                    conversession.setFullname(jsonObject2.getString("fullname"));


                                } else {
                                    if (jsonObject2.getString("receiver_type").equalsIgnoreCase("Member")) {
                                        conversession.setSenderid(jsonObject2.getString("tid"));
                                        conversession.setSenderimg(jsonObject2.getString("member_image"));
                                        conversession.setSendername(jsonObject2.getString("affiliate_name"));
                                        conversession.setFullname(jsonObject2.getString("fullname"));

                                    } else {
                                        if (jsonObject2.has("id")) {
                                            conversession.setSenderid(jsonObject2.getString("id"));
                                            conversession.setSenderimg(jsonObject2.getString("merchant_image"));
                                            conversession.setSendername(jsonObject2.getString("business_name"));
                                            conversession.setFullname(jsonObject2.getString("business_name"));
                                        }
                                    }

                                }

                           /* if (jsonObject2.getString("receiver_type").equalsIgnoreCase("Member")){
                                conversession.setSenderid(jsonObject2.getString("tid"));
                                conversession.setSenderimg(jsonObject2.getString("member_image"));
                                conversession.setSendername(jsonObject2.getString("affiliate_name"));
                            }
                            else {
                                conversession.setSenderid(jsonObject2.getString("id"));
                                conversession.setSenderimg(jsonObject2.getString("merchant_image"));
                                conversession.setSendername(jsonObject2.getString("business_name"));
                            }*/

                           /* conversession.setSenderimg(jsonObject2.getString("member_image"));
                            conversession.setSendername(jsonObject2.getString("fullname"));*/

                                conversession.setDatetime(jsonObject2.getString("date_time"));
                                Date date1 = null;
                                try {
                                    date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(jsonObject2.getString("date_time").trim());
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm aa", Locale.ENGLISH);
                                    String strDate = formatter.format(date1);
                                    conversession.setDatetime(strDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // conversession.setTime(jsonObject2.getString("time"));
                                converSessionArrayList.add(conversession);
                            } else {

                            }


                        }

                    }
                    if (converSessionArrayList != null || !converSessionArrayList.isEmpty()) {
                        messageRecycladp = new MessageRecycladp(MerMessageAct.this, converSessionArrayList);
                        mychat.setAdapter(messageRecycladp);
                        messageRecycladp.notifyDataSetChanged();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private class DeleteChat extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            progresbar.setVisibility(View.VISIBLE);
            super.onPreExecute();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/clear_conversation.php?receiver_id=377&sender_id=927
            try {
                String postReceiverUrl = BaseUrl.baseurl + "clear_conversation.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl >>", " .." + postReceiverUrl + "sender_id=" + user_id + "&receiver_id=" + strings[0]);
                params.put("receiver_id", user_id);
                params.put("sender_id", strings[0]);

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
            Log.e("Chat List >>", "" + result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {

            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int jsonlenth = 0;
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                        if (converSessionArrayList != null || !converSessionArrayList.isEmpty()) {
                            converSessionArrayList.remove(del_item_pos);
                            messageRecycladp = new MessageRecycladp(MerMessageAct.this, converSessionArrayList);
                            mychat.setAdapter(messageRecycladp);
                            messageRecycladp.notifyDataSetChanged();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    public class MessageRecycladp extends RecyclerView.Adapter<MessageRecycladp.MyViewHolder> {
        Context context;
        ArrayList<ConverSession> converSessionArrayList;

        public MessageRecycladp(Activity myContacts, ArrayList<ConverSession> converSessionArrayList) {
            this.context = myContacts;
            this.converSessionArrayList = converSessionArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.messagelistlay, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);


            return holder;
            // return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MessageRecycladp.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ConverSession item = converSessionArrayList.get(position);
            if (converSessionArrayList.get(position).getNo_of_message() != null && !converSessionArrayList.get(position).getNo_of_message().equalsIgnoreCase("") && !converSessionArrayList.get(position).getNo_of_message().equalsIgnoreCase("0")) {
                holder.reqcount.setVisibility(View.VISIBLE);
                holder.reqcount.setText("" + converSessionArrayList.get(position).getNo_of_message());
            } else {
                holder.reqcount.setVisibility(View.GONE);
            }
            if (converSessionArrayList.get(position).getFullname() == null || converSessionArrayList.get(position).getFullname().equalsIgnoreCase("")) {
                holder.name.setText("@" + converSessionArrayList.get(position).getSendername());
            } else {
                holder.fullname.setText("" + converSessionArrayList.get(position).getFullname());
                holder.name.setText("@" + converSessionArrayList.get(position).getSendername());

            }
            // holder.name.setText(""+converSessionArrayList.get(position).getSendername());
            holder.lastmaessage_tv.setText("" + converSessionArrayList.get(position).getMessage());
            holder.datetiem.setText("" + converSessionArrayList.get(position).getDatetime());

            String imagelist = converSessionArrayList.get(position).getSenderimg();
            if (imagelist.equalsIgnoreCase("") || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl) || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                //Picasso.with(ChatingAct.this).load(imagelist).into(my_profile);

                Glide.with(MerMessageAct.this)
                        .load(BaseUrl.image_baseurl + imagelist)
                        .thumbnail(0.5f)
                        .override(200, 200)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.propic);

            }
            holder.itemView.setOnClickListener(v -> {
                Log.e("TAG", "onClick:  getSenderid" + converSessionArrayList.get(position).getSenderid());
                Log.e("TAG", "onClick:  getReciverid " + converSessionArrayList.get(position).getReciverid());
                Intent i = new Intent(MerMessageAct.this, MemberChatAct.class);
                i.putExtra("receiver_id", converSessionArrayList.get(position).getSenderid());
                i.putExtra("type", "Merchant");
                // i.putExtra("receiver_fullname", converSessionArrayList.get(position).getFullname());
                i.putExtra("receiver_fullname", converSessionArrayList.get(position).getFullname());
                i.putExtra("receiver_type", converSessionArrayList.get(position).getReceiver_type());
                i.putExtra("receiver_img", BaseUrl.image_baseurl + converSessionArrayList.get(position).getSenderimg());
                i.putExtra("receiver_name", converSessionArrayList.get(position).getSendername());
                startActivity(i);

            });
            holder.deletecon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    del_item_pos = position;
                    new DeleteChat().execute(converSessionArrayList.get(position).getSenderid());

                }
            });

            holder.data = item;
        }

        @Override
        public int getItemCount() {
            //return 6;
            return converSessionArrayList == null ? 0 : converSessionArrayList.size();
        }

        public class MyViewHolder extends SwipeToAction.ViewHolder<ConverSession> {

            private final TextView fullname;
            private final TextView name;
            private final TextView lastmaessage_tv;
            private final TextView datetiem;
            private final TextView reqcount;
            private final CircleImageView propic;
            private final ImageView deletecon;


            public MyViewHolder(View view) {
                super(view);
                reqcount = itemView.findViewById(R.id.reqcount);
                propic = itemView.findViewById(R.id.propic);
                name = itemView.findViewById(R.id.name);
                fullname = itemView.findViewById(R.id.fullname);
                lastmaessage_tv = itemView.findViewById(R.id.lastmaessage_tv);
                datetiem = itemView.findViewById(R.id.datetiem);
                deletecon = itemView.findViewById(R.id.deletecon);
            }
        }
    }

}
//https://www.dropbox.com/s/0fj2ghbguytz5zd/BamtukatCours.zip?dl=0