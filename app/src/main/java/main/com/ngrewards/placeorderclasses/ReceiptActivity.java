package main.com.ngrewards.placeorderclasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.fragments.FragmentOrder;
import main.com.ngrewards.fragments.FragmentWebView;

public class ReceiptActivity extends AppCompatActivity {

    private final String shipping_name_str = "";
    private RelativeLayout backlay;
    private ImageView sharebut;
    private TextView ngcashredeem, username_tv, employee_tv, member_name, tipamount_tv, due_amount, order_id, merchant_name, merchant_number, date_tv, address_tv, total_amt_tv, cardnumber_tv, special_request;
    private String user_id = "";
    private String due_amt_tv_str = "";
    private String ngcash_str = "";
    private String tip_str = "";
    private String business_name = "";
    private String username = "";
    private String employee_name = "";
    private String merchant_contact_name_str = "";
    private String merchant_img_str = "";
    private String merchant_id_str = "";
    private String shipaddress_2_str = "";
    private String cardbrand_str = "";
    private String order_id_str = "";
    private String merchant_name_str = "";
    private String merchant_number_str = "";
    private String date_tv_str = "";
    private String address_tv_str = "";
    private String total_amt_tv_str = "";
    private String cardnumber_tv_str = "";
    private MySession mySession;
    private TextView tv_date, tv_time, tv_guest_user, tv_table_no;
    private LinearLayout li_memberinfo, li_order_info;
    private String mdate, time, Order_guset_No, Order_Table_No;
    private Button btn_strip_receipt, btn_order;
    private String reciept_url;
    private String order_special;
    private String shipaddress_1;
    private String shipaddress_2;
    private String Order_guset_No_str;
    private String Order_Table_No_str;
    private String member_name_str;
    private TextView busseness_name;
    private String Order_cart_id_str;
    private String product_id;
    private String type;
    private String member_user_name;
    private String Type;
    private String type123;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_receipt);
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
            Log.e("TAG", " bundlebundlebundle   -------" + bundle);
            Order_cart_id_str = bundle.getString("order_cart_id");
            order_id_str = bundle.getString("order_id");
            merchant_name_str = bundle.getString("merchant_name");
            member_name_str = bundle.getString("member_name");
            merchant_contact_name_str = bundle.getString("merchant_contact_name");
            merchant_img_str = bundle.getString("merchant_img_str");
            merchant_id_str = bundle.getString("merchant_id");
            merchant_number_str = bundle.getString("merchant_number");
            date_tv_str = bundle.getString("date_tv");
            address_tv_str = bundle.getString("address");
            shipaddress_2_str = bundle.getString("address_2");
            type = bundle.getString("type");
            total_amt_tv_str = bundle.getString("total_amt_tv_str");
            due_amt_tv_str = bundle.getString("due_amt_tv_str");
            ngcash_str = bundle.getString("ngcash_str");
            tip_str = bundle.getString("tip_str");
            cardnumber_tv_str = bundle.getString("cardnumber_tv");
            cardbrand_str = bundle.getString("cardbrand");
            employee_name = bundle.getString("employee_name");
            mdate = bundle.getString("mdate");
            time = bundle.getString("time");
            Order_guset_No = bundle.getString("Order_guset_No");
            Order_Table_No = bundle.getString("Order_Table_No");
            reciept_url = bundle.getString("reciept_url");
            order_special = bundle.getString("order_special");
            product_id = bundle.getString("product_id");
            member_user_name = bundle.getString("member_user_name");
            Order_guset_No_str = bundle.getString("Order_guset_No");
            Order_Table_No_str = bundle.getString("Order_Table_No");
            type123 = bundle.getString("type123");
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

        ngcashredeem = findViewById(R.id.ngcashredeem);
        employee_tv = findViewById(R.id.username_tv);
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
        special_request = findViewById(R.id.special_request);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_guest_user = findViewById(R.id.tv_guest_user);
        tv_table_no = findViewById(R.id.tv_table_no);
        li_memberinfo = findViewById(R.id.li_memberinfo);
        li_order_info = findViewById(R.id.li_order_info);
        btn_strip_receipt = findViewById(R.id.btn_strip_receipt);
        btn_order = findViewById(R.id.btn_order123);

        busseness_name = findViewById(R.id.busseness_name);
        busseness_name.setText(merchant_name_str);
        merchant_name.setText(getString(R.string.merchant_no) + member_name_str);
        member_name.setText(getString(R.string.name_colan) + username);
        employee_tv.setText(getString(R.string.employee_name) + employee_name);
        special_request.setText(getString(R.string.special_request) + " " + order_special);
        order_id.setText("#" + order_id_str);

        if (type123.equals("Paybill")) {

            String mytime = date_tv_str;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.US);

            try {
                Date myDate1 = null;
                myDate1 = dateFormat.parse(mytime);

            } catch (ParseException e) {

                String mytime1 = date_tv_str;
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.US);
                Date myDate1 = null;
                try {
                    myDate1 = dateFormat1.parse(mytime1);
                } catch (ParseException ex) {

                    Log.e("EXC TRUE", " RRR");
                    date_tv.setText(getString(R.string.date) + date_tv_str);
                }

            }
        } else {
            try {
                String mytime = date_tv_str;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy");
                String finalDate = timeFormat.format(myDate);
                date_tv.setText("Date:- " + finalDate + " " + time);
                System.out.println(finalDate);

            } catch (Exception e) {
                Log.e("EXC TRUE", " RRR");
                date_tv.setText(getString(R.string.date) + date_tv_str + " " + time);
            }
        }

        address_tv.setText(getString(R.string.address) + address_tv_str + "\n" + shipaddress_2_str);
        total_amt_tv.setText(getString(R.string.total) + mySession.getValueOf(MySession.CurrencySign) + total_amt_tv_str);
        due_amount.setText(getString(R.string.amount_due) + mySession.getValueOf(MySession.CurrencySign) + due_amt_tv_str);
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

        if (mdate != null && !mdate.equals("null")) {
            tv_date.setText(getString(R.string.date) + mdate);
            tv_time.setText(getString(R.string.time) + time);
            tv_guest_user.setText(getString(R.string.guest_no) + Order_guset_No);
            tv_table_no.setText(getString(R.string.table_no) + Order_Table_No);
            li_memberinfo.setVisibility(View.GONE);

        } else {
            li_order_info.setVisibility(View.GONE);

        }

        btn_strip_receipt.setOnClickListener(v -> {
            new FragmentWebView().setData(getString(R.string.receipt), reciept_url).show(getSupportFragmentManager(), "");
        });


        btn_order.setOnClickListener(v -> {

            /* Log.e("order_id_str",order_id_str);*/
            new FragmentOrder().setData(Order_cart_id_str).show(getSupportFragmentManager(), "");
        });
    }
}
