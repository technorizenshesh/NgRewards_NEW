package main.com.ngrewards.marchant.stripemerchantclasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.stripepaymentclasses.CreditCardFormatTextWatcher;
import main.com.ngrewards.stripepaymentclasses.Utils;

public class AddStripeConnectAccount extends AppCompatActivity {

    private final String accountid = "";
    int month, year_int;
    ArrayList<String> modellist;
    ArrayList<String> datelist;
    CreditCardFormatTextWatcher tv, tv2;
    private RelativeLayout backlay;
    private EditText email_address;
    private CheckBox acceptcondition;
    private TextView submit, myaccountemail, addcardbut;
    private ProgressBar prgressbar;
    private boolean asc_sts = true;
    private MySession mySession;
    private LinearLayout alreadyavbacountlay, accountcreate, adddebitcardlay;
    private EditText cardname, cardnumber, security_code;
    private String cardname_str = "", cardnumber_str = "", security_code_str = "";
    private Spinner yearspinner, datespinner;
    private String expiryyear_str = "", date_str = "", user_id = "", card_id = "";
    private BasicCustomAdp basicCustomAdp;
    private String token_id = "";
    private String email_str = "";
    private String stripe_account_id = "";
    private TextView savedcardnumber, validdate, cardbrand, cardtype;
    private LinearLayout savecardlay;
    private ImageView delete_card;

