package main.com.ngrewards.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
import java.util.LinkedHashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.placeorderclasses.AllAddedAddressAct;
import main.com.ngrewards.placeorderclasses.SelectPaymentMethodAct;

public class ItemOrderPaySuccessFully extends AppCompatActivity {

    private String user_id, product_id, quantity, merchant_id, email, first_name, last_name,
            company, phone, address_1, address_2, city, state, postcode, payment_method, ngcash, country, card_id, customer_id,
            card_number, card_brand, shipping_price, timezone;
    private String reciept_url;
    private String amount;
    private ProgressBar progresbar;
    private Myapisession myapisession;
    private SweetAlertDialog pDialog;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_order_pay_success_fully);
        progresbar = (ProgressBar) findViewById(R.id.progresbar);
        myapisession = new Myapisession(this);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null && !bundle.isEmpty()) {

            user_id = bundle.getString("user_id");
            product_id = bundle.getString("product_id");
            quantity = bundle.getString("quantity");
            merchant_id = bundle.getString("merchant_id");
            email = bundle.getString("email");
            first_name = bundle.getString("first_name");
            last_name = bundle.getString("last_name");
            company = bundle.getString("company");
            phone = bundle.getString("phone");
            address_1 = bundle.getString("address_1");
            amount = bundle.getString("amount");
            address_2 = bundle.getString("address_2");
            city = bundle.getString("city");
            state = bundle.getString("state");
            postcode = bundle.getString("postcode");
            payment_method = bundle.getString("payment_method");
            ngcash = bundle.getString("ngcash");
            country = bundle.getString("country");
            card_id = bundle.getString("card_id");
            customer_id = bundle.getString("customer_id");
            card_number = bundle.getString("card_number");
            card_brand = bundle.getString("card_brand");
            shipping_price = bundle.getString("shipping_price");
            timezone = bundle.getString("timezone");
        }

        //  PayitemOrderBill();

        if (!(customer_id == "" && customer_id == "" && card_number == "" && card_brand == "")) {

            new PlaceOrderAsc1().execute();

        } else {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Alert")
                    .setContentText("Card Was Declined")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();

        }

    }

    private class PlaceOrderAsc1 extends AsyncTask<String, String, String> {
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

            try {

                String postReceiverUrl = BaseUrl.baseurl + "place_order.php?";
                Log.e("PlaceOrderURL4"," URL TRUE "+postReceiverUrl+"user_id="+user_id+"&merchant_id="+merchant_id
                        +"&product_id="+product_id+"&quantity="+quantity+"&email="+email+"&first_name="+
                        AllAddedAddressAct.fullname_str+"&last_name=&company=&phone="+
                        AllAddedAddressAct.phonetv_str+"&address_1="+AllAddedAddressAct.address1_str+
                        "&address_2="+AllAddedAddressAct.address2_str+"&city="
                        +AllAddedAddressAct.city_str+"&state="+AllAddedAddressAct.state_str+
                        "&postcode="+AllAddedAddressAct.zippcode_str+"&timezone="+timezone+
                        "&payment_method=Card&ngcash="+ngcash+"&card_id="+SelectPaymentMethodAct.card_id+
                        "&card_number="+SelectPaymentMethodAct.card_number+"&card_brand="+
                        SelectPaymentMethodAct.card_brand+"&shipping_price="+
                        shipping_price+"&customer_id="+
                        SelectPaymentMethodAct.customer_id+"&country="+
                        AllAddedAddressAct.countrytv_str);

                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

                params.put("user_id", user_id);
                params.put("product_id", product_id);
                params.put("quantity", quantity);
                params.put("merchant_id", merchant_id);
                params.put("email", email);
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
                params.put("ngcash", ngcash);
                params.put("country", AllAddedAddressAct.countrytv_str);
                params.put("card_id", SelectPaymentMethodAct.card_id);
                params.put("customer_id", SelectPaymentMethodAct.customer_id);
                params.put("card_number", SelectPaymentMethodAct.card_number);
                params.put("card_brand", SelectPaymentMethodAct.card_brand);
                params.put("shipping_price", shipping_price);
                params.put("timezone", timezone);
                params.put("f_amount", amount);

                Log.e("paramsss", "" + params);

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
            progresbar.setVisibility(View.GONE);

            Log.e("Place Order", ">>>>>>>>>>>>" + result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");

                if (status.equals("1")) {

                    reciept_url = jsonObject.getString("reciept_url");

                    try {

                        Intent intent = new Intent(ItemOrderPaySuccessFully.this, AlertDailoge.class);
                        intent.putExtra("reciept_url", reciept_url);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {

                    pDialog = new SweetAlertDialog(ItemOrderPaySuccessFully.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                    pDialog.setTitleText("Card Was Declined ");
                    pDialog.setConfirmText("Ok");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            finish();

                            pDialog.dismissWithAnimation();
                        }
                    });

                    pDialog.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                        finish();

                    } else {

             /*           pDialog = new SweetAlertDialog(ItemOrderPaySuccessFully.this, SweetAlertDialog.ERROR_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                        pDialog.setTitleText("Card Was Declined Error !!!");
                        pDialog.setConfirmText("Ok");
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                finish();
                                pDialog.dismissWithAnimation();
                            }
                        });

                        pDialog.show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
