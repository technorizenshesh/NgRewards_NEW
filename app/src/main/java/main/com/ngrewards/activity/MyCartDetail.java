package main.com.ngrewards.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.CartBean;
import main.com.ngrewards.beanclasses.CartListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.placeorderclasses.CheckOutAct;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartDetail extends AppCompatActivity {

    private final boolean apickeck = false;
    SwipeRefreshLayout swipeToRefresh;
    MySession mySession;
    Myapisession myapisession;
    private RecyclerView mycartlistRecycleview;
    private String user_id = "";
    private ArrayList<CartListBean> cartListBeanArrayList;
    private MycartAdapter mycartAdapter;
    private TextView total_amount, nocartitem;
    private RelativeLayout backlay;
    private TextView checkout;
    private boolean apists = false;
    private String idd;
    private ProgressBar progresbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_my_cart_detail);
        progresbar = (ProgressBar) findViewById(R.id.progresbar);
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
                    Log.e("user_id >>", " >" + user_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyCartDetail.this, CheckOutAct.class);
                startActivity(i);
            }
        });
    }

    private void idinit() {
        checkout = findViewById(R.id.checkout);
        backlay = findViewById(R.id.backlay);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        nocartitem = findViewById(R.id.nocartitem);
        total_amount = findViewById(R.id.total_amount);
        mycartlistRecycleview = findViewById(R.id.mycartlist);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MyCartDetail.this, LinearLayoutManager.VERTICAL, false);

        mycartlistRecycleview.setLayoutManager(horizontalLayoutManagaer);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!apists) {
                    getMyCartDetail();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myapisession.getKeyCartitem() == null || myapisession.getKeyCartitem().equalsIgnoreCase("")) {
            if (!apists) {
                getMyCartDetail();
            }

        } else {
            try {
                cartListBeanArrayList = new ArrayList<>();
                String responseData = myapisession.getKeyCartitem();

                JSONObject object = new JSONObject(responseData);
                Log.e("My Cart Sess >", " >" + responseData);
                if (object.getString("status").equals("1")) {
                    CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                    cartListBeanArrayList.addAll(successData.getResult());
                    total_amount.setText(mySession.getValueOf(MySession.CurrencySign) + successData.getTotalPrice());
                }
                if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty() || cartListBeanArrayList.size() == 0) {
                    nocartitem.setVisibility(View.VISIBLE);
                    total_amount.setText(mySession.getValueOf(MySession.CurrencySign) + " 0.00");
                    mycartAdapter = new MycartAdapter(cartListBeanArrayList);
                    mycartlistRecycleview.setAdapter(mycartAdapter);
                    mycartAdapter.notifyDataSetChanged();
                } else {
                    nocartitem.setVisibility(View.GONE);
                    mycartAdapter = new MycartAdapter(cartListBeanArrayList);
                    mycartlistRecycleview.setAdapter(mycartAdapter);
                    mycartAdapter.notifyDataSetChanged();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addUpdateQty(String id) {

        swipeToRefresh.setRefreshing(true);
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().removeSinglecartItem(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progresbar.setVisibility(View.GONE);
                //  swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Remove Cart >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            if (!apickeck) {
                                getMyCartDetail();
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
                // swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getMyCartDetail() {
        apists = true;
        swipeToRefresh.setRefreshing(true);
        cartListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMyCart(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    apists = false;

                    try {

                        String responseData = response.body().string();

                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Cart Items >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyCartitem(responseData);
                            CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                            cartListBeanArrayList.addAll(successData.getResult());
                            total_amount.setText(mySession.getValueOf(MySession.CurrencySign) + successData.getTotalPrice());
                        } else {
                            myapisession.setKeyCartitem("");
                        }

                        if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty() || cartListBeanArrayList.size() == 0) {
                            nocartitem.setVisibility(View.VISIBLE);
                            total_amount.setText(mySession.getValueOf(MySession.CurrencySign) + " 0.00");
                            mycartAdapter = new MycartAdapter(cartListBeanArrayList);
                            mycartlistRecycleview.setAdapter(mycartAdapter);
                            mycartAdapter.notifyDataSetChanged();
                        } else {
                            nocartitem.setVisibility(View.GONE);
                            mycartAdapter = new MycartAdapter(cartListBeanArrayList);
                            mycartlistRecycleview.setAdapter(mycartAdapter);
                            mycartAdapter.notifyDataSetChanged();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    apists = false;
                    myapisession.setKeyCartitem("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                apists = false;
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void updateMyCartItemQuantity(String id, String quantity) {
        swipeToRefresh.setRefreshing(true);
        //progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().updatCartItem(user_id, id, quantity);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Update Cart>", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            if (!apists) {
                                getMyCartDetail();
                            }

                        }


                    } catch (Exception e) {
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

    class MycartAdapter extends RecyclerView.Adapter<MycartAdapter.MyViewHolder> {
        ArrayList<CartListBean> mycartlistxx;

        public MycartAdapter(ArrayList<CartListBean> mycartlist) {
            this.mycartlistxx = mycartlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_cart_item_lay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MycartAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            if (mycartlistxx.get(listPosition).getProductDetail() != null) {
                Log.e("TAG", "onBindViewHolder: " + mycartlistxx.get(listPosition).getProductDetail());
                holder.product_desc.setText("" + mycartlistxx.get(listPosition).getProductDetail().getProductDescription());
                holder.product_name.setText("" + mycartlistxx.get(listPosition).getProductDetail().getProductName());


                // holder.merchant_name.setText("" + mycartlistxx.get(listPosition).getUserDetails().get(0).getBusinessName());
                holder.quant_tv.setText("" + mycartlistxx.get(listPosition).getQuantity());
                holder.mainprice.setText(mySession.getValueOf(MySession.CurrencySign) + mycartlistxx.get(listPosition).getProductDetail().getProduct_cart_price());

                String image_url = mycartlistxx.get(listPosition).getProductDetail().getThumbnailImage();
                if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                    Glide.with(MyCartDetail.this).load(image_url).placeholder(R.drawable.placeholder).into(holder.product_img);
                }


                holder.plusq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mycartlistxx.get(listPosition).getQuantity() != null && !mycartlistxx.get(listPosition).getQuantity().equalsIgnoreCase("")) {
                            int total_stock_count = 0;
                            int total_count = Integer.parseInt(mycartlistxx.get(listPosition).getQuantity());
                            if (mycartlistxx.get(listPosition).getProductDetail().getStock() != null && !mycartlistxx.get(listPosition).getProductDetail().getStock().equalsIgnoreCase("")) {
                                total_stock_count = Integer.parseInt(mycartlistxx.get(listPosition).getProductDetail().getStock());

                            }
                            if (total_count < total_stock_count) {
                                int new_count = ++total_count;

                                updateMyCartItemQuantity(mycartlistxx.get(listPosition).getProductId(), "" + new_count);
                            }
                        }
                    }
                });

                holder.minusq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mycartlistxx.get(listPosition).getQuantity() != null && !mycartlistxx.get(listPosition).getQuantity().equalsIgnoreCase("")) {
                            int total_count = Integer.parseInt(mycartlistxx.get(listPosition).getQuantity());
                            if (total_count > 1) {
                                int new_count = --total_count;
                                updateMyCartItemQuantity(mycartlistxx.get(listPosition).getProductId(), "" + new_count);
                            }

                        }
                    }
                });

                holder.removecartitem123.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addUpdateQty(mycartlistxx.get(listPosition).getId());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            //return 4;
            return mycartlistxx == null ? 0 : mycartlistxx.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView product_name, mainprice, merchant_name, product_desc, quant_tv;
            ImageView product_img, removecartitem123;
            Button plusq, minusq;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.product_name = itemView.findViewById(R.id.product_name);
                this.merchant_name = itemView.findViewById(R.id.merchant_name);
                this.product_desc = itemView.findViewById(R.id.product_desc);
                this.quant_tv = itemView.findViewById(R.id.quant_tv);

                this.product_img = itemView.findViewById(R.id.product_img);
                this.mainprice = itemView.findViewById(R.id.mainprice);
                this.plusq = itemView.findViewById(R.id.plusq);
                this.minusq = itemView.findViewById(R.id.minusq);
                this.removecartitem123 = itemView.findViewById(R.id.removecartitem123);

            }
        }
    }
}