    private static int getLastModelYear() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -10);
        return prevYear.get(Calendar.YEAR);
    }

    private static int getThisYear() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, 0);
        return prevYear.get(Calendar.YEAR);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stripe_connect_account);
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
        datelist = new ArrayList<>();
        modellist = new ArrayList<>();
        int year = getLastModelYear();
        int thisyear = getThisYear();
        for (int i = 0; i <= 10; i++) {
            int nextyear = thisyear + i;
            modellist.add("" + nextyear);
        }
        // modellist.add("" + thisyear);
        for (int i = 1; i < 13; i++) {
            String dates = String.valueOf(i);
            if (i < 10) {
                dates = "0" + dates;
            }
            datelist.add("" + dates);
        }

        idninit();
        clickevent();
        new GetAlreadyAccountDetail().execute();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addcardbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardname_str = cardname.getText().toString();
                cardnumber_str = cardnumber.getText().toString();
                security_code_str = security_code.getText().toString();
                if (cardname_str == null || cardname_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.entercardname), Toast.LENGTH_LONG).show();
                } else if (cardnumber_str == null || cardnumber_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.entercardnumber), Toast.LENGTH_LONG).show();

                } else if (date_str == null || date_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.selectexpdate), Toast.LENGTH_LONG).show();

                } else if (expiryyear_str == null || expiryyear_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.selexpyear), Toast.LENGTH_LONG).show();

                } else if (expiryyear_str == null || expiryyear_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.selexpyear), Toast.LENGTH_LONG).show();

                } else {

                    month = Integer.parseInt(date_str);
                    year_int = Integer.parseInt(expiryyear_str);

                    onClickSomething(cardnumber_str, month, year_int, security_code_str);
                    CardParams cardParams = new CardParams(cardnumber_str, month, year_int, security_code_str);
                    cardParams.setCurrency(mySession.getValueOf(MySession.CurrencyCode));

                    Stripe stripe = new Stripe(AddStripeConnectAccount.this, BaseUrl.stripe_publish);
                    prgressbar.setVisibility(View.VISIBLE);
                    stripe.createCardToken(
                            cardParams,
                            new ApiResultCallback<Token>() {
                                @Override
                                public void onSuccess(@NonNull Token token) {
                                    // Send token to your server
                                    prgressbar.setVisibility(View.GONE);
                                    Log.e("Token", ">>" + token);

                                    token_id = token.getId();
                                    paymentwithcard();

                                }

                                @Override
                                public void onError(@NonNull Exception e) {
                                    prgressbar.setVisibility(View.GONE);
                                    Log.e("Eeeeeeeeeeeeeeerrrrr", ">>" + e);
                                    // Show localized error message
                                    Toast.makeText(AddStripeConnectAccount.this, "\n" + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            });
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_str = email_address.getText().toString();
                if (email_str == null || email_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.entervalidemailaddress), Toast.LENGTH_LONG).show();
                } else {
                    if (acceptcondition.isChecked()) {
                        if (asc_sts) {
                            new CreateStripeAccount().execute();
                        } else {
                            Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.plswait), Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.acceptstrterms), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void onClickSomething(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        CardParams cardParams = new CardParams(cardNumber, cardExpMonth, cardExpYear, cardCVC);
    }

    private void paymentwithcard() {
        // Tag used to cancel the request
        if (Utils.isConnected(getApplicationContext())) {
            new CreateCardCustomer().execute();
            //  new CreateTestPayment().execute();

        } else {
            Toast.makeText(getApplicationContext(), "Please Cheeck network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    private void idninit() {
        cardtype = findViewById(R.id.cardtype);
        cardbrand = findViewById(R.id.cardbrand);
        delete_card = findViewById(R.id.delete_card);
        validdate = findViewById(R.id.validdate);
        savecardlay = findViewById(R.id.savecardlay);
        savedcardnumber = findViewById(R.id.savedcardnumber);
        adddebitcardlay = findViewById(R.id.adddebitcardlay);
        addcardbut = findViewById(R.id.addcardbut);
        accountcreate = findViewById(R.id.accountcreate);
        myaccountemail = findViewById(R.id.myaccountemail);
        alreadyavbacountlay = findViewById(R.id.alreadyavbacountlay);
        prgressbar = findViewById(R.id.prgressbar);
        submit = findViewById(R.id.submit);
        backlay = findViewById(R.id.backlay);
        acceptcondition = findViewById(R.id.acceptcondition);
        email_address = findViewById(R.id.email_address);
        security_code = findViewById(R.id.security_code);
        datespinner = findViewById(R.id.datespinner);
        yearspinner = findViewById(R.id.yearspinner);
        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expiryyear_str = modellist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        datespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date_str = datelist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backlay = findViewById(R.id.backlay);
        cardname = findViewById(R.id.cardname);
        cardnumber = findViewById(R.id.cardnumber);
        tv = new CreditCardFormatTextWatcher(cardnumber);
        tv2 = new CreditCardFormatTextWatcher(savedcardnumber);
        cardnumber.addTextChangedListener(tv);
        savedcardnumber.addTextChangedListener(tv2);


        basicCustomAdp = new BasicCustomAdp(AddStripeConnectAccount.this, android.R.layout.simple_spinner_item, modellist);
        yearspinner.setAdapter(basicCustomAdp);
        basicCustomAdp = new BasicCustomAdp(AddStripeConnectAccount.this, android.R.layout.simple_spinner_item, datelist);
        datespinner.setAdapter(basicCustomAdp);


    }

    private class CreateCardCustomer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                String postReceiverUrl = BaseUrl.baseurl + "create_external_account.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

                params.put("token", token_id);
                params.put("account_id", stripe_account_id);

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
            // prgressbar.setVisibility(View.GONE);

            Log.e("Create Customer Res", ">>>>>>>>>>>>" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String customer_id = jsonObject1.getString("id");

                        Log.e("customer_id >> ", " >> " + customer_id);
                        //  new TransferAmount().execute(customer_id);

                        Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.cardaddedsuc), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(AddStripeConnectAccount.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // BaseActivity.Card_Added_Sts="Added";

            }

        }
    }

    private class CreateStripeAccount extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asc_sts = false;
            prgressbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "create_custom_stripe_account.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

                params.put("stripe_account_email", email_str);
                params.put("email", email_str);
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
            asc_sts = true;
            prgressbar.setVisibility(View.GONE);

            Log.e("Create Stripe Account", ">>>>>>>>>>>>" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String accountid = jsonObject1.getString("id");
                        Log.e("My account id >> ", " >> " + accountid);
                        // new TransferAmount().execute(customer_id);
                        Toast.makeText(AddStripeConnectAccount.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        finish();


                    } else {
                        Toast.makeText(AddStripeConnectAccount.this, "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // BaseActivity.Card_Added_Sts="Added";

            }

        }
    }

    private class GetAlreadyAccountDetail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asc_sts = false;
            prgressbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "merchant_stripe_account.php?";
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
            asc_sts = true;
            prgressbar.setVisibility(View.GONE);

            Log.e("Get My Account", ">>>>>>>>>>>>" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        stripe_account_id = jsonObject1.getString("stripe_account_id");

                        if (stripe_account_id == null || stripe_account_id.equalsIgnoreCase("") || stripe_account_id.equalsIgnoreCase("null") || stripe_account_id.equalsIgnoreCase("0")) {
                            alreadyavbacountlay.setVisibility(View.GONE);
                            accountcreate.setVisibility(View.VISIBLE);
                        } else {
                            myaccountemail.setText("" + jsonObject1.getString("stripe_account_email"));

                            alreadyavbacountlay.setVisibility(View.VISIBLE);
                            accountcreate.setVisibility(View.GONE);
                        }
                        if (jsonObject.has("card_detail")) {

                            if (!jsonObject.isNull("card_detail")) {

                                JSONObject jsonObject2 = jsonObject.getJSONObject("card_detail");
                                JSONArray jsonArray = jsonObject2.getJSONArray("data");
                                if (jsonArray.length() == 0) {
                                    adddebitcardlay.setVisibility(View.VISIBLE);
                                    savecardlay.setVisibility(View.GONE);
                                }
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    card_id = jsonObject3.getString("id");
                                    adddebitcardlay.setVisibility(View.GONE);
                                    savecardlay.setVisibility(View.VISIBLE);
                                    String star = "************";
                                    String cardlastfour = jsonObject3.getString("last4");
                                    savedcardnumber.setText(star + cardlastfour);
                                    validdate.setText("" + jsonObject3.getString("exp_month") + "/" + jsonObject3.getString("exp_year"));
                                    cardbrand.setText("" + jsonObject3.getString("brand"));
                                    cardtype.setText("" + jsonObject3.getString("funding"));
                                    cardtype.setAllCaps(true);
                                }

                            }
                        }
                    } else {
                        alreadyavbacountlay.setVisibility(View.GONE);
                        accountcreate.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class BasicCustomAdp extends ArrayAdapter<String> {
        private final ArrayList<String> carmodel;
        Context context;
        Activity activity;

        public BasicCustomAdp(Context context, int resourceId, ArrayList<String> carmodel) {
            super(context, resourceId);
            this.context = context;
            this.carmodel = carmodel;
        }

        @Override
        public int getCount() {
            return carmodel.size();
        }

        @Override
        public String getItem(int position) {
            return carmodel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            BasicCustomAdp.ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.spn_head_lay, null);
                holder = new BasicCustomAdp.ViewHolder();
                holder.headername = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
            } else {
                holder = (BasicCustomAdp.ViewHolder) convertView.getTag();
            }
            holder.headername.setText("" + carmodel.get(position));

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            BasicCustomAdp.ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.spn_head_lay, null);
                holder = new BasicCustomAdp.ViewHolder();
                holder.cartype = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
            } else {
                holder = (BasicCustomAdp.ViewHolder) convertView.getTag();
            }

            holder.cartype.setText("" + carmodel.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView headername;
            TextView cartype;
        }
    }
}
