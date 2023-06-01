package main.com.ngrewards.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.fragments.FragmentWebView;

public class PurchasedItemDetailAct extends AppCompatActivity {

    private RelativeLayout backlay;
    private ImageView product_img;
    private TextView product_name, merchant_name, mainprice, order_id, saledate, upspackage, shipaddress;
    private String quantity_str="",color_str="",size_str="",shipping_price="",review_status="",review_str="",average_rating="",user_id="",comment_str="",rating_str="",merchant_img_str="",merchant_contact_name="",product_img_str = "", delivery_date_str = "", shipping_username = "", product_id = "", merchant_id = "", product_name_str = "", merchant_name_str = "", mainprice_str = "", order_id_str = "", saledate_str = "", upspackage_str = "", shipaddress_str = "", shipadd_opt_str = "";
    private TextView quantity_tv,size_tv,color_tv,writereview, contactseller;
    private LinearLayout post_review_lay,done_review_lay;
    private EditText comment_et;
    private RatingBar rating,rating_done;
    private TextView shipping_price_tv,submit_review,donereview_tv;
    private ProgressBar progresbar;
    private MySession mySession;
    private Button btn_strip_receipt;
    private String reciept_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_item_detail);
        btn_strip_receipt = findViewById(R.id.btn_strip_receipt);
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


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {

        } else {
            review_str = bundle.getString("review");
            size_str = bundle.getString("size");
            color_str = bundle.getString("color");
            quantity_str = bundle.getString("quantity");
            review_status = bundle.getString("review_status");
            average_rating = bundle.getString("average_rating");
            product_id = bundle.getString("product_id");
            merchant_id = bundle.getString("merchant_id");
            order_id_str = bundle.getString("order_id");
            shipping_username = bundle.getString("shipping_name");
            shipaddress_str = bundle.getString("shipaddress_1");
            shipadd_opt_str = bundle.getString("shipaddress_2");
            delivery_date_str = bundle.getString("upspackage");
            saledate_str = bundle.getString("saledate");

            shipping_price = bundle.getString("shipping_price");
            mainprice_str = bundle.getString("mainprice");
            merchant_name_str = bundle.getString("merchant_name");
            merchant_img_str = bundle.getString("merchant_img_str");
            product_name_str = bundle.getString("product_name");
            product_img_str = bundle.getString("product_img_str");
            merchant_contact_name = bundle.getString("merchant_contact_name");
            reciept_url = bundle.getString("reciept_url");


        }


        idinit();
        clickevent();
    }

    private void clickevent() {
        writereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (post_review_lay.getVisibility()==View.VISIBLE){
                    rating_str = String.valueOf(rating.getRating());
                    comment_str = comment_et.getText().toString();
                    if (rating_str==null||rating_str.equalsIgnoreCase("")||rating_str.equalsIgnoreCase("0")||rating_str.equalsIgnoreCase("0.0")){
Toast.makeText(PurchasedItemDetailAct.this,getResources().getString(R.string.selectatleast),Toast.LENGTH_LONG).show();
                    }
                    else {
                        new AddReviewAsc().execute();
                    }
                }
                if (post_review_lay.getVisibility()==View.GONE){
                    post_review_lay.setVisibility(View.VISIBLE);
                    writereview.setText(""+getResources().getString(R.string.submitreview));
                }

            }
        });

        contactseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PurchasedItemDetailAct.this, MemberChatAct.class);
                i.putExtra("receiver_id",merchant_id);
                i.putExtra("type","Member");
                i.putExtra("receiver_type","Merchant");
               // i.putExtra("receiver_fullname", merchant_contact_name);
                i.putExtra("receiver_fullname", merchant_name_str);

                i.putExtra("receiver_img",merchant_img_str);
                i.putExtra("receiver_name",merchant_name_str);
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
        quantity_tv = findViewById(R.id.quantity_tv);
        size_tv = findViewById(R.id.size_tv);
        color_tv = findViewById(R.id.color_tv);
        shipping_price_tv = findViewById(R.id.shipping_price_tv);
        rating_done = findViewById(R.id.rating_done);
        donereview_tv = findViewById(R.id.donereview_tv);
        done_review_lay = findViewById(R.id.done_review_lay);
        progresbar = findViewById(R.id.progresbar);
        submit_review = findViewById(R.id.submit_review);
        rating = findViewById(R.id.rating);
        comment_et = findViewById(R.id.comment_et);
        post_review_lay = findViewById(R.id.post_review_lay);
        backlay = findViewById(R.id.backlay);
        contactseller = findViewById(R.id.contactseller);
        writereview = findViewById(R.id.writereview);
        product_img = findViewById(R.id.product_img);
        product_name = findViewById(R.id.product_name);
        merchant_name = findViewById(R.id.merchant_name);
        mainprice = findViewById(R.id.mainprice);
        order_id = findViewById(R.id.order_id);
        saledate = findViewById(R.id.saledate);
        upspackage = findViewById(R.id.upspackage);
        shipaddress = findViewById(R.id.shipaddress);

        if(review_status==null||review_status.equalsIgnoreCase("")||review_status.equalsIgnoreCase("No")||review_status.equalsIgnoreCase("no"))
        {
            writereview.setVisibility(View.VISIBLE);
            post_review_lay.setVisibility(View.GONE);
            done_review_lay.setVisibility(View.GONE);

        }
        else {
            donereview_tv.setText(""+review_str);
            rating_done.setRating(Float.parseFloat(average_rating));
            post_review_lay.setVisibility(View.GONE);
            writereview.setVisibility(View.GONE);
            done_review_lay.setVisibility(View.VISIBLE);
        }

        size_tv.setText(getResources().getString(R.string.size_s)+" :" + size_str);
        color_tv.setText(getResources().getString(R.string.color)+" :" +color_str);
        quantity_tv.setText(getResources().getString(R.string.quanity)+" :" +quantity_str);
        product_name.setText("" + product_name_str);
        merchant_name.setText("" + merchant_name_str);
        mainprice.setText("$" + mainprice_str);
        shipping_price_tv.setText("$" + shipping_price);
        order_id.setText("" + order_id_str);

        btn_strip_receipt.setOnClickListener(v -> {
            new FragmentWebView().setData("Receipt", reciept_url).show(getSupportFragmentManager(), "");
        });


        if (saledate_str != null) {
            try {
                String mytime = saledate_str;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss");
                Date myDate = null;
                myDate = dateFormat.parse(mytime);

                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                String finalDate = timeFormat.format(myDate);
                saledate.setText("" + finalDate);

                System.out.println(finalDate);
            } catch (Exception e) {
                saledate.setText("" + saledate_str);

            }

        }


        //   saledate.setText(""+saledate_str);
         String str = "" + shipping_username + "\n" + shipaddress_str + " " + shipadd_opt_str;
         if (str.contains("null")){
             String daa= str.replace("null","");
             shipaddress.setText(daa);

         }else {
        shipaddress.setText(str);}
       // upspackage.setText(delivery_date_str);
        if (product_img_str != null && !product_img_str.equalsIgnoreCase("") && !product_img_str.equalsIgnoreCase(BaseUrl.image_baseurl)) {
            Picasso.with(PurchasedItemDetailAct.this).load(product_img_str).placeholder(R.drawable.placeholder).into(product_img);
        }

    }

    private class AddReviewAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://myngrewards.com/demo/wp-content/plugins/webservice/add_to_cart.php?user_id=23&product_id=5658&quantity=4
            try {
                String postReceiverUrl = BaseUrl.baseurl + "add_product_reviews.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                //params.put("order_id", order_id_str);
                params.put("product_id", product_id);
                params.put("member_id", user_id);
                params.put("review", comment_str);
                params.put("rating", rating_str);
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
                Log.e("Add Review", ">>>>>>>>>>>>" + response);
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
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    Log.e("result review >"," >> "+result);
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        donereview_tv.setText(""+comment_str);
                        if (rating_str!=null){
                            rating_done.setRating(Float.parseFloat(rating_str));
                        }
                        post_review_lay.setVisibility(View.GONE);
                        writereview.setVisibility(View.GONE);
                        done_review_lay.setVisibility(View.VISIBLE);
                        Toast.makeText(PurchasedItemDetailAct.this,getResources().getString(R.string.reviewsubmited),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }


}
