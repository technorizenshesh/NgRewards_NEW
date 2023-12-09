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
import main.com.ngrewards.beanclasses.AddressBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;

public class AllAddedAddressAct extends AppCompatActivity {

    public static String fullname_str = "", state_str = "", city_str = "", zippcode_str = "", address1_str = "", address2_str = "", statecityadd_str = "", countrytv_str = "", phonetv_str = "", AddressID = "";
    Myapisession myapisession;
    private ArrayList<AddressBean> addressBeanArrayList;
    private ExpandableHeightListView addresslist;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "";
    private CustomAddresAdp customAddresAdp;
    private RelativeLayout addaddressdlay, backlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_added_address);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    Log.e("user_id >>", " >" + user_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinit();
        clickevent();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myapisession.getKeyAddressdata() == null || myapisession.getKeyAddressdata().equalsIgnoreCase("")) {
            new GetSavedAddress().execute();
        } else {
            try {
                addressBeanArrayList = new ArrayList<>();
                String result = myapisession.getKeyAddressdata();
                JSONObject jsonObject = new JSONObject(result);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    myapisession.setKeyAddressdata(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        AddressBean addressBean = new AddressBean();
                        addressBean.setId(jsonObject1.getString("id"));
                        addressBean.setUser_id(jsonObject1.getString("user_id"));
                        addressBean.setFullname(jsonObject1.getString("fullname"));
                        addressBean.setPhone_number(jsonObject1.getString("phone_number"));
                        addressBean.setCountry(jsonObject1.getString("country"));
                        addressBean.setState(jsonObject1.getString("state"));
                        addressBean.setCity(jsonObject1.getString("city"));
                        addressBean.setAddress_1(jsonObject1.getString("address_1"));
                        addressBean.setAddress_2(jsonObject1.getString("address_2"));
                        addressBean.setZipcode(jsonObject1.getString("zipcode"));
                        addressBean.setCreated_date(jsonObject1.getString("created_date"));

                        addressBeanArrayList.add(addressBean);


                    }

                    customAddresAdp = new CustomAddresAdp(AllAddedAddressAct.this, addressBeanArrayList);
                    addresslist.setAdapter(customAddresAdp);
                    customAddresAdp.notifyDataSetChanged();

/*
                        if (genderlist!=null&&!genderlist.isEmpty()){
                            for (int i=0;i<genderlist.size();i++){
                                if (gender_str!=null&&!gender_str.equalsIgnoreCase("")){
                                    if (gender_str.equalsIgnoreCase(genderlist.get(i))){
                                        genderspn.setSelection(i);
                                    }
                                }
                            }

                        }
*/


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void clickevent() {
        addaddressdlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllAddedAddressAct.this, AddShipingAddress.class);
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
        backlay = findViewById(R.id.backlay);
        addaddressdlay = findViewById(R.id.addaddressdlay);
        progresbar = findViewById(R.id.progresbar);
        addresslist = findViewById(R.id.addresslist);
        addresslist.setExpanded(true);
    }


    private class GetSavedAddress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            addressBeanArrayList = new ArrayList<>();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "get_address_lists.php?";
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
                Log.e("Saved Address", ">>>>>>>>>>>>" + response);
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
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        myapisession.setKeyAddressdata(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AddressBean addressBean = new AddressBean();
                            addressBean.setId(jsonObject1.getString("id"));
                            addressBean.setUser_id(jsonObject1.getString("user_id"));
                            addressBean.setFullname(jsonObject1.getString("fullname"));
                            addressBean.setPhone_number(jsonObject1.getString("phone_number"));
                            addressBean.setCountry(jsonObject1.getString("country"));
                            addressBean.setState(jsonObject1.getString("state"));
                            addressBean.setCity(jsonObject1.getString("city"));
                            addressBean.setAddress_1(jsonObject1.getString("address_1"));
                            addressBean.setAddress_2(jsonObject1.getString("address_2"));
                            addressBean.setZipcode(jsonObject1.getString("zipcode"));
                            addressBean.setCreated_date(jsonObject1.getString("created_date"));

                            addressBeanArrayList.add(addressBean);


                        }

                        customAddresAdp = new CustomAddresAdp(AllAddedAddressAct.this, addressBeanArrayList);
                        addresslist.setAdapter(customAddresAdp);

/*
                        if (genderlist!=null&&!genderlist.isEmpty()){
                            for (int i=0;i<genderlist.size();i++){
                                if (gender_str!=null&&!gender_str.equalsIgnoreCase("")){
                                    if (gender_str.equalsIgnoreCase(genderlist.get(i))){
                                        genderspn.setSelection(i);
                                    }
                                }
                            }

                        }
*/


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    public class CustomAddresAdp extends BaseAdapter {
        Context context;
        ArrayList<AddressBean> addressBeanArrayList;
        private LayoutInflater inflater = null;

        public CustomAddresAdp(Context contexts, ArrayList<AddressBean> addressBeanArrayList) {
            this.context = contexts;
            this.addressBeanArrayList = addressBeanArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return addressBeanArrayList == null ? 0 : addressBeanArrayList.size();
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

            rowView = inflater.inflate(R.layout.custom_address_list, null);
            ImageView edit_but = rowView.findViewById(R.id.edit_but);
            RadioButton addsel_rdb = rowView.findViewById(R.id.addsel_rdb);
            TextView fullname = rowView.findViewById(R.id.fullname);
            TextView address1 = rowView.findViewById(R.id.address1);
            TextView address2 = rowView.findViewById(R.id.address2);
            TextView phonetv = rowView.findViewById(R.id.phonetv);
            fullname.setText("" + addressBeanArrayList.get(position).getFullname());
            address1.setText("" + addressBeanArrayList.get(position).getAddress_1());
            address2.setText("" + addressBeanArrayList.get(position).getAddress_2());
            phonetv.setText("" + addressBeanArrayList.get(position).getPhone_number());
            addsel_rdb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AddressID = addressBeanArrayList.get(position).getId();
                    fullname_str = addressBeanArrayList.get(position).getFullname();
                    address1_str = addressBeanArrayList.get(position).getAddress_1();
                    address2_str = addressBeanArrayList.get(position).getAddress_2();
                    state_str = addressBeanArrayList.get(position).getState();
                    city_str = addressBeanArrayList.get(position).getCity();
                    countrytv_str = addressBeanArrayList.get(position).getCountry();
                    phonetv_str = addressBeanArrayList.get(position).getPhone_number();
                    zippcode_str = addressBeanArrayList.get(position).getZipcode();
                    finish();
                }
            });


            edit_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(AllAddedAddressAct.this, UpdateShipingAddress.class);
                    i.putExtra("addid", addressBeanArrayList.get(position).getId());
                    i.putExtra("fullname_str", addressBeanArrayList.get(position).getFullname());
                    i.putExtra("address1_str", addressBeanArrayList.get(position).getAddress_1());
                    i.putExtra("address2_str", addressBeanArrayList.get(position).getAddress_2());
                    i.putExtra("state_str", addressBeanArrayList.get(position).getState());
                    i.putExtra("city_str", addressBeanArrayList.get(position).getCity());
                    i.putExtra("countrytv_str", addressBeanArrayList.get(position).getCountry());
                    i.putExtra("phonetv_str", addressBeanArrayList.get(position).getPhone_number());
                    i.putExtra("zippcode_str", addressBeanArrayList.get(position).getZipcode());
                    startActivity(i);

                }
            });
            // cardnumber.setText(""+getLastfour(cardBeanArrayList.get(position).getCard_number()));
            return rowView;
        }

        public class Holder {

        }

    }


}
