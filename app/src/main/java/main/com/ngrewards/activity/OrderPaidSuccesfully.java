package main.com.ngrewards.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.placeorderclasses.AllAddedAddressAct;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class OrderPaidSuccesfully extends AppCompatActivity {

    MySession mySession;
    private String user_id, merchant_id, amount, tip_amount, employee_name,
            employee_id, ngcash, card_id, card_number, card_brand, customer_id;
    private String reciept_url;
    private String order_cart_id;
    private String merchant_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_paid_succesfully);
        mySession = new MySession(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && !bundle.isEmpty()) {

            user_id = bundle.getString("user_id");
            merchant_id = bundle.getString("merchant_id");
            amount = bundle.getString("due_amount_str");
            tip_amount = bundle.getString("tip_amt_str");
            employee_name = bundle.getString("employee_name");
            employee_id = bundle.getString("employee_id");
            ngcash = bundle.getString("apply_ngcassh");
            card_id = bundle.getString("card_id");
            card_number = bundle.getString("card_number");
            card_brand = bundle.getString("card_brand");
            customer_id = bundle.getString("customer_id");
            order_cart_id = bundle.getString("order_cart_id");
            merchant_number = bundle.getString("merchant_number");

        }

        PayOrderBill(order_cart_id, amount);
    }

    private void PayOrderBill(String order_cart_id, String amount) {

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", user_id);
        param.put("merchant_id", merchant_id);
        param.put("merchant_no", merchant_number);
        param.put("amount", amount);
        param.put("tip_amount", tip_amount);
        param.put("ngcash", ngcash);
        param.put("employee_name", employee_name);
        param.put("employee_id", employee_id);
        param.put("order_guset_No", PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.guest_no, ""));
        param.put("order_Table_No", PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.table_no, ""));
        param.put("oreder_meber_Name", "sa");
        param.put("order_Address_Id", PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.order_Address_Id, AllAddedAddressAct.AddressID));
        param.put("order_special_request", PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.order_special_request, ""));
        param.put("order_Date", PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.order_Date, ""));
        param.put("order_Time", PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.order_Time, ""));
        param.put("card_id", card_id);
        param.put("card_number", card_number);
        param.put("card_brand", card_brand);
        param.put("customer_id", customer_id);
        param.put("type", "order");
        param.put("order_cart_id", order_cart_id);
        param.put("currency", mySession.getValueOf(MySession.CurrencyCode));
//ToDo{PENDING_WORK}
        new ApiCallBuilder().build(this).setUrl(BaseUrl.orderPayBill()).setParam(param).isShowProgressBar(true).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    boolean status = object.getString("status").contains("1");
                    String message = object.getString("message");

                    if (status) {

                        reciept_url = object.getString("reciept_url");

                        Log.e("response!", response);
                        Log.e("params", String.valueOf(param));

                        SweetAlertDialog pDialog = new SweetAlertDialog(OrderPaidSuccesfully.this, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                        pDialog.setTitleText("Your Order Successfully Placed");
                        pDialog.setConfirmText("Ok");
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(OrderPaidSuccesfully.this, WebViewCalled.class);
                                intent.putExtra("reciept_url", reciept_url);
                                startActivity(intent);
                                finish();
                                pDialog.dismissWithAnimation();
                            }
                        });

                        pDialog.show();

                    } else {

                        SweetAlertDialog pDialog = new SweetAlertDialog(OrderPaidSuccesfully.this, SweetAlertDialog.ERROR_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                        pDialog.setTitleText(message);
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
            }

            @Override
            public void Failed(String error) {

            }
        });
    }
}
