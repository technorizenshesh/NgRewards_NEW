package main.com.ngrewards.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import main.com.ngrewards.Models.SplitList;
import main.com.ngrewards.R;
import main.com.ngrewards.RecyclerViewClickListenerSplit;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.fragments.FragmentWebView;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaledItemDetailAct extends
        AppCompatActivity implements
        RecyclerViewClickListenerSplit {
    private final String review_str = "";
    private final String average_rating = "";
    private final String comment_str = "";
    private final String rating_str = "";
    private final String member_img_str = "";
    private final String upspackage_str = "";
    private MySession mySession;
    private RelativeLayout backlay;
    private ImageView product_img;
    private TextView size_tv, color_tv, product_name, quantity, merchant_name, mainprice, order_id, purchasedate, upspackage, shipaddress;
    private TextView shipprice, estdeliver, contactseller;
    private String color_str = "";
    private String size_str = "";
    private String shipping_price = "";
    private String quantity_str = "";
    private String user_id = "";
    private String member_contact_name = "";
    private String product_img_str = "";
    private String delivery_date_str = "";
    private String shipping_username = "";
    private String product_id = "";
    private String member_id = "";
    private String product_name_str = "";
    private String member_name_str = "";
    private String mainprice_str = "";
    private String order_id_str = "";
    private String saledate_str = "";
    private String shipaddress_str = "";
    private String shipadd_opt_str = "";
    private String business_name = "";
    private String business_no = "";
    private String order_date;
    private TextView strip_recipt, show_remaining_payments, send_reminder, download_invoice;
    private String reciept_url;
    private String post_code;
    private String created_date;
    private String split_invoice = "", cart_id = "";
    private String split_date = "";
    private String split_payment = "";

    private String payment_made_by_emi = "";
    private String split_amount = "";
    private BottomSheetBehavior<View> behavior;
    private Dialog dialogSts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_saled_item_detail);
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {
        } else {
            try {

                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    /*{"result":{"id":"3","business_no":"ed58126","email":"ngrewardsllc@gmail.com","
                    password":"123456","business_name":"REACH","contact_name":"X","bank_name":"X","
                    account_no":"X","latitude":"29.5124819","longitude":"-98.5493952","zip_code":"
                    78230","contact_number":"2109939000","address":"8100 Pinebrook Dr, San Antonio,
                     TX  78230, United States",*/

                    user_id = jsonObject1.getString("id");
                    business_name = jsonObject1.getString("business_name");
                    business_no = jsonObject1.getString("business_no");
                }
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {

        } else {
            cart_id = bundle.getString("cart_id");
            split_invoice = bundle.getString("split_invoice");
            split_date = bundle.getString("split_date");
            payment_made_by_emi = bundle.getString("payment_made_by_emi");
            split_payment = bundle.getString("split_payment");
            split_amount = bundle.getString("split_amount");
            product_id = bundle.getString("product_id");
            member_id = bundle.getString("member_id");
            order_id_str = bundle.getString("order_id");
            shipping_username = bundle.getString("shipping_name");
            shipaddress_str = bundle.getString("shipaddress_1");
            shipadd_opt_str = bundle.getString("shipaddress_2");
            delivery_date_str = bundle.getString("upspackage");
            saledate_str = bundle.getString("saledate");
            size_str = bundle.getString("size");
            color_str = bundle.getString("color");
            shipping_price = bundle.getString("shipping_price");
            mainprice_str = bundle.getString("mainprice");
            member_name_str = bundle.getString("member_name");
            created_date = bundle.getString("created_date");
            product_name_str = bundle.getString("product_name");
            product_img_str = bundle.getString("product_img_str");
            member_contact_name = bundle.getString("member_contact_name");
            quantity_str = bundle.getString("quantity");
            order_date = bundle.getString("order_date");
            reciept_url = bundle.getString("reciept_url");
            post_code = bundle.getString("post_code");
        }

        idinit();
        clickevent();
    }

    private void clickevent() {
        contactseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SaledItemDetailAct.this, MemberChatAct.class);
                // i.putExtra("receiver_id",member_id);
                i.putExtra("type", "Merchant");
                i.putExtra("receiver_fullname", member_contact_name);
                i.putExtra("receiver_type", "Member");
                // i.putExtra("receiver_img",member_img_str);
                i.putExtra("receiver_name", member_name_str);
                startActivity(i);
            }
        });

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void listSplits(ArrayList<SplitList> splitLists, String type) {
        try {

            FinalPuzzelAdapter finalPuzzelAdapter = new FinalPuzzelAdapter(splitLists,
                    SaledItemDetailAct.this, type, this::onClick1);
            dialogSts = new Dialog(SaledItemDetailAct.this, R.style.DialogSlideAnim);
            dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSts.setCancelable(false);
            dialogSts.setContentView(R.layout.bottem_split__list_item);
            dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RecyclerView recy_list = dialogSts.findViewById(R.id.recy_list);
            Button submitt = (Button) dialogSts.findViewById(R.id.submitt);
            TextView cancel = (TextView) dialogSts.findViewById(R.id.cancel);
            recy_list.hasFixedSize();
            recy_list.setLayoutManager(new LinearLayoutManager(this));
            recy_list.setAdapter(finalPuzzelAdapter);
            submitt.setOnClickListener(v -> {
                // split_amount = TextUtils.join(", ", peopleList);
                //  Log.e("TAG", "listSplits:split_amountsplit_amount " + split_amount);
                // IsSplited = true ;
                //  split_check.setChecked(true);
                dialogSts.dismiss();
            });
            cancel.setOnClickListener(v -> dialogSts.dismiss());
            dialogSts.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "listSplits: " + e.getMessage());
            Log.e("TAG", "listSplits: " + e.getLocalizedMessage());
        }

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void idinit() {
        send_reminder = findViewById(R.id.send_reminder);
        show_remaining_payments = findViewById(R.id.show_remaining_payments);
        download_invoice = findViewById(R.id.download_invoice);
        shipprice = findViewById(R.id.shipprice);
        estdeliver = findViewById(R.id.estdeliver);
        backlay = findViewById(R.id.backlay);
        contactseller = findViewById(R.id.contactseller);
        size_tv = findViewById(R.id.size_tv);
        color_tv = findViewById(R.id.color_tv);

        quantity = findViewById(R.id.quantity);
        product_img = findViewById(R.id.product_img);
        product_name = findViewById(R.id.product_name);
        merchant_name = findViewById(R.id.merchant_name);
        mainprice = findViewById(R.id.mainprice);
        order_id = findViewById(R.id.order_id);
        purchasedate = findViewById(R.id.purchasedate);
        upspackage = findViewById(R.id.upspackage);
        shipaddress = findViewById(R.id.shipaddress);
        strip_recipt = findViewById(R.id.strip_recipt);

        strip_recipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentWebView().setData("Receipt", reciept_url).show(getSupportFragmentManager(), "");

            }
        });

        size_tv.setText(getResources().getString(R.string.size_s) + " :" + size_str);
        color_tv.setText(getResources().getString(R.string.color) + " :" + color_str);
        quantity.setText(getResources().getString(R.string.quanity) + " :" + quantity_str);

        product_name.setText("" + product_name_str);
        merchant_name.setText("" + member_contact_name);
        mainprice.setText(mySession.getValueOf(MySession.CurrencySign) + mainprice_str);
        shipprice.setText(mySession.getValueOf(MySession.CurrencySign) + shipping_price);
        order_id.setText("" + order_id_str);
        estdeliver.setText("Est. Delivery " + delivery_date_str);
        purchasedate.setText("" + order_date);
        download_invoice.setVisibility(View.VISIBLE);
        download_invoice.setOnClickListener(v -> {
            new FragmentWebView().setData("Receipt", reciept_url).show(getSupportFragmentManager(), "");

        });
        Log.e("TAG", "idinit: +payment_made_by_emi" + payment_made_by_emi);
        Log.e("TAG", "idinit: +split_amount       " + split_amount);
        Log.e("TAG", "idinit: +split_invoice      " + split_invoice);
        Log.e("TAG", "idinit: +   split_date      " + split_date);
        Log.e("TAG", "idinit: +split_payment      " + split_payment);
        if (payment_made_by_emi != null &&
                payment_made_by_emi.equalsIgnoreCase(
                        "Yes")) {
            try {
                ArrayList<String> split_invoicess =
                        new ArrayList<String>(Arrays.asList(split_invoice.split(",")));
                ArrayList<String> amountss = new ArrayList<String>(Arrays.asList(split_amount.split(",")));
                ArrayList<String> datess = new ArrayList<String>(Arrays.asList(split_date.split(",")));
                ArrayList<String> ispaid = new ArrayList<String>(Arrays.asList(split_payment.split(",")));
                ArrayList<SplitList> splitLists = new ArrayList<>();
                for (int i = 0; i < ispaid.size(); i++) {
                    int ie = i + 1;

                    SplitList splitList = new SplitList(ie + "",
                            datess.get(i), ispaid.get(i),
                            amountss.get(i), split_invoicess.get(i));
                    splitLists.add(splitList);
                }

                show_remaining_payments.setVisibility(View.VISIBLE);
                send_reminder.setVisibility(View.VISIBLE);
                show_remaining_payments.setOnClickListener(v -> {
                    listSplits(splitLists, "1");

                });

                send_reminder.setOnClickListener(v -> {
                    listSplits(splitLists, "2");


                });
              /*  split_invoice
                        split_date
                payment_made_by_emi
                        split_payment
                split_amount*/
            } catch (Exception e) {

            }

        }
        if (saledate_str != null) {
            try {
                String mytime = saledate_str;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                String finalDate = timeFormat.format(myDate);
                purchasedate.setText("" + order_date);

                System.out.println(finalDate);
            } catch (Exception e) {
                purchasedate.setText("" + order_date);

            }

        }

        //   saledate.setText(""+saledate_str);

        String str = "" + shipping_username + "\n" + shipaddress_str + " " + shipadd_opt_str + "\n" + post_code;
        if (str.contains("null")) {
            String daa = str.replace("null", "");
            shipaddress.setText(daa);

        } else {
            shipaddress.setText(str);
        }


        // upspackage.setText(delivery_date_str);
        if (product_img_str != null && !product_img_str.equalsIgnoreCase("") && !product_img_str.equalsIgnoreCase(BaseUrl.image_baseurl)) {
            Glide.with(SaledItemDetailAct.this).load(product_img_str).placeholder(R.drawable.placeholder).into(product_img);
        }

    }


    @Override
    public void onClick1(SplitList id_item) {

        dialogSts.dismiss();
        getMerOrderActivity(id_item);
        // cart_id=157&=7446&=4.8&=3&=ed58126&=REACH&=247&=2012-01-22&number_of_emi=1&json=on

    }

    private void getMerOrderActivity(SplitList id_item) {
        String dfgfd = PreferenceConnector.readString(SaledItemDetailAct.this,
                PreferenceConnector.UserNAme, "");
        String dfgfd1 = PreferenceConnector.readString(SaledItemDetailAct.this, PreferenceConnector.UserNAme1, "");

        Log.e("user_idd", user_id);
        Log.e("user_idd", dfgfd);
        Log.e("user_idd", dfgfd1);
        Map<String, String> map = new HashMap<>();
        map.put("cart_id", cart_id);
        map.put("order_id", order_id_str);
        map.put("split_amount_x", id_item.getAmount());
        map.put("merchant_id", user_id);
        map.put("merchant_business_no", business_no);
        map.put("merchant_business_name", business_name);
        map.put("due_date", id_item.getDate());
        map.put("member_id", member_id);
        map.put("number_of_emi", id_item.getId());
        map.put("json", "on");
        Call<ResponseBody> call = ApiClient.getApiInterface().notification_emi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        new SweetAlertDialog(SaledItemDetailAct.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Ng Rewards")
                                .setContentText("EMI reminder sent successfully")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        //finish();
                                    }
                                })
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("TAG", "onResponse: " + e.getLocalizedMessage());
                        Log.e("TAG", "onResponse: " + e.getMessage());
                        Log.e("TAG", "onResponse: " + e.getCause());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("TAG", t.toString());
            }
        });
    }

    public class FinalPuzzelAdapter extends RecyclerView.Adapter<FinalPuzzelAdapter.SelectTimeViewHolder> {
        private final ArrayList<SplitList> peopleList;
        Context context;
        String type;
        RecyclerViewClickListenerSplit recyclerViewClickListener1;

        public FinalPuzzelAdapter(ArrayList<SplitList> peopleList,
                                  Context context, String type
                , RecyclerViewClickListenerSplit recyclerViewClickListener1) {
            this.peopleList = peopleList;
            this.context = context;
            this.type = type;
            this.recyclerViewClickListener1 = recyclerViewClickListener1;
        }

        @NonNull
        @Override
        public FinalPuzzelAdapter.SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.list_split_item_item_two, parent,
                    false);
            FinalPuzzelAdapter.SelectTimeViewHolder viewHolder = new FinalPuzzelAdapter.SelectTimeViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FinalPuzzelAdapter.SelectTimeViewHolder holder
                , @SuppressLint("RecyclerView") int position) {
            TextView ivFinalImage = holder.itemView.findViewById(R.id.emi_item);
            SplitList splitList = peopleList.get(position);
            String str = "th";
            if (position == 0) str = "st";
            if (position == 1) str = "nd";
            if (position == 2) str = "rd";
            if (type.equalsIgnoreCase("1")) {


                if (peopleList.get(position).getIsPaid().equalsIgnoreCase("done")) {
                    ivFinalImage.setText(splitList.getId() + str + " Payment " + " " + mySession.getValueOf(MySession.CurrencySign) + " " + splitList.getAmount() + " Paid");

                } else {
                    ivFinalImage.setTextColor(getColor(R.color.red));
                    ivFinalImage.setText(splitList.getId() + str + " Payment " + " " + mySession.getValueOf(MySession.CurrencySign) + " " + splitList.getAmount() + " Due - " + splitList.getDate());
                }
            } else {
                if (peopleList.get(position).getIsPaid().equalsIgnoreCase("done")) {
                    ivFinalImage.setText("done");

                } else {
                    ivFinalImage.setTextColor(getColor(R.color.red));
                    ivFinalImage.setTextSize(10);
                    ivFinalImage.setText("Send Reminder For " + splitList.getId() + str + " Payment " + " " + mySession.getValueOf(MySession.CurrencySign) + " " + splitList.getAmount() + " Due On - " + splitList.getDate());
                    ivFinalImage.setOnClickListener(v -> {
                        recyclerViewClickListener1.onClick1(splitList);
                    });

                }


            }
        }

        @Override
        public int getItemCount() {
            return peopleList.size();
        }

        public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
            public SelectTimeViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


}
//E3:78:16:49:3C:20:C0:DF:17:9D:C4:10:9A:A7:78:24:36:7B:BA:AD