package main.com.ngrewards.marchant.merchantbottum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Locale;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.SaledItemDetailAct;
import main.com.ngrewards.beanclasses.GalleryBean;
import main.com.ngrewards.beanclasses.MerOrderBean;
import main.com.ngrewards.beanclasses.OrderMerchantAct;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.draweractivity.MerchantBaseActivity;
import main.com.ngrewards.placeorderclasses.MerchantReceiptActivity;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerHomeActivity extends MerchantBaseActivity {
    public static ArrayList<GalleryBean> ImagePathArrayList=new ArrayList<>();
    FrameLayout contentFrameLayout;
    ActivityRecAdp activityRecAdp;
    ProgressBar progresbar;
    private RecyclerView activity_list;
    private SwipeRefreshLayout swipeToRefresh;
    private ArrayList<MerOrderBean> merOrderBeanArrayList=new ArrayList<>();
    private MySession mySession;
    private String user_id = "";
    private TextView notransmade;
    private EditText search_et_home;
    private String dfghjbvdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        contentFrameLayout = findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_mer_home, contentFrameLayout);
        progresbar = findViewById(R.id.progresbar);
        ImagePathArrayList = new ArrayList<>();

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
        reqcounft.setVisibility(View.GONE);
        new GetProfile().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getMerOrderActivity();
            new GetProfile().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickevent() {

    }

    private void idinits() {

        notransmade = findViewById(R.id.notransmade);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMerOrderActivity();
            }
        });


        activity_list = findViewById(R.id.activity_list);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search_et_home = findViewById(R.id.search_et_home);

        search_et_home.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    activityRecAdp.filter(s.toString());

                } catch (Exception e) {
                    Log.e("TAG", "onResponse: " + e.getLocalizedMessage());
                    Log.e("TAG", "onResponse: " + e.getMessage());
                    Log.e("TAG", "onResponse: " + e.getCause());

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MerHomeActivity.this, LinearLayoutManager.VERTICAL, false);
        activity_list.setLayoutManager(horizontalLayoutManagaer);
    }

    private void getMerOrderActivity() {
        Log.e("user_idd", user_id);
        merOrderBeanArrayList = new ArrayList<>();
        swipeToRefresh.setRefreshing(true);
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantOrder(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    swipeToRefresh.setRefreshing(false);
                    try {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {

                            OrderMerchantAct successData = new Gson().fromJson(responseData, OrderMerchantAct.class);
                            merOrderBeanArrayList.addAll(successData.getResult());

                        }
                        Log.e("responseData >> ", " >> " + responseData);

                        activityRecAdp = new ActivityRecAdp(MerHomeActivity.this, merOrderBeanArrayList);
                        activity_list.setAdapter(activityRecAdp);
                        activityRecAdp.notifyDataSetChanged();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", "onResponse: " + e.getLocalizedMessage());
                        Log.e("TAG", "onResponse: " + e.getMessage());
                        Log.e("TAG", "onResponse: " + e.getCause());
                    }
                } else {
                    swipeToRefresh.setRefreshing(false);
                }

                if (merOrderBeanArrayList == null || merOrderBeanArrayList.isEmpty()) {
                    notransmade.setVisibility(View.VISIBLE);
                } else {
                    notransmade.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (merOrderBeanArrayList == null || merOrderBeanArrayList.isEmpty()) {
                    notransmade.setVisibility(View.VISIBLE);
                } else {
                    notransmade.setVisibility(View.GONE);
                }

                Log.e("TAG", t.toString());
            }
        });
    }

    private class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            ImagePathArrayList = new ArrayList<>();

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "merchant_profile.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("merchant_id", user_id);
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
                Log.e("  MER HOME GetProfile Response", ">>>>>>>>>>>>" + response);

                return response;

            } catch (Exception e1) {

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
                    String message = jsonObject.getString("status");

                    reqcounft.setVisibility(View.GONE);

                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                        String unseen_count = jsonObject1.getString("unseen_count");

                        if (unseen_count.equals("0")) {
                            reqcounft.setVisibility(View.GONE);

                        } else {
                            reqcounft.setVisibility(View.VISIBLE);
                            reqcounft.setText("" + unseen_count);
                        }

                        Log.e("unseen_count>>>", unseen_count);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class ActivityRecAdp extends RecyclerView.Adapter<ActivityRecAdp.MyViewHolder> {
        Context context;
        ArrayList<MerOrderBean> orderBeanArrayList  =new ArrayList<>();
        ArrayList<MerOrderBean> searchmerchantListBeanArrayList=new ArrayList<>();;
        MySession mySessione;

        public ActivityRecAdp(Activity myContacts, ArrayList<MerOrderBean> orderBeanArrayList) {
            this.context = myContacts;
            this.orderBeanArrayList = orderBeanArrayList;
            this.searchmerchantListBeanArrayList = new ArrayList<>();
            searchmerchantListBeanArrayList.addAll(orderBeanArrayList);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_activity_item_lay, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);
            return holder;

        }

        public void filter(String charText) {

            if (charText != null) {

                orderBeanArrayList.clear();

                if (charText.isEmpty()) {

                    orderBeanArrayList.addAll(searchmerchantListBeanArrayList);

                } else {

                    charText = charText.toLowerCase(Locale.getDefault());

                    for (MerOrderBean wp : searchmerchantListBeanArrayList) {

                        try {

                            if (wp.getSearch_id().toLowerCase().startsWith(charText) ||
                                    wp.getB_name().toLowerCase().startsWith(charText) ||
                                    wp.getTotal_amount().toLowerCase().startsWith(charText) ||
                                    wp.getCreated_date().toLowerCase().startsWith(charText)) {
                                orderBeanArrayList.add(wp);

                            }

                        } catch (Exception e3) {

                            e3.printStackTrace();
                        }

                        notifyDataSetChanged();
                    }

                }

                notifyDataSetChanged();
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            mySessione = new MySession(context);
            try {
                Log.e("TAG", "onBindViewHolder: " + orderBeanArrayList.get(position).getType());
                if (orderBeanArrayList.get(position).getType() != null &&
                        orderBeanArrayList.get(position).getType().equalsIgnoreCase("Paybill")) {
                    holder.total_order_price.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getTotal_amount());
                    holder.order_id.setText(orderBeanArrayList.get(position).getSearch_id());
                    holder.order_category.setText("" + orderBeanArrayList.get(position).getType());
                    String mytime = orderBeanArrayList.get(position).getCreated_date();
                    holder.date_tv.setText("" + mytime);

               /* try {
                    Date myDate = Calendar.getInstance().getTime();

                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd , MMM , yyyy hh:mm a");
                    String finalDate = timeFormat.format(myDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
*/
                    holder.paidamount_bycard.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getPaid_by_card());
                    if (orderBeanArrayList.get(position).getNgcash() == null ||
                            orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0") ||
                            orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("")) {
                        holder.ngcash.setText(mySessione.getValueOf(MySession.CurrencySign) + "0.00");

                    } else {
                        holder.ngcash.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                    }

                    holder.merchant_member_name.setText("" + orderBeanArrayList.get(position).getB_name());
                    if (orderBeanArrayList != null) {
                        if (orderBeanArrayList.get(position).getMemberDetail() != null) {
                            holder.merchant_member_name.setText("" + orderBeanArrayList.get(position).getB_name());
                        }

                    }

                } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType()
                        .equalsIgnoreCase("Item")) {

                    holder.total_order_price.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getTotal_price_with_shipping());
                    holder.order_id.setText(orderBeanArrayList.get(position).getSearch_id());
                    holder.order_category.setText("" + orderBeanArrayList.get(position).getType());
                    holder.total_order_price.setTextColor(getResources().getColor(R.color.black));

                    // String mytime = orderBeanArrayList.get(position).getOrderDate2() + " " + orderBeanArrayList.get(position).getOrder_Time();
                    holder.date_tv.setText("" + orderBeanArrayList.get(position).getCreated_date());
                    holder.paidamount_bycard.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getPaid_by_card());
                    if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0")) {
                        holder.ngcash.setText(mySessione.getValueOf(MySession.CurrencySign) + "0.00");
                    } else {
                        holder.ngcash.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                    }
                    String mername = orderBeanArrayList.get(position).getB_name();
                    holder.merchant_member_name.setText("" + mername);

                    if (orderBeanArrayList.get(position).getMemberDetail() != null) {
                        try {
                            String mername1 = orderBeanArrayList.get(position).getB_name();
                            holder.merchant_member_name.setText("" + mername1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else {

                    holder.total_order_price.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getTotal_amount());
                    holder.order_id.setText(orderBeanArrayList.get(position).getSearch_id());
                    holder.order_category.setText("" + orderBeanArrayList.get(position).getType());
                    holder.total_order_price.setTextColor(getResources().getColor(R.color.black));
                    String mytime = orderBeanArrayList.get(position).getOrderDate2() + " " + orderBeanArrayList.get(position).getOrder_Time();
                    holder.date_tv.setText("" + mytime);
                    holder.paidamount_bycard.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getPaid_by_card());
                    if (orderBeanArrayList.get(position).getNgcash() == null || orderBeanArrayList.get(position).getNgcash().equalsIgnoreCase("0")) {
                        holder.ngcash.setText(mySessione.getValueOf(MySession.CurrencySign) + "0.00");
                    } else {
                        holder.ngcash.setText(mySessione.getValueOf(MySession.CurrencySign) + orderBeanArrayList.get(position).getNgcash());
                    }
                    String mername = orderBeanArrayList.get(position).getB_name();
                    holder.merchant_member_name.setText("" + mername);

                    if (orderBeanArrayList.get(position).getMerchantDetail() != null) {
                        try {
                            String mername1 = orderBeanArrayList.get(position).getB_name();
                            holder.merchant_member_name.setText("" + mername1);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
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
                holder.itemView.setOnClickListener(v -> {
                    if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Paybill")) {

                        dfghjbvdf = orderBeanArrayList.get(position).getOrder_cart_id();
                        Intent i = new Intent(MerHomeActivity.this, MerchantReceiptActivity.class);
                        i.putExtra("member_user_name", orderBeanArrayList.get(position).getMemberDetail().get(0).getAffiliateName());
                        i.putExtra("member_id", orderBeanArrayList.get(position).getMemberDetail().get(0).getId());
                        i.putExtra("member_fullname_number", orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                        i.putExtra("member_img_str", orderBeanArrayList.get(position).getMemberDetail().get(0).getMemberImage());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getId());
                        i.putExtra("cardnumber_tv", "" + orderBeanArrayList.get(position).getCardNumber());
                        i.putExtra("cardbrand", "" + orderBeanArrayList.get(position).getCardBrand());
                        i.putExtra("total_amt_tv_str", "" + orderBeanArrayList.get(position).getTotal_amount());
                        i.putExtra("due_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("ngcash_str", "" + orderBeanArrayList.get(position).getNgcash());
                        i.putExtra("tip_str", "" + orderBeanArrayList.get(position).getTip_amount());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployee_name());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_date", "" + orderBeanArrayList.get(position).getCreated_date());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        startActivity(i);

                    } else if (orderBeanArrayList.get(position).getType() != null && orderBeanArrayList.get(position).getType().equalsIgnoreCase("Order")) {

                        Intent i = new Intent(MerHomeActivity.this, MerchantReceiptActivity.class);
                        if (orderBeanArrayList.get(position).getMemberDetail() != null) {
                            i.putExtra("member_user_name", orderBeanArrayList.get(position).getMemberDetail().get(0).getUsername());
                            i.putExtra("member_id", orderBeanArrayList.get(position).getMemberDetail().get(0).getId());
                            i.putExtra("member_fullname_number", orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                            i.putExtra("member_img_str", orderBeanArrayList.get(position).getMemberDetail().get(0).getMemberImage());
                        }
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getId());
                        i.putExtra("cardnumber_tv",
                                "" + orderBeanArrayList.get(position).getCardNumber());
                        i.putExtra("cardbrand", "" + orderBeanArrayList.get(position).getCardBrand());
                        i.putExtra("total_amt_tv_str", "" + orderBeanArrayList.get(position).getTotal_amount());
                        i.putExtra("due_amt_tv_str", "" + orderBeanArrayList.get(position).getAmount());
                        i.putExtra("ngcash_str", "" + orderBeanArrayList.get(position).getNgcash());
                        i.putExtra("tip_str", "" + orderBeanArrayList.get(position).getTip_amount());
                        i.putExtra("mdate", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("time", "" + orderBeanArrayList.get(position).getOrder_Time());
                        i.putExtra("Order_guset_No", "" + orderBeanArrayList.get(position).getOrder_guset_No());
                        i.putExtra("Order_Table_No", "" + orderBeanArrayList.get(position).getOrder_Table_No());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployee_name());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_date", "" + orderBeanArrayList.get(position).getOrderDate2());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        i.putExtra("split",
                                "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        startActivity(i);

                    } else {

                        Log.e("orderBeanArrayList>>>>",
                                "heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + orderBeanArrayList);

                        Intent i = new Intent(MerHomeActivity.this,
                                SaledItemDetailAct.class);
                        i.putExtra("product_name", "" + orderBeanArrayList.get(position).getProductName());
                        i.putExtra("size", "" + orderBeanArrayList.get(position).getSize());
                        i.putExtra("color", "" + orderBeanArrayList.get(position).getColor());

                        try {
                            i.putExtra("member_name", "" + orderBeanArrayList.get(position).getMemberDetail().get(0).getAffiliateName());
                            i.putExtra("member_contact_name", "" + orderBeanArrayList.get(position).getMemberDetail().get(0).getFullname());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        i.putExtra("member_img_str", "" + orderBeanArrayList.get(position).getMemberDetail().get(0).getMemberImage());
                        i.putExtra("mainprice", "" + orderBeanArrayList.get(position).getTotalProductPrice());
                        i.putExtra("order_id", "" + orderBeanArrayList.get(position).getOrderId());
                        i.putExtra("upspackage", "" + orderBeanArrayList.get(position).getDeliveryDate());
                        i.putExtra("shipaddress_1", "" + orderBeanArrayList.get(position).getShippingAddress1());
                        i.putExtra("shipaddress_2", "" + orderBeanArrayList.get(position).getShippingAddress2());
                        i.putExtra("shipping_name", "" + orderBeanArrayList.get(position).getShippingFirstName());
                        i.putExtra("shipping_price", "" + orderBeanArrayList.get(position).getShipping_price());
                        i.putExtra("member_id", "" + orderBeanArrayList.get(position).getMemberDetail().get(0).getId());
                        i.putExtra("product_id", "" + orderBeanArrayList.get(position).getOrderId());
                        i.putExtra("product_img_str", "" + orderBeanArrayList.get(position).getThumbnailImage());
                        i.putExtra("quantity", "" + orderBeanArrayList.get(position).getQuantity());
                        i.putExtra("order_special", "" + orderBeanArrayList.get(position).getOrder_special_request());
                        i.putExtra("employee_name", "" + orderBeanArrayList.get(position).getEmployee_name());
                        i.putExtra("reciept_url", "" + orderBeanArrayList.get(position).getReciept_url());
                        i.putExtra("order_date", "" + orderBeanArrayList.get(position).getCreated_date());
                        i.putExtra("order_cart_id", "" + orderBeanArrayList.get(position).getOrder_cart_id());
                        i.putExtra("split_invoice", "" + orderBeanArrayList.get(position).getSplit_invoice());
                        i.putExtra("split_date", "" + orderBeanArrayList.get(position).getSplit_date());
                        i.putExtra("payment_made_by_emi", "" + orderBeanArrayList.get(position).getPayment_made_by_emi());
                        i.putExtra("split_payment", "" + orderBeanArrayList.get(position).getSplit_payment());
                        i.putExtra("split_amount", "" + orderBeanArrayList.get(position).getSplit_amount());
                        i.putExtra("cart_id", "" + orderBeanArrayList.get(position).getCart_id());


                        startActivity(i);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
