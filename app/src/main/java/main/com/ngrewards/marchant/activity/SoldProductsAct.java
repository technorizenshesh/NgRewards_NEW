package main.com.ngrewards.marchant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.SaledItemDetailAct;
import main.com.ngrewards.beanclasses.MerchantItemList;
import main.com.ngrewards.beanclasses.SoldItem;
import main.com.ngrewards.beanclasses.SoldItemList;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoldProductsAct extends AppCompatActivity {
    public static MerchantItemList product_item_detail;
    private RelativeLayout backlay, addoffer;
    private RecyclerView myofferslist;
    private MyActiveProAdp myActiveProAdp;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "";
    private SwipeRefreshLayout swipeToRefresh;
    private int current_offer_pos;
    private ArrayList<SoldItemList> soldItemListArrayList;

    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_products);

        mySession = new MySession(SoldProductsAct.this);
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
        idinit();
        clickevent();
        Log.e("activeproduct size>", " >" + soldItemListArrayList);
        myActiveProAdp = new MyActiveProAdp(soldItemListArrayList);
        myofferslist.setAdapter(myActiveProAdp);
        myActiveProAdp.notifyDataSetChanged();

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
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        myofferslist = findViewById(R.id.myofferslist);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(SoldProductsAct.this, LinearLayoutManager.VERTICAL, false);
        myofferslist.setLayoutManager(horizontalLayoutManagaer);

        addoffer = findViewById(R.id.addoffer);
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

        Log.e("user_id>>>", user_id);

        swipeToRefresh.setRefreshing(true);
        //progresbar.setVisibility(View.VISIBLE);
        soldItemListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getSoldProductList(user_id);
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
                            SoldItem successData = new Gson().fromJson(responseData, SoldItem.class);
                            soldItemListArrayList.addAll(successData.getResult());

                        }
                        myActiveProAdp = new MyActiveProAdp(soldItemListArrayList);
                        myofferslist.setAdapter(myActiveProAdp);
                        myActiveProAdp.notifyDataSetChanged();

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

    private void deleteOffer(final int listPosition, String id) {
        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().deleteProduct(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Offer Products >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            Toast.makeText(SoldProductsAct.this, getResources().getString(R.string.offerdeleted), Toast.LENGTH_LONG).show();
                            if (soldItemListArrayList != null && !soldItemListArrayList.isEmpty()) {
                                Log.e("My Offer Remove >", " >" + current_offer_pos);
                                soldItemListArrayList.remove(current_offer_pos);
                                myActiveProAdp = new MyActiveProAdp(soldItemListArrayList);
                                myofferslist.setAdapter(myActiveProAdp);
                                myActiveProAdp.notifyDataSetChanged();
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

                Log.e("TAG", t.toString());
            }
        });
    }

    private void hidepublishOffer(String status, String id) {
        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().hideProduct(id, status);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Offer Pub Hide >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            soldItemListArrayList.remove(current_offer_pos);
                            myActiveProAdp = new MyActiveProAdp(soldItemListArrayList);
                            myofferslist.setAdapter(myActiveProAdp);
                            myActiveProAdp.notifyDataSetChanged();
                            ActivityCompat.invalidateOptionsMenu(SoldProductsAct.this);
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

    class MyActiveProAdp extends RecyclerView.Adapter<MyActiveProAdp.MyViewHolder> {
        ArrayList<SoldItemList> soldItemListArrayList;

        public MyActiveProAdp(ArrayList<SoldItemList> soldItemListArrayList) {
            this.soldItemListArrayList = soldItemListArrayList;
        }

        @Override
        public MyActiveProAdp.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_my_solditem, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyActiveProAdp.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.offer_title.setText("" + soldItemListArrayList.get(position).getProductName());
            holder.offer_desc.setText("" + soldItemListArrayList.get(position).getProductDescription());

            holder.member_name.setText(soldItemListArrayList.get(position).getMemberDetail().get(0).getAffiliateName());
            holder.price_discount.setText(mySession.getValueOf(MySession.CurrencySign) + soldItemListArrayList.get(position).getPrice());
            holder.order_date_time.setText(getString(R.string.order_date) + soldItemListArrayList.get(position).getOrderDate());

            holder.option.setVisibility(View.GONE);

            String product_img = soldItemListArrayList.get(position).getThumbnailImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(SoldProductsAct.this).load(product_img).placeholder(R.drawable.placeholder).into(holder.offer_img);


            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SoldProductsAct.this, SaledItemDetailAct.class);
                  /*  i.putExtra("split_invoice", "" + soldItemListArrayList.get(position).getSplit_invoice());
                    i.putExtra("split_date", "" + soldItemListArrayList.get(position).getSplit_date());
                    i.putExtra("payment_made_by_emi", "" + soldItemListArrayList.get(position).getPayment_made_by_emi());
                    i.putExtra("split_payment", "" + soldItemListArrayList.get(position).getSplit_payment());
                    i.putExtra("split_amount", "" + soldItemListArrayList.get(position).getSplit_amount());
*/
                    i.putExtra("product_name", soldItemListArrayList.get(position).getProductName());
                    i.putExtra("size", soldItemListArrayList.get(position).getSize());
                    i.putExtra("color", soldItemListArrayList.get(position).getColor());
                    i.putExtra("member_name", soldItemListArrayList.get(position).getMemberDetail().get(0).getAffiliateName());
                    i.putExtra("member_contact_name", soldItemListArrayList.get(position).getMemberDetail().get(0).getFullname());
                    i.putExtra("member_img_str", soldItemListArrayList.get(position).getMemberDetail().get(0).getMemberImage());
                    i.putExtra("mainprice", "" + soldItemListArrayList.get(position).getTotalProductPrice());
                    i.putExtra("order_id", "" + soldItemListArrayList.get(position).getOrderId());
                    i.putExtra("saledate", soldItemListArrayList.get(position).getOrderDate());
                    i.putExtra("order_date", soldItemListArrayList.get(position).getOrderDate());
                    i.putExtra("post_code", soldItemListArrayList.get(position).getShippingPostcode());
                    i.putExtra("reciept_url", soldItemListArrayList.get(position).getReciept_url());
                    i.putExtra("created_date", soldItemListArrayList.get(position).getCreatedDate());
                    i.putExtra("upspackage", soldItemListArrayList.get(position).getDeliveryDate());
                    i.putExtra("shipaddress_1", soldItemListArrayList.get(position).getShippingAddress1());
                    i.putExtra("shipaddress_2", soldItemListArrayList.get(position).getShippingAddress2());
                    i.putExtra("shipping_name", soldItemListArrayList.get(position).getShippingFirstName());
                    i.putExtra("shipping_price", soldItemListArrayList.get(position).getShipping_price());
                    i.putExtra("member_id", soldItemListArrayList.get(position).getMemberDetail().get(0).getId());
                    i.putExtra("product_id", soldItemListArrayList.get(position).getId());
                    i.putExtra("product_img_str", soldItemListArrayList.get(position).getThumbnailImage());
                    i.putExtra("quantity", "" + soldItemListArrayList.get(position).getQuantity());
                    i.putExtra("split_invoice", "" + soldItemListArrayList.get(position).getSplit_invoice());
                    i.putExtra("split_date", "" + soldItemListArrayList.get(position).getSplit_date());
                    i.putExtra("payment_made_by_emi", "" + soldItemListArrayList.get(position).getPayment_made_by_emi());
                    i.putExtra("split_payment", "" + soldItemListArrayList.get(position).getSplit_payment());
                    i.putExtra("split_amount", "" + soldItemListArrayList.get(position).getSplit_amount());
                    i.putExtra("cart_id", "" + soldItemListArrayList.get(position).getOrderId());
                    Log.e("split_payment", "getPayment_made_by_emigetPayment_made_by_emi" + soldItemListArrayList.get(position).getPayment_made_by_emi());
                    Log.e("split_payment",
                            "split_datesplit_datesplit_datesplit_datesplit_datesplit_date" + soldItemListArrayList.get(position).getSplit_date());
                    Log.e("split_payment", "getPayment_made_by_emigetPayment_made_by_emi" + soldItemListArrayList.get(position).getPayment_made_by_emi());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            //return 4;
            return soldItemListArrayList == null ? 0 : soldItemListArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView offer_title, offer_desc, price_discount, member_name, order_date_time;
            ImageView offer_img, option;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.offer_title = itemView.findViewById(R.id.offer_title);
                this.offer_desc = itemView.findViewById(R.id.offer_desc);
                this.price_discount = itemView.findViewById(R.id.price_discount);
                this.price_discount = itemView.findViewById(R.id.price_discount);
                this.offer_img = itemView.findViewById(R.id.offer_img);
                this.option = itemView.findViewById(R.id.option);
                this.member_name = itemView.findViewById(R.id.member_name);
                this.order_date_time = itemView.findViewById(R.id.order_date_time);

            }
        }
    }

}
//https://articles.braintreepayments.com/guides/payment-methods/paypal/setup-guide