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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.stripepaymentclasses.CreditCardFormatTextWatcher;
import main.com.ngrewards.stripepaymentclasses.Utils;

public class UpdateMemberCard extends AppCompatActivity {

    private final boolean asc_sts = true;
    private final String security_code_str = "";
    private final String token_id = "";
    private final String email_str = "";
    private final String accountid = "";
    private final String stripe_account_id = "";
    int month, year_int;
    ArrayList<String> modellist;
    ArrayList<String> datelist;
    CreditCardFormatTextWatcher tv, tv2;
    private RelativeLayout backlay;
    private EditText email_address;
    private CheckBox acceptcondition;
    private TextView submit, myaccountemail, addcardbut;
    private ProgressBar prgressbar;
    private MySession mySession;
    private LinearLayout alreadyavbacountlay, accountcreate, adddebitcardlay;
    private EditText cardname, security_code;
    private TextView cardnumber;
    private Spinner yearspinner, datespinner;
    private String expiryyear_str = "", date_str = "", user_id = "", card_id = "", customer_id = "";
    private BasicCustomAdp basicCustomAdp;
    private String cardname_str = "";
    private String cardnumber_str = "";
    private TextView savedcardnumber, validdate, cardbrand, cardtype;
    private LinearLayout savecardlay;
    private ImageView delete_card;
    private MySavedCardInfo mySavedCardInfo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_membercardlay);
        mySession = new MySession(this);
        mySavedCardInfo = new MySavedCardInfo(this);
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            cardnumber_str = bundle.getString("cardnumber_str");
            cardname_str = bundle.getString("cardholder_name");
            date_str = bundle.getString("expmonth");
            expiryyear_str = bundle.getString("expyear");
            card_id = bundle.getString("card_id");
            customer_id = bundle.getString("customer_id");
            if (date_str != null && !date_str.equalsIgnoreCase("")) {
                int dd = Integer.parseInt(date_str);
                if (dd < 10) {
                    date_str = "0" + date_str;
                }
            }

        }

        idninit();
        clickevent();
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

                if (cardname_str == null || cardname_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateMemberCard.this, getResources().getString(R.string.entercardname), Toast.LENGTH_LONG).show();
                } else if (cardnumber_str == null || cardnumber_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateMemberCard.this, getResources().getString(R.string.entercardnumber), Toast.LENGTH_LONG).show();

                } else if (date_str == null || date_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateMemberCard.this, getResources().getString(R.string.selectexpdate), Toast.LENGTH_LONG).show();

                } else if (expiryyear_str == null || expiryyear_str.equalsIgnoreCase("")) {
                    Toast.makeText(UpdateMemberCard.this, getResources().getString(R.string.selexpyear), Toast.LENGTH_LONG).show();

                } else {


                    month = Integer.parseInt(date_str);
                    year_int = Integer.parseInt(expiryyear_str);

                    if (Utils.isConnected(getApplicationContext())) {
                        new UpdateCardAsc().execute();
                        //  new CreateTestPayment().execute();

                    } else {
                        prgressbar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Please Cheeck network conection..", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


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
        cardnumber.setText("" + cardnumber_str);
        cardname.setText("" + cardname_str);

        basicCustomAdp = new BasicCustomAdp(UpdateMemberCard.this, android.R.layout.simple_spinner_item, modellist);
        yearspinner.setAdapter(basicCustomAdp);
        basicCustomAdp = new BasicCustomAdp(UpdateMemberCard.this, android.R.layout.simple_spinner_item, datelist);
        datespinner.setAdapter(basicCustomAdp);


        Log.e("date_str >> ", " >>" + date_str);
        if (datelist != null && !datelist.isEmpty()) {
            for (int i = 0; i < datelist.size(); i++) {
                if (date_str != null && !date_str.equalsIgnoreCase("")) {
                    if (date_str.equalsIgnoreCase(datelist.get(i))) {
                        datespinner.setSelection(i);
                    }
                }
            }

        }

        if (modellist != null && !modellist.isEmpty()) {
            for (int i = 0; i < modellist.size(); i++) {
                if (expiryyear_str != null && !expiryyear_str.equalsIgnoreCase("")) {
                    if (expiryyear_str.equalsIgnoreCase(modellist.get(i))) {
                        yearspinner.setSelection(i);
                    }
                }
            }

        }


    }

    private class UpdateCardAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgressbar.setVisibility(View.VISIBLE);
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

                String postReceiverUrl = BaseUrl.baseurl + "update_customer_card.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

                params.put("customer_id", customer_id);
                params.put("card_id", card_id);
                params.put("name", cardname_str);
                params.put("exp_month", date_str);
                params.put("exp_year", expiryyear_str);
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
            prgressbar.setVisibility(View.GONE);
            Log.e("Create Customer Res", ">>>>>>>>>>>>" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        // JSONObject jsonObject2 = jsonObject1.getJSONObject("sources");
                        String customer_id = jsonObject1.getString("id");
                        mySavedCardInfo.clearCardData();

                        Toast.makeText(UpdateMemberCard.this, getResources().getString(R.string.cardupdatedsucc), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateMemberCard.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // BaseActivity.Card_Added_Sts="Added";

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
