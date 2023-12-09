package main.com.ngrewards.activity;

import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualPaybillSucess extends AppCompatActivity {
    MySession mySession;
    private String user_id, merchant_id, merchant_no, amount, tip_amount, employee_name,
            employee_id, order_guset_No, order_Table_No, oreder_meber_Name, order_Address_Id,
            order_special_request, ngcash, order_Date, order_Time, card_id, card_number, timezone, card_brand, customer_id, type, cart_id = "";
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
    private String merchant_number;
    private String due_amount_str;
    private String tip_amt_str;
    private String merchant_name;
    private String myFormattedtime;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_paybill_sucess);
        progresbar = findViewById(R.id.progresbar);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
        mySession = new MySession(this);
        final Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        format.setTimeZone(c1.getTimeZone());
        myFormattedtime = format.format(c1.getTime());

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && !bundle.isEmpty()) {

            type = bundle.getString("type");
            user_id = bundle.getString("user_id");
            member_id = bundle.getString("merchant_id");
            merchant_number = bundle.getString("merchant_number");
            merchant_name = bundle.getString("merchant_name");
            due_amount_str = bundle.getString("due_amount_str");
            tip_amt_str = bundle.getString("tip_amt_str");
            apply_ngcassh = bundle.getString("apply_ngcassh");
            card_id = bundle.getString("card_id");
            card_number = bundle.getString("card_number");
            card_brand = bundle.getString("card_brand");
            customer_id = bundle.getString("customer_id");
            employee_name = bundle.getString("employee_name");
            employee_id = bundle.getString("employee_id");
        }
        if (type.equalsIgnoreCase("payemi")) {
            cart_id = bundle.getString("order_cart_id");
            Log.e("TAG", "onCreate:cart_idcart_idcart_id " + cart_id);
            payEmiMerchant(user_id, member_id, merchant_number, due_amount_str, tip_amt_str, apply_ngcassh, card_id, card_number, card_brand, customer_id);

        } else {
            payBiilMerchant(user_id, member_id, merchant_number, due_amount_str, tip_amt_str, apply_ngcassh, card_id, card_number, card_brand, customer_id);
            //Log.e("user_idd", "em" + employee_id + "em" + member_id + merchant_name + due_amount_str + tip_amt_str + apply_ngcassh + card_id + card_number + card_number + card_brand);
        }
    }

    private void payBiilMerchant(String user_id, String merchant_id, String merchant_number, String due_amount_str, String tip_amt_str, String ngcash_app_str,
                                 String card_id, String card_number, String card_brand, String customer_id) {

        Toast.makeText(this, "employee_idd" + employee_name, Toast.LENGTH_SHORT).show();

        Log.e("employee_idd", employee_id);
        Log.e("employee_name", employee_name);

        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().payBillToMerchant(mySession.getValueOf(MySession.CurrencyCode),
                user_id, merchant_id, merchant_number, due_amount_str, tip_amt_str, ngcash_app_str, card_id, card_number, card_brand, customer_id, "Paybill", time_zone, employee_id, employee_name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    try {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        Log.e("Paybill Res >", " >" + responseData);

                        if (object.getString("status").equals("1")) {
                            Log.e("object", String.valueOf(object));
                            reciept_url = object.getString("reciept_url");
                            SweetAlertDialog pDialog = new SweetAlertDialog(ManualPaybillSucess.this, SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                            pDialog.setTitleText("Your Transaction Successfully Done");
                            pDialog.setConfirmText("Ok");
                            pDialog.setCancelable(false);
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(ManualPaybillSucess.this, WebViewCalled.class);
                                    intent.putExtra("reciept_url", reciept_url);
                                    intent.putExtra("scrsts", "activity")
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    pDialog.dismissWithAnimation();
                                }
                            });

                            pDialog.show();

                        } else {

                            new SweetAlertDialog(ManualPaybillSucess.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Unsuccessful Transaction !")
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

                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();

            }
        });
    }

    private void payEmiMerchant(String user_id, String merchant_id, String merchant_number,
                                String due_amount_str, String tip_amt_str, String ngcash_app_str,
                                String card_id, String card_number, String card_brand, String customer_id) {

        Toast.makeText(this, "employee_idd" + employee_name, Toast.LENGTH_SHORT).show();
/*pay_bill_emi.php?member_id=\(USER_DEFAULT.value(forKey: MemberID)!)&merchant_id=\(self.strMerchntId!)
&merchant_no=\(text_Search.text!)&amount=\(text_AmountDue.text!)&tip_amount=\(strAmountTip!)&ngcash=\
(strNgCash!)&card_id=\(dic_SelectCard["id"] ?? "")&card_number=\(dic_SelectCard["last4"] ?? "")
&card_brand=\(dic_SelectCard["brand"] ?? "")&customer_id=\(dic_SelectCard["customer"] ?? "")
&type=Paybill&timezone=\(Calendar.current.timeZone.identifier)&employee_name=\(lbl_Employee.text ??
"")&employee_id=\(strEmployeeinviteId ?? "")&cart_id=\(strOrderCartId!)*/
        Log.e("employee_idd", employee_id);
        Log.e("employee_name", employee_name);

        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().payBillEmiToMerchant
                (mySession.getValueOf(MySession.CurrencyCode), user_id, merchant_id, merchant_number, due_amount_str, tip_amt_str,
                        ngcash_app_str, card_id, card_number, card_brand, customer_id,
                        "Paybill", time_zone, employee_id, employee_name, cart_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    try {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        Log.e("Paybill Res >", " >" + responseData);

                        if (object.getString("status").equals("1")) {
                            Log.e("object", String.valueOf(object));
                            reciept_url = object.getString("reciept_url");
                            SweetAlertDialog pDialog = new SweetAlertDialog(ManualPaybillSucess.this, SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#042587"));
                            pDialog.setTitleText("Your Transaction Successfully Done");
                            pDialog.setConfirmText("Ok");
                            pDialog.setCancelable(false);
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.dismissWithAnimation();

                                    Intent intent = new Intent(ManualPaybillSucess.this, WebViewCalled.class);
                                    intent.putExtra("reciept_url", reciept_url);
                                    intent.putExtra("scrsts", "activity")
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            pDialog.show();

                        } else {

                            new SweetAlertDialog(ManualPaybillSucess.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Unsuccessful Transaction !")
                                    .hideConfirmButton()
                                    .setCancelButton(getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            finish();
                                        }
                                    })
                                    .show();
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

                t.printStackTrace();

            }
        });
    }
}
