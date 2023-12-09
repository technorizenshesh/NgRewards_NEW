package main.com.ngrewards.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import org.json.JSONArray;
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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.CardBean;
import main.com.ngrewards.beanclasses.MarchantBean;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.stripepaymentclasses.CreditCardFormatTextWatcher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EMIManualActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 3;
    private final String ngcash_app_str = "";
    private final String order_guset_No = "";
    private final String order_Table_No = "";
    private final String order_Address_Id = "";
    private final String order_special_request = "";
    private final String order_Date = "";
    private final String order_Time = "";
    public String card_id = "", time_zone = "", member_ngcash = "", apply_ngcassh = "0", card_number = "", card_brand = "", customer_id = "";
    public ArrayList<MemberDetail> memberDetailArrayList;
    ArrayList<MerchantListBean> merchantListBeanArrayList;
    int count = 0;
    int count1 = 0;
    KeyStore keyStore;
    String KEY_NAME = "NgRewards", order_id = "", employee_id = "", employee_name = "", status_touchid
            = "", user_id = "", merchant_id = "", merchant_name = "", merchant_number = "";
    Cipher cipher;
    CreditCardFormatTextWatcher tv, tv2;
    CustomCardAdp customCardAdp;
    private TextView tenPer, fifteenPer, twentyPer, otherPer;
    private RelativeLayout backlay;
    private EditText dueamount_et, tipamount_et, ngcashavb;
    private RadioButton creditcard_rbut;
    private RadioButton paypalbut;
    private TextView total_amt, merchantname;
    private ProgressBar progresbar;
    private AutoCompleteTextView merchant_num_auto;
    private TextView paybill_tv, textView;
    private Myapisession myapisession;
    private MySession mySession;
    private MySavedCardInfo mySavedCardInfo;
    private ImageView cardimg;
    private TextView cardnumber, avbngcash, applytv, card_amount_tv;
    private LinearLayout cardlay;
    private RelativeLayout addcardlay;
    private ArrayList<CardBean> cardBeanArrayList;
    private ExpandableHeightListView savedcardlist;
    private double ngcash_val = 0, total_amt_calculate = 0, apply_ng_cash = 0;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {

                    String ngcash_str = intent.getExtras().getString("ngcash");
                    if (ngcash_str == null || ngcash_str.equalsIgnoreCase("") || ngcash_str.equalsIgnoreCase("null") || ngcash_str.equalsIgnoreCase("0")) {
                        avbngcash.setText(mySession.getValueOf(MySession.CurrencySign) + "0.00 Available");
                    } else {
                        avbngcash.setText(mySession.getValueOf(MySession.CurrencySign) + ngcash_str + " Available");
                        ngcash_val = Double.parseDouble(ngcash_str);
                    }
                }

            } catch (Exception e) {

            }
        }
    };
    private ImageView qrcode;
    private AutoCompleteTextView edt_name;
    private MerchantListBean MerchantData;
    private String sub_total_price;
    private String phone;
    private String due_amount_str = "";
    private String tip_amt_str = "";
    private String order_cart_id;
    private String result;
    private String tax_price;
    private String total_amount_due;
    private String card_amount_tv1;
    private String type;
    private TextView paybill_tv1;
    private double apply_ng;
    private String username_str;
    private String who_invite_str;
    private String quantity;
    private boolean employee;
    private String invited_user_name;
    private String employee_sales_id = "";
    private String employee_slaes_name;
    private GPSTracker gpsTracker;
    private double latitude;
    private double longitude;
    private String country_id;
    private ArrayList<String> distance_filter_list;
    private String edit_text_string = "";
    private String fullname = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_emi_manual);
        tenPer = findViewById(R.id.ten_per);
        fifteenPer = findViewById(R.id.fifteen_per);
        twentyPer = findViewById(R.id.twenty_per);
        otherPer = findViewById(R.id.other_per);

        mySession = new MySession(getApplicationContext());
        myapisession = new Myapisession(getApplicationContext());
        distance_filter_list = new ArrayList<>();
        distance_filter_list.add(getString(R.string.any_distance));
        distance_filter_list.add("5.0");
        distance_filter_list.add("10.0");
        distance_filter_list.add("20.0");
        distance_filter_list.add("50.0");
        checkGps();
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {
        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    country_id = jsonObject1.getString("country_id");
                    Log.e("country_id >>", " >" + country_id);
                    /*     i.putExtra("member_id", data.getString("member_id"));
                            i.putExtra("cart_id", data.getString("cart_id"));
                            i.putExtra("split_amount_x", data.getString("split_amount_x"));
                            i.putExtra("merchant_business_name", data.getString("merchant_business_name"));
                            i.putExtra("merchant_business_no", data.getString("merchant_business_no"));
                            i.putExtra("merchant_id", data.getString("merchant_id"));
                            i.putExtra("message", data.getString("message"));
                            i.putExtra("type", data.getString("type"));
                            i.putExtra("order_id", data.getString("order_id"));
                        */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        employee_id = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.employee_id, employee_id);
        employee_name = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.employee_name, employee_name);
        progresbar = findViewById(R.id.progresbar);
        getUsername();
        edt_name = findViewById(R.id.edt_name);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        edit_text_string = edt_name.getText().toString().trim();
        time_zone = tz.getID();
        mySavedCardInfo = new MySavedCardInfo(this);
        myapisession = new Myapisession(this);
        mySession = new MySession(EMIManualActivity.this);
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s == null || s.equals("")) {

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count1 == 0) {

                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    Log.e("l1kamal", String.valueOf(l1));

                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setAffiliateName(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter1 ga = new GeoAutoCompleteAdapter1(EMIManualActivity.this, l1, "", "");
                        edt_name.setAdapter(ga);

                        if (edit_text_string.equals(null) && edit_text_string.equals("null")) {
                            edt_name.setText("");
                        }

                        ga.notifyDataSetChanged();

                    }
                }
                count1++;
            }
        });
        Log.e("USER DATA", ">> " + user_log_data);
        if (user_log_data != null) {

            try {

                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                    status_touchid = jsonObject1.getString("touch_status");
                    user_id = jsonObject1.getString("id");
                    phone = jsonObject1.getString("phone");
                    fullname = jsonObject1.getString("fullname");
                    member_ngcash = jsonObject1.getString("member_ngcash");
                    ngcash_val = Double.parseDouble((member_ngcash.replace(",", "")));
                    Log.e("", "onCreate:  ngcash_valngcash_valngcash_val " + ngcash_val);
                }

            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }
        idint();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            /*{"member_id":"225","merchant_business_name":"For","merchant_business_no":
            "Farmers","due_date":"07\/05\/23","merchant_id":"3","number_of_emi":"2","message"
            :"pay_by_emi","type":"member","result":"successful","cart_id":"170","split_amount_x":
            " 1.71","order_id":"7484","key":"You have emi pending please pay now"}*/
            try {
                JSONObject data = new JSONObject(getIntent().getExtras().getString("object"));
                merchant_id = data.getString("merchant_id");
                merchant_name = data.getString("merchant_business_name");
                merchant_number = data.getString("merchant_business_no");
                order_cart_id = data.getString("cart_id");
                sub_total_price = data.getString("split_amount_x");
                tax_price = data.getString("split_amount_x");
                total_amount_due = data.getString("split_amount_x");
                // type = data.getString("type");
                type = "payemi";
                quantity = data.getString("split_amount_x");
                employee_sales_id = data.getString("split_amount_x");
                employee_slaes_name = data.getString("split_amount_x");
                order_id = data.getString("order_id");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (merchant_name != null) {
                merchant_num_auto.setText("" + merchant_number);
                merchantname.setText("" + merchant_name);
            }

            if (edit_text_string.equals(null) && edit_text_string.equals("null") && edit_text_string == null) {

            }

            if (total_amount_due != null) {

                dueamount_et.setText("" + total_amount_due);
                total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + total_amount_due);
                card_amount_tv.setText(total_amount_due);

            }
        }

        try {

            if (type.equals("payemi")) {

                paybill_tv = (TextView) findViewById(R.id.paybill_tv);
                paybill_tv.setText("Pay Emi");

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        clickevet();

        getBusnessNumber();

        // edt_name = findViewById(R.id.edt_name);

        qrcode = findViewById(R.id.qrcode);

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(EMIManualActivity.this, QrCodeActivity.class);
                //  startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });

        if (getIntent().getExtras() != null) {

            MerchantData = (MerchantListBean) getIntent().getExtras().getSerializable("merchant_data");

            try {

                merchant_number = MerchantData.getBusinessNo();
                merchant_id = MerchantData.getId();

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                //   merchant_name = MerchantData.getBusinessName();

            } catch (Exception e) {
                e.printStackTrace();
            }

            merchant_num_auto.setText(merchant_number);
            merchantname.setText(merchant_name);
        }
        // new GetProfile().execute();
        //getProfile();
    }

    private void checkGps() {

        gpsTracker = new GPSTracker(getApplicationContext());
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            Log.e("REFRESH_LOCATION ", " >> " + latitude);
            longitude = gpsTracker.getLongitude();
            if (latitude == 0.0) {

                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;

            }
        }
    }

    /*
        private class GetProfile extends AsyncTask<String, String, String> {
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

                try {

                    String postReceiverUrl = BaseUrl.baseurl + "member_profile.php?";
                    URL url = new URL(postReceiverUrl);
                    Map<String, Object> params = new LinkedHashMap<>();
                    params.put("user_id", user_id);
                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String, Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    Log.e("GetProfile Response", "GetProfileGetProfile urlurl>>>>>>>>>>>>" + postData);

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
                    Log.e("GetProfile Response", "GetProfileGetProfile>>>>>>>>>>>>" + user_id);

                    Log.e("GetProfile Response", "GetProfileGetProfile>>>>>>>>>>>>" + response);
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

                        JSONObject jsonObject = new JSONObject(result);

                        Log.e("jsonObjectresult", String.valueOf(jsonObject));

                        String message = jsonObject.getString("status");

                        if (message.equalsIgnoreCase("1")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                            username_str = jsonObject1.getString("affiliate_name");
                            fullname = jsonObject1.getString("fullname");
                            who_invite_str = jsonObject1.getString("how_invited_you");
                            invited_user_name = jsonObject1.getString("invited_user_name");
                            member_ngcash = jsonObject1.getString("member_ngcash");


                            edt_name.setEnabled(true);

                            if (member_ngcash == null || member_ngcash.equalsIgnoreCase("0")
                                    || member_ngcash.equalsIgnoreCase("") || member_ngcash.equalsIgnoreCase("0.0") ||
                                    member_ngcash.equalsIgnoreCase("null")) {
                                avbngcash.setText("$0.00");

                            } else {

                                avbngcash.setText("$" + member_ngcash);
                                ngcash_val = Double.parseDouble((member_ngcash.replace(",", "")));
                            }

                            Log.e("employeesalesname", "yyu" + employee_slaes_name);

                            if (employee_name == null) {

                                edt_name.setText("");

                            } else {

                            }

                            if (who_invite_str != null) {

                                who_invite_str = who_invite_str.replaceAll("(\\r|\\n)", "");
                            }

                            String social_id = jsonObject1.getString("social_id");

                            if (who_invite_str != null && !who_invite_str.equalsIgnoreCase("") && !who_invite_str.equalsIgnoreCase("0") && !who_invite_str.equalsIgnoreCase("null")) {

                            }

                            if (social_id != null && !social_id.equalsIgnoreCase("") && !social_id.equalsIgnoreCase("0") && !social_id.equalsIgnoreCase("null")) {
                                if (who_invite_str != null && !who_invite_str.equalsIgnoreCase("") && !who_invite_str.equalsIgnoreCase("0") && !who_invite_str.equalsIgnoreCase("null")) {
                                    // edt_name.setText(""+employee_slaes_name);
                                    edt_name.setEnabled(true);

                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    */
    private void getProfile() {
        Call<ResponseBody> call = ApiClient.getApiInterface().member_profile(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", "onResponse:  responseresponseresponse " + response);
                    try {
                        String responseData = response.body().toString();

                        JSONObject jsonObject = new JSONObject(responseData);

                        Log.e("jsonObjectresult", String.valueOf(jsonObject));

                        String message = jsonObject.getString("status");

                        if (message.equalsIgnoreCase("1")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                            username_str = jsonObject1.getString("affiliate_name");
                            fullname = jsonObject1.getString("fullname");
                            who_invite_str = jsonObject1.getString("how_invited_you");
                            invited_user_name = jsonObject1.getString("invited_user_name");
                            member_ngcash = jsonObject1.getString("member_ngcash");


                            edt_name.setEnabled(true);

                            if (member_ngcash == null || member_ngcash.equalsIgnoreCase("0")
                                    || member_ngcash.equalsIgnoreCase("") || member_ngcash.equalsIgnoreCase("0.0") ||
                                    member_ngcash.equalsIgnoreCase("null")) {
                                avbngcash.setText(mySession.getValueOf(MySession.CurrencySign) + "0.00");

                            } else {

                                avbngcash.setText(mySession.getValueOf(MySession.CurrencySign) + member_ngcash);
                                ngcash_val = Double.parseDouble((member_ngcash.replace(",", "")));
                            }

                            Log.e("employeesalesname", "yyu" + employee_slaes_name);

                            if (employee_name == null) {

                                edt_name.setText("");

                            } else {

                            }

                            if (who_invite_str != null) {

                                who_invite_str = who_invite_str.replaceAll("(\\r|\\n)", "");
                            }

                            String social_id = jsonObject1.getString("social_id");

                            if (who_invite_str != null && !who_invite_str.equalsIgnoreCase("") && !who_invite_str.equalsIgnoreCase("0") && !who_invite_str.equalsIgnoreCase("null")) {

                            }

                            if (social_id != null && !social_id.equalsIgnoreCase("") && !social_id.equalsIgnoreCase("0") && !social_id.equalsIgnoreCase("null")) {
                                if (who_invite_str != null && !who_invite_str.equalsIgnoreCase("") && !who_invite_str.equalsIgnoreCase("0") && !who_invite_str.equalsIgnoreCase("null")) {
                                    // edt_name.setText(""+employee_slaes_name);
                                    edt_name.setEnabled(true);

                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getUsername() {
        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername(user_id, mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("User", responseData);
                        Log.e("User name list>", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyMemberusername(responseData);
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);
                            memberDetailArrayList.addAll(successData.getResult());
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

                Log.e("TAG", t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 3) {
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

                try {

                    String[] arr = result.split(",");
                    edt_name.setText(arr[1]);
                    employee_name = arr[1];
                    employee_id = arr[4];
                    edt_name.setVisibility(View.VISIBLE);
                    employee = true;

                    Log.e("employee_iddddd", result);
                    // edt_name.setText(employee_name);

                    PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.employee_id, employee_id);
                    PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.employee_name, employee_name);


                } catch (Exception e) {
                    Toast.makeText(EMIManualActivity.this, "Wrong QR Code!!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private void clickevet() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        paypalbut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                creditcard_rbut.setChecked(!isChecked);
            }
        });

        creditcard_rbut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                paypalbut.setChecked(!isChecked);
            }
        });

        applytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("total_amt_calculate", String.valueOf(ngcash_val));

                String tip_amt = tipamount_et.getText().toString();
                String due_amt = dueamount_et.getText().toString();

                if (tip_amt == null || tip_amt.equalsIgnoreCase("")) {
                    tip_amt = "0.0";
                }
                if (due_amt == null || due_amt.equalsIgnoreCase("")) {
                    due_amt = "0.0";
                }

                double sp_dob = Double.parseDouble(tip_amt);
                double wait_dob = Double.parseDouble(due_amt);

                double tot = sp_dob + wait_dob;

                total_amt_calculate = tot;

                total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));

                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = tot - apply_ng_cash;
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                }

                try {
                    apply_ngcassh = ngcashavb.getText().toString();
                    apply_ng = Double.parseDouble(apply_ngcassh);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("apply_ng", String.valueOf(apply_ng));

                if (ngcash_val < apply_ng) {
                    Toast.makeText(EMIManualActivity.this, getResources().getString(R.string.amountgreterthendue), Toast.LENGTH_LONG).show();
                } else {
                    apply_ng_cash = apply_ng;
                    double amt = total_amt_calculate - apply_ng;
                    //card_amount_tv.setText();
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                    applytv.setText("" + getResources().getString(R.string.applied));
                }

            }
        });


        tenPer.setOnClickListener(
                v -> {
                    if (!dueamount_et.getText().toString().equalsIgnoreCase("")) {
                        String due_amt = dueamount_et.getText().toString();
                        String tip_amt = "" + Double.parseDouble(due_amt) * 10 / 100;
                        if (tip_amt.equalsIgnoreCase("")) {
                            tip_amt = "0.0";
                        }
                        if (due_amt.equalsIgnoreCase("")) {
                            due_amt = "0.0";
                        }
                        double sp_dob = Double.parseDouble(tip_amt);
                        double wait_dob = Double.parseDouble(due_amt);
                        double tot = sp_dob + wait_dob;
                        total_amt_calculate = tot;
                        total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                        card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));
                        tipamount_et.setText(String.format("%.2f", new BigDecimal(sp_dob)));
                        if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                            double amt = tot - apply_ng_cash;
                            card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                        }
                    }

                });
        fifteenPer.setOnClickListener(
                v -> {
                    if (!dueamount_et.getText().toString().equalsIgnoreCase("")) {
                        String due_amt = dueamount_et.getText().toString();
                        String tip_amt = "" + Double.parseDouble(due_amt) * 15 / 100;
                        if (tip_amt.equalsIgnoreCase("")) {
                            tip_amt = "0.0";
                        }
                        if (due_amt.equalsIgnoreCase("")) {
                            due_amt = "0.0";
                        }
                        double sp_dob = Double.parseDouble(tip_amt);
                        double wait_dob = Double.parseDouble(due_amt);
                        double tot = sp_dob + wait_dob;
                        total_amt_calculate = tot;
                        total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                        card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));
                        tipamount_et.setText(String.format("%.2f", new BigDecimal(sp_dob)));
                        if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                            double amt = tot - apply_ng_cash;
                            card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                        }
                    }

                });
        twentyPer.setOnClickListener(
                v -> {
                    if (!dueamount_et.getText().toString().equalsIgnoreCase("")) {
                        String due_amt = dueamount_et.getText().toString();
                        String tip_amt = "" + Double.parseDouble(due_amt) * 20 / 100;
                        if (tip_amt.equalsIgnoreCase("")) {
                            tip_amt = "0.0";
                        }
                        if (due_amt.equalsIgnoreCase("")) {
                            due_amt = "0.0";
                        }
                        double sp_dob = Double.parseDouble(tip_amt);
                        double wait_dob = Double.parseDouble(due_amt);
                        double tot = sp_dob + wait_dob;
                        total_amt_calculate = tot;
                        total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                        card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));
                        tipamount_et.setText(String.format("%.2f", new BigDecimal(sp_dob)));
                        if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                            double amt = tot - apply_ng_cash;
                            card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                        }
                    }

                });

        otherPer.setOnClickListener(
                v -> {
                    try {
                        if (!dueamount_et.getText().toString().equalsIgnoreCase("")) {
                            tipamount_et.setEnabled(true);
                            tipamount_et.setText("");
                            tipamount_et.requestFocus();
                            total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " "
                                    + String.format("%.2f", new BigDecimal(dueamount_et.getText().toString())));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        tipamount_et.addTextChangedListener(new TextWatcher() {

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
                String tip_amt = tipamount_et.getText().toString();
                String due_amt = dueamount_et.getText().toString();

                if (tip_amt == null || tip_amt.equalsIgnoreCase("")) {
                    tip_amt = "0.0";
                }
                if (due_amt == null || due_amt.equalsIgnoreCase("")) {
                    due_amt = "0.0";
                }

                double sp_dob = Double.parseDouble(tip_amt);
                double wait_dob = Double.parseDouble(due_amt);
                double tot = sp_dob + wait_dob;
                total_amt_calculate = tot;
                total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));

                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = tot - apply_ng_cash;
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                }
            }
        });
        tipamount_et.addTextChangedListener(new TextWatcher() {

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

                Log.e("TAG", "afterTextChanged: tip_amttip_amt  " + s.toString());
                String tip_amt = tipamount_et.getText().toString();
                String due_amt = dueamount_et.getText().toString();
                Log.e("TAG", "afterTextChanged: tip_amttip_amt  " + tip_amt);
                Log.e("TAG", "afterTextChanged: due_amtdue_amt" + due_amt);

                if (tip_amt.equalsIgnoreCase("")) {
                    tip_amt = "0.0";
                }
                if (due_amt.equalsIgnoreCase("")) {
                    due_amt = "0.0";
                }

                double sp_dob = Double.parseDouble(tip_amt);
                double wait_dob = Double.parseDouble(due_amt);

                double tot = sp_dob + wait_dob;

                total_amt_calculate = tot;
                total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));

                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = tot - apply_ng_cash;
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                }
            }

        });

        ngcashavb.addTextChangedListener(new TextWatcher() {

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
                applytv.setText("" + getResources().getString(R.string.apply));
                //  grandtotal.setText(" " + tot);

                try {
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(total_amount_due)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = total_amt_calculate - apply_ng_cash;
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                }
            }
        });


        dueamount_et.addTextChangedListener(new TextWatcher() {

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
                String tip_amt = tipamount_et.getText().toString();
                String due_amt = dueamount_et.getText().toString();

                if (tip_amt == null || tip_amt.equalsIgnoreCase("")) {
                    tip_amt = "0.0";
                }

                if (due_amt == null || due_amt.equalsIgnoreCase("")) {
                    due_amt = "0.0";
                }

                double sp_dob = Double.parseDouble(tip_amt);
                double wait_dob = Double.parseDouble(due_amt);

                double tot = sp_dob + wait_dob;
                total_amt_calculate = tot;
                total_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(tot)));
                card_amount_tv.setText(String.format("%.2f", new BigDecimal(tot)));
                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = tot - apply_ng_cash;
                    card_amount_tv.setText(String.format("%.2f", new BigDecimal(amt)));
                }
            }
        });

        paybill_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                due_amount_str = dueamount_et.getText().toString();

                tip_amt_str = tipamount_et.getText().toString();
                merchant_number = merchant_num_auto.getText().toString();
                if (due_amount_str == null || due_amount_str.equalsIgnoreCase("") || due_amount_str.equalsIgnoreCase("0") || due_amount_str.equalsIgnoreCase("0.00") || due_amount_str.equalsIgnoreCase("0.0")) {
                    Toast.makeText(EMIManualActivity.this, getResources().getString(R.string.enterdueamt), Toast.LENGTH_LONG).show();

                } else if (card_id == null || card_id.equalsIgnoreCase("")) {
                    Toast.makeText(EMIManualActivity.this, getResources().getString(R.string.selectpaymentmethod), Toast.LENGTH_LONG).show();

                } else {

                    // Toast.makeText(getApplicationContext(), "emplouyyee_id" + employee_name +"tesst"+ employee_slaes_name, Toast.LENGTH_LONG).show();

                    apply_ngcassh = ngcashavb.getText().toString();

                    if (apply_ngcassh != null || !apply_ngcassh.equalsIgnoreCase("") || !apply_ngcassh.equalsIgnoreCase("0")) {

                        if (type.equals("payemi")) {

                            employee_name = edt_name.getText().toString().trim();
                            card_amount_tv1 = card_amount_tv.getText().toString().trim();

                            Intent intent = new Intent(EMIManualActivity.this, ManualPaybillSucess.class);
                            intent.putExtra("type", type);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("merchant_id", merchant_id);
                            intent.putExtra("merchant_number", merchant_number);
                            intent.putExtra("due_amount_str", due_amount_str);
                            intent.putExtra("tip_amt_str", tip_amt_str);
                            intent.putExtra("apply_ngcassh", apply_ngcassh);
                            intent.putExtra("card_id", card_id);
                            intent.putExtra("card_number", card_number);
                            intent.putExtra("card_brand", card_brand);
                            intent.putExtra("customer_id", customer_id);
                            intent.putExtra("order_cart_id", order_cart_id);
                            intent.putExtra("merchant_name", merchant_name);
                            intent.putExtra("employee_name", employee_name);

                            if (employee) {

                                intent.putExtra("employee_id", employee_id);

                            } else {

                                intent.putExtra("employee_id", employee_id);

                            }

                            intent.putExtra("quantity", quantity);
                            startActivity(intent);
                            //finish();


                            //  payBiilMerchant(user_id, merchant_id, merchant_number, due_amount_str, tip_amt_str, apply_ngcassh, card_id, card_number, card_brand, customer_id);
                        } else {

                            //TODO:=========PayOrderBill==============
                            PayOrderBill(order_cart_id);
                        }

                    } else {

                        due_amount_str = total_amt.getText().toString();
                        tip_amt_str = tipamount_et.getText().toString();
                        int TotalAmount = Integer.parseInt(due_amount_str);

                        //      Toast.makeText(getApplicationContext(), "test123  : >>>  " + employee_id, Toast.LENGTH_SHORT).show();

                        merchant_number = merchant_num_auto.getText().toString();

                        double apply_ng = Double.parseDouble(apply_ngcassh);

                        if (apply_ng > ngcash_val) {

                            Toast.makeText(EMIManualActivity.this, getResources().getString(R.string.appliedamtisgreaterthanngcash), Toast.LENGTH_LONG).show();
                            applytv.setText("" + getResources().getString(R.string.apply));

                        } else {
                            if (total_amt_calculate < apply_ng) {

                                Toast.makeText(EMIManualActivity.this, getResources().getString(R.string.amountgreterthendue), Toast.LENGTH_LONG).show();
                            } else {

                                applytv.setText("" + getResources().getString(R.string.applied));

                                Log.e("user_id >> ", " >> " + user_id);

                                if (type.equals("payemi")) {

                                    apply_ngcassh = ngcashavb.getText().toString();

                                    //  Toast.makeText(getApplicationContext(),"dssuccess!!!!",Toast.LENGTH_LONG).show();

                                    card_amount_tv1 = card_amount_tv.getText().toString().trim();
                                    Log.e("card_amount_tv1", card_amount_tv1);
                                    Log.e("customer_id", customer_id);

                                    Intent intent = new Intent(EMIManualActivity.this, ManualPaybillSucess.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("type", type);

                                    intent.putExtra("member_id", merchant_id);
                                    intent.putExtra("merchant_number", merchant_number);
                                    intent.putExtra("due_amount_str", "" + TotalAmount);
                                    intent.putExtra("tip_amt_str", tip_amt_str);
                                    intent.putExtra("apply_ngcassh", apply_ngcassh);
                                    intent.putExtra("card_id", card_id);
                                    intent.putExtra("card_number", card_number);
                                    intent.putExtra("card_brand", card_brand);
                                    intent.putExtra("customer_id", customer_id);
                                    intent.putExtra("order_cart_id", order_cart_id);
                                    intent.putExtra("merchant_name", merchant_name);
                                    intent.putExtra("employee_name", employee_name);

                                    if (employee) {
                                        intent.putExtra("employee_id", employee_id);
                                    } else {
                                        intent.putExtra("employee_id", employee_sales_id);
                                    }

                                    intent.putExtra("quantity", quantity);
                                    startActivity(intent);
                                    /*finish();*/


                                    // payBiilMerchant(user_id, merchant_id, merchant_number, due_amount_str, tip_amt_str, apply_ngcassh, card_id, card_number, card_brand, customer_id);
                                } else {
                                    //TODO:=========PayOrderBill==============
                                    PayOrderBill(order_cart_id);
                                }

                            }

                        }
                    }


                }

            }
        });

        addcardlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EMIManualActivity.this, AddMemberCard.class);
                startActivity(i);
            }
        });
    }

    private void idint() {

        card_amount_tv = findViewById(R.id.card_amount_tv);
        avbngcash = findViewById(R.id.avbngcash);
        applytv = findViewById(R.id.applytv);
        savedcardlist = findViewById(R.id.savedcardlist);
        savedcardlist.setExpanded(true);
        addcardlay = findViewById(R.id.addcardlay);
        cardlay = findViewById(R.id.cardlay);
        cardnumber = findViewById(R.id.cardnumber);
        tv = new CreditCardFormatTextWatcher(cardnumber);
        cardnumber.addTextChangedListener(tv);
        cardimg = findViewById(R.id.cardimg);
        paybill_tv = findViewById(R.id.paybill_tv);
        merchantname = findViewById(R.id.merchantname);
        merchant_num_auto = findViewById(R.id.merchant_num_auto);
        progresbar = findViewById(R.id.progresbar);
        total_amt = findViewById(R.id.total_amt);
        tipamount_et = findViewById(R.id.tipamount_et);
        ngcashavb = findViewById(R.id.ngcashavb);
        backlay = findViewById(R.id.backlay);
        dueamount_et = findViewById(R.id.dueamount_et);
        creditcard_rbut = findViewById(R.id.creditcard_rbut);
        paypalbut = findViewById(R.id.paypalbut);
        edt_name = findViewById(R.id.edt_name);
        avbngcash.setText(mySession.getValueOf(MySession.CurrencySign) + member_ngcash);

        ngcashavb.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    final int beforeDecimal = 8;
                    final int afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = ngcashavb.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });


    /*    tipamount_et.setFilters(new InputFilter[]{

                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    final int beforeDecimal = 8;
                    final int afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String temp = tipamount_et.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
          });*/

        dueamount_et.setFilters(new InputFilter[]{

                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    final int beforeDecimal = 8;
                    final int afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = dueamount_et.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });

        merchant_num_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s == null || s.equals("")) {
                    merchantname.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {
                    Log.e("FIRST", "KK");
                    ArrayList<MerchantListBean> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MerchantListBean memberlist = new MerchantListBean();
                        memberlist.setBusinessNo(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(EMIManualActivity.this, l1, "", "");
                        merchant_num_auto.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }
                }

                count++;

            }
        });

    }

    private void payBiilMerchant(String user_id, String merchant_id, String merchant_number, String due_amount_str, String tip_amt_str, String ngcash_app_str, String card_id, String card_number, String card_brand, String customer_id) {
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().payBillToMerchant(mySession.getValueOf(MySession.CurrencyCode), user_id, merchant_id, merchant_number, due_amount_str, tip_amt_str, ngcash_app_str, card_id, card_number, card_brand, customer_id, "Paybill", time_zone, employee_id, employee_name);
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

                            Intent intent = new Intent(EMIManualActivity.this, OrderPaidSuccesfully.class);
                            intent.putExtra("order_cart_id", order_cart_id);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(EMIManualActivity.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();
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

                Log.e("TAG", t.toString());
            }
        });
    }

    private void getBusnessNumber() {
        progresbar.setVisibility(View.VISIBLE);
        merchantListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantBusNum(mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Get_Business_Number >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyBusinessnumber(responseData);
                            MarchantBean successData = new Gson().fromJson(responseData, MarchantBean.class);
                            merchantListBeanArrayList.addAll(successData.getResult());
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

                Log.e("TAG", t.toString());
            }
        });
    }

    private void PayOrderBill(String order_cart_id) {

        employee_name = edt_name.getText().toString().trim();
        card_amount_tv1 = card_amount_tv.getText().toString().trim();

        //Toast.makeText(this, "test  : >>>  " + employee_id, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EMIManualActivity.this, OrderPaidSuccesfully.class);

        intent.putExtra("user_id", user_id);
        intent.putExtra("merchant_id", merchant_id);
        intent.putExtra("merchant_number", merchant_number);
        intent.putExtra("due_amount_str", due_amount_str);
        intent.putExtra("tip_amt_str", tipamount_et.getText().toString());
        intent.putExtra("apply_ngcassh", apply_ngcassh);
        intent.putExtra("card_id", card_id);
        intent.putExtra("card_number", card_number);
        intent.putExtra("card_brand", card_brand);
        intent.putExtra("customer_id", customer_id);
        intent.putExtra("order_cart_id", order_cart_id);
        intent.putExtra("merchant_name", merchant_name);
        intent.putExtra("employee_name", employee_name);

        if (employee) {

            intent.putExtra("employee_id", employee_id);

        } else {

            intent.putExtra("employee_id", employee_id);
        }

        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    private void fingerPrintLay() {
        final Dialog dialogSts = new Dialog(EMIManualActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.fingerprint_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);


        textView = (TextView) dialogSts.findViewById(R.id.errorText);
        TextView cancel_tv = (TextView) dialogSts.findViewById(R.id.cancel_tv);
        if (!fingerprintManager.isHardwareDetected()) {

            textView.setText("Your Device does not have a Fingerprint Sensor");
        } else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText("Fingerprint authentication permission not enabled");
            } else {
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    textView.setText("Register at least one fingerprint in Settings");
                } else {
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        textView.setText("Lock screen security not enabled in Settings");
                    } else {
                        generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(EMIManualActivity.this, textView, "Payment");
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException
                 | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("Unseen Count"));

        if (mySavedCardInfo.getKeyCarddata() != null && !mySavedCardInfo.getKeyCarddata().isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(mySavedCardInfo.getKeyCarddata());
                cardBeanArrayList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("sources");
                    String customer_id = jsonObject1.getString("id");
                    Log.e("customer_id >> ", " >> " + customer_id);
                    String default_source = "";
                    if (jsonObject1.has("default_source")) {
                        default_source = jsonObject1.getString("default_source");
                    }

                    JSONArray jsonArray = jsonObject2.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        CardBean cardBean = new CardBean();
                        if (jsonObject3.getString("id").equalsIgnoreCase(default_source)) {
                            cardBean.setDefaultCard(false);
                        } else {
                            cardBean.setDefaultCard(false);

                        }

                        cardBean.setId(jsonObject3.getString("id"));
                        cardBean.setLast4(jsonObject3.getString("last4"));
                        cardBean.setExp_month(jsonObject3.getString("exp_month"));
                        cardBean.setExp_year(jsonObject3.getString("exp_year"));
                        cardBean.setBrand(jsonObject3.getString("brand"));
                        cardBean.setFunding(jsonObject3.getString("funding"));
                        cardBean.setCustomer(jsonObject3.getString("customer"));
                        cardBean.setCard_name(jsonObject3.getString("name"));

                        String star = "************";
                        String cardlastfour = jsonObject3.getString("last4");
                        cardBean.setSetfullcardnumber(star + cardlastfour);
                        cardBean.setSetfullexpyearmonth(jsonObject3.getString("exp_month") + "/" + jsonObject3.getString("exp_year"));
                        cardBeanArrayList.add(cardBean);

                    }

                    customCardAdp = new CustomCardAdp(EMIManualActivity.this, cardBeanArrayList);
                    savedcardlist.setAdapter(customCardAdp);
                    customCardAdp.notifyDataSetChanged();


                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            new GetAddedCard().execute();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    class GeoAutoCompleteAdapter1 extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private ArrayList<MemberDetail> l21 = new ArrayList<>();

        public GeoAutoCompleteAdapter1(Activity context, ArrayList<MemberDetail> l2, String lat, String lon) {
            this.context = context;
            this.l21 = l2;
            layoutInflater = LayoutInflater.from(context);
            Log.e("FIRST", "CONS");
        }

        @Override
        public int getCount() {

            return l21 == null ? 0 : l21.size();
        }

        @Override
        public Object getItem(int i) {
            return l21.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
            TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
            try {
                geo_search_result_text.setText(l21.get(i).getAffiliateName());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        if (l21 != null && !l21.isEmpty()) {
                            ArrayList<MemberDetail> test = new ArrayList<>();
                            test.add(l21.get(i));

                            Log.e("test123456", String.valueOf(test));

                            if (test != null) {

                                edt_name.setText(test.get(0).getAffiliateName());
                                edt_name.dismissDropDown();
                            }

                        }

                    }
                });

            } catch (Exception e) {

            }

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter1 = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        if (constraint.length() == 0) {

                        } else {
                            l21.clear();
                            if (memberDetailArrayList != null && !memberDetailArrayList.isEmpty()) {
                                for (MemberDetail wp : memberDetailArrayList) {
                                    if (wp.getAffiliateName().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                    {
                                        Log.e("TRUE", " >> FILTER" + wp.getAffiliateName());
                                        Log.e("TRUEE", " >> FILTERID" + wp.getId());
                                        employee_id = wp.getId();
                                        l21.add(wp);
                                    }
                                }

                            }
                        }
                        // Assign the data to the FilterResults
                        filterResults.values = l21;
                        filterResults.count = l21.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l21 = (ArrayList<MemberDetail>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter1;
        }
    }

    public class CustomCardAdp extends BaseAdapter {
        Context context;
        ArrayList<CardBean> cardBeanArrayList;
        private LayoutInflater inflater = null;

        public CustomCardAdp(Context contexts, ArrayList<CardBean> cardBeanArrayList) {
            this.context = contexts;
            this.cardBeanArrayList = cardBeanArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cardBeanArrayList == null ? 0 : cardBeanArrayList.size();
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder;
            holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.custom_manual_card_lay, null);
            RadioButton creditcard_rbut = rowView.findViewById(R.id.creditcard_rbut);

            if (cardBeanArrayList.get(position).isDefaultCard()) {
                creditcard_rbut.setChecked(true);
                card_id = cardBeanArrayList.get(position).getId();
                customer_id = cardBeanArrayList.get(position).getCustomer();
                card_number = cardBeanArrayList.get(position).getLast4();
                card_brand = cardBeanArrayList.get(position).getBrand();

            } else {
                creditcard_rbut.setChecked(false);
            }

            TextView cardnumber = rowView.findViewById(R.id.cardnumber);
            ImageView cardimg = rowView.findViewById(R.id.cardimg);
            TextView cardholdername = rowView.findViewById(R.id.cardholdername);
            TextView expiresdate = rowView.findViewById(R.id.expiresdate);

            String cardbrand = cardBeanArrayList.get(position).getBrand();
            String carnum = cardBeanArrayList.get(position).getLast4();
            cardholdername.setText("" + cardBeanArrayList.get(position).getCard_name());

            if (cardbrand.length() > 4) {
                cardbrand = cardbrand.substring(0, 4);
            }

            String stars = "**** ****";
            cardnumber.setText("" + cardbrand + " " + stars + " " + carnum);
            expiresdate.setText(getResources().getString(R.string.validtill) + " " + cardBeanArrayList.get(position).getSetfullexpyearmonth());

            creditcard_rbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = position;
                    if (!cardBeanArrayList.get(position).isDefaultCard()) {

                        for (int k = 0; k < cardBeanArrayList.size(); k++) {
                            if (pos == k) {
                                if (cardBeanArrayList.get(k).isDefaultCard()) {
                                    cardBeanArrayList.get(k).setDefaultCard(false);
                                    card_id = "";
                                    customer_id = "";
                                    card_number = "";
                                } else {
                                    card_id = cardBeanArrayList.get(position).getId();
                                    customer_id = cardBeanArrayList.get(position).getCustomer();
                                    card_number = cardBeanArrayList.get(position).getLast4();
                                    card_brand = cardBeanArrayList.get(position).getBrand();
                                    cardBeanArrayList.get(k).setDefaultCard(true);

                                }
                            } else {

                                cardBeanArrayList.get(k).setDefaultCard(false);
                            }
                        }
                        customCardAdp = new CustomCardAdp(EMIManualActivity.this, cardBeanArrayList);
                        savedcardlist.setAdapter(customCardAdp);
                        savedcardlist.setSelection(position);
                        customCardAdp.notifyDataSetChanged();

                    } else {

                        card_id = cardBeanArrayList.get(position).getId();
                        customer_id = cardBeanArrayList.get(position).getCustomer();
                        card_number = cardBeanArrayList.get(position).getLast4();
                        card_brand = cardBeanArrayList.get(position).getBrand();
                        cardBeanArrayList.get(pos).setDefaultCard(true);


                        customCardAdp = new CustomCardAdp(EMIManualActivity.this, cardBeanArrayList);
                        savedcardlist.setAdapter(customCardAdp);
                        savedcardlist.setSelection(position);
                        customCardAdp.notifyDataSetChanged();
                    }

                }
            });
            return rowView;
        }

        public class Holder {

        }
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private ArrayList<MerchantListBean> l2 = new ArrayList<>();

        public GeoAutoCompleteAdapter(Activity context, ArrayList<MerchantListBean> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            layoutInflater = LayoutInflater.from(context);
            Log.e("FIRST", "CONS");
        }

        @Override
        public int getCount() {

            return l2 == null ? 0 : l2.size();
        }

        @Override
        public Object getItem(int i) {
            return l2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
            TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
            try {
                geo_search_result_text.setText(l2.get(i).getBusinessNo());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        Log.e("ADP LIST", " >> " + l2);
                        if (l2 != null) {

                            try {
                                merchant_id = l2.get(i).getId();
                                merchant_num_auto.setText(l2.get(i).getBusinessNo());
                                merchantname.setText(l2.get(i).getBusinessName());
                                merchant_num_auto.dismissDropDown();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });

            } catch (Exception e) {

            }

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {


                        if (constraint.length() == 0) {
                        } else {
                            l2.clear();
                            for (MerchantListBean wp : merchantListBeanArrayList) {
                                if (wp.getBusinessNo().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                {
                                    Log.e("TRUE", " >> FILTER" + wp.getBusinessNo());
                                    l2.add(wp);
                                }
                            }
                        }
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l2 = (ArrayList<MerchantListBean>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }

    private class GetAddedCard extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            cardBeanArrayList = new ArrayList<>();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                String postReceiverUrl = BaseUrl.baseurl + "get_customer_stripe_card_list.php?";

                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);

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
                Log.e("Json Login Response", ">>>>>>>>>>>>" + response);
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
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String default_source = "";
                        if (jsonObject1.has("default_source")) {
                            default_source = jsonObject1.getString("default_source");
                        }

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("sources");
                        String customer_id = jsonObject1.getString("id");
                        Log.e("customer_id >> ", " >> " + customer_id);
                        mySavedCardInfo.setKeyCarddata(result);

                        JSONArray jsonArray = jsonObject2.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            CardBean cardBean = new CardBean();
                            cardBean.setDefaultCard(jsonObject3.getString("id").equalsIgnoreCase(default_source));

                            cardBean.setId(jsonObject3.getString("id"));
                            cardBean.setLast4(jsonObject3.getString("last4"));
                            cardBean.setExp_month(jsonObject3.getString("exp_month"));
                            cardBean.setExp_year(jsonObject3.getString("exp_year"));
                            cardBean.setBrand(jsonObject3.getString("brand"));
                            cardBean.setFunding(jsonObject3.getString("funding"));
                            cardBean.setCustomer(jsonObject3.getString("customer"));
                            cardBean.setCard_name(jsonObject3.getString("name"));
                            String star = "************";
                            String cardlastfour = jsonObject3.getString("last4");
                            cardBean.setSetfullcardnumber(star + cardlastfour);
                            cardBean.setSetfullexpyearmonth(jsonObject3.getString("exp_month") + "/" + jsonObject3.getString("exp_year"));
                            cardBeanArrayList.add(cardBean);

                        }

                        customCardAdp = new CustomCardAdp(EMIManualActivity.this, cardBeanArrayList);
                        savedcardlist.setAdapter(customCardAdp);
                        customCardAdp.notifyDataSetChanged();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        }
    }

}