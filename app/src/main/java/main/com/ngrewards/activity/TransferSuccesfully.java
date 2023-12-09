package main.com.ngrewards.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferSuccesfully extends AppCompatActivity {

    MySession mySession;
    private String user_id, merchant_id, merchant_no, amount, tip_amount, employee_name,
            employee_id, order_guset_No, order_Table_No, oreder_meber_Name, order_Address_Id,
            order_special_request, ngcash, order_Date, order_Time, card_id, card_number, timezone, card_brand, customer_id, type;
    private String reciept_url;
    private String order_cart_id;
    private ProgressBar progresbar;
    private String member_id;
    private String comment_str;
    private String amount_str;
    private String apply_ngcassh;
    private String Transfer;
    private String time_zone;
    private JSONObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_paid_succesfully);
        mySession = new MySession(this);
        progresbar = findViewById(R.id.progresbar);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            user_id = bundle.getString("user_id");
            member_id = bundle.getString("member_id");
            comment_str = bundle.getString("comment_str");
            amount_str = bundle.getString("amount_str");
            apply_ngcassh = bundle.getString("apply_ngcassh");
            card_id = bundle.getString("card_id");
            card_number = bundle.getString("card_number");
            card_brand = bundle.getString("card_brand");
            customer_id = bundle.getString("customer_id");
            type = bundle.getString("Transfer");
        }

        TransferOrderBill(user_id, member_id, comment_str, amount_str, apply_ngcassh, card_id, card_number, card_brand, customer_id, type);
    }

    private void TransferOrderBill(String user_id, String member_id, String comment_str, String amount_str, String apply_ngcassh, String card_id, String card_number, String card_brand, String customer_id, String Transfer) {

        Log.e("transfer_money", "user_id" + user_id + "member_id" + member_id + "comment_str" + comment_str + "amount_str" + amount_str + "card_id" + card_id + "card_number" + card_number + "card_brand" + card_brand + "customer_id" + customer_id + "Transfer" + Transfer);
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().transferorrequest(user_id, member_id, comment_str, amount_str,
                apply_ngcassh, card_id, card_number, card_brand, customer_id, Transfer, time_zone, mySession.getValueOf(MySession.CurrencyCode));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    try {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        Log.e("Request or Transfer >", " >" + responseData);

                        if (object.getString("status").equals("1")) {

                            if (type.equalsIgnoreCase("Transfer")) {

                                result = object.getJSONObject("result");
                                reciept_url = result.getString("reciept_url");

                                SweetAlertDialog pDialog = new SweetAlertDialog(TransferSuccesfully.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                                pDialog.setTitleText("Your Transaction Successfully Done");
                                pDialog.setConfirmText("Ok");
                                pDialog.setCancelable(false);

                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(TransferSuccesfully.this, WebViewCalled.class);
                                        intent.putExtra("reciept_url", reciept_url);
                                        intent.putExtra("scrsts", "activity");
                                        startActivity(intent);
                                        finish();
                                        pDialog.dismissWithAnimation();
                                    }
                                });
                                pDialog.show();

                            } else {
                                Toast.makeText(TransferSuccesfully.this, getResources().getString(R.string.paymentreqsendsucc), Toast.LENGTH_LONG).show();
                            }

                        } else {

                            new SweetAlertDialog(TransferSuccesfully.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(getString(R.string.unsuccessful_transaction))
                                    .hideConfirmButton()
                                    .setCancelButton(getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            finish();
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    } catch (IOException e) {
                        new SweetAlertDialog(TransferSuccesfully.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Unsuccessfulll Transaction !")
                                .hideConfirmButton()
                                .setCancelButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        new SweetAlertDialog(TransferSuccesfully.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Unsuccessfullll Transaction !")
                                .hideConfirmButton()
                                .setCancelButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });

    }
}
