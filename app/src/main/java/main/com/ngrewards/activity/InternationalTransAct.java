package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.CountryBean;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;

public class InternationalTransAct extends AppCompatActivity {

    int count = 0;
    private RelativeLayout backlay;
    private Spinner country_spn;
    private CountryListAdapter countryListAdapter;
    private ArrayList<CountryBean> countryBeanArrayList;
    private ProgressBar progresbar;
    private AutoCompleteTextView usernameauto;
    private TextView memname;
    private EditText amount;
    private MySession mySession;
    private Myapisession myapisession;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_trans);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        idint();
        clcikevent();
        //  if (myapisession.getKeyCountry()==null||myapisession.getKeyCountry().equalsIgnoreCase("")){
        new GetCountryList().execute();
        //  }
       /* else {
            JSONObject jsonObject = null;
            try {
                countryBeanArrayList= new ArrayList<>();
                jsonObject = new JSONObject(myapisession.getKeyCountry());
                String message = jsonObject.getString("message");
                if (message.equalsIgnoreCase("successful")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CountryBean countryBean = new CountryBean();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        countryBean.setId(jsonObject1.getString("id"));
                        countryBean.setName(jsonObject1.getString("name"));
                        countryBean.setSortname(jsonObject1.getString("sortname"));
                        countryBean.setFlag_url(jsonObject1.getString("flag"));
                        countryBeanArrayList.add(countryBean);
                    }
                    if (countryBeanArrayList != null) {
                        Collections.reverse(countryBeanArrayList);
                    }


                   *//* countryListAdapter = new CountryListAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, countryBeanArrayList);
                    country_spn.setAdapter(countryListAdapter);*//*
                    countryListAdapter = new CountryListAdapter(InternationalTransAct.this, countryBeanArrayList);
                    country_spn.setAdapter(countryListAdapter);
                    countryListAdapter.notifyDataSetChanged();
                }
                else {
                    new GetCountryList().execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        // }
    }

    private void clcikevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idint() {
        amount = findViewById(R.id.amount);
        amount.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    final int beforeDecimal = 8;
                    final int afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = amount.getText() + source.toString();

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

        memname = findViewById(R.id.memname);
        usernameauto = findViewById(R.id.usernameauto);
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        country_spn = findViewById(R.id.country_spn);

        usernameauto.addTextChangedListener(new TextWatcher() {
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

                if (count == 0) {
                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setAffiliateName(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(InternationalTransAct.this, l1, "", "");
                        usernameauto.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }

                }
                count++;


            }
        });

    }

    private class GetCountryList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            countryBeanArrayList = new ArrayList<>();
            countryBeanArrayList.clear();
        /*    CountryBean countryListBean = new CountryBean();
            countryListBean.setId("0");
            countryListBean.setName("Country");
            countryListBean.setSortname("");
            countryBeanArrayList.add(countryListBean);*/
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "country_lists.php?contry_id=" + mySession.getValueOf(MySession.CountryId);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

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
                Log.e("Json Country Response", ">>>>>>>>>>>>" + response);
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
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("successful")) {
                        myapisession.setCountrydata(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryBean countryBean = new CountryBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            countryBean.setId(jsonObject1.getString("id"));
                            countryBean.setName(jsonObject1.getString("name"));
                            countryBean.setSortname(jsonObject1.getString("sortname"));
                            countryBean.setFlag_url(jsonObject1.getString("flag"));
                            countryBeanArrayList.add(countryBean);
                        }
                        if (countryBeanArrayList != null) {
                            Collections.reverse(countryBeanArrayList);
                        }


                   /* countryListAdapter = new CountryListAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, countryBeanArrayList);
                    country_spn.setAdapter(countryListAdapter);*/
                        countryListAdapter = new CountryListAdapter(InternationalTransAct.this, countryBeanArrayList);
                        country_spn.setAdapter(countryListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    public class CountryListAdapter extends BaseAdapter {
        private final ArrayList<CountryBean> values;
        Context context;
        LayoutInflater inflter;

        public CountryListAdapter(Context applicationContext, ArrayList<CountryBean> values) {
            this.context = applicationContext;
            this.values = values;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {

            return values == null ? 0 : values.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.country_item_lay_flag, null);

            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            if (values.get(i).getFlag_url() == null || values.get(i).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(InternationalTransAct.this)
                        .load(values.get(i).getFlag_url())
                        .thumbnail(0.5f)
                        .override(50, 50)
                        .centerCrop()

                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(country_flag);
            }


            names.setText(values.get(i).getName());


            return view;
        }
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private ArrayList<MemberDetail> l2 = new ArrayList<>();

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
                geo_search_result_text.setText(l2.get(i).getAffiliateName());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        usernameauto.dismissDropDown();
                        // checkic.setImageResource(R.drawable.check);
                        usernameauto.setText(l2.get(i).getAffiliateName());
                        memname.setText(l2.get(i).getFullname());

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
                            if (MemberTransfer.memberDetailArrayList != null && !MemberTransfer.memberDetailArrayList.isEmpty()) {
                                for (MemberDetail wp : MemberTransfer.memberDetailArrayList) {
                                    if (wp.getAffiliateName().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                    {
                                        Log.e("TRUE", " >> FILTER" + wp.getAffiliateName());
                                        l2.add(wp);
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
// && results.count != 0
                    if (results != null && results.count != 0) {
                        l2 = (ArrayList<MemberDetail>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }


    }

}
