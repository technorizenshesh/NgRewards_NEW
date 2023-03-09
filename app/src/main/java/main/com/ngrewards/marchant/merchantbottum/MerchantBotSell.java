package main.com.ngrewards.marchant.merchantbottum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.MerchantItem;
import main.com.ngrewards.beanclasses.MerchantItemList;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.activity.ActiveProductsAct;
import main.com.ngrewards.marchant.activity.SoldProductsAct;
import main.com.ngrewards.marchant.activity.StartYourListing;
import main.com.ngrewards.marchant.activity.UnsoldProductsAct;
import main.com.ngrewards.marchant.draweractivity.MerchantBaseActivity;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantBotSell extends MerchantBaseActivity {
    FrameLayout contentFrameLayout;
    private RecyclerView activity_list;
    private TextView listitem;
    SwipeRefreshLayout swipeToRefresh;
    public static ArrayList<MerchantItemList> myproductslist;
    private MySession mySession;
    private String user_id="";
    private TextView active_count_tv,sold_count_tv,unsold_count_tv,total_earning;
    private LinearLayout active_lay,unsold_lay,sold_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_merchant_bot_sell, contentFrameLayout);
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

    }

    private void clickevent() {
        listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBotSell.this, StartYourListing.class);
                startActivity(i);
            }
        });
        active_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBotSell.this, ActiveProductsAct.class);
                startActivity(i);
            }
        });
        sold_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBotSell.this, SoldProductsAct.class);
                startActivity(i);
            }
        });
        unsold_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantBotSell.this, UnsoldProductsAct.class);
                startActivity(i);
            }
        });
    }

    private void idinits() {
        total_earning = findViewById(R.id.total_earning);
        sold_lay = findViewById(R.id.sold_lay);
        active_lay = findViewById(R.id.active_lay);
        unsold_lay = findViewById(R.id.unsold_lay);
        active_count_tv = findViewById(R.id.active_count_tv);
        sold_count_tv = findViewById(R.id.sold_count_tv);
        unsold_count_tv = findViewById(R.id.unsold_count_tv);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        listitem = findViewById(R.id.listitem);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSoldItems();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getSoldItems();
    }

    private void getSoldItems() {
        swipeToRefresh.setRefreshing(true);
        //progresbar.setVisibility(View.VISIBLE);
        myproductslist = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantOwnProduct(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Sold Products >", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            MerchantItem successData = new Gson().fromJson(responseData, MerchantItem.class);
                            myproductslist.addAll(successData.getResult());
                            active_count_tv.setText(""+successData.getActiveProductCount());
                            unsold_count_tv.setText(""+successData.getUnsoldProductCount());
                            sold_count_tv.setText(""+successData.getSoldProductCount());
                           if (successData.getTotal_earning_with_shipping()==null||successData.getTotal_earning_with_shipping().equalsIgnoreCase("")||successData.getTotal_earning_with_shipping().equalsIgnoreCase("null")||successData.getTotal_earning_with_shipping().equalsIgnoreCase("0")){
                               total_earning.setText("$0.00");
                           }
                           else {
                               total_earning.setText("$"+successData.getTotal_earning_with_shipping());
                           }

                        }
                        else {
                            active_count_tv.setText("0");
                            unsold_count_tv.setText("0");
                            sold_count_tv.setText("0");
                            total_earning.setText("$0.00");

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
                //progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

}