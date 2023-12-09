package main.com.ngrewards.placeorderclasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.fragments.FragmentWebView;

public class TransferRequestDetActivity extends AppCompatActivity {

    private final String type = "";
    private final String amount_trans_by_card = "";
    private final String tip_str = "";
    private final String merchant_contact_name_str = "";
    private final String merchant_img_str = "";
    private final String merchant_id_str = "";
    private final String shipaddress_2_str = "";
    private final String shipping_name_str = "";
    private final String order_id_str = "";
    private final String merchant_name_str = "";
    private final String merchant_number_str = "";
    private final String address_tv_str = "";
    private final String cardnumber_tv_str = "";
    private RelativeLayout backlay;
    private ImageView sharebut;
    private TextView ngwal_tv, member_message, type_head, reqest_type, ngcashredeem, username_tv, member_name, tipamount_tv, due_amount, order_id, merchant_name, merchant_number, date_tv, address_tv, total_amt_tv, cardnumber_tv;
    private String comment = "";
    private String transfer_request_user_id = "";
    private String user_id = "";
    private String due_amt_tv_str = "";
    private String ngcash_str = "";
    private String business_name = "";
    private String username = "";
    private String cardbrand_str = "";
    private String date_tv_str = "";
    private String total_amt_tv_str = "";
    private MySession mySession;
    private LinearLayout visalay;
    private Button btn_strip_receipt, btn_order;
    private String reciept_url;
    private String member_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_transfer_request_lay);
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
                    business_name = jsonObject1.getString("fullname");
                    username = jsonObject1.getString("affiliate_name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            date_tv_str = bundle.getString("date_tv");
            ngcash_str = bundle.getString("ngcash_str");
            reciept_url = bundle.getString("reciept_url");
            member_id = bundle.getString("member_id");
            transfer_request_user_id = bundle.getString("transfer_request_user_id");
            total_amt_tv_str = bundle.getString("total_amt_tv_str");
            due_amt_tv_str = bundle.getString("due_amt_tv_str");
            comment = bundle.getString("comment");
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
        sharebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private void idinit() {
        ngwal_tv = findViewById(R.id.ngwal_tv);
        visalay = findViewById(R.id.visalay);
        reqest_type = findViewById(R.id.reqest_type);
        type_head = findViewById(R.id.type_head);

        if (type != null && type.equalsIgnoreCase("Transfer")) {
            reqest_type.setText("" + type);
            String text = merchant_contact_name_str + " transferred " + mySession.getValueOf(MySession.CurrencySign) + due_amt_tv_str + " to you";

            if (!transfer_request_user_id.equalsIgnoreCase(user_id)) {
                text = "Transfer of " + mySession.getValueOf(MySession.CurrencySign) + " " + due_amt_tv_str + " to " + merchant_contact_name_str;
            }
            type_head.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        } else {
            reqest_type.setText("" + type);
            type_head.setText("" + merchant_contact_name_str + getString(R.string.send_request_for) + mySession.getValueOf(MySession.CurrencySign) + due_amt_tv_str);
        }

        member_message = findViewById(R.id.member_message);
        ngcashredeem = findViewById(R.id.ngcashredeem);
        username_tv = findViewById(R.id.username_tv);
        member_name = findViewById(R.id.member_name);
        tipamount_tv = findViewById(R.id.tipamount_tv);
        due_amount = findViewById(R.id.due_amount);
        backlay = findViewById(R.id.backlay);
        sharebut = findViewById(R.id.sharebut);
        order_id = findViewById(R.id.order_id);
        merchant_name = findViewById(R.id.merchant_name);
        merchant_number = findViewById(R.id.merchant_number);
        date_tv = findViewById(R.id.date_tv);
        address_tv = findViewById(R.id.address_tv);
        total_amt_tv = findViewById(R.id.total_amt_tv);
        cardnumber_tv = findViewById(R.id.cardnumber_tv);
        btn_strip_receipt = findViewById(R.id.btn_strip_receipt);

        btn_strip_receipt.setOnClickListener(v -> {
            new FragmentWebView().setData(getString(R.string.receipt), reciept_url).show(getSupportFragmentManager(), "");
        });

        member_name.setText(getString(R.string.name_colan) + merchant_contact_name_str);
        member_message.setText(getString(R.string.message) + comment);
        username_tv.setText(getString(R.string.username_at_the_rad) + merchant_name_str);

        order_id.setText("#" + order_id_str);
        if (amount_trans_by_card == null || amount_trans_by_card.equalsIgnoreCase("") || amount_trans_by_card.equalsIgnoreCase("0") || amount_trans_by_card.equalsIgnoreCase("0.00")) {
            visalay.setVisibility(View.GONE);
            ngwal_tv.setVisibility(View.VISIBLE);
        } else {
            visalay.setVisibility(View.VISIBLE);
            ngwal_tv.setVisibility(View.GONE);
        }

        if (merchant_name_str == null || merchant_name_str.equalsIgnoreCase("") || merchant_name_str.equalsIgnoreCase("null")) {
            merchant_name.setText("" + getResources().getString(R.string.staticmerchantname));
        } else {
            merchant_name.setText("" + merchant_name_str);
        }
        merchant_number.setText(getString(R.string.merchant_no) + merchant_number_str);
        try {
            String mytime = date_tv_str;
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            Date myDate = null;
            myDate = dateFormat.parse(mytime);

            SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            String finalDate = timeFormat.format(myDate);
            date_tv.setText(getString(R.string.date) + finalDate);

            System.out.println(finalDate);
        } catch (Exception e) {
            Log.e("EXC TRUE", " RRR");
            date_tv.setText(getString(R.string.date) + date_tv_str);

        }
        address_tv.setText(getString(R.string.address) + address_tv_str + "\n" + shipaddress_2_str);
        total_amt_tv.setText(getString(R.string.total) + mySession.getValueOf(MySession.CurrencySign) + total_amt_tv_str);
        due_amount.setText(getString(R.string.amountdue) + mySession.getValueOf(MySession.CurrencySign) + due_amt_tv_str);
        tipamount_tv.setText(getString(R.string.tip) + mySession.getValueOf(MySession.CurrencySign) + tip_str);
        if (ngcash_str == null || ngcash_str.equalsIgnoreCase("") || ngcash_str.equalsIgnoreCase("0")) {
            ngcashredeem.setText(getResources().getString(R.string.ngcashredeem) + " :- " + mySession.getValueOf(MySession.CurrencySign) + " 0.00");

        } else {
            ngcashredeem.setText(getResources().getString(R.string.ngcashredeem) + " :- " + mySession.getValueOf(MySession.CurrencySign) + ngcash_str);

        }
        if (cardbrand_str != null) {
            if (cardbrand_str.length() > 4) {
                cardbrand_str = cardbrand_str.substring(0, 4);
            }
            String stars = "**** ****";
            cardnumber_tv.setText("" + cardbrand_str + " " + stars + " " + cardnumber_tv_str);
        }


    }
}
