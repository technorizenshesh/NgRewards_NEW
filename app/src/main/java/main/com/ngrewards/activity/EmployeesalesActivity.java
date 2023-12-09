package main.com.ngrewards.activity;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.OrderAct;
import main.com.ngrewards.beanclasses.OrderBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.placeorderclasses.ReceiptActivity;
import main.com.ngrewards.placeorderclasses.TransferRequestDetActivity;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeesalesActivity extends AppCompatActivity {

    FrameLayout contentFrameLayout;
    ActivityRecAdp activityRecAdp;
    private RecyclerView activity_list;
    private MySession mySession;
    private String user_id = "";
    private ArrayList<OrderBean> orderBeanArrayList;
    private SwipeRefreshLayout swipeToRefresh;
    private RelativeLayout backlay;
    private String type;
    private String mername;
    private String username_str;
    private TextView employee_tv_sales;
    private String posision_name;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_employeesales);

        employee_tv_sales = findViewById(R.id.employee_tv_sales);
        contentFrameLayout = findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_tras, contentFrameLayout);
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

        idinits();
        clickevent();

        new GetProfile().execute();

        backlay = findViewById(R.id.backlay);

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderActivity();
    }

    private void clickevent() {

    }

    private void idinits() {

        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        activity_list = findViewById(R.id.activity_list);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(EmployeesalesActivity.this, LinearLayoutManager.VERTICAL, false);
        activity_list.setLayoutManager(horizontalLayoutManagaer);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getOrderActivity();
            }
        });
    }

    private void getOrderActivity() {

        Log.e("user_idd", user_id);
        orderBeanArrayList = new ArrayList<>();
        swipeToRefresh.setRefreshing(true);
        Call<ResponseBody> call = ApiClient.getApiInterface().employee_sales(user_id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    swipeToRefresh.setRefreshing(false);

                    try {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            OrderAct successData = new Gson().fromJson(responseData, OrderAct.class);
                            orderBeanArrayList.addAll(successData.getResult());
                        }

                        Log.e("responseData >> ", " >> " + responseData);
                        activityRecAdp = new ActivityRecAdp(EmployeesalesActivity.this, orderBeanArrayList);
                        activity_list.setAdapter(activityRecAdp);
                        activityRecAdp.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    swipeToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                Log.e("TAG", t.toString());
            }
        });
    }

    private class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
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
                Log.e("GetProfile Response", ">>>>>>>>>>>>" + response);
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
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                        if (jsonObject1.getString("gender") != null) {
                            //gender_str = jsonObject1.getString("gender").trim();
                        }

                        if (jsonObject1.getString("age") != null) {
                            //age_str = jsonObject1.getString("age").trim();
                        }

                        username_str = jsonObject1.getString("affiliate_name");

                        employee_tv_sales.setText(username_str);

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public class ActivityRecAdp extends RecyclerView.Adapter<ActivityRecAdp.MyViewHolder> {

        Context context;
        ArrayList<OrderBean> orderBeanArrayList;

        public ActivityRecAdp(EmployeesalesActivity myContacts, ArrayList<OrderBean> orderBeanArrayList) {
            this.context = myContacts;
            this.orderBeanArrayList = orderBeanArrayList;
        }

        @Override
        public ActivityRecAdp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_activity_item_lay, parent, false);
            ActivityRecAdp.MyViewHolder holder = new ActivityRecAdp.MyViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ActivityRecAdp.MyViewHolder holder, final int position) {

            Log.e("orderBeanArrayListt", "" + orderBeanArrayList.get(position).getMerchantDetail());

            if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Paybill")) {
                holder.total_order_price.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getTotal_amount());
                holder.order_id.setText("PB" + orderBeanArrayList.get(position).getId());
                holder.order_category.setText("Paybill");

                holder.total_order_price.setTextColor(getResources().getColor(R.color.black));

                try {

                    String mytime = orderBeanArrayList.get(position).getCreated_date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss");
                    Date myDate = null;
                    myDate = dateFormat.parse(mytime);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm:ss");
                    String finalDate = timeFormat.format(myDate);
                    holder.date_tv.setText("" + finalDate + " " + orderBeanArrayList.get(position).getOrder_Time());
                    System.out.println(finalDate);

                } catch (Exception e) {
                    holder.date_tv.setText("" + orderBeanArrayList.get(position).getCreated_date());

                }

                holder.paidamount_bycard.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getPaid_by_card());
                if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0") || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("")) {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + "0.00");
                } else {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                }
                try {
                    holder.merchant_member_name.setText("" + orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Transfer")) {
                holder.total_order_price.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getAmount());
                if (orderBeanArrayList.get(position).getTransferRequestUserId().equalsIgnoreCase(user_id)) {
                    holder.total_order_price.setTextColor(getResources().getColor(R.color.green));

                } else {
                    holder.total_order_price.setTextColor(getResources().getColor(R.color.red));
                }

                holder.order_id.setText("TR" + orderBeanArrayList.get(position).getId());
                holder.order_category.setText("Transfer");

                try {
                    String mytime = orderBeanArrayList.get(position).getCreated_date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date myDate = null;
                    myDate = dateFormat.parse(mytime);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy");
                    String finalDate = timeFormat.format(myDate);
                    holder.date_tv.setText("" + finalDate + " " + orderBeanArrayList.get(position).getOrder_Time());

                    System.out.println(finalDate);
                } catch (Exception e) {
                    holder.date_tv.setText("" + orderBeanArrayList.get(position).getCreated_date() + " " + orderBeanArrayList.get(position).getOrder_Time());

                }

                holder.paidamount_bycard.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getAmount_by_card());
                if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0") || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("")) {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + "0.00");
                } else {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                }

                String mername = orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname();
                if (mername == null || mername.equalsIgnoreCase("")) {
                    holder.merchant_member_name.setText("" + getResources().getString(R.string.staticmerchantname));
                } else {
                    holder.merchant_member_name.setText("" + orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                }

            } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("order")) {
                holder.total_order_price.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getAmount());
                holder.order_id.setText("MO" + orderBeanArrayList.get(position).getId());
                holder.order_category.setText("order");
                holder.total_order_price.setTextColor(getResources().getColor(R.color.black));

                try {
                    String mytime = orderBeanArrayList.get(position).getCreated_date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date myDate = null;
                    myDate = dateFormat.parse(mytime);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy");
                    String finalDate = timeFormat.format(myDate);
                    holder.date_tv.setText("" + finalDate + " " + orderBeanArrayList.get(position).getOrder_Time());

                    System.out.println(finalDate);
                } catch (Exception e) {
                    holder.date_tv.setText("" + orderBeanArrayList.get(position).getCreated_date() + " " + orderBeanArrayList.get(position).getOrder_Time());

                }

                holder.paidamount_bycard.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getAmount());
                if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0") || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("")) {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + "0.00");
                } else {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                }

                try {
                    holder.merchant_member_name.setText("" + orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
  /*              holder.total_order_price.setText(mySession.getValueOf(MySession.CurrencySign)  + orderBeanArrayList.get(position).getTotal_amount());
                holder.order_id.setText("MO" + orderBeanArrayList.get(position).getId());
                holder.order_category.setText("order");

                holder.total_order_price.setTextColor(getResources().getColor(R.color.black));
                try {
                    String mytime = orderBeanArrayList.get(position).getCreated_date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date myDate = null;
                    myDate = dateFormat.parse(mytime);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                    String finalDate = timeFormat.format(myDate);
                    holder.date_tv.setText("" + finalDate);
                    System.out.println(finalDate);

                } catch (Exception e) {
                    holder.date_tv.setText("" + orderBeanArrayList.get(position).getCreated_date());

                }

                holder.paidamount_bycard.setText(mySession.getValueOf(MySession.CurrencySign)  + orderBeanArrayList.get(position).getPaid_by_card());
                if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0") || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("")) {
                    holder.ngcash.setText("$0.00");
                } else {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign)  + orderBeanArrayList.get(position).getNgcash());
                }
                holder.merchant_member_name.setText("" + orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());*/
                holder.total_order_price.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getTotal_amount());
                holder.order_id.setText("#" + orderBeanArrayList.get(position).getId());
                holder.order_category.setText(getString(R.string.order));
                holder.total_order_price.setTextColor(getResources().getColor(R.color.black));
                String mytime = orderBeanArrayList.get(position).getOrderDate2() + " " + orderBeanArrayList.get(position).getOrder_Time();
                holder.date_tv.setText("" + mytime);
                holder.paidamount_bycard.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getPaid_by_card());
                if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0")) {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + "0.00");
                } else {
                    holder.ngcash.setText(mySession.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                }

                try {
                    mername = orderBeanArrayList.get(position).getMemberDetail().get(2).getFullname();
                    holder.merchant_member_name.setText("" + mername);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            holder.img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.lesslay.setVisibility(View.VISIBLE);
                    holder.img_plus.setVisibility(View.GONE);
                    holder.img_minus.setVisibility(View.VISIBLE);
                }
            });

            holder.img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.lesslay.setVisibility(View.GONE);
                    holder.img_plus.setVisibility(View.VISIBLE);
                    holder.img_minus.setVisibility(View.GONE);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TYPE >> ", " >> " + orderBeanArrayList.get(position).getType());
                    if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Paybill")) {

                        Intent i = new Intent(EmployeesalesActivity.this, ReceiptActivity.class);
                        type = orderBeanArrayList.get(position).getType();

                        i.putExtra("merchant_name", orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                        i.putExtra("member_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessNo());
                        i.putExtra("merchant_id", orderBeanArrayList.get(position).getMerchantDetail().get(0).getId());
                        i.putExtra("merchant_number", orderBeanArrayList.get(position).getMerchant_no());
                        i.putExtra("merchant_contact_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getContactName());
                        i.putExtra("address", orderBeanArrayList.get(position).getMerchantDetail().get(0).getAddress());
                        i.putExtra("address_2", orderBeanArrayList.get(position).getMerchantDetail().get(0).getAddressTwo());
                        i.putExtra("merchant_img_str", orderBeanArrayList.get(position).getMerchantDetail().get(0).getMerchantImage());
                        i.putExtra("date_tv", orderBeanArrayList.get(position).getCreated_date());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getId());
                        i.putExtra("cardnumber_tv", "" + orderBeanArrayList.get(position).getCardNumber());
                        i.putExtra("cardbrand", "" + orderBeanArrayList.get(position).getCardBrand());
                        i.putExtra("total_amt_tv_str", "" + orderBeanArrayList.get(position).getTotal_amount());
                        i.putExtra("due_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("ngcash_str", "" + orderBeanArrayList.get(position).getNgcash());
                        i.putExtra("tip_str", "" + orderBeanArrayList.get(position).getTip_amount());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployeeName());
                        i.putExtra("mdate", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("time", "" + orderBeanArrayList.get(position).getOrder_Time());
                        i.putExtra("Order_guset_No", "" + orderBeanArrayList.get(position).getOrder_guset_No());
                        i.putExtra("Order_Table_No", "" + orderBeanArrayList.get(position).getOrder_Table_No());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        i.putExtra("type123", orderBeanArrayList.get(position).getType());
                        Toast.makeText(getApplicationContext(), orderBeanArrayList.get(position).getOrder_cart_id(), Toast.LENGTH_LONG).show();
                        startActivity(i);

                    } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Order")) {
                        Intent i = new Intent(EmployeesalesActivity.this, ReceiptActivity.class);
                        Toast.makeText(getApplicationContext(), orderBeanArrayList.get(position).getOrder_cart_id(), Toast.LENGTH_LONG).show();
                        i.putExtra("merchant_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessName());
                        i.putExtra("merchant_id", orderBeanArrayList.get(position).getMerchantDetail().get(0).getId());
                        i.putExtra("member_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessNo());
                        i.putExtra("merchant_number", orderBeanArrayList.get(position).getMerchant_no());
                        i.putExtra("merchant_contact_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getContactName());
                        i.putExtra("address", orderBeanArrayList.get(position).getMerchantDetail().get(0).getAddress());
                        i.putExtra("address_2", orderBeanArrayList.get(position).getMerchantDetail().get(0).getAddressTwo());
                        i.putExtra("merchant_img_str", orderBeanArrayList.get(position).getMerchantDetail().get(0).getMerchantImage());
                        i.putExtra("date_tv", orderBeanArrayList.get(position).getCreated_date());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getId());
                        i.putExtra("cardnumber_tv", "" + orderBeanArrayList.get(position).getCardNumber());
                        i.putExtra("cardbrand", "" + orderBeanArrayList.get(position).getCardBrand());
                        i.putExtra("total_amt_tv_str", "" + orderBeanArrayList.get(position).getTotal_amount());
                        i.putExtra("due_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("ngcash_str", "" + orderBeanArrayList.get(position).getNgcash());
                        i.putExtra("tip_str", "" + orderBeanArrayList.get(position).getTip_amount());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployeeName());
                        i.putExtra("mdate", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("time", "" + orderBeanArrayList.get(position).getOrder_Time());
                        i.putExtra("Order_guset_No", "" + orderBeanArrayList.get(position).getOrder_guset_No());
                        i.putExtra("Order_Table_No", "" + orderBeanArrayList.get(position).getOrder_Table_No());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        i.putExtra("type123", orderBeanArrayList.get(position).getType());
                        startActivity(i);

                    } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Transfer")) {
                        Intent i = new Intent(EmployeesalesActivity.this, TransferRequestDetActivity.class);
                        i.putExtra("member_user_name", orderBeanArrayList.get(position).getMemberDetail().get(0).getAffiliateName());
                        i.putExtra("member_id", orderBeanArrayList.get(position).getMemberDetail().get(0).getId());
                        i.putExtra("member_fullname_number", orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                        i.putExtra("comment", orderBeanArrayList.get(position).getComment());
                        i.putExtra("member_img_str", orderBeanArrayList.get(position).getMemberDetail().get(0).getMemberImage());
                        i.putExtra("date_tv", orderBeanArrayList.get(position).getCreated_date());
                        i.putExtra("member_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessNo());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getId());
                        i.putExtra("cardnumber_tv", "" + orderBeanArrayList.get(position).getCardNumber());
                        i.putExtra("cardbrand", "" + orderBeanArrayList.get(position).getCardBrand());
                        i.putExtra("total_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("due_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("ngcash_str", "" + orderBeanArrayList.get(position).getNgcash());
                        i.putExtra("tip_str", "" + orderBeanArrayList.get(position).getTip_amount());
                        i.putExtra("type", "" + orderBeanArrayList.get(position).getType());
                        i.putExtra("mdate", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("time", "" + orderBeanArrayList.get(position).getOrder_Time());
                        i.putExtra("Order_guset_No", "" + orderBeanArrayList.get(position).getOrder_guset_No());
                        i.putExtra("Order_Table_No", "" + orderBeanArrayList.get(position).getOrder_Table_No());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployeeName());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        startActivity(i);
                    } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Request")) {

                        Intent i = new Intent(EmployeesalesActivity.this, TransferRequestDetActivity.class);
                        i.putExtra("order_id", orderBeanArrayList.get(position).getId());
                        i.putExtra("member_user_name", orderBeanArrayList.get(position).getMemberDetail().get(0).getAffiliateName());
                        i.putExtra("member_id", orderBeanArrayList.get(position).getMemberDetail().get(0).getId());
                        i.putExtra("member_name", orderBeanArrayList.get(position).getMerchant_no());
                        i.putExtra("member_fullname_number", orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                        i.putExtra("comment", orderBeanArrayList.get(position).getComment());
                        i.putExtra("member_img_str", orderBeanArrayList.get(position).getMemberDetail().get(0).getMemberImage());
                        i.putExtra("date_tv", orderBeanArrayList.get(position).getCreated_date());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getId());
                        i.putExtra("cardnumber_tv", "" + orderBeanArrayList.get(position).getCardNumber());
                        i.putExtra("cardbrand", "" + orderBeanArrayList.get(position).getCardBrand());
                        i.putExtra("total_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("due_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("ngcash_str", "" + orderBeanArrayList.get(position).getNgcash());
                        i.putExtra("tip_str", "" + orderBeanArrayList.get(position).getTip_amount());
                        i.putExtra("type", "" + orderBeanArrayList.get(position).getType());
                        i.putExtra("mdate", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("time", "" + orderBeanArrayList.get(position).getOrder_Time());
                        i.putExtra("Order_guset_No", "" + orderBeanArrayList.get(position).getOrder_guset_No());
                        i.putExtra("Order_Table_No", "" + orderBeanArrayList.get(position).getOrder_Table_No());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        startActivity(i);
                    } else {

                        Intent i = new Intent(EmployeesalesActivity.this, PurchasedItemDetailAct.class);
                        i.putExtra("product_name", orderBeanArrayList.get(position).getProductName());
                        i.putExtra("size", orderBeanArrayList.get(position).getSize());
                        i.putExtra("color", orderBeanArrayList.get(position).getColor());
                        i.putExtra("quantity", orderBeanArrayList.get(position).getQuantity());
                        i.putExtra("member_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessNo());
                        i.putExtra("merchant_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getBusinessName());
                        i.putExtra("merchant_contact_name", orderBeanArrayList.get(position).getMerchantDetail().get(0).getContactName());
                        i.putExtra("merchant_img_str", orderBeanArrayList.get(position).getMerchantDetail().get(0).getMerchantImage());
                        i.putExtra("mainprice", orderBeanArrayList.get(position).getTotalproductprice());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getOrderId());
                        i.putExtra("saledate", orderBeanArrayList.get(position).getOrderDate());
                        i.putExtra("upspackage", orderBeanArrayList.get(position).getDeliveryDate());
                        i.putExtra("shipaddress_1", orderBeanArrayList.get(position).getShippingAddress1());
                        i.putExtra("shipaddress_2", orderBeanArrayList.get(position).getShippingAddress2());
                        i.putExtra("shipping_name", orderBeanArrayList.get(position).getShippingFirstName());
                        i.putExtra("shipping_price", orderBeanArrayList.get(position).getShipping_price());
                        i.putExtra("merchant_id", orderBeanArrayList.get(position).getMerchantDetail().get(0).getId());
                        i.putExtra("product_id", orderBeanArrayList.get(position).getId());
                        i.putExtra("product_img_str", orderBeanArrayList.get(position).getThumbnailImage());
                        i.putExtra("average_rating", orderBeanArrayList.get(position).getAverage_rating());
                        i.putExtra("review", orderBeanArrayList.get(position).getReview());
                        i.putExtra("review_status", orderBeanArrayList.get(position).getReviewstatus());
                        i.putExtra("mdate", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("time", "" + orderBeanArrayList.get(position).getOrder_Time());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployeeName());
                        i.putExtra("Order_guset_No", "" + orderBeanArrayList.get(position).getOrder_guset_No());
                        i.putExtra("Order_Table_No", "" + orderBeanArrayList.get(position).getOrder_Table_No());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderBeanArrayList == null ? 0 : orderBeanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout backlay;
            ImageView img_plus, img_minus;
            LinearLayout lesslay, click_plus_minus;
            TextView order_category, merchant_member_name, ngcash, paidamount_bycard, date_tv, order_id, total_order_price;

            public MyViewHolder(View view) {
                super(view);
                img_plus = itemView.findViewById(R.id.img_plus);
                order_category = itemView.findViewById(R.id.order_category);
                paidamount_bycard = itemView.findViewById(R.id.paidamount_bycard);
                ngcash = itemView.findViewById(R.id.ngcash);
                date_tv = itemView.findViewById(R.id.date_tv);
                order_id = itemView.findViewById(R.id.order_id);
                total_order_price = itemView.findViewById(R.id.total_order_price);
                merchant_member_name = itemView.findViewById(R.id.merchant_member_name);
                img_minus = itemView.findViewById(R.id.img_minus);
                lesslay = itemView.findViewById(R.id.lesslay);
                click_plus_minus = itemView.findViewById(R.id.click_plus_minus);

            }
        }
    }
}
