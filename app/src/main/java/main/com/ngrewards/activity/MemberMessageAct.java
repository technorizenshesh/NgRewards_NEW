package main.com.ngrewards.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import cz.msebera.android.httpclient.extras.Base64;
import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.ConverSession;
import main.com.ngrewards.beanclasses.MarchantBean;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberMessageAct extends Fragment {

    MessageRecycladp messageRecycladp;
    RecyclerView mychat;
    ArrayList<MerchantListBean> merchantListBeanArrayList=new ArrayList<>();
    FrameLayout contentFrameLayout;
    View root;
    private ImageView write_to;
    private SwipeRefreshLayout swipeToRefresh;
    private RelativeLayout backlay, writelay;
    private ArrayList<ConverSession> converSessionArrayList=new ArrayList<>();
    private MySession mySession;
    private String user_id = "";
    private ProgressBar progresbar;
    private Myapisession myapisession;
    private int del_item_pos;
    private ArrayList<MemberDetail> memberDetailArrayList=new ArrayList<>();
    private String craete_profile;

    public static String fromBase64(String message) {
        try {
            byte[] data = Base64.decode(message, Base64.DEFAULT);
            return new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_member_message, container, false);
        myapisession = new Myapisession(requireActivity());
        mySession = new MySession(requireActivity());
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

        idinitui1();


        if (myapisession.getKeyBusinessnumber() == null || myapisession.getKeyBusinessnumber().equalsIgnoreCase("")) {
            getBusnessNumber();
        } else {
            try {
                merchantListBeanArrayList = new ArrayList<>();
                JSONObject object = new JSONObject(myapisession.getKeyBusinessnumber());
                Log.e("Product Category >", " >" + myapisession.getKeyBusinessnumber());
                if (object.getString("status").equals("1")) {
                    MarchantBean successData = new Gson().fromJson(myapisession.getKeyBusinessnumber(), MarchantBean.class);
                    merchantListBeanArrayList.addAll(successData.getResult());
                } else {
                    getBusnessNumber();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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

        return root;
    }

    private void idinitui1() {

        craete_profile = PreferenceConnector.readString(requireActivity(),
                PreferenceConnector.Create_Profile, "");
        if (!craete_profile.equals("craete_profile")) {
            //   dialogSts.dismiss();
        }
    }

    private void clickevent() {
        write_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity(), NewMemberMessageActivity.class);
                startActivity(i);
            }
        });
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetChatList().execute();
    }

    private void idinti() {

        progresbar = root.findViewById(R.id.progresbar);
        backlay = root.findViewById(R.id.backlay);
        mychat = root.findViewById(R.id.mychat);
        write_to = root.findViewById(R.id.write_to);
        swipeToRefresh = root.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetChatList().execute();

            }
        });

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        mychat.setLayoutManager(horizontalLayoutManagaer);

    }

    private void getBusnessNumber() {
        Log.e("loginCall >", " > FIRST");
        progresbar.setVisibility(View.VISIBLE);
        merchantListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantBusNum(mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Get Business Number >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyBusinessnumber(responseData);
                            MarchantBean successData = new Gson().fromJson(responseData, MarchantBean.class);
                            merchantListBeanArrayList.addAll(successData.getResult());
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

    private void getUsername() {
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
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }

    public class MessageRecycladp extends RecyclerView.Adapter<MessageRecycladp.MyViewHolder> {
        Context context;
        ArrayList<ConverSession> converSessionArrayList=new ArrayList<>();

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
        }

        @Override
        public void onBindViewHolder(final MessageRecycladp.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (converSessionArrayList.get(position).getFullname() == null || converSessionArrayList.get(position).getFullname().equalsIgnoreCase("")) {
                holder.name.setText("@" + converSessionArrayList.get(position).getSendername());
            } else {
                holder.fullname.setText("" + converSessionArrayList.get(position).getFullname());
                holder.name.setText("@" + converSessionArrayList.get(position).getSendername());

            }

            holder.lastmaessage_tv.setText("" + converSessionArrayList.get(position).getMessage());
            holder.datetiem.setText("" + converSessionArrayList.get(position).getDatetime());
            if (converSessionArrayList.get(position).getNo_of_message() != null && !converSessionArrayList.get(position).getNo_of_message().equalsIgnoreCase("") && !converSessionArrayList.get(position).getNo_of_message().equalsIgnoreCase("0")) {
                holder.reqcount.setVisibility(View.VISIBLE);
                holder.reqcount.setText("" + converSessionArrayList.get(position).getNo_of_message());
            } else {
                holder.reqcount.setVisibility(View.GONE);
            }
            String imagelist = converSessionArrayList.get(position).getSenderimg();
            if (imagelist.equalsIgnoreCase("") || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl) || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(requireActivity())
                        .load(BaseUrl.image_baseurl + imagelist)
                        .thumbnail(0.5f)
                        .override(200, 200)
                        .centerCrop()
                        //  .placeholder(R.drawable.profile_ic)

                        //.dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(holder.propic);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(requireActivity(), MemberChatAct.class);
                    Log.e("TAG", "onClick:  getSenderid" + converSessionArrayList.get(position).getSenderid());
                    Log.e("TAG", "onClick:  getReciverid " + converSessionArrayList.get(position).getReciverid());

                    i.putExtra("receiver_id", converSessionArrayList.get(position).getSenderid());
                    i.putExtra("type", "Member");
                    i.putExtra("receiver_type", converSessionArrayList.get(position).getReceiver_type());
                    if (converSessionArrayList.get(position).getReceiver_type().equalsIgnoreCase("Merchant")) {
                        i.putExtra("receiver_name", converSessionArrayList.get(position).getSendername());
                        i.putExtra("receiver_fullname", converSessionArrayList.get(position).getFullname());

                    } else {
                        i.putExtra("receiver_name", converSessionArrayList.get(position).getSendername());
                        i.putExtra("receiver_fullname", converSessionArrayList.get(position).getFullname());

                    }
                    i.putExtra("receiver_img", BaseUrl.image_baseurl + converSessionArrayList.get(position).getSenderimg());
                    startActivity(i);
                }
            });
            holder.deletecon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    del_item_pos = position;
                    new DeleteChat().execute(converSessionArrayList.get(position).getSenderid());
                }
            });
        }

        @Override
        public int getItemCount() {
            //return 6;
            return converSessionArrayList == null ? 0 : converSessionArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView name;
            private final TextView fullname;
            private final TextView lastmaessage_tv;
            private final TextView datetiem;
            private final TextView reqcount;
            private final CircleImageView propic;
            private final ImageView deletecon;
            public RelativeLayout backlay;

            public MyViewHolder(View view) {
                super(view);
                propic = itemView.findViewById(R.id.propic);
                reqcount = itemView.findViewById(R.id.reqcount);
                name = itemView.findViewById(R.id.name);
                fullname = itemView.findViewById(R.id.fullname);
                lastmaessage_tv = itemView.findViewById(R.id.lastmaessage_tv);
                datetiem = itemView.findViewById(R.id.datetiem);
                deletecon = itemView.findViewById(R.id.deletecon);
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
                            messageRecycladp = new MessageRecycladp(requireActivity(), converSessionArrayList);
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

    private class GetChatList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            converSessionArrayList = new ArrayList<>();
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
//https://international.myngrewards.com/demo/wp-content/plugins/webservice/get_conversation.php?receiver_id=927
            //https://international.myngrewards.com/demo/wp-content/plugins/webservice/get_conversation_test.php?user_id=382&type=Merchant
            try {
                String postReceiverUrl = BaseUrl.baseurl + "get_conversation.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("postReceiverUrl >>", " .." + postReceiverUrl + "user_id=" + user_id + "&type=Member");
                params.put("user_id", user_id);
                params.put("type", "Member");

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
                            ConverSession conversession = new ConverSession();
                            conversession.setChat_id(jsonObject2.getString("chat_id"));
                            conversession.setNo_of_message(jsonObject2.getString("no_of_message"));

                            if (jsonObject2.getString("type").equalsIgnoreCase("Merchant")) {
                                conversession.setSenderid(jsonObject2.getString("id"));
                                conversession.setSenderimg(jsonObject2.getString("merchant_image"));
                                conversession.setSendername(jsonObject2.getString("business_no"));
                                conversession.setFullname(jsonObject2.getString("business_name"));
                            } else {

                                if (jsonObject2.getString("receiver_type").equalsIgnoreCase("Member")) {
                                    conversession.setSenderid(jsonObject2.getString("tid"));
                                    conversession.setSenderimg(jsonObject2.getString("member_image"));
                                    conversession.setSendername(jsonObject2.getString("username"));
                                    conversession.setFullname(jsonObject2.getString("fullname"));
                                } else {
                                    conversession.setSenderid(jsonObject2.getString("id"));
                                    conversession.setSenderimg(jsonObject2.getString("merchant_image"));
                                    if (jsonObject2.has("username")) {
                                        conversession.setSendername(jsonObject2.getString("username"));

                                    } else {
                                        if (jsonObject2.has("business_no")) {
                                            conversession.setSendername(jsonObject2.getString("business_no"));

                                        } else {
                                            conversession.setSendername(jsonObject2.getString("business_name"));

                                        }
                                    }
                                    conversession.setFullname(jsonObject2.getString("business_name"));
                                }
                            }

                            conversession.setReceiver_type(jsonObject2.getString("receiver_type"));

                            if (!(jsonObject2.getString("last_message") == null) || (!jsonObject2.getString("last_message").equalsIgnoreCase(""))) {
                                conversession.setMessage(jsonObject2.getString("last_message"));

                            } else {
                                conversession.setMessage("" + fromBase64(jsonObject2.getString("last_message")));
                            }

                            conversession.setDatetime(jsonObject2.getString("date_time"));

                            Date date1 = null;
                            try {
                                if (jsonObject2.getString("date_time") != null && !jsonObject2.getString("date_time").equalsIgnoreCase("")) {
                                    date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(jsonObject2.getString("date_time").trim());
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm aa", Locale.ENGLISH);
                                    String strDate = formatter.format(date1);
                                    conversession.setDatetime(strDate);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            converSessionArrayList.add(conversession);
                            Log.e("TAG", "converSessionArrayListconverSessionArrayList:---     " + conversession);
                        }

                    }
                    if (converSessionArrayList != null || !converSessionArrayList.isEmpty()) {
                        messageRecycladp = new MessageRecycladp(requireActivity(), converSessionArrayList);
                        mychat.setAdapter(messageRecycladp);
                        messageRecycladp.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
