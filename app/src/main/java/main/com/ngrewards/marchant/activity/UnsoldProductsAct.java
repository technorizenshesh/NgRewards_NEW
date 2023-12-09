package main.com.ngrewards.marchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.MerchantItemList;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.merchantbottum.MerchantBotSell;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsoldProductsAct extends AppCompatActivity {

    private RelativeLayout backlay, addoffer;
    private RecyclerView myofferslist;
    private MyActiveProAdp myActiveProAdp;
    private ProgressBar progresbar;

    private MySession mySession;
    private String user_id = "";
    private SwipeRefreshLayout swipeToRefresh;
    private int current_offer_pos;
    private ArrayList<MerchantItemList> myactiveproductslist;

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
        setContentView(R.layout.activity_unsold_products);
        myactiveproductslist = new ArrayList<>();
        if (MerchantBotSell.myproductslist != null && !MerchantBotSell.myproductslist.isEmpty()) {
            for (int k = 0; k < MerchantBotSell.myproductslist.size(); k++) {
                if (!MerchantBotSell.myproductslist.get(k).getStatus().equalsIgnoreCase("Publish")) {
                    myactiveproductslist.add(MerchantBotSell.myproductslist.get(k));
                }
            }

        }
        mySession = new MySession(UnsoldProductsAct.this);
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
        Log.e("activeproduct size>", " >" + myactiveproductslist);
        myActiveProAdp = new MyActiveProAdp(myactiveproductslist);
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
                = new LinearLayoutManager(UnsoldProductsAct.this, LinearLayoutManager.VERTICAL, false);
        myofferslist.setLayoutManager(horizontalLayoutManagaer);

        addoffer = findViewById(R.id.addoffer);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeToRefresh.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

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
                            Toast.makeText(UnsoldProductsAct.this, getResources().getString(R.string.offerdeleted), Toast.LENGTH_LONG).show();
                            if (myactiveproductslist != null && !myactiveproductslist.isEmpty()) {
                                Log.e("My Offer Remove >", " >" + current_offer_pos);
                                myactiveproductslist.remove(current_offer_pos);
                                myActiveProAdp = new MyActiveProAdp(myactiveproductslist);
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
                            myactiveproductslist.remove(current_offer_pos);
                            myActiveProAdp = new MyActiveProAdp(myactiveproductslist);
                            myofferslist.setAdapter(myActiveProAdp);
                            myActiveProAdp.notifyDataSetChanged();
                            ActivityCompat.invalidateOptionsMenu(UnsoldProductsAct.this);
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
        ArrayList<MerchantItemList> myactivelist;

        public MyActiveProAdp(ArrayList<MerchantItemList> myactivelist) {
            this.myactivelist = myactivelist;
        }

        @Override
        public MyActiveProAdp.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_myofferlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyActiveProAdp.MyViewHolder holder, final int listPosition) {
            holder.offer_title.setText("" + myactivelist.get(listPosition).getProductName());
            holder.offer_desc.setText("" + myactivelist.get(listPosition).getProductDescription());
            // holder.price_discount.setText("$" + myactivelist.get(listPosition).getPrice() + " (" + myactivelist.get(listPosition).getSalePrice() + "%)");
            holder.price_discount.setText(mySession.getValueOf(MySession.CurrencySign) + myactivelist.get(listPosition).getPrice());

            String product_img = myactivelist.get(listPosition).getThumbnailImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(UnsoldProductsAct.this).load(product_img).placeholder(R.drawable.placeholder).into(holder.offer_img);

/*
                Glide.with(UnsoldProductsAct.this)
                        .load(product_img)
                        .thumbnail(0.5f)
                        .override(400, 150)
                        .centerCrop()

                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(holder.offer_img);
*/


            }
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(UnsoldProductsAct.this, v);
                    popup.getMenuInflater().inflate(R.menu.offermenu, popup.getMenu());
                    MenuItem bedMenuItem = popup.getMenu().findItem(R.id.hideoffer);
                    if (myactivelist.get(listPosition).getStatus().equalsIgnoreCase("publish")) {

                        bedMenuItem.setTitle("Hide Offer");
                    } else {
                        bedMenuItem.setTitle("Publish");
                    }
                    setForceShowIcon(popup);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editoffer:
                                    Intent i = new Intent(UnsoldProductsAct.this, UpdateListingProduct.class);
                                   /* i.putExtra("id", myactivelist.get(listPosition).getId());
                                    i.putExtra("offername", myactivelist.get(listPosition).getOfferName());
                                    //i.putExtra("category_id",offerBeanListArrayList.get(listPosition).getCategory_id());
                                    i.putExtra("offerdescription", myactivelist.get(listPosition).getOfferDescription());
                                    i.putExtra("offerdescount", myactivelist.get(listPosition).getOfferDiscount());
                                    i.putExtra("offerprice", myactivelist.get(listPosition).getOfferPrice());
                                    i.putExtra("offerimage", myactivelist.get(listPosition).getOfferImage());
                                */
                                    startActivity(i);

                                    break;
                                case R.id.hideoffer:
                                    if (myactivelist.get(listPosition).getStatus().equalsIgnoreCase("publish")) {

                                        hidepublishOffer("trash", myactivelist.get(listPosition).getId());
                                    } else {
                                        hidepublishOffer("publish", myactivelist.get(listPosition).getId());

                                    }
                                    break;


                                case R.id.deleteoffer:
                                    current_offer_pos = listPosition;
                                    deleteOffer(listPosition, myactivelist.get(listPosition).getId());
                                    break;
                            }
                            return false;

                            //   Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    popup.show();
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
            //return 4;
            return myactivelist == null ? 0 : myactivelist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView offer_title, offer_desc, price_discount;
            ImageView offer_img, option;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.offer_title = itemView.findViewById(R.id.offer_title);
                this.offer_desc = itemView.findViewById(R.id.offer_desc);
                this.price_discount = itemView.findViewById(R.id.price_discount);
                this.offer_img = itemView.findViewById(R.id.offer_img);
                this.option = itemView.findViewById(R.id.option);
            }
        }
    }

}
