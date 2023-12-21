package main.com.ngrewards.activity;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;

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
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.stripepaymentclasses.CreditCardFormatTextWatcher;
import main.com.ngrewards.stripepaymentclasses.Utils;

public class AddCreditCardAct extends AppCompatActivity {
    private final boolean status_match = false;
    private final String accountid = "";
    ArrayList<String> listOfPattern=new ArrayList<>();
    ArrayList<String> modellist=new ArrayList<>();
    ArrayList<String> datelist=new ArrayList<>();
    int month, year_int;
    CreditCardFormatTextWatcher tv;
    private EditText cardname, cardnumber, security_code;
    private String cardname_str = "", cardnumber_str = "", security_code_str = "";
    private TextView addcardbut;
    private RelativeLayout backlay;
    private Spinner yearspinner, datespinner;
    private String expiryyear_str = "", date_str = "", user_id = "";
    private BasicCustomAdp basicCustomAdp;
    private ProgressBar progressBar;
    private MySession mySession;
    private String token_id = "";
    private String email_str = "";

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
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_add_credit_card);
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
                    email_str = jsonObject1.getString("email");

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
        listOfPattern = new ArrayList<String>();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);
        idinti();
        clickevent();

        //  new CreateStripeAccount().execute();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.entercardname), Toast.LENGTH_LONG).show();
                } else if (cardnumber_str == null || cardnumber_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.entercardnumber), Toast.LENGTH_LONG).show();

                } else if (date_str == null || date_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.selectexpdate), Toast.LENGTH_LONG).show();

                } else if (expiryyear_str == null || expiryyear_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.selexpyear), Toast.LENGTH_LONG).show();

                } else if (expiryyear_str == null || expiryyear_str.equalsIgnoreCase("")) {
                    Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.selexpyear), Toast.LENGTH_LONG).show();

                } else {

                    month = Integer.parseInt(date_str);
                    year_int = Integer.parseInt(expiryyear_str);

                    onClickSomething(cardnumber_str, month, year_int, security_code_str);
                    CardParams card = new CardParams(cardnumber_str, month, year_int, security_code_str);  //
                    card.setCurrency(mySession.getValueOf(MySession.CurrencyCode));
                    Stripe stripe = new Stripe(AddCreditCardAct.this, BaseUrl.stripe_publish);
                    progressBar.setVisibility(View.VISIBLE);
                    stripe.createCardToken(
                            card,
                            new ApiResultCallback<Token>() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
                                    progressBar.setVisibility(View.GONE);
                                    Log.e("Token", ">>" + token);

                                    token_id = token.getId();
                                    paymentwithcard();

                                }

                                public void onError(Exception error) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.e("Eeeeeeeeeeeeeeerrrrr", ">>" + error.toString());
                                    // Show localized error message
                                    Toast.makeText(AddCreditCardAct.this, error.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();


                                }
                            });


                 /*   if (status_match){
                        new AddCardAsc().execute();
                    }
                    else {
                        Toast.makeText(AddCreditCardAct.this,getResources().getString(R.string.cardnumberisnotvalid),Toast.LENGTH_LONG).show();

                    }*/

                }
            }
        });
    }

    public void onClickSomething(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        CardParams cardParams = new CardParams(cardNumber, cardExpMonth, cardExpYear, cardCVC);

       /* Card card = new Card(cardNumber, cardExpMonth, cardExpYear, cardCVC);
        card.validateNumber();
        card.validateCVC();*/
    }

    private void idinti() {
        addcardbut = findViewById(R.id.addcardbut);
        progressBar = findViewById(R.id.progressBar);
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
        cardnumber.addTextChangedListener(tv);
        basicCustomAdp = new BasicCustomAdp(AddCreditCardAct.this, android.R.layout.simple_spinner_item, modellist);
        yearspinner.setAdapter(basicCustomAdp);
        basicCustomAdp = new BasicCustomAdp(AddCreditCardAct.this, android.R.layout.simple_spinner_item, datelist);
        datespinner.setAdapter(basicCustomAdp);
    }

    private void paymentwithcard() {
        // Tag used to cancel the request
        if (Utils.isConnected(getApplicationContext())) {

            new CreateTestPayment().execute();

        } else {
            Toast.makeText(getApplicationContext(), "Please Cheeck network conection..", Toast.LENGTH_SHORT).show();
        }
    }

    public class BasicCustomAdp extends ArrayAdapter<String> {
        private ArrayList<String> carmodel=new ArrayList<>();
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
            ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.spn_head_lay, null);
                holder = new ViewHolder();
                holder.headername = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.headername.setText("" + carmodel.get(position));

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.spn_head_lay, null);
                holder = new ViewHolder();
                holder.cartype = (TextView) convertView.findViewById(R.id.heading);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cartype.setText("" + carmodel.get(position));
            return convertView;
        }

        private class ViewHolder {
            TextView headername;
            TextView cartype;
        }
    }

    private class AddCardAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//https://myngrewards.com/demo/wp-content/plugins/webservice/add_member_card_details.php?member_id=1&card_name=ks&card_number=122334455&expiry_date=12/08&expiry_year=2020
            try {
                String postReceiverUrl = BaseUrl.baseurl + "add_member_card_details.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
                params.put("card_name", cardname_str);
                params.put("card_number", cardnumber_str);
                params.put("expiry_date", date_str);
                params.put("expiry_year", expiryyear_str);
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
                Log.e("Json AddCard Response", ">>>>>>>>>>>>" + response);
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
            progressBar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.cardaddedsuc), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(AddCreditCardAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {

                }


            }


        }
    }

    private class CreateTestPayment extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //String postReceiverUrl = BaseUrl.baseurl + "payment.php?";
                // String postReceiverUrl = BaseUrl.baseurl + "stripe_split_payment.php?";
                String postReceiverUrl = BaseUrl.baseurl + "stripe_spilit.php?";
                //String postReceiverUrl = BaseUrl.baseurl + "stripe_charge.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("total_amount", "1");
                //     params.put("currency", "USD");
                params.put("currency", mySession.getValueOf(MySession.CurrencyCode));
                params.put("token", token_id);
                params.put("cart_id", "1");
                params.put("account_id1", "acct_1DSI3ZI0Sxc8XAKz");
                params.put("account_id2", "acct_1DSHwkBbWm512c3W");

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
            Log.e("Test Payment Response", ">>>>>>>>>>>>" + result);
            progressBar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {


            }


        }
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
                params.put("account_id", "acct_1DSI3ZI0Sxc8XAKz");
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // BaseActivity.Card_Added_Sts="Added";
            }
        }
    }
}
//https://github.com/braintree/android-card-form