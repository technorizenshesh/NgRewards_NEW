package main.com.ngrewards.merchant_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.FragItemDetails;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.beanclasses.MerchantItemBean;
import main.com.ngrewards.beanclasses.MerchantItemBeanList;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 14/6/18.
 */

public class MerchantItemsFrag extends Fragment {
    private final String rating_filter_str = "";
    private final String fill_category_id = "";
    private final String price_type_str = "";
    private final String fill_category_id_loc = "";
    View v;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    SwipeRefreshLayout swipeToRefresh;
    ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private ItemProductGridAdp itemProductGridAdp;
    private RecyclerView item_product_rec;
    private ProgressBar progresbar;
    private ArrayList<MerchantItemBeanList> soldItemListArrayList;
    private Myapisession myapisession;
    private MySession mySession;
    private String user_id = "";
    private TextView filter_tv, noitemtv;

    public MerchantItemsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.merchant_items_frag_lay, container, false);
        mySession = new MySession(getActivity());
        myapisession = new Myapisession(getActivity());
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
        clickent();
        return v;
    }

    private void clickent() {

    }

    private void idinit() {

        noitemtv = v.findViewById(R.id.noitemtv);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        progresbar = v.findViewById(R.id.progresbar);
        item_product_rec = v.findViewById(R.id.item_product_rec);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 1);
        item_product_rec.setLayoutManager(recyclerViewLayoutManager);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSoldItems();
                // swipeToRefresh.setRefreshing(false);
            }
        });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getSoldItems();
        }
    }

    private void getSoldItems() {
        Log.e("FILTER DATA >>", " >> user_id =" + user_id + "  fill_category_id>" + fill_category_id + " rr>>" + MerchantDetailAct.merchant_id);

        swipeToRefresh.setRefreshing(true);
        soldItemListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantOwnProduct(MerchantDetailAct.merchant_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("All Merchant Products >", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {

                            MerchantItemBean successData = new Gson().fromJson(responseData, MerchantItemBean.class);
                            soldItemListArrayList.addAll(successData.getResult());

                        }
                        if (soldItemListArrayList == null || soldItemListArrayList.isEmpty() || soldItemListArrayList.size() == 0) {
                            noitemtv.setVisibility(View.VISIBLE);
                        } else {
                            noitemtv.setVisibility(View.GONE);
                        }

                        itemProductGridAdp = new ItemProductGridAdp(soldItemListArrayList);
                        item_product_rec.setAdapter(itemProductGridAdp);
                        itemProductGridAdp.notifyDataSetChanged();

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

    class ItemProductGridAdp extends RecyclerView.Adapter<ItemProductGridAdp.MyViewHolder> {
        ArrayList<MerchantItemBeanList> soldItemListArrayList;
        ArrayList<MerchantItemBeanList> searchsoldItemListArrayList;

        public ItemProductGridAdp(ArrayList<MerchantItemBeanList> soldItemListArrayList) {
            this.soldItemListArrayList = soldItemListArrayList;
            this.searchsoldItemListArrayList = new ArrayList<>();
            searchsoldItemListArrayList.addAll(soldItemListArrayList);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_productlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        public void filter(String charText) {
            //charText = charText.toLowerCase(Locale.getDefault());
            if (charText == null) {

            } else {
                charText = charText.toLowerCase();
                soldItemListArrayList.clear();
                if (charText.length() == 0) {
                    soldItemListArrayList.addAll(searchsoldItemListArrayList);
                } else {
                    for (MerchantItemBeanList wp : searchsoldItemListArrayList) {
                        if (wp.getProductName().toLowerCase().startsWith(charText))//.toLowerCase(Locale.getDefault())
                        {
                            soldItemListArrayList.add(wp);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int listPosition) {
            holder.product_name.setText("" + soldItemListArrayList.get(listPosition).getProductName());
            holder.product_desc.setText("" + soldItemListArrayList.get(listPosition).getProductDescription());
            holder.price_discount.setText(mySession.getValueOf(MySession.CurrencySign) + soldItemListArrayList.get(listPosition).getPrice());
            holder.mainprice.setText(mySession.getValueOf(MySession.CurrencySign) + soldItemListArrayList.get(listPosition).getPrice());
            holder.shipping_info.setText("" + soldItemListArrayList.get(listPosition).getShippingTime());

            if (soldItemListArrayList.get(listPosition).getBusinessName() == null || soldItemListArrayList.get(listPosition).getBusinessName().equalsIgnoreCase("")) {
                holder.merchant_name.setText("by " + getResources().getString(R.string.staticmerchantname));

            } else {
                holder.merchant_name.setText("by " + soldItemListArrayList.get(listPosition).getBusinessName());

            }

            String rat_str = soldItemListArrayList.get(listPosition).getAverageRating();
            if (rat_str != null && !rat_str.equalsIgnoreCase("")) {
                holder.rating.setRating(Float.parseFloat(rat_str));
                holder.rating_count.setText("(" + soldItemListArrayList.get(listPosition).getReviewCount() + ")");
            }

            String product_img = soldItemListArrayList.get(listPosition).getThumbnailImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(getActivity()).load(product_img).placeholder(R.drawable.placeholder).into(holder.product_img);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String EMi = "NO";
                    try {
                        String spliting = soldItemListArrayList.get(listPosition).getSplit_amount();
                        if (spliting != null && !spliting.equalsIgnoreCase("")) {
                            EMi = "YES";
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "onClick: ");
                    }
                    Log.e("PRO ss", " >" + soldItemListArrayList.get(listPosition).getProductName());
                    Intent i = new Intent(getActivity(), FragItemDetails.class);
                    Log.e("TAG", "EMiEMiEMi: " + EMi);
                    i.putExtra("EMI", EMi);
                    i.putExtra("product_id", soldItemListArrayList.get(listPosition).getId());
                    i.putExtra("product_name", soldItemListArrayList.get(listPosition).getProductName());
                    i.putExtra("product_description", soldItemListArrayList.get(listPosition).getProductDescription());
                    i.putExtra("product_thumbimg", soldItemListArrayList.get(listPosition).getThumbnailImage());
                    i.putExtra("product_price", soldItemListArrayList.get(listPosition).getPrice());
                    i.putExtra("share_link", soldItemListArrayList.get(listPosition).getShareLink());
                    i.putExtra("merchant_name_str", soldItemListArrayList.get(listPosition).getBusinessName());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 4;
            return soldItemListArrayList == null ? 0 : soldItemListArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView merchant_name, rating_count, product_desc, shipping_info, product_name, mainprice, price_discount, total_bought;
            ImageView product_img;
            RatingBar rating;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.total_bought = itemView.findViewById(R.id.total_bought);
                this.price_discount = itemView.findViewById(R.id.price_discount);
                this.product_desc = itemView.findViewById(R.id.product_desc);
                this.product_name = itemView.findViewById(R.id.product_name);
                this.mainprice = itemView.findViewById(R.id.mainprice);
                this.product_img = itemView.findViewById(R.id.product_img);
                this.shipping_info = itemView.findViewById(R.id.shipping_info);
                this.rating = itemView.findViewById(R.id.rating);
                this.rating_count = itemView.findViewById(R.id.rating_count);
                this.merchant_name = itemView.findViewById(R.id.merchant_name);
            }
        }
    }

}