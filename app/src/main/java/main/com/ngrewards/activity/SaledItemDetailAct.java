package main.com.ngrewards.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.fragments.FragmentWebView;
import main.com.ngrewards.marchant.merchantbottum.MerHomeActivity;
import main.com.ngrewards.marchant.merchantbottum.MerMessageAct;

public class SaledItemDetailAct extends AppCompatActivity {

    private RelativeLayout backlay;
    private ImageView product_img;
    private TextView size_tv, color_tv, product_name, quantity, merchant_name, mainprice, order_id, purchasedate, upspackage, shipaddress;
    private TextView shipprice, estdeliver, contactseller;
    private String color_str = "", size_str = "", shipping_price = "",
            review_str = "", quantity_str = "", average_rating = "",
            user_id = "", comment_str = "", rating_str = "",
            member_img_str = "", member_contact_name = "",
            product_img_str = "", delivery_date_str = "",
            shipping_username = "", product_id = "",
            member_id = "", product_name_str = "",
            member_name_str = "", mainprice_str = "",
            order_id_str = "", saledate_str = "",
            upspackage_str = "", shipaddress_str = "",
            shipadd_opt_str = "";

    private String order_date;
    private TextView strip_recipt;
    private String reciept_url;
    private String post_code;
    private String created_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saled_item_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {

        } else {
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

    private void idinit() {
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
        mainprice.setText("$" + mainprice_str);
        shipprice.setText("$" + shipping_price);
        order_id.setText("" + order_id_str);
        estdeliver.setText("Est. Delivery " + delivery_date_str);
        purchasedate.setText("" + created_date);

        if (saledate_str != null) {
            try {
                String mytime = saledate_str;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                String finalDate = timeFormat.format(myDate);
                purchasedate.setText("" + created_date);

                System.out.println(finalDate);
            } catch (Exception e) {
                purchasedate.setText("" + created_date);

            }

        }

        //   saledate.setText(""+saledate_str);
        shipaddress.setText("" + shipping_username + "\n" + shipaddress_str + " " + shipadd_opt_str + "\n" + post_code);
        // upspackage.setText(delivery_date_str);
        if (product_img_str != null && !product_img_str.equalsIgnoreCase("") && !product_img_str.equalsIgnoreCase(BaseUrl.image_baseurl)) {
            Picasso.with(SaledItemDetailAct.this).load(product_img_str).placeholder(R.drawable.placeholder).into(product_img);
        }

    }
}
//E3:78:16:49:3C:20:C0:DF:17:9D:C4:10:9A:A7:78:24:36:7B:BA:AD