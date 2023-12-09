package main.com.ngrewards.marchant.draweractivity;

import android.content.Intent;
import android.graphics.Paint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.OfferBeanList;
import main.com.ngrewards.beanclasses.Offerbean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends AppCompatActivity {

    private RelativeLayout backlay, addoffer;
    private RecyclerView myofferslist;
    private MyOffersApd myOffersApd;
    private ProgressBar progresbar;
    private ArrayList<OfferBeanList> offerBeanListArrayList;
    private MySession mySession;
    private String user_id = "";
    private SwipeRefreshLayout swipeToRefresh;
    private int current_offer_pos;

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
        setContentView(R.layout.activity_offers);
        mySession = new MySession(OffersActivity.this);
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

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OffersActivity.this, AddOffersAct.class);
                startActivity(i);
            }
        });
    }

    private void idinit() {
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        myofferslist = findViewById(R.id.myofferslist);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(OffersActivity.this, LinearLayoutManager.VERTICAL, false);
        myofferslist.setLayoutManager(horizontalLayoutManagaer);

        addoffer = findViewById(R.id.addoffer);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOffers();
                //swipeToRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getOffers();
    }

    private void getOffers() {
        swipeToRefresh.setRefreshing(true);
        //progresbar.setVisibility(View.VISIBLE);
        offerBeanListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMyOfferProduct(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Offer Products >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            Offerbean successData = new Gson().fromJson(responseData, Offerbean.class);
                            offerBeanListArrayList.addAll(successData.getResult());

                        }

                        myOffersApd = new MyOffersApd(offerBeanListArrayList);
                        myofferslist.setAdapter(myOffersApd);
                        myOffersApd.notifyDataSetChanged();
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
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void deleteOffer(final int listPosition, String id) {
        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().deleteMyOffer(id);
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
                            Toast.makeText(OffersActivity.this, getResources().getString(R.string.offerdeleted), Toast.LENGTH_LONG).show();
                            if (offerBeanListArrayList != null && !offerBeanListArrayList.isEmpty()) {
                                Log.e("My Offer Remove >", " >" + current_offer_pos);
                                offerBeanListArrayList.remove(current_offer_pos);
                                myOffersApd = new MyOffersApd(offerBeanListArrayList);
                                myofferslist.setAdapter(myOffersApd);
                                myOffersApd.notifyDataSetChanged();
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

        Call<ResponseBody> call = ApiClient.getApiInterface().hidepublishProduct(id, status);
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

    class MyOffersApd extends RecyclerView.Adapter<MyOffersApd.MyViewHolder> {
        ArrayList<OfferBeanList> offerBeanListArrayList;

        public MyOffersApd(ArrayList<OfferBeanList> offerBeanListArrayList) {
            this.offerBeanListArrayList = offerBeanListArrayList;
        }

        @Override
        public MyOffersApd.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_myofferlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            holder.offer_title.setText("" + offerBeanListArrayList.get(listPosition).getOfferName());
            holder.offer_desc.setText("" + offerBeanListArrayList.get(listPosition).getOfferDescription());
            // holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign)  + offerBeanListArrayList.get(listPosition).getOfferPrice());
            if (offerBeanListArrayList.get(listPosition).getOffer_discount_price() == null) {
                holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanListArrayList.get(listPosition).getOfferPrice().trim());

            } else {
                if (offerBeanListArrayList.get(listPosition).getOffer_discount_price() != null && !offerBeanListArrayList.get(listPosition).getOffer_discount_price().equalsIgnoreCase("") && !offerBeanListArrayList.get(listPosition).getOffer_discount_price().equalsIgnoreCase("0")) {
                    holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanListArrayList.get(listPosition).getOfferPrice().trim());
                    holder.real_price.setTextColor(getResources().getColor(R.color.back_pop_col));
                    holder.real_price.setPaintFlags(holder.real_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanListArrayList.get(listPosition).getOfferPrice().trim());

                }

            }

            if (offerBeanListArrayList.get(listPosition).getOffer_discount_price() == null || offerBeanListArrayList.get(listPosition).getOffer_discount_price().equalsIgnoreCase("")) {

            } else {
                holder.price_discount.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanListArrayList.get(listPosition).getOffer_discount_price().trim() + " (" + offerBeanListArrayList.get(listPosition).getOfferDiscount() + "%)");

            }

            String product_img = offerBeanListArrayList.get(listPosition).getOfferImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {

                Glide.with(OffersActivity.this)
                        .load(product_img)
                        .thumbnail(0.5f)
                        .override(400, 150)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(holder.offer_img);
            }

            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(OffersActivity.this, v);
                    popup.getMenuInflater().inflate(R.menu.offermenu, popup.getMenu());
                    MenuItem bedMenuItem = popup.getMenu().findItem(R.id.hideoffer);
                    if (offerBeanListArrayList.get(listPosition).getStatus().equalsIgnoreCase("publish")) {

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
                                    Intent i = new Intent(OffersActivity.this, UpdateOfferProduct.class);
                                    i.putExtra("id", offerBeanListArrayList.get(listPosition).getId());
                                    i.putExtra("offername", offerBeanListArrayList.get(listPosition).getOfferName());
                                    i.putExtra("category_id", offerBeanListArrayList.get(listPosition).getCategory_id());
                                    i.putExtra("offerdescription", offerBeanListArrayList.get(listPosition).getOfferDescription());
                                    i.putExtra("offerdescount", offerBeanListArrayList.get(listPosition).getOfferDiscount());
                                    i.putExtra("offerprice", offerBeanListArrayList.get(listPosition).getOfferPrice());
                                    i.putExtra("offerimage", offerBeanListArrayList.get(listPosition).getOfferImage());
                                    i.putExtra("offer_discountprice_str", offerBeanListArrayList.get(listPosition).getOffer_discount_price().trim());
                                    startActivity(i);

                                    break;
                                case R.id.hideoffer:
                                    if (offerBeanListArrayList.get(listPosition).getStatus().equalsIgnoreCase("publish")) {

                                        hidepublishOffer("trash", offerBeanListArrayList.get(listPosition).getId());
                                    } else {
                                        hidepublishOffer("publish", offerBeanListArrayList.get(listPosition).getId());

                                    }


                                    break;


                                case R.id.deleteoffer:
                                    current_offer_pos = listPosition;
                                    deleteOffer(listPosition, offerBeanListArrayList.get(listPosition).getId());
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
            return offerBeanListArrayList == null ? 0 : offerBeanListArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView offer_title, offer_desc, price_discount, real_price;
            ImageView offer_img, option;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.offer_title = itemView.findViewById(R.id.offer_title);
                this.offer_desc = itemView.findViewById(R.id.offer_desc);
                this.price_discount = itemView.findViewById(R.id.price_discount);
                this.offer_img = itemView.findViewById(R.id.offer_img);
                this.option = itemView.findViewById(R.id.option);
                this.real_price = itemView.findViewById(R.id.real_price);
            }
        }
    }

}
