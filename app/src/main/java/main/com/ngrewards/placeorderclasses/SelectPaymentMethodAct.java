package main.com.ngrewards.placeorderclasses;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.AddMemberCard;
import main.com.ngrewards.activity.UpdateMemberCard;
import main.com.ngrewards.beanclasses.CardBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;

public class SelectPaymentMethodAct extends AppCompatActivity {

    public static String card_id = "", card_number = "", card_brand = "", customer_id = "";
    private ExpandableHeightListView addedcardlist;
    private ArrayList<CardBean> cardBeanArrayList;
    private MySavedCardInfo mySavedCardInfo;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "";
    private CustomCardAdp customCardAdp;
    private RelativeLayout addaddressdlay, backlay;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_select_payment_method);
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
        idinit();
        clickevent();
    }

    private void clickevent() {
        addaddressdlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectPaymentMethodAct.this, AddMemberCard.class);
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
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        addaddressdlay = findViewById(R.id.addaddressdlay);
        addedcardlist = findViewById(R.id.addedcardlist);
        addedcardlist.setExpanded(true);
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


                    JSONArray jsonArray = jsonObject2.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        CardBean cardBean = new CardBean();
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
                    customCardAdp = new CustomCardAdp(SelectPaymentMethodAct.this, cardBeanArrayList);
                    addedcardlist.setAdapter(customCardAdp);
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

            rowView = inflater.inflate(R.layout.custom_select_card_itemlay, null);
            TextView savedcardnumber = rowView.findViewById(R.id.savedcardnumber);
            TextView savedcardtv = rowView.findViewById(R.id.savedcardtv);
            TextView validdate = rowView.findViewById(R.id.validdate);
            TextView cardbrand = rowView.findViewById(R.id.cardbrand);
            TextView cardtype = rowView.findViewById(R.id.cardtype);
            ImageView delete_card = rowView.findViewById(R.id.delete_card);
            ImageView update_card = rowView.findViewById(R.id.update_card);
            RadioButton creditcard_rbut = rowView.findViewById(R.id.creditcard_rbut);


            String cardbrandstr = cardBeanArrayList.get(position).getBrand();
            String carnum = cardBeanArrayList.get(position).getLast4();
            if (cardbrandstr.length() > 4) {
                cardbrandstr = cardbrandstr.substring(0, 4);
            }
            String stars = "**** ****";
            savedcardnumber.setText("" + cardbrandstr + " " + stars + " " + carnum);

            // savedcardnumber.setText(""+cardBeanArrayList.get(position).getSetfullcardnumber());


            validdate.setText("" + cardBeanArrayList.get(position).getSetfullexpyearmonth());
            cardbrand.setText("" + cardBeanArrayList.get(position).getBrand());
            cardtype.setText("" + cardBeanArrayList.get(position).getFunding());
            cardtype.setAllCaps(true);

            creditcard_rbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card_id = cardBeanArrayList.get(position).getId();
                    customer_id = cardBeanArrayList.get(position).getCustomer();
                    card_number = cardBeanArrayList.get(position).getLast4();
                    card_brand = cardBeanArrayList.get(position).getBrand();
                    finish();
                }
            });
            update_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(SelectPaymentMethodAct.this, UpdateMemberCard.class);
                    i.putExtra("cardnumber_str", cardBeanArrayList.get(position).getSetfullcardnumber());
                    i.putExtra("cardholder_name", cardBeanArrayList.get(position).getCard_name());
                    i.putExtra("expmonth", cardBeanArrayList.get(position).getExp_month());
                    i.putExtra("expyear", cardBeanArrayList.get(position).getExp_year());
                    i.putExtra("card_id", cardBeanArrayList.get(position).getId());
                    i.putExtra("customer_id", cardBeanArrayList.get(position).getCustomer());
                    startActivity(i);
                }
            });
            // cardnumber.setText(""+getLastfour(cardBeanArrayList.get(position).getCard_number()));
            return rowView;
        }

        public class Holder {

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
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("sources");
                        String customer_id = jsonObject1.getString("id");
                        Log.e("customer_id >> ", " >> " + customer_id);
                        mySavedCardInfo.setKeyCarddata(result);

                        JSONArray jsonArray = jsonObject2.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            CardBean cardBean = new CardBean();
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

                        customCardAdp = new CustomCardAdp(SelectPaymentMethodAct.this, cardBeanArrayList);
                        addedcardlist.setAdapter(customCardAdp);
                        customCardAdp.notifyDataSetChanged();

                        // new TransferAmount().execute(customer_id);


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
