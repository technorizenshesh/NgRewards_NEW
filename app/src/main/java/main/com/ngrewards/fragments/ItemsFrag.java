package main.com.ngrewards.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.beanclasses.MerchantItem;
import main.com.ngrewards.constant.BaseUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.FragItemDetails;
import main.com.ngrewards.beanclasses.MerchantItemList;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;

/**
 * Created by technorizen on 14/6/18.
 */

public class ItemsFrag extends Fragment {
    View v;

    private GridView item_product;
    private ItemProductGridAdp itemProductGridAdp;
    private RecyclerView item_product_rec;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ProgressBar progresbar;
    private ArrayList<MerchantItemList> soldItemListArrayList;
    private Myapisession myapisession;
    private MySession mySession;
    SwipeRefreshLayout swipeToRefresh;
    private String user_id = "", rating_filter_str = "", fill_category_id = "", price_type_str = "", fill_category_id_loc = "";
    private TextView filter_tv, noitemtv;
    ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private EditText search_et_home;

    private String result;

    public ItemsFrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ItemsFrag(String result) {
        // Required empty public constructor
        this.result = result;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.items_frag_lay, container, false);
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
        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterlay();
            }
        });

    }

    private void idinit() {
        search_et_home = getActivity().findViewById(R.id.search_et_home);
        filter_tv = v.findViewById(R.id.filter_tv);
        noitemtv = v.findViewById(R.id.noitemtv);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        progresbar = v.findViewById(R.id.progresbar);
        item_product_rec = v.findViewById(R.id.item_product_rec);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 1);
        item_product_rec.setLayoutManager(recyclerViewLayoutManager);
        swipeToRefresh.setOnRefreshListener(this::getSoldItems);
        search_et_home.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s == null) {

                } else {

                    if (s.toString().length() > 0) {
                        //  clear_text.setVisibility(View.VISIBLE);
                        if (itemProductGridAdp == null) {

                        } else {
                            itemProductGridAdp.filter(s.toString());
                        }
                    } else {
                        // clear_text.setVisibility(View.GONE);
                        if (itemProductGridAdp == null) {

                        } else {
                            itemProductGridAdp.filter("");
                        }
                    }


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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

    class ItemProductGridAdp extends RecyclerView.Adapter<ItemProductGridAdp.MyViewHolder> {
        ArrayList<MerchantItemList> soldItemListArrayList;
        ArrayList<MerchantItemList> searchsoldItemListArrayList;

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

        public ItemProductGridAdp(ArrayList<MerchantItemList> soldItemListArrayList) {
            this.soldItemListArrayList = soldItemListArrayList;
            this.searchsoldItemListArrayList = new ArrayList<>();
            searchsoldItemListArrayList.addAll(soldItemListArrayList);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_item_productlay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        public void filter(String charText) {
            //charText = charText.toLowerCase(Locale.getDefault());
            if (charText == null) {

            } else {
                charText = charText.toString().toLowerCase();
                soldItemListArrayList.clear();
                if (charText.length() == 0) {
                    soldItemListArrayList.addAll(searchsoldItemListArrayList);
                } else {
                    for (MerchantItemList wp : searchsoldItemListArrayList) {
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
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            holder.product_name.setText("" + soldItemListArrayList.get(listPosition).getProductName());
            holder.product_desc.setText("" + soldItemListArrayList.get(listPosition).getProductDescription());
            holder.price_discount.setText("$" + soldItemListArrayList.get(listPosition).getPrice());
            holder.mainprice.setText("$" + soldItemListArrayList.get(listPosition).getPrice());
            holder.shipping_info.setText("" + soldItemListArrayList.get(listPosition).getShipping_time());
            if (soldItemListArrayList.get(listPosition).getBusiness_name() == null || soldItemListArrayList.get(listPosition).getBusiness_name().equalsIgnoreCase("")) {
                holder.merchant_name.setText("by " + getResources().getString(R.string.staticmerchantname));

            } else {
                holder.merchant_name.setText("by " + soldItemListArrayList.get(listPosition).getBusiness_name());

            }
            //holder.mainprice.setText("" + soldItemListArrayList.get(listPosition).getSalePrice());
            String rat_str = soldItemListArrayList.get(listPosition).getAverage_rating();
            if (rat_str != null && !rat_str.equalsIgnoreCase("")) {
                holder.rating.setRating(Float.parseFloat(rat_str));
                holder.rating_count.setText("(" + soldItemListArrayList.get(listPosition).getReviewCount() + ")");
            }

            String product_img = soldItemListArrayList.get(listPosition).getThumbnailImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Picasso.with(getActivity()).load(product_img).placeholder(R.drawable.placeholder).into(holder.product_img);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("PRO ID", " >" + soldItemListArrayList.get(listPosition).getShare_link());
                    Log.e("PRO ss", " >" + soldItemListArrayList.get(listPosition).getProductName());
                    Intent i = new Intent(getActivity(), FragItemDetails.class);
                    i.putExtra("product_id", soldItemListArrayList.get(listPosition).getId());
                    i.putExtra("product_name", soldItemListArrayList.get(listPosition).getProductName());
                    i.putExtra("product_description", soldItemListArrayList.get(listPosition).getProductDescription());
                    i.putExtra("product_thumbimg", soldItemListArrayList.get(listPosition).getThumbnailImage());
                    i.putExtra("product_price", soldItemListArrayList.get(listPosition).getPrice());
                    i.putExtra("share_link", soldItemListArrayList.get(listPosition).getShare_link());
                    i.putExtra("merchant_name_str", soldItemListArrayList.get(listPosition).getBusiness_name());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 4;
            return soldItemListArrayList == null ? 0 : soldItemListArrayList.size();
        }
    }


    private void getSoldItems() {
        Log.e("FILTER DATA >>", " >> user_id =" + user_id + "  fill_category_id>" + fill_category_id + " rr>>" + rating_filter_str);

        /*swipeToRefresh.setRefreshing(true);*/
        //progresbar.setVisibility(View.VISIBLE);
        soldItemListArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getFeaturedProduct(user_id, fill_category_id, price_type_str, rating_filter_str);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                /*swipeToRefresh.setRefreshing(false);*/
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Sold Products >", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {

                            MerchantItem successData = new Gson().fromJson(responseData, MerchantItem.class);
                            soldItemListArrayList.addAll(successData.getResult());

                        }
                        if (soldItemListArrayList == null || soldItemListArrayList.isEmpty() || soldItemListArrayList.size() == 0) {
                            noitemtv.setVisibility(View.VISIBLE);
                        } else {
                            noitemtv.setVisibility(View.GONE);
                        }
                        itemProductGridAdp = new ItemProductGridAdp(soldItemListArrayList);
                        item_product_rec.setAdapter(itemProductGridAdp);

                        if(!result.equalsIgnoreCase(""))
                        {
                            gotoDetails();
                        }

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
                /*   swipeToRefresh.setRefreshing(false);*/
                Log.e("TAG", t.toString());
            }
        });
    }


    public void gotoDetails()
    {

        MerchantItemList soldItem = null;

        for(MerchantItemList soldMyItem:soldItemListArrayList)
        {

            if(result.equalsIgnoreCase(soldMyItem.getId()))
            {
                soldItem = soldMyItem;
            }

        }

        if(soldItem!=null)
        {
            Intent i = new Intent(getActivity(), FragItemDetails.class);
            i.putExtra("product_id", soldItem.getId());
            i.putExtra("product_name", soldItem.getProductName());
            i.putExtra("product_description", soldItem.getProductDescription());
            i.putExtra("product_thumbimg", soldItem.getThumbnailImage());
            i.putExtra("product_price", soldItem.getPrice());
            i.putExtra("share_link", soldItem.getShare_link());
            i.putExtra("merchant_name_str", soldItem.getBusiness_name());
            startActivity(i);
        }

    }

    private void filterlay() {
        CategoryAdpters categoryAdpters;
        final Dialog dialogSts = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.item_filter_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.back_pop_col)));
        TextView reset_tv = dialogSts.findViewById(R.id.reset_tv);
        TextView filter_tv = dialogSts.findViewById(R.id.filter_tv);
        final RadioButton lowtohigh = dialogSts.findViewById(R.id.lowtohigh);
        final RadioButton hightolow = dialogSts.findViewById(R.id.hightolow);
        final RatingBar rating = dialogSts.findViewById(R.id.rating);

        Spinner category_spinner = dialogSts.findViewById(R.id.category_spinner);
        if (myapisession.getProductdata() == null || myapisession.getProductdata().equalsIgnoreCase("")) {
            // getCategoryType();
        } else {
            try {
                categoryBeanListArrayList = new ArrayList<>();
                CategoryBeanList categoryBeanList = new CategoryBeanList();
                categoryBeanList.setCategoryId("0");
                categoryBeanList.setCategoryName("Select category");
                categoryBeanListArrayList.add(categoryBeanList);

                JSONObject object = new JSONObject(myapisession.getProductdata());
                Log.e("Product Category >", " >" + myapisession.getProductdata());
                if (object.getString("status").equals("1")) {
                    CategoryBean successData = new Gson().fromJson(myapisession.getProductdata(),
                            CategoryBean.class);
                    categoryBeanListArrayList.addAll(successData.getResult());
                }

                categoryAdpters = new CategoryAdpters(getActivity(), categoryBeanListArrayList);
                category_spinner.setAdapter(categoryAdpters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    if (categoryBeanListArrayList.get(position).getCategoryId().equalsIgnoreCase("0")) {
                        fill_category_id_loc = "";
                    } else {
                        fill_category_id_loc = categoryBeanListArrayList.get(position).getCategoryId();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hightolow.isChecked()) {
                    price_type_str = "HighToLow";
                }
                if (lowtohigh.isChecked()) {
                    price_type_str = "LowToHigh";
                }
                double rat = rating.getRating();
                if (rat == 0) {
                    rating_filter_str = "";
                } else {
                    rating_filter_str = String.valueOf(rat);
                }
                fill_category_id = fill_category_id_loc;
                Log.e("Filter Cat Id", " >" + fill_category_id);
                Log.e("price_type_str", " >" + price_type_str);
                if (fill_category_id != null && !fill_category_id.equalsIgnoreCase("") && !fill_category_id.equalsIgnoreCase("0")) {
                    getSoldItems();
                } else if (price_type_str != null && !price_type_str.equalsIgnoreCase("") && !price_type_str.equalsIgnoreCase("0")) {

                    getSoldItems();
                } else if (rating_filter_str != null && !rating_filter_str.equalsIgnoreCase("") && !rating_filter_str.equalsIgnoreCase("0")) {

                    getSoldItems();
                }

                dialogSts.dismiss();
            }
        });
        reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fill_category_id != null && !fill_category_id.equalsIgnoreCase("") && !fill_category_id.equalsIgnoreCase("0")) {
                    fill_category_id = "";
                    price_type_str = "";
                    fill_category_id_loc = "";
                    getSoldItems();
                } else if (price_type_str != null && !price_type_str.equalsIgnoreCase("") && !price_type_str.equalsIgnoreCase("0")) {
                    fill_category_id = "";
                    price_type_str = "";
                    fill_category_id_loc = "";
                    getSoldItems();
                } else if (rating_filter_str != null && !rating_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    price_type_str = "";

                    getSoldItems();
                }

                dialogSts.dismiss();
            }
        });

        dialogSts.show();
    }


    public class CategoryAdpters extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private ArrayList<CategoryBeanList> categoryBeanLists;

        public CategoryAdpters(Context applicationContext, ArrayList<CategoryBeanList> categoryBeanLists) {
            this.context = applicationContext;
            this.categoryBeanLists = categoryBeanLists;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return categoryBeanLists == null ? 0 : categoryBeanLists.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinner_layout, null);
            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            if (categoryBeanLists.get(i).getCategoryId().equals("982")) {
                names.setText(categoryBeanLists.get(i).getCategoryName());
            } else {
                names.setVisibility(View.GONE);
            }
            return view;
        }
    }
}