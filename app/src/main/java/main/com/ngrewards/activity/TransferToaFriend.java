package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import main.com.ngrewards.beanclasses.CardBean;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.R;
import main.com.ngrewards.bottumtab.MainTabActivity;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.draweractivity.BaseActivity;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.stripepaymentclasses.CreditCardFormatTextWatcher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferToaFriend extends AppCompatActivity {

    private RelativeLayout backlay;
    private TextView memname, request_tv, transfer_tv;
    private EditText amount, comment_et;
    int count = 0;
    private AutoCompleteTextView usernameauto;
    public ArrayList<MemberDetail> memberDetailArrayList;
    private Myapisession myapisession;
    private ProgressBar progresbar;
    private String aff_name = "", user_id = "", member_ngcash = "", time_zone = "", memberfull_name = "", member_id = "", comment_str = "", amount_str = "";
    private MySavedCardInfo mySavedCardInfo;
    private MySession mySession;
    private String card_id = "", apply_ngcassh = "0", card_number = "", card_brand = "", customer_id = "";
    private ArrayList<CardBean> cardBeanArrayList;
    CreditCardFormatTextWatcher tv, tv2;
    CustomCardAdp customCardAdp;
    private ExpandableHeightListView savedcardlist;
    private RelativeLayout addcardlay;
    private TextView avbngcash, applytv, card_amount_tv;
    private EditText ngcashavb;
    private double ngcash_val = 0, total_amt_calculate = 0, apply_ng_cash = 0;
    private static final int REQUEST_CODE_QR_SCAN = 3;
    private ImageView qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_toa_friend);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        qrcode = findViewById(R.id.qrcode);
        time_zone = tz.getID();
        mySavedCardInfo = new MySavedCardInfo(this);
        myapisession = new Myapisession(this);
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
            } catch (JSONException ee) {
                ee.printStackTrace();
            }
        }

        idinit();

        getUsername();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            aff_name = bundle.getString("aff_name");
            memberfull_name = bundle.getString("memberfull_name");
            member_id = bundle.getString("member_id");
            if (aff_name != null) {
                usernameauto.setText("" + aff_name);
                memname.setText("" + memberfull_name);
            }
        }

        clickevent();

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransferToaFriend.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });
    }

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
                params.put("member_id", user_id);
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
                Log.e("GetProfile Response", ">>>>>>>>>>>>" + response);

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
                        member_ngcash = jsonObject1.getString("member_ngcash");

                        if (member_ngcash == null || member_ngcash.equalsIgnoreCase("0") || member_ngcash.equalsIgnoreCase("") || member_ngcash.equalsIgnoreCase("0.0") || member_ngcash.equalsIgnoreCase("null")) {
                            avbngcash.setText("$0.00");
                        } else {
                            avbngcash.setText("$" + member_ngcash);
                            ngcash_val = Double.parseDouble((member_ngcash.replace(",", "")));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 3:
                    String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

                    try {
                        Log.e("TAG", "onActivityResultresultresultresult: "+ result);
                        /*{"murchant_name":"REACH","merchant_number":"ed58126","merchant_id":"3"}*/
                        String arr[] = result.split(",");
                     //   JSONObject jsonObject = new JSONObject(result);
                     //   String murchant_name = jsonObject.getString("murchant_name");
                        member_id    = arr[4];
                        usernameauto.setText(arr[1]);
                        memname   .setText((arr[2]));


                    } catch (Exception e) {
                         JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String murchant_name = jsonObject.getString("murchant_name");
                            String merchant_number = jsonObject.getString("merchant_number");
                            String merchant_id = jsonObject.getString("merchant_id");
                            usernameauto.setText(""+merchant_number);
                            memname   .setText((""+murchant_name));
                            member_id    = merchant_id;
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            Toast.makeText(TransferToaFriend.this, "Wrong QR Code!!!", Toast.LENGTH_SHORT).show();
                            usernameauto.setText("");
                            memname   .setText("");
                            member_id    = "";
                        }



                    }
                    break;
            }
        }
    }

    private void clickevent() {

        amount.addTextChangedListener(new TextWatcher() {

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

                String due_amt = amount.getText().toString();

                if (due_amt == null || due_amt.equalsIgnoreCase("")) {
                    due_amt = "0.0";
                }

                double wait_dob = Double.parseDouble(due_amt);

                double tot = wait_dob;
                total_amt_calculate = tot;
                card_amount_tv.setText("$ " + String.format("%.2f", new BigDecimal(tot)));
                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = tot - apply_ng_cash;
                    card_amount_tv.setText("$ " + String.format("%.2f", new BigDecimal(amt)));
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
                card_amount_tv.setText("$ " + String.format("%.2f", new BigDecimal(total_amt_calculate)));
                if (applytv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.applied))) {
                    double amt = total_amt_calculate - apply_ng_cash;
                    card_amount_tv.setText("$ " + String.format("%.2f", new BigDecimal(amt)));
                }
            }
        });

        applytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_ngcassh = ngcashavb.getText().toString();
                if (apply_ngcassh == null || apply_ngcassh.equalsIgnoreCase("") || apply_ngcassh.equalsIgnoreCase("0")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.enteramount), Toast.LENGTH_LONG).show();
                    apply_ng_cash = 0;
                    applytv.setText("" + getResources().getString(R.string.apply));
                } else {
                    apply_ng_cash = 0;
                    double apply_ng = Double.parseDouble(apply_ngcassh);
                    if (apply_ng > ngcash_val) {
                        Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.appliedamtisgreaterthanngcash), Toast.LENGTH_LONG).show();
                        applytv.setText("" + getResources().getString(R.string.apply));
                    } else {

                        if (total_amt_calculate != 0) {
                            if (total_amt_calculate < apply_ng) {
                                Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.amountgreterthentransfer), Toast.LENGTH_LONG).show();
                            } else {
                                apply_ng_cash = apply_ng;
                                double amt = total_amt_calculate - apply_ng;
                                //card_amount_tv.setText();
                                card_amount_tv.setText("$ " + String.format("%.2f", new BigDecimal(amt)));
                                applytv.setText("" + getResources().getString(R.string.applied));
                            }
                        } else {
                            Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.firstentertransamt), Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        });
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addcardlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransferToaFriend.this, AddMemberCard.class);
                startActivity(i);
            }
        });

        request_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_str = comment_et.getText().toString();
                amount_str = amount.getText().toString();
                if (member_id == null || member_id.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.enterusername), Toast.LENGTH_LONG).show();
                } else if (amount_str == null || amount_str.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.enteramount), Toast.LENGTH_LONG).show();
                } else if (comment_str == null || comment_str.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.entermessage), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(TransferToaFriend.this, TransferSuccesfully.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("member_id", member_id);
                    intent.putExtra("comment_str", comment_str);
                    intent.putExtra("amount_str", amount_str);
                    intent.putExtra("apply_ngcassh", "0");
                    intent.putExtra("card_id", "");
                    intent.putExtra("card_number", "");
                    intent.putExtra("card_brand", "");
                    intent.putExtra("customer_id", "");
                    intent.putExtra("Transfer", "Request");
                    startActivity(intent);
                }
            }
        });

        transfer_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_str = comment_et.getText().toString();
                amount_str = amount.getText().toString();
                if (member_id == null || member_id.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.enterusername), Toast.LENGTH_LONG).show();
                } else if (amount_str == null || amount_str.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.enteramount), Toast.LENGTH_LONG).show();
                } else if (comment_str == null || comment_str.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.entermessage), Toast.LENGTH_LONG).show();
                } /*else if (card_id == null || card_id.equalsIgnoreCase("")) {
                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.selectcard), Toast.LENGTH_LONG).show();
                }*/ else {

                    apply_ngcassh = ngcashavb.getText().toString();
                    if (apply_ngcassh == null || apply_ngcassh.equalsIgnoreCase("") || apply_ngcassh.equalsIgnoreCase("0")) {
                        Intent intent = new Intent(TransferToaFriend.this, TransferSuccesfully.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("member_id", member_id);
                        intent.putExtra("comment_str", comment_str);
                        intent.putExtra("amount_str", amount_str);
                        intent.putExtra("apply_ngcassh", "0");
                        intent.putExtra("card_id", card_id);
                        intent.putExtra("card_number", card_number);
                        intent.putExtra("card_brand", card_brand);
                        intent.putExtra("customer_id", customer_id);
                        intent.putExtra("Transfer", "Transfer");
                        startActivity(intent);

                    } else {
                        double apply_ng = Double.parseDouble(apply_ngcassh);
                        if (apply_ng > ngcash_val) {
                            Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.appliedamtisgreaterthanngcash), Toast.LENGTH_LONG).show();
                            applytv.setText("" + getResources().getString(R.string.apply));
                        } else {
                            applytv.setText("" + getResources().getString(R.string.applied));
                            double amount_dob = Double.parseDouble(amount_str);
                            if (amount_dob > apply_ng) {
                                if (card_id == null || card_id.equalsIgnoreCase("")) {
                                    Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.selectcard), Toast.LENGTH_LONG).show();
                                } else {
                                    Intent intent = new Intent(TransferToaFriend.this, TransferSuccesfully.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("member_id", member_id);
                                    intent.putExtra("comment_str", comment_str);
                                    intent.putExtra("amount_str", amount_str);
                                    intent.putExtra("apply_ngcassh", apply_ngcassh);
                                    intent.putExtra("card_id", card_id);
                                    intent.putExtra("card_number", card_number);
                                    intent.putExtra("card_brand", card_brand);
                                    intent.putExtra("customer_id", customer_id);
                                    intent.putExtra("Transfer", "Transfer");
                                    startActivity(intent);
                                }

                            } else {
                                Intent intent = new Intent(TransferToaFriend.this, TransferSuccesfully.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("member_id", member_id);
                                intent.putExtra("comment_str", comment_str);
                                intent.putExtra("amount_str", amount_str);
                                intent.putExtra("apply_ngcassh", apply_ngcassh);
                                intent.putExtra("card_id", card_id);
                                intent.putExtra("card_number", card_number);
                                intent.putExtra("card_brand", card_brand);
                                intent.putExtra("customer_id", customer_id);
                                intent.putExtra("Transfer", "Transfer");
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        cardBean.setDefaultCard(false);
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
                    customCardAdp = new CustomCardAdp(TransferToaFriend.this, cardBeanArrayList);
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

    public class CustomCardAdp extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;
        ArrayList<CardBean> cardBeanArrayList;

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

        public class Holder {

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
                        customCardAdp = new CustomCardAdp(TransferToaFriend.this, cardBeanArrayList);
                        savedcardlist.setAdapter(customCardAdp);
                        savedcardlist.setSelection(position);
                        customCardAdp.notifyDataSetChanged();
                    } else {
                        card_id = cardBeanArrayList.get(position).getId();
                        customer_id = cardBeanArrayList.get(position).getCustomer();
                        card_number = cardBeanArrayList.get(position).getLast4();
                        card_brand = cardBeanArrayList.get(position).getBrand();
                        cardBeanArrayList.get(pos).setDefaultCard(true);
                        customCardAdp = new CustomCardAdp(TransferToaFriend.this, cardBeanArrayList);
                        savedcardlist.setAdapter(customCardAdp);
                        savedcardlist.setSelection(position);
                        customCardAdp.notifyDataSetChanged();
                    }
                }
            });
            return rowView;
        }

    }

    private void transferrequest(String user_id, String member_id, String comment_str, String amount_str, String ngcash_app_str, String card_id, String card_number, String card_brand, String customer_id, final String type) {
        //https://myngrewards.com/demo/wp-content/plugins/webservice/all_business_list.php
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().transferorrequest(user_id, member_id, comment_str, amount_str, ngcash_app_str, card_id, card_number, card_brand, customer_id, type, time_zone);
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
                                Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.amountistransfersuccess), Toast.LENGTH_LONG).show();

                                Intent i = new Intent(TransferToaFriend.this, MainTabActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                i.putExtra("scrsts", "activity");
                                startActivity(i);
                            } else {
                                Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.paymentreqsendsucc), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(TransferToaFriend.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();
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

    private void idinit() {
        card_amount_tv = findViewById(R.id.card_amount_tv);
        ngcashavb = findViewById(R.id.ngcashavb);
        applytv = findViewById(R.id.applytv);
        avbngcash = findViewById(R.id.avbngcash);
        savedcardlist = findViewById(R.id.savedcardlist);
        savedcardlist.setExpanded(true);
        transfer_tv = findViewById(R.id.transfer_tv);
        addcardlay = findViewById(R.id.addcardlay);
        request_tv = findViewById(R.id.request_tv);
        comment_et = findViewById(R.id.comment_et);
        progresbar = findViewById(R.id.progresbar);
        amount = findViewById(R.id.amount);
        backlay = findViewById(R.id.backlay);
        memname = findViewById(R.id.memname);

        new GetProfile().execute();
/*

        if (BaseActivity.member_ngcash == null || BaseActivity.member_ngcash.equalsIgnoreCase("0") || BaseActivity.member_ngcash.equalsIgnoreCase("") || BaseActivity.member_ngcash.equalsIgnoreCase("0.0") || BaseActivity.member_ngcash.equalsIgnoreCase("null")) {
            avbngcash.setText("$0.00");
        } else {
            avbngcash.setText("$" + BaseActivity.member_ngcash);
            ngcash_val = Double.parseDouble((BaseActivity.member_ngcash.replace(",", "")));
        }
*/

        ngcashavb.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 8, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = ngcashavb.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.toString().indexOf(".") == -1) {
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

        usernameauto = findViewById(R.id.usernameauto);
        usernameauto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {
                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setUsername(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(TransferToaFriend.this, l1, "", "");
                        usernameauto.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }
                }
                count++;
            }
        });


        amount.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 8, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = amount.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.toString().indexOf(".") == -1) {
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
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private Activity context;
        private ArrayList<MemberDetail> l2 = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public GeoAutoCompleteAdapter(Activity context, ArrayList<MemberDetail> l2, String lat, String lon) {
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
                geo_search_result_text.setText(l2.get(i).getUsername());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        // checkic.setImageResource(R.drawable.check);
                        if (l2 != null && !l2.isEmpty()) {
                            ArrayList<MemberDetail> test = new ArrayList<>();
                            test.add(l2.get(i));

                            if (test != null) {
                                usernameauto.setText(test.get(0).getUsername());
                                memname.setText(test.get(0).getFullname());
                                member_id = test.get(0).getId();
                                usernameauto.dismissDropDown();
                            }

                           /*   usernameauto.setText(l2.get(i).getAffiliateName());
                            memname.setText(l2.get(i).getFullname());
                            member_id = l2.get(i).getId();
                            usernameauto.dismissDropDown();*/
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
                            if (memberDetailArrayList != null && !memberDetailArrayList.isEmpty()) {
                                for (MemberDetail wp : memberDetailArrayList) {
                                    if (wp.getUsername().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                    {
                                        l2.add(wp);
                                        Log.e("TRUE", " >> FILTER" + wp.getUsername());
                                        filterResults.values = l2;
                                        filterResults.count = l2.size();
                                    }
                                }

                            }
                        }
                        // Assign the data to the FilterResults
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l2 = (ArrayList<MemberDetail>) results.values;
                        notifyDataSetChanged();
                    } else {
                        if(l2.size()>0)
                        {
                            notifyDataSetChanged();
                        }

                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }

    private void getUsername() {
        Log.e("Get User Name>", " >GET NAME");

        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
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
//https://myngrewards.com/demo/wp-content/plugins/webservice/get_customer_stripe_card_list.php?user_id=1009
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
                       /* if (jsonArray.length()==0){
                            addcardlay.setVisibility(View.VISIBLE);
                            cardlay.setVisibility(View.GONE);
                        }else {
                            addcardlay.setVisibility(View.GONE);
                            cardlay.setVisibility(View.VISIBLE);
                        }*/
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            CardBean cardBean = new CardBean();
                          /*  if (jsonObject3.getString("id").equalsIgnoreCase(default_source)) {
                                cardBean.setDefaultCard(true);
                            } else {
                                cardBean.setDefaultCard(false);

                            }*/
                            cardBean.setDefaultCard(false);
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
                        customCardAdp = new CustomCardAdp(TransferToaFriend.this, cardBeanArrayList);
                        savedcardlist.setAdapter(customCardAdp);
                        customCardAdp.notifyDataSetChanged();

                        //  new TransferAmount().execute(customer_id);


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

}
