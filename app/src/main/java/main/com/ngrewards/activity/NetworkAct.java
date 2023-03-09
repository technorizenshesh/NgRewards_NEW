package main.com.ngrewards.activity;

import android.annotation.SuppressLint;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.numetriclabz.numandroidcharts.BarChart;
import com.numetriclabz.numandroidcharts.ChartData;
import com.squareup.picasso.Picasso;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.beanclasses.NetworkBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkAct extends AppCompatActivity {

    private RelativeLayout backlay;
    private RecyclerView friendslist;
    private CustomFriendsAdpter customFriendsAdpter;
    private View network_view_back, view_friends;
    private LinearLayout friend_lay, network_lay, friend_list_lay, networl_graph_lay;
    private BarChart mynetworkchart;
    private Spinner select_friends_drop;
    private ArrayList<String> dropdownlist;
    private BasicCustomAdp basicCustomAdp;
    private TextView total_friends, total_spent, earn_ng_cash, nofriends;
    private SwipeRefreshLayout swipeToRefreshFriends;
    private ArrayList<MemberDetail> memberDetailArrayList;
    private MySession mySession;
    private String user_id = "", affiliate_number = "";
    private ArrayList<NetworkBean> networkBeanArrayList;
    List<ChartData> friendsvalue;
    List<ChartData> spent_this_weeklist;
    List<ChartData> spent_this_data_weeklist;
    List<ChartData> ng_cash_earned;
    List<ChartData> ng_cash_data_earned;
    private ProgressBar progresbar;
    BarChart myspentbarchart, myearningchart;
    private float friend_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_two);
        dropdownlist = new ArrayList<>();
        dropdownlist.add("Total Friends");
        dropdownlist.add("Total Spent");
        dropdownlist.add("Total Earnings");
        networkBeanArrayList = new ArrayList<>();

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
                    Log.e("affiliate_number ", " .> " + affiliate_number);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinti();
        clickevent();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        friend_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network_view_back.setVisibility(View.GONE);
                view_friends.setVisibility(View.VISIBLE);
                swipeToRefreshFriends.setVisibility(View.VISIBLE);
                friend_list_lay.setVisibility(View.VISIBLE);
                networl_graph_lay.setVisibility(View.GONE);
                if (memberDetailArrayList == null || memberDetailArrayList.isEmpty() || memberDetailArrayList.size() == 0) {
                    nofriends.setVisibility(View.VISIBLE);
                } else {
                    nofriends.setVisibility(View.GONE);
                }
            }
        });

        network_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                network_view_back.setVisibility(View.VISIBLE);
                view_friends.setVisibility(View.GONE);
                swipeToRefreshFriends.setVisibility(View.GONE);
                friend_list_lay.setVisibility(View.GONE);
                networl_graph_lay.setVisibility(View.VISIBLE);

                if (nofriends.getVisibility() == View.VISIBLE) {
                    nofriends.setVisibility(View.GONE);
                }
            }
        });
    }

    private void idinti() {
        myearningchart = findViewById(R.id.myearningchart);
        myspentbarchart = findViewById(R.id.myspentbarchart);
        progresbar = findViewById(R.id.progresbar);
        nofriends = findViewById(R.id.nofriends);
        swipeToRefreshFriends = findViewById(R.id.swipeToRefreshFriends);
        total_friends = findViewById(R.id.total_friends);
        earn_ng_cash = findViewById(R.id.earn_ng_cash);
        total_spent = findViewById(R.id.total_spent);
        mynetworkchart = findViewById(R.id.mynetworkchart);
        select_friends_drop = findViewById(R.id.select_friends_drop);
        basicCustomAdp = new BasicCustomAdp(NetworkAct.this, android.R.layout.simple_spinner_item, dropdownlist);
        select_friends_drop.setAdapter(basicCustomAdp);
        networl_graph_lay = findViewById(R.id.networl_graph_lay);
        friend_list_lay = findViewById(R.id.friend_list_lay);
        friend_lay = findViewById(R.id.friend_lay);
        network_lay = findViewById(R.id.network_lay);
        view_friends = findViewById(R.id.view_friends);
        network_view_back = findViewById(R.id.network_view_back);
        backlay = findViewById(R.id.backlay);
        friendslist = findViewById(R.id.friendslist);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(NetworkAct.this, LinearLayoutManager.VERTICAL, false);
        friendslist.setLayoutManager(horizontalLayoutManagaer);

        swipeToRefreshFriends.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyFriends();
            }
        });

        new GetMyNetworkAsc().execute();

        getMyFriends();

        select_friends_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dropdownlist != null) {
                    if (dropdownlist.get(position).equalsIgnoreCase("Total Friends")) {

                        mynetworkchart.setVisibility(View.VISIBLE);
                        myspentbarchart.setVisibility(View.GONE);
                        myearningchart.setVisibility(View.GONE);

                        if (friendsvalue != null && !friendsvalue.isEmpty()) {
                            Log.e("COME TREUE", ">> " + friendsvalue.size());
                            mynetworkchart.setData(friendsvalue);

                            synchronized (mynetworkchart) {
                                mynetworkchart.notify();
                            }
                            mynetworkchart.invalidate();

                        } else {

                            Toast.makeText(NetworkAct.this, "Loading data ", Toast.LENGTH_SHORT).show();
                            List<ChartData> value = new ArrayList<>();
                            value.add(new ChartData(0f, "Me"));
                            value.add(new ChartData(0f, "Level 1"));
                            value.add(new ChartData(0f, "Level 2"));
                            value.add(new ChartData(0f, "Level 3"));
                            value.add(new ChartData(0f, "Level 4"));
                            value.add(new ChartData(0f, "Level 5"));
                            value.add(new ChartData(0f, "Level 6"));
                            mynetworkchart.setData(value);
                        }
                    }

                    if (dropdownlist.get(position).equalsIgnoreCase("Total Spent")) {

                        mynetworkchart.setVisibility(View.GONE);
                        myspentbarchart.setVisibility(View.VISIBLE);
                        myearningchart.setVisibility(View.GONE);

                        if (spent_this_data_weeklist != null && !spent_this_data_weeklist.isEmpty()) {
                            Log.e("COME Spent", ">> " + spent_this_data_weeklist.size());
                            myspentbarchart.setData(spent_this_data_weeklist);

                            synchronized (myspentbarchart) {
                                myspentbarchart.notify();
                            }
                            myspentbarchart.invalidate();

                        } else {
                            Toast.makeText(NetworkAct.this, "Loading data ", Toast.LENGTH_SHORT).show();
                            List<ChartData> value_spent = new ArrayList<>();
                            value_spent.add(new ChartData(0f, "Me"));
                            value_spent.add(new ChartData(0f, "Level 1"));
                            value_spent.add(new ChartData(0f, "Level 2"));
                            value_spent.add(new ChartData(0f, "Level 3"));
                            value_spent.add(new ChartData(0f, "Level 4"));
                            value_spent.add(new ChartData(0f, "Level 5"));
                            value_spent.add(new ChartData(0f, "Level 6"));
                            myspentbarchart.setData(value_spent);
                        }
                    }
                    if (dropdownlist.get(position).equalsIgnoreCase("Total Earnings")) {
                        mynetworkchart.setVisibility(View.GONE);
                        myspentbarchart.setVisibility(View.GONE);
                        myearningchart.setVisibility(View.VISIBLE);

                        if (ng_cash_data_earned != null && !ng_cash_data_earned.isEmpty()) {
                            Log.e("COME Earned", ">> " + ng_cash_data_earned.size());
                            myearningchart.setData(ng_cash_data_earned);
                            synchronized (myearningchart) {
                                myearningchart.notify();
                            }
                            myearningchart.invalidate();

                        } else {

                            Toast.makeText(NetworkAct.this, "Loading data ", Toast.LENGTH_SHORT).show();

                            List<ChartData> value_earning = new ArrayList<>();
                            value_earning.add(new ChartData(0f, "Me"));
                            value_earning.add(new ChartData(0f, "Level 1"));
                            value_earning.add(new ChartData(0f, "Level 2"));
                            value_earning.add(new ChartData(0f, "Level 3"));
                            value_earning.add(new ChartData(0f, "Level 4"));
                            value_earning.add(new ChartData(0f, "Level 5"));
                            value_earning.add(new ChartData(0f, "Level 6"));
                            myearningchart.setData(value_earning);

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class BasicCustomAdp extends ArrayAdapter<String> {
        Context context;
        Activity activity;
        private ArrayList<String> carmodel;

        public BasicCustomAdp(Context context, int resourceId, ArrayList<String> carmodel) {
            super(context, resourceId);
            this.context = context;
            this.carmodel = carmodel;
        }

        private class ViewHolder {
            TextView headername;
            TextView cartype;
        }

        @Override
        public int getCount() {
            return carmodel.size();
        }

        @Override
        public String getItem(int position) {
            return carmodel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            BasicCustomAdp.ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.spn_head_wrap_lay, null);
                holder = new BasicCustomAdp.ViewHolder();
                holder.headername = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
            } else {
                holder = (BasicCustomAdp.ViewHolder) convertView.getTag();
            }
            holder.headername.setText("" + carmodel.get(position));

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            BasicCustomAdp.ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.spn_head_lay, null);
                holder = new BasicCustomAdp.ViewHolder();
                holder.cartype = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
            } else {
                holder = (BasicCustomAdp.ViewHolder) convertView.getTag();
            }

            holder.cartype.setText("" + carmodel.get(position));
            return convertView;
        }
    }

    class CustomFriendsAdpter extends RecyclerView.Adapter<CustomFriendsAdpter.MyViewHolder> {
        ArrayList<MemberDetail> memberDetailArrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView option, chating;
            CircleImageView friendimg;
            TextView member_name;

            public MyViewHolder(View itemView) {
                super(itemView);

                this.option = itemView.findViewById(R.id.option);
                this.member_name = itemView.findViewById(R.id.member_name);
                this.friendimg = itemView.findViewById(R.id.friendimg);
                this.chating = itemView.findViewById(R.id.chating);
            }
        }

        public CustomFriendsAdpter(ArrayList<MemberDetail> memberDetailArrayList) {
            this.memberDetailArrayList = memberDetailArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_friends_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            holder.member_name.setText("" + memberDetailArrayList.get(listPosition).getBusiness_name());
            String imagelist = memberDetailArrayList.get(listPosition).getMemberImage();

            if (imagelist.equalsIgnoreCase("") || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl) || imagelist.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Picasso.with(NetworkAct.this).load(imagelist).into(holder.friendimg);
            }

            if (memberDetailArrayList.get(listPosition).getUserType() != null && memberDetailArrayList.get(listPosition).getUserType().equalsIgnoreCase("Merchant")) {
                holder.option.setVisibility(View.GONE);
            } else {
                holder.option.setVisibility(View.VISIBLE);
            }

            holder.chating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (memberDetailArrayList.get(listPosition).getUserType() != null && memberDetailArrayList.get(listPosition).getUserType().equalsIgnoreCase("Merchant")) {
                        Intent i = new Intent(NetworkAct.this, MemberChatAct.class);
                        i.putExtra("receiver_id", memberDetailArrayList.get(listPosition).getId());
                        i.putExtra("type", "Member");
                        i.putExtra("receiver_type", "Merchant");
                        i.putExtra("receiver_img", memberDetailArrayList.get(listPosition).getMemberImage());
                        i.putExtra("receiver_name", memberDetailArrayList.get(listPosition).getBusiness_name());
                        i.putExtra("receiver_fullname", memberDetailArrayList.get(listPosition).getBusiness_name());
                        startActivity(i);

                    } else {
                        Intent i = new Intent(NetworkAct.this, MemberChatAct.class);
                        i.putExtra("receiver_id", memberDetailArrayList.get(listPosition).getId());
                        i.putExtra("type", "Member");
                        i.putExtra("receiver_type", "Member");
                        i.putExtra("receiver_img", memberDetailArrayList.get(listPosition).getMemberImage());
                        i.putExtra("receiver_name", memberDetailArrayList.get(listPosition).getAffiliateName());
                        i.putExtra("receiver_fullname", memberDetailArrayList.get(listPosition).getFullname());
                        startActivity(i);
                    }

                }
            });

            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.send_transfer, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch (id) {
                                case R.id.nav_transfer:
                                    Intent i = new Intent(NetworkAct.this, TransferToaFriend.class);
                                    i.putExtra("aff_name", memberDetailArrayList.get(listPosition).getAffiliateName());
                                    i.putExtra("memberfull_name", memberDetailArrayList.get(listPosition).getFullname());
                                    i.putExtra("member_id", memberDetailArrayList.get(listPosition).getId());
                                    startActivity(i);
                                    finish();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {

            return memberDetailArrayList.size();

        }
    }

    private void getMyFriends() {
        swipeToRefreshFriends.setRefreshing(true);
        memberDetailArrayList = new ArrayList<>();

        Call<ResponseBody> call = ApiClient.getApiInterface().getMyCodeUseFriend(affiliate_number, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                swipeToRefreshFriends.setRefreshing(false);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("User name list>", " >" + responseData);

                        if (object.getString("status").equals("1")) {
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);

                            if (successData.getResult() != null) {

                                memberDetailArrayList.addAll(successData.getResult());

                                if (successData.getTotalfriends() != null && !successData.getTotalfriends().equalsIgnoreCase("")) {
                                    total_friends.setText("" + successData.getTotalfriends());
                                }
                                if (successData.getTotal_spent_this_week() != null && !successData.getTotal_spent_this_week().equalsIgnoreCase("")) {
                                    total_spent.setText("$" + successData.getTotal_spent_this_week());
                                }
                                if (successData.getTotal_earning_this_week() != null && !successData.getTotal_earning_this_week().equalsIgnoreCase("")) {
                                    earn_ng_cash.setText("$" + successData.getTotal_earning_this_week());
                                }
                            }

                        } else {

                        }

                        if (memberDetailArrayList == null || memberDetailArrayList.isEmpty() || memberDetailArrayList.size() == 0) {
                            nofriends.setVisibility(View.VISIBLE);
                        } else {
                            nofriends.setVisibility(View.GONE);
                        }

                        HashMap<String, MemberDetail> hashMap = new HashMap<>();

                        for (int i = 0; i < memberDetailArrayList.size(); i++) {
                            hashMap.put(memberDetailArrayList.get(i).getId(), memberDetailArrayList.get(i));
                        }

                        for (int i = 0; i < memberDetailArrayList.size(); i++)
                            Log.e("afsafdsafdsf", "Before = " + memberDetailArrayList.get(i).getId());

                        ArrayList<MemberDetail> newmemberDetailArrayList = new ArrayList<>(hashMap.values());

                        for (int i = 0; i < newmemberDetailArrayList.size(); i++)
                            Log.e("afsafdsafdsf", "After = " + newmemberDetailArrayList.get(i).getId());

                        customFriendsAdpter = new CustomFriendsAdpter(newmemberDetailArrayList);
                        friendslist.setAdapter(customFriendsAdpter);

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
                swipeToRefreshFriends.setRefreshing(false);

                Log.e("TAG", t.toString());
            }
        });
    }

    private class GetMyNetworkAsc extends AsyncTask<String, String, String> {
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

            Log.e("affiliate_number", " >>  " + affiliate_number);

            try {
                String postReceiverUrl = BaseUrl.baseurl + "my_network_earning.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);
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
                Log.e("Get My Network", ">>>>>>>>>>>>" + response);
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

                        JSONObject json = jsonObject.getJSONObject("result");

                        Log.e("json123", "" + json);

                        if (json.getString("level_first_member_count") != null && !json.getString("level_first_member_count").equalsIgnoreCase("")) {
                            total_friends.setText("" + json.getString("level_first_member_count"));
                        }
                        if (json.getString("total_spent_this_week") != null && !json.getString("total_spent_this_week").equalsIgnoreCase("")) {
                            total_spent.setText("$" + json.getString("total_spent_this_week"));
                        }
                        if (json.getString("total_earning_this_week") != null && !json.getString("total_earning_this_week").equalsIgnoreCase("")) {
                            earn_ng_cash.setText("$" + json.getString("total_earning_this_week"));
                        }

                        JSONObject jsonObject1 = json.getJSONObject("result");

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("zero");

                        NetworkBean networkBean = new NetworkBean();
                        networkBean.setFriend_count(jsonObject2.getString("friend_count"));
                        networkBean.setNg_cash_earned(jsonObject2.getString("ng_cash_earned"));
                        networkBean.setSpent_this_week(jsonObject2.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean);

                        JSONObject jsonObject3 = jsonObject1.getJSONObject("first");

                        NetworkBean networkBean1 = new NetworkBean();
                        networkBean1.setFriend_count(jsonObject3.getString("friend_count"));
                        networkBean1.setNg_cash_earned(jsonObject3.getString("ng_cash_earned"));
                        networkBean1.setSpent_this_week(jsonObject3.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean1);

                        JSONObject jsonObject4 = jsonObject1.getJSONObject("second");
                        NetworkBean networkBean2 = new NetworkBean();
                        networkBean2.setFriend_count(jsonObject4.getString("friend_count"));
                        networkBean2.setNg_cash_earned(jsonObject4.getString("ng_cash_earned"));
                        networkBean2.setSpent_this_week(jsonObject4.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean2);

                        JSONObject jsonObject5 = jsonObject1.getJSONObject("third");
                        NetworkBean networkBean3 = new NetworkBean();
                        networkBean3.setFriend_count(jsonObject5.getString("friend_count"));
                        networkBean3.setNg_cash_earned(jsonObject5.getString("ng_cash_earned"));
                        networkBean3.setSpent_this_week(jsonObject5.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean3);

                        JSONObject jsonObject6 = jsonObject1.getJSONObject("fourth");
                        NetworkBean networkBean4 = new NetworkBean();
                        networkBean4.setFriend_count(jsonObject6.getString("friend_count"));
                        networkBean4.setNg_cash_earned(jsonObject6.getString("ng_cash_earned"));
                        networkBean4.setSpent_this_week(jsonObject6.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean4);

                        JSONObject jsonObject7 = jsonObject1.getJSONObject("fifth");
                        NetworkBean networkBean5 = new NetworkBean();
                        networkBean5.setFriend_count(jsonObject7.getString("friend_count"));
                        networkBean5.setNg_cash_earned(jsonObject7.getString("ng_cash_earned"));
                        networkBean5.setSpent_this_week(jsonObject7.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean5);

                        JSONObject jsonObject8 = jsonObject1.getJSONObject("sixth");
                        NetworkBean networkBean6 = new NetworkBean();
                        networkBean6.setFriend_count(jsonObject8.getString("friend_count"));
                        networkBean6.setNg_cash_earned(jsonObject8.getString("ng_cash_earned"));
                        networkBean6.setSpent_this_week(jsonObject8.getString("spent_this_week"));
                        networkBeanArrayList.add(networkBean6);

                        friendsvalue = new ArrayList<>();
                        Log.e("sizee=====", networkBeanArrayList.size() + "");

                        for (int i = 0; i < networkBeanArrayList.size(); i++) {
                            int level = i;

                            try {
                                friend_count = Float.parseFloat(networkBeanArrayList.get(i).getFriend_count());
                                int friend_count_int = Integer.parseInt(networkBeanArrayList.get(i).getFriend_count());

                            } catch (Exception re) {
                                re.printStackTrace();
                            }

                            if (i == 0) {
                                friendsvalue.add(new ChartData(friend_count, "Me"));
                            } else {
                                friendsvalue.add(new ChartData(friend_count, "Level " + level));
                            }
                        }

                        spent_this_weeklist = new ArrayList<>();
                        spent_this_data_weeklist = new ArrayList<>();

                        for (int i = 0; i < networkBeanArrayList.size(); i++) {
                            int level = i;
                            float spent_intspent = Float.parseFloat(networkBeanArrayList.get(i).getSpent_this_week());
                            float spent_int = Float.parseFloat(networkBeanArrayList.get(i).getSpent_this_week());
                            if (i == 0) {
                                spent_this_weeklist.add(new ChartData("Me", spent_int));
                                spent_this_data_weeklist.add(new ChartData(spent_int, "Me"));
                            } else {
                                spent_this_weeklist.add(new ChartData("Level " + level, spent_int));
                                spent_this_data_weeklist.add(new ChartData(spent_int, "Level " + level));

                            }
                        }

                        ng_cash_earned = new ArrayList<>();
                        ng_cash_data_earned = new ArrayList<>();
                        for (int i = 0; i < networkBeanArrayList.size(); i++) {
                            int level = i;
                            float getNg_cash_earned = Float.parseFloat(networkBeanArrayList.get(i).getNg_cash_earned());
                            float getNg_cash_earned_int = Float.parseFloat(networkBeanArrayList.get(i).getNg_cash_earned());
                            if (i == 0) {
                                ng_cash_data_earned.add(new ChartData(getNg_cash_earned_int, "Me"));
                            } else {
                                ng_cash_data_earned.add(new ChartData(getNg_cash_earned_int, "Level " + level));
                            }
                        }

                        if (friendsvalue != null) {
                            Log.e("COME TREUE", ">> " + friendsvalue.size());
                            mynetworkchart.setData(friendsvalue);
                            synchronized (mynetworkchart) {
                                mynetworkchart.notify();
                            }
                            mynetworkchart.invalidate();
                        }

                    } else {
                        total_friends.setText("0");
                        total_spent.setText("$0.00");
                        earn_ng_cash.setText("$0.00");
                    }
                    mynetworkchart.setData(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}