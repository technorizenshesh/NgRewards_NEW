package main.com.ngrewards.placeorderclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.ItemOrderPaySuccessFully;
import main.com.ngrewards.beanclasses.CartBean;
import main.com.ngrewards.beanclasses.CartListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.draweractivity.BaseActivity;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutAct extends AppCompatActivity {

    boolean click_sts = false;
    private ImageView clear_pick_ic;
    private ProgressBar progresbar;
    public static LatLng orderlatlong;

    //cart dynamic
    private ExpandableHeightListView mycartlist;
    SwipeRefreshLayout swipeToRefresh;
    private String user_id = "";
    MySession mySession;
    private ArrayList<CartListBean> cartListBeanArrayList;
    private MycartAdapter mycartAdapter;
    private MycartAdapterExpand mycartAdapterExpand;
    private TextView   notice , total_amount, nocartitem, place_order_but, avbngcash;
    private RelativeLayout backlay, addaddressdlay, addcardlay;
    private EditText optionaladdress, zipcode, applyamtcash;
    private String email_str = "", shipping_price = "", time_zone = "", ngcash_send_str = "0",
            apply_ngcassh = "0", member_ngcash = "", phone_str = "", total_amount_str = "",
            emi_amount_str = "", fullname_str = "", order_address = "", streat_address = "",
            zipcode_code_str = "", product_id_comma = "", merchant_id_comma_sep = "", product_quantity_comm = "";
    private RelativeLayout selectedaddlay, paywithlay;
    private TextView shipping_price_tv, finalngcashredeem, itemcount, ngapply_tv, fullname, total_item_price, address1, address2, statecityadd, countrytv, phonetv, cardnumber_tv;
    private Myapisession myapisession;
    private boolean apickeck = false;
    private double ngcash_val = 0;
    private LinearLayout emi_lay;
    private   boolean IS =false;
CheckBox termscheck1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
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
                    email_str = jsonObject1.getString("email");
                    phone_str = jsonObject1.getString("phone");
                    fullname_str = jsonObject1.getString("fullname");
                    member_ngcash = jsonObject1.getString("member_ngcash");
                    Log.e("user_id >>", " >" + user_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        idinits();

        clickevetn();
        if (myapisession.getKeyCartitem() == null || myapisession.getKeyCartitem()
                .equalsIgnoreCase("")) {
            if (!apickeck) {
                getMyCartDetail();
            }

        } else {

            try {
                cartListBeanArrayList = new ArrayList<>();
                String responseData = myapisession.getKeyCartitem();
                JSONObject object = new JSONObject(responseData);
                if (object.getString("status").equals("1")) {
                    CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                    cartListBeanArrayList.addAll(successData.getResult());
                    total_amount_str = successData.getTotal_with_shipping_price();
                    if (cartListBeanArrayList.get(0).getPay_by_emi().equalsIgnoreCase("YES")){
                        emi_lay.setVisibility(View.VISIBLE);
                    }else {
                        emi_lay.setVisibility(View.GONE);

                    }
                    String spliting =cartListBeanArrayList.get(0).getProductDetail().getSplit_amount();
                    if (spliting!=null &&!spliting.equalsIgnoreCase("")){
                        Log.e("TAG", "onBindViewHolder:zzzzzzzzzzzzzzzzzzzzzzz " +spliting);
                        String []  splitings = spliting.split(",");
                        Log.e("TAG", "onBindViewHolder:xxxxxxxxxxxxxxxxxxxxxxx " +splitings[0]);
                        Log.e("TAG", "onBindViewHolder:yyyyyyyyyyyyyyyyyyyyyyy " +splitings[0]);
                      notice.setText("Total payment is divide by "+splitings.length+" easy Emi " +
                              "you can " +
                              "see text " +
                              "payment invoice in order details after purchase item successfully");

                        String str =splitings[0];
                        if (str.contains("$")) {
                          str=  str.replace("$","");
                        }
                        emi_amount_str = str;
                    }
                    Log.e("TAG", "emi_amount_stremi_amount_str: --- "+emi_amount_str );
                    shipping_price = successData.getTotal_shipping_price();

                   if (!IS) {
                       total_amount.setText("$" + successData.getTotal_with_shipping_price());
                       shipping_price_tv.setText("$" + successData.getTotal_shipping_price());


                   } else {
                       shipping_price_tv.setText("Included In EMI");

                       total_amount.setText("1st EMI - $" + emi_amount_str);}
                    total_item_price.setText("$" + successData.getTotalPrice());
                   // shipping_price_tv.setText("$" + successData.getTotal_shipping_price());
                    itemcount.setText("Items(" + successData.getTotal_cart_count() + ")");
                }
                if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty() || cartListBeanArrayList.size() == 0) {
                    // nocartitem.setVisibility(View.VISIBLE);
                    total_amount.setText("$ 0.00");
                    total_amount_str = "0";
                    emi_amount_str = "0";
                    shipping_price = "0";
                    total_item_price.setText("$ 0.00");
                    itemcount.setText("Items(0)");
                    shipping_price_tv.setText("$" + "0.00");
                    mycartAdapterExpand = new MycartAdapterExpand(CheckOutAct.this, cartListBeanArrayList);
                    mycartlist.setAdapter(mycartAdapterExpand);
                    mycartAdapter.notifyDataSetChanged();

                } else {
                    // nocartitem.setVisibility(View.GONE);
                    mycartAdapterExpand = new MycartAdapterExpand(CheckOutAct.this, cartListBeanArrayList);
                    mycartlist.setAdapter(mycartAdapterExpand);
                    mycartAdapterExpand.notifyDataSetChanged();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public class MycartAdapterExpand extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;
        ArrayList<CartListBean> mycartlist;

        public MycartAdapterExpand(Activity context, ArrayList<CartListBean> mycartlist) {
            this.mycartlist = mycartlist;
            this.context = context;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mycartlist == null ? 0 : mycartlist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;
            TextView product_name, shipingprice, estimatedelivery, mainprice, merchant_name, product_desc, quant_tv;
            ImageView product_img, removecartitem;
            Button plusq, minusq;
            rowView = inflater.inflate(R.layout.custom_checkout_cart_item_lay, null);

            shipingprice = rowView.findViewById(R.id.shipingprice);
            estimatedelivery = rowView.findViewById(R.id.estimatedelivery);
            product_name = rowView.findViewById(R.id.product_name);
            merchant_name = rowView.findViewById(R.id.merchant_name);
            product_desc = rowView.findViewById(R.id.product_desc);
            quant_tv = rowView.findViewById(R.id.quant_tv);

            product_img = rowView.findViewById(R.id.product_img);
            mainprice = rowView.findViewById(R.id.mainprice);
            plusq = rowView.findViewById(R.id.plusq);
            minusq = rowView.findViewById(R.id.minusq);
            removecartitem = rowView.findViewById(R.id.removecartitem);

            product_desc.setText("" + mycartlist.get(position).getProductDetail().getProductDescription());
            product_name.setText("" + mycartlist.get(position).getProductDetail().getProductName());
            merchant_name.setText("" + mycartlist.get(position).getUserDetails().get(0).getBusinessName());
            quant_tv.setText("" + mycartlist.get(position).getQuantity());
            mainprice.setText("$" + mycartlist.get(position).getProductDetail().getProduct_cart_price());
            shipingprice.setText("$" + mycartlist.get(position).getShipping_price());

            try {
                String mytime = mycartlist.get(position).getEstimated_delivery_date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("EEE, MMM dd");
                String finalDate = timeFormat.format(myDate);
                estimatedelivery.setText("Est. Delivery : " + finalDate);

                System.out.println(finalDate);
            } catch (Exception e) {
                estimatedelivery.setText("Est. Delivery : " + mycartlist.get(position).getEstimated_delivery_date());

            }

            String image_url = mycartlist.get(position).getProductDetail().getThumbnailImage();
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Picasso.with(CheckOutAct.this).load(image_url).placeholder(R.drawable.placeholder).into(product_img);
            }


            plusq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mycartlist.get(position).getQuantity() != null && !mycartlist.get(position).getQuantity().equalsIgnoreCase("")) {
                        int total_stock_count = 0;
                        int total_count = Integer.parseInt(mycartlist.get(position).getQuantity());
                        if (mycartlist.get(position).getProductDetail().getStock() != null && !mycartlist.get(position).getProductDetail().getStock().equalsIgnoreCase("")) {
                            total_stock_count = Integer.parseInt(mycartlist.get(position).getProductDetail().getStock());

                        }
                        if (total_count < total_stock_count) {
                            int new_count = ++total_count;
                            updateMyCartItemQuantity(mycartlist.get(position).getProductId(), "" + new_count);
                        }

                    }

                }
            });

            minusq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mycartlist.get(position).getQuantity() != null && !mycartlist.get(position).getQuantity().equalsIgnoreCase("")) {
                        int total_count = Integer.parseInt(mycartlist.get(position).getQuantity());
                        if (total_count > 1) {
                            int new_count = --total_count;
                            updateMyCartItemQuantity(mycartlist.get(position).getProductId(), "" + new_count);
                        }

                    }
                }
            });

      /*      holder.removecartitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMySingleCartItem(mycartlist.get(position).getId());
                    Toast.makeText(getApplicationContext(),"Success!!!",Toast.LENGTH_SHORT).show();
                }
            });
*/
            return rowView;
        }

    }


    class MycartAdapter extends RecyclerView.Adapter<MycartAdapter.MyViewHolder> {
        ArrayList<CartListBean> mycartlist;

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

        public MycartAdapter(ArrayList<CartListBean> mycartlist) {
            this.mycartlist = mycartlist;
        }

        @Override
        public MycartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_cart_item_lay, parent, false);
            MycartAdapter.MyViewHolder myViewHolder = new MycartAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MycartAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int listPosition) {
            holder.product_desc.setText("" + mycartlist.get(listPosition).getProductDetail().getProductDescription());
            holder.product_name.setText("" + mycartlist.get(listPosition).getProductDetail().getProductName());
            holder.merchant_name.setText("" + mycartlist.get(listPosition).getUserDetails().get(0).getBusinessName());
            holder.quant_tv.setText("" + mycartlist.get(listPosition).getQuantity());
            holder.mainprice.setText("$" + mycartlist.get(listPosition).getProductDetail().getProduct_cart_price());

            String image_url = mycartlist.get(listPosition).getProductDetail().getThumbnailImage();
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Picasso.with(CheckOutAct.this).load(image_url).placeholder(R.drawable.placeholder).into(holder.product_img);
            }


            holder.plusq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mycartlist.get(listPosition).getQuantity() != null && !mycartlist.get(listPosition).getQuantity().equalsIgnoreCase("")) {
                        int total_stock_count = 0;
                        int total_count = Integer.parseInt(mycartlist.get(listPosition).getQuantity());
                        if (mycartlist.get(listPosition).getProductDetail().getStock() != null && !mycartlist.get(listPosition).getProductDetail().getStock().equalsIgnoreCase("")) {
                            total_stock_count = Integer.parseInt(mycartlist.get(listPosition).getProductDetail().getStock());
                        }
                        if (total_count < total_stock_count) {
                            int new_count = ++total_count;
                            // updateMyCartItemQuantity(mycartlist.get(listPosition).getProductId(),""+new_count );
                        }
                    }
                }
            });
            holder.minusq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mycartlist.get(listPosition).getQuantity() != null && !mycartlist.get(listPosition).getQuantity().equalsIgnoreCase("")) {
                        int total_count = Integer.parseInt(mycartlist.get(listPosition).getQuantity());
                        if (total_count > 1) {
                            int new_count = --total_count;
                            updateMyCartItemQuantity(mycartlist.get(listPosition).getProductId(), "" + new_count);
                        }
                    }
                }
            });

            holder.removecartitem123.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     Toast.makeText(getApplicationContext(),"successs12345!!!",Toast.LENGTH_SHORT).show();
                     removeMySingleCartItem(mycartlist.get(listPosition).getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mycartlist == null ? 0 : mycartlist.size();
        }
    }


    private void getMyCartDetail() {
        apickeck = true;
        progresbar.setVisibility(View.VISIBLE);
        cartListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMyCart(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    apickeck = false;
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Cart Items >", " ItemsItemsItemsItems>" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyCartitem(responseData);
                            CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                            cartListBeanArrayList.addAll(successData.getResult());
                            total_amount_str = successData.getTotal_with_shipping_price();
                            String spliting =cartListBeanArrayList.get(0).getProductDetail().getSplit_amount();
                            if (cartListBeanArrayList.get(0).getPay_by_emi().equalsIgnoreCase("YES")){
                                emi_lay.setVisibility(View.VISIBLE);
                            }else {
                                emi_lay.setVisibility(View.GONE);

                            }
                            if (spliting!=null &&!spliting.equalsIgnoreCase("")){
                                String []  splitings = spliting.split(",");
                                Log.e("TAG", "onBindViewHolder:splitingssplitings " +splitings[0]);
                                notice.setText("Total payment is divide by "+splitings.length+" easy Emi " +
                                        "you can " +
                                        "see text " +
                                        "payment invoice in order details after purchase item successfully");

                                String str =splitings[0];

                                if (str.contains("$")) {
                                    str=  str.replace("$","");
                                }
                                emi_amount_str = str;
                            }
                            Log.e("TAG", "emi_amount_stremi_amount_str: --- "+emi_amount_str );
                            shipping_price = successData.getTotal_shipping_price();
                            if (!IS) {
                                total_amount.setText("$" + successData.getTotal_with_shipping_price());
                                shipping_price_tv.setText("Include in EMI");

                            }  else {
                                total_amount.setText("1st EMI - $" + emi_amount_str);
                                shipping_price_tv.setText("$" + successData.getTotal_with_shipping_price());

                            }

                            total_item_price.setText("$" + successData.getTotalPrice());
                          //  shipping_price_tv.setText("$" + successData
                            //  .getTotal_with_shipping_price());
                            itemcount.setText("Items(" + successData.getTotal_cart_count() + ")");
                        } else {
                            myapisession.setKeyCartitem("");
                        }
                        if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty() || cartListBeanArrayList.size() == 0) {
                            total_amount.setText("$0.00");
                            total_amount_str = "0";
                            shipping_price = "0";
                            total_item_price.setText("$0.00");
                            itemcount.setText("Items(0)");
                            shipping_price_tv.setText("$" + "0.00");
                            mycartAdapterExpand = new MycartAdapterExpand(CheckOutAct.this, cartListBeanArrayList);
                            mycartlist.setAdapter(mycartAdapterExpand);
                            try {
                                mycartlist.setAdapter(mycartAdapterExpand);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mycartAdapter.notifyDataSetChanged();
                        } else {
                            mycartAdapterExpand = new MycartAdapterExpand(CheckOutAct.this, cartListBeanArrayList);
                            mycartlist.setAdapter(mycartAdapterExpand);
                            mycartAdapterExpand.notifyDataSetChanged();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    apickeck = false;
                    myapisession.setKeyCartitem("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                apickeck = false;
                progresbar.setVisibility(View.GONE);
                // progresbar.setVisibility(View.GONE);
                //  swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void clickevetn() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addaddressdlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckOutAct.this, AllAddedAddressAct.class);
                startActivity(i);
            }
        });
        selectedaddlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckOutAct.this, AllAddedAddressAct.class);
                startActivity(i);
            }
        });
        addcardlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckOutAct.this, SelectPaymentMethodAct.class);
                startActivity(i);
            }
        });
        paywithlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckOutAct.this, SelectPaymentMethodAct.class);
                startActivity(i);
            }
        });

        ngapply_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double apply_ng = 0;
                apply_ngcassh = applyamtcash.getText().toString();
                if (apply_ngcassh == null || apply_ngcassh.equalsIgnoreCase("") || apply_ngcassh.equalsIgnoreCase("0")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.enteramount), Toast.LENGTH_LONG).show();
                    ngapply_tv.setText("" + getResources().getString(R.string.apply));
                    finalngcashredeem.setHint("-$0.00");
                    finalngcashredeem.setText("");
                } else {
                    apply_ng = Double.parseDouble(apply_ngcassh);
                    if (apply_ng > ngcash_val) {
                        Toast.makeText(CheckOutAct.this, getResources().getString(R.string.appliedamtisgreaterthanngcash), Toast.LENGTH_LONG).show();
                        ngapply_tv.setText("" + getResources().getString(R.string.apply));
                        finalngcashredeem.setHint("-$0.00");
                        finalngcashredeem.setText("");
                    } else {

                        if (!IS){
                            ngapply_tv.setText("" + getResources().getString(R.string.applied));
                            double tot = 0;
                            if (total_amount_str == null || total_amount_str.equalsIgnoreCase("")) {
                                total_amount_str = "0.0";

                            }
                            if (apply_ngcassh == null || apply_ngcassh.equalsIgnoreCase("")) {
                                apply_ngcassh = "0.0";
                            }

                            double cart_tot_dob = Double.parseDouble(total_amount_str);
                            double apply_ng_dob = Double.parseDouble(apply_ngcassh);

                            if (cart_tot_dob > apply_ng_dob) {
                                tot = cart_tot_dob - apply_ng_dob;
                                //finalngcashredeem.setText("-$ " + apply_ng_dob);
                                finalngcashredeem.setText("-$ " + String.format("%.2f", new BigDecimal(apply_ng_dob)));


                            } else {
                                tot = 0;
                                //finalngcashredeem.setText("-$ "+cart_tot_dob);
                                finalngcashredeem.setText("-$ " + String.format("%.2f", new BigDecimal(cart_tot_dob)));

                            }

                            //  grandtotal.setText(" " + tot);
                            total_amount.setText("$ " + String.format("%.2f", new BigDecimal(tot)));
                        }
                        else {

                            ngapply_tv.setText("" + getResources().getString(R.string.applied));
                            double tot = 0;
                            if (emi_amount_str == null || emi_amount_str.equalsIgnoreCase("")) {
                                emi_amount_str = "0.0";

                            }
                            if (apply_ngcassh == null || apply_ngcassh.equalsIgnoreCase("")) {
                                apply_ngcassh = "0.0";
                            }

                            double cart_tot_dob = Double.parseDouble(emi_amount_str);
                            double apply_ng_dob = Double.parseDouble(apply_ngcassh);

                            if (cart_tot_dob > apply_ng_dob) {
                                tot = cart_tot_dob - apply_ng_dob;
                                //finalngcashredeem.setText("-$ " + apply_ng_dob);
                                finalngcashredeem.setText("-$ " + String.format("%.2f", new BigDecimal(apply_ng_dob)));


                            } else {
                                tot = 0;
                                //finalngcashredeem.setText("-$ "+cart_tot_dob);
                                finalngcashredeem.setText("-$ " + String.format("%.2f", new BigDecimal(cart_tot_dob)));

                            }

                            //  grandtotal.setText(" " + tot);hereeeee
                            total_amount.setText("1st EMI - $"+ String.format("%.2f", new BigDecimal(tot)));



                        }


                    }
                }
            }
        });

        place_order_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty()) {

                } else {
                    product_id_comma = "";
                    product_quantity_comm = "";
                    merchant_id_comma_sep = "";
                    StringBuilder quantity = new StringBuilder();
                    StringBuilder productid = new StringBuilder();
                    StringBuilder merchantid = new StringBuilder();

                    for (int i = 0; i < cartListBeanArrayList.size(); i++) {
                        productid.append(cartListBeanArrayList.get(i).getProductId());
                        quantity.append(cartListBeanArrayList.get(i).getQuantity());
                        merchantid.append(cartListBeanArrayList.get(i).getUserDetails().get(0).getId());
                        if (i != cartListBeanArrayList.size() - 1) {
                            productid.append(",");
                            quantity.append(",");
                            merchantid.append(",");
                        }


                    }

                    product_id_comma = productid.toString();
                    product_quantity_comm = quantity.toString();
                    merchant_id_comma_sep = merchantid.toString();
                }

                if (AllAddedAddressAct.fullname_str == null || AllAddedAddressAct.fullname_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                } else if (AllAddedAddressAct.address1_str == null || AllAddedAddressAct.address1_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                }/*else if (AllAddedAddressAct.address2_str == null || AllAddedAddressAct.address2_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                }else if (AllAddedAddressAct.address2_str == null || AllAddedAddressAct.address2_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                }*/ else if (AllAddedAddressAct.city_str == null || AllAddedAddressAct.city_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                } else if (AllAddedAddressAct.state_str == null || AllAddedAddressAct.state_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                } else if (AllAddedAddressAct.countrytv_str == null || AllAddedAddressAct.countrytv_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                } else if (AllAddedAddressAct.phonetv_str == null || AllAddedAddressAct.phonetv_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                } else if (AllAddedAddressAct.zippcode_str == null || AllAddedAddressAct.zippcode_str.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectshipingaddres), Toast.LENGTH_LONG).show();
                } else if (SelectPaymentMethodAct.card_id == null || SelectPaymentMethodAct.card_id.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.selectpaymethod), Toast.LENGTH_LONG).show();
                } else if (product_id_comma == null || product_id_comma.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();
                } else if (product_id_comma == null || product_id_comma.equalsIgnoreCase("")) {
                    Toast.makeText(CheckOutAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();
                } else {
                    Log.e("product_id_comma >>", " >> " + product_id_comma);
                    Log.e("product_qua_comm >>", " >> " + product_quantity_comm);
                    Log.e("merchant_id_comma >>", " >> " + merchant_id_comma_sep);

                    apply_ngcassh = applyamtcash.getText().toString();
                    if (apply_ngcassh == null || apply_ngcassh.equalsIgnoreCase("") || apply_ngcassh.equalsIgnoreCase("0")) {

                    } else {
                        double apply_ng = Double.parseDouble(apply_ngcassh);
                        if (apply_ng > ngcash_val) {
                            ngcash_send_str = "0";
                            Toast.makeText(CheckOutAct.this, getResources().getString(R.string.appliedamtisgreaterthanngcash), Toast.LENGTH_LONG).show();
                            ngapply_tv.setText("" + getResources().getString(R.string.apply));
                        } else {
                            ngcash_send_str = apply_ngcassh;
                            ngapply_tv.setText("" + getResources().getString(R.string.applied));

                        }
                    }

                    Item_Oreder_Pay_Successfully1();

                }
            }
        });
    }

    private void Item_Oreder_Pay_Successfully1() {
        Intent intent = new Intent(this, ItemOrderPaySuccessFully.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("product_id", product_id_comma);
        intent.putExtra("quantity", product_quantity_comm);
        intent.putExtra("merchant_id", merchant_id_comma_sep);
        intent.putExtra("email", email_str);
        intent.putExtra("first_name", AllAddedAddressAct.fullname_str);
        intent.putExtra("last_name", "");
        intent.putExtra("company", "");
        intent.putExtra("phone", AllAddedAddressAct.phonetv_str);
        intent.putExtra("address_1", AllAddedAddressAct.address1_str);
        intent.putExtra("address_2", AllAddedAddressAct.address2_str);
        intent.putExtra("city", AllAddedAddressAct.city_str);
        intent.putExtra("state", AllAddedAddressAct.state_str);
        intent.putExtra("postcode", AllAddedAddressAct.zippcode_str);
        intent.putExtra("payment_method", "Card");
        intent.putExtra("ngcash", ngcash_send_str);
        intent.putExtra("country", AllAddedAddressAct.countrytv_str);
        intent.putExtra("card_id", SelectPaymentMethodAct.card_id);
        intent.putExtra("customer_id", SelectPaymentMethodAct.customer_id);
        intent.putExtra("card_number", SelectPaymentMethodAct.card_number);
        intent.putExtra("card_brand", SelectPaymentMethodAct.card_brand);
        intent.putExtra("shipping_price", shipping_price);
        intent.putExtra("timezone", time_zone);
         if (IS){
            String amnt =  total_amount.getText().toString();
            if (amnt.contains("1st EMI - $")){
                amnt = amnt.replace("1st EMI - $"," ");
            }
             Log.e("TAG", "Item_Oreder_Pay_Successfully1: "+amnt.trim() );
             intent.putExtra("amount", amnt.trim());
             intent.putExtra("payment_made_by_emi","Yes");
             //purchase_date=\(Date())&payment_made_by_emi=\(ischeckYes!)

            // return;
         }else {
             intent.putExtra("payment_made_by_emi", "No");
             intent.putExtra("amount", total_amount_str);

         }

        startActivity(intent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("Unseen Count"));
        if (AllAddedAddressAct.fullname_str == null || AllAddedAddressAct.fullname_str.equalsIgnoreCase("")) {
            selectedaddlay.setVisibility(View.GONE);
            addaddressdlay.setVisibility(View.VISIBLE);
        } else {
            selectedaddlay.setVisibility(View.VISIBLE);
            addaddressdlay.setVisibility(View.GONE);
            fullname.setText("" + AllAddedAddressAct.fullname_str);
            address1.setText("" + AllAddedAddressAct.address1_str);
            if (AllAddedAddressAct.address2_str == null || AllAddedAddressAct.address2_str.equalsIgnoreCase("")) {
                address2.setVisibility(View.GONE);
            }
            address2.setText("" + AllAddedAddressAct.address2_str);
            statecityadd.setText("" + AllAddedAddressAct.city_str + "," + AllAddedAddressAct.state_str + " " + AllAddedAddressAct.zippcode_str);
            countrytv.setText("" + AllAddedAddressAct.countrytv_str);
            phonetv.setText("" + AllAddedAddressAct.phonetv_str);
        }

        if (SelectPaymentMethodAct.card_id == null || SelectPaymentMethodAct.card_id.equalsIgnoreCase("")) {
            addcardlay.setVisibility(View.VISIBLE);
            paywithlay.setVisibility(View.GONE);
        } else {
            addcardlay.setVisibility(View.GONE);
            paywithlay.setVisibility(View.VISIBLE);
            String cardbrandstr = SelectPaymentMethodAct.card_brand;
            String carnum = SelectPaymentMethodAct.card_number;
            if (cardbrandstr.length() > 4) {
                cardbrandstr = cardbrandstr.substring(0, 4);
            }
            String stars = "**** ****";
            cardnumber_tv.setText("" + cardbrandstr + " " + stars + " " + carnum);
        }
    }


    private void idinits() {

        emi_lay = findViewById(R.id.emi_lay);
        notice = findViewById(R.id.notice);
        termscheck1 = findViewById(R.id.termscheck1);
        shipping_price_tv = findViewById(R.id.shipping_price_tv);
        finalngcashredeem = findViewById(R.id.finalngcashredeem);
        ngapply_tv = findViewById(R.id.ngapply_tv);
        avbngcash = findViewById(R.id.avbngcash);
        applyamtcash = findViewById(R.id.applyamtcash);
        total_item_price = findViewById(R.id.total_item_price);
        itemcount = findViewById(R.id.itemcount);
        paywithlay = findViewById(R.id.paywithlay);
        cardnumber_tv = findViewById(R.id.cardnumber_tv);
        phonetv = findViewById(R.id.phonetv);
        countrytv = findViewById(R.id.countrytv);
        statecityadd = findViewById(R.id.statecityadd);
        address2 = findViewById(R.id.address2);
        address1 = findViewById(R.id.address1);
        fullname = findViewById(R.id.fullname);
        selectedaddlay = findViewById(R.id.selectedaddlay);
        addcardlay = findViewById(R.id.addcardlay);
        addaddressdlay = findViewById(R.id.addaddressdlay);
        place_order_but = findViewById(R.id.place_order_but);
        optionaladdress = findViewById(R.id.optionaladdress);
        zipcode = findViewById(R.id.zipcode);

        progresbar = (ProgressBar) findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        //swipeToRefresh = findViewById(R.id.swipeToRefresh);
        nocartitem = findViewById(R.id.nocartitem);
        total_amount = findViewById(R.id.total_amount);
        mycartlist = findViewById(R.id.mycartlist);
        mycartlist.setExpanded(true);
        termscheck1.setOnCheckedChangeListener((buttonView, isChecked) -> {
             if (isChecked){
                 IS = true;
                 notice.setVisibility(View.VISIBLE);
                 total_amount.setText("1st EMI - $"  + emi_amount_str  );

             }else {
                 IS = false;
                 notice.setVisibility(View.GONE);
                 total_amount.setText("$" + total_amount_str);

             }

        });
        if (BaseActivity.member_ngcash == null || BaseActivity.member_ngcash.equalsIgnoreCase("0") || BaseActivity.member_ngcash.equalsIgnoreCase("null") || BaseActivity.member_ngcash.equalsIgnoreCase("") || BaseActivity.member_ngcash.equalsIgnoreCase("0.0")) {
            avbngcash.setText("$0.00 Available");
        } else {

            avbngcash.setText("$" + BaseActivity.member_ngcash + " Available");

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ngcash_val = Double.parseDouble(BaseActivity.member_ngcash);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        applyamtcash.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ngapply_tv.setText("" + getResources().getString(R.string.apply));
                if (!IS)total_amount.setText("$" + total_amount_str);
                else total_amount.setText("1st EMI - $" + emi_amount_str);
              //  total_amount.setText("$ " + total_amount_str);
                finalngcashredeem.setText("-$ 0.00");
            }
        });

    }

    //dghdfugfdughdfuhg/////

    private class PlaceOrderAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            // prgressbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//http://myngrewards.com/demo/wp-content/plugins/webservice/place_order.php
            try {

                String postReceiverUrl = BaseUrl.baseurl + "place_order.php?";
                Log.e("PlaceOrderURL4", " URL TRUE " + postReceiverUrl + "user_id=" + user_id + "&merchant_id=" + merchant_id_comma_sep + "&product_id=" + product_id_comma + "&quantity=" + product_quantity_comm + "&email=" + email_str + "&first_name=" + fullname_str + "&last_name=&company=&phone=" + phone_str + "&address_1=" + AllAddedAddressAct.address1_str + "&address_2=" + AllAddedAddressAct.address2_str + "&city=" + AllAddedAddressAct.city_str + "&state=" + AllAddedAddressAct.state_str + "&postcode=" + zipcode_code_str + "&timezone=" + time_zone + "&payment_method=Card&ngcash=" + ngcash_send_str + "&card_id=" + SelectPaymentMethodAct.card_id + "&card_number=" + SelectPaymentMethodAct.card_number + "&card_brand=" + SelectPaymentMethodAct.card_brand + "&shipping_price=" + shipping_price + "&customer_id=" + SelectPaymentMethodAct.customer_id);

                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);
                params.put("product_id", product_id_comma);
                params.put("quantity", product_quantity_comm);
                params.put("merchant_id", merchant_id_comma_sep);
                params.put("email", email_str);
                params.put("first_name", AllAddedAddressAct.fullname_str);
                params.put("last_name", "");
                params.put("company", "");
                params.put("phone", AllAddedAddressAct.phonetv_str);
                params.put("address_1", AllAddedAddressAct.address1_str);
                params.put("address_2", AllAddedAddressAct.address2_str);
                params.put("city", AllAddedAddressAct.city_str);
                params.put("state", AllAddedAddressAct.state_str);
                params.put("postcode", AllAddedAddressAct.zippcode_str);
                params.put("payment_method", "Card");
                params.put("ngcash", ngcash_send_str);
                params.put("country", AllAddedAddressAct.countrytv_str);
                params.put("card_id", SelectPaymentMethodAct.card_id);
                params.put("customer_id", SelectPaymentMethodAct.customer_id);
                params.put("card_number", SelectPaymentMethodAct.card_number);
                params.put("card_brand", SelectPaymentMethodAct.card_brand);
                params.put("shipping_price", shipping_price);
                params.put("timezone", time_zone);

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
            // prgressbar.setVisibility(View.GONE);
            progresbar.setVisibility(View.GONE);
            Log.e("Place Order", ">>>>>>>>>>>>" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        myapisession.setKeyCartitem("");
                        AllAddedAddressAct.phonetv_str = "";
                        AllAddedAddressAct.fullname_str = "";
                        AllAddedAddressAct.address1_str = "";
                        AllAddedAddressAct.address2_str = "";
                        AllAddedAddressAct.city_str = "";
                        AllAddedAddressAct.state_str = "";
                        AllAddedAddressAct.zippcode_str = "";
                        AllAddedAddressAct.countrytv_str = "";
                        SelectPaymentMethodAct.card_id = "";
                        SelectPaymentMethodAct.card_brand = "";
                        SelectPaymentMethodAct.card_number = "";
                        Toast.makeText(CheckOutAct.this, getResources().getString(R.string.orderplacedsucc), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(CheckOutAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // BaseActivity.Card_Added_Sts="Added";

            }


        }
    }

    private void removeMySingleCartItem(String id) {

        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
      /*  HashMap<String,String> param=new HashMap<>();
        new ApiCallBuilder().build(CheckOutAct.this)
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl("https://myngrewards.com/wp-content/plugins/webservice/remove_cart_product.php?cart_id="+id)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                Toast.makeText(getApplicationContext(),"Success!!!",Toast.LENGTH_SHORT).show();
                                getMyCartDetail();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failed(String error) {

                    }
                });*/
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

    private void updateMyCartItemQuantity(String id, String quantity) {
        //swipeToRefresh.setRefreshing(true);
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().updatCartItem(user_id, id, quantity);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);
                // swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Update Cart>", " >" + responseData);
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {

                    String ngcash_str = intent.getExtras().getString("ngcash");


                    if (ngcash_str == null || ngcash_str.equalsIgnoreCase("") || ngcash_str.equalsIgnoreCase("null") || ngcash_str.equalsIgnoreCase("0")) {
                        avbngcash.setText("$0.00 Available");
                    } else {
                        avbngcash.setText("$" + ngcash_str + " Available");
                        ngcash_val = Double.parseDouble(ngcash_str);
                    }
                }

            } catch (Exception e) {

            }


        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

}