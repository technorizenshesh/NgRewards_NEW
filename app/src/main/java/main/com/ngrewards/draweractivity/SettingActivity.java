package main.com.ngrewards.draweractivity;

import static main.com.ngrewards.constant.MySession.KEY_LANGUAGE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.AccountTypeSelectionAct;
import main.com.ngrewards.activity.AddMemberCard;
import main.com.ngrewards.activity.UpdateMemberCard;
import main.com.ngrewards.androidmigx.MainTabActivity;
import main.com.ngrewards.beanclasses.CardBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySavedCardInfo;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.settingclasses.AboutNgReward;
import main.com.ngrewards.settingclasses.CareeersAct;
import main.com.ngrewards.settingclasses.ChangePasswordAct;
import main.com.ngrewards.settingclasses.InviteFacebookFriend;
import main.com.ngrewards.settingclasses.NgHelpCenter;
import main.com.ngrewards.settingclasses.ReportProblem;
import main.com.ngrewards.settingclasses.TouchIdAct;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    String selected_lang = "";

    CountryListAdapter languageListAdapter;
    private RelativeLayout changelang, changepass, addcardlay, invitecontact, invitefacelay, backlay, career_lay, aboutng_rew, helpcenter, reportproblem, touchidlay, deleteAccount;
    private ExpandableHeightListView addedcardlist;
    private CustomCardAdp customCardAdp;
    private ArrayList<CardBean> cardBeanArrayList;
    private MySavedCardInfo mySavedCardInfo;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "", user_type = "";
    private Myapisession myapisession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mySession = new MySession(this);
        mySavedCardInfo = new MySavedCardInfo(this);
        String user_log_data = mySession.getKeyAlldata();
        myapisession = new Myapisession(this);
        if (user_log_data == null) {

        } else {

            try {

                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    user_type = jsonObject1.getString("user_type");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        idinti();
        clickevent();
    }

    private void senOTP() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        progresbar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("type", user_type);

        Call<ResponseBody> call = ApiClient.getApiInterface().requestOtp(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progresbar.setVisibility(View.GONE);
                    try {
                        String responseData = response.body().string();
                        Log.e("SALE DATA", " >>" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            selectImage();
                        } else {
                            Toast.makeText(SettingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progresbar.setVisibility(View.GONE);
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

    private void clickevent() {

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        career_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, CareeersAct.class);
                startActivity(i);
            }
        });
        aboutng_rew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, AboutNgReward.class);
                startActivity(i);
            }
        });
        helpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, NgHelpCenter.class);
                startActivity(i);
            }
        });

        reportproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, ReportProblem.class);
                startActivity(i);
            }
        });
        touchidlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, TouchIdAct.class);
                startActivity(i);
            }
        });

        invitefacelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, InviteFacebookFriend.class);
                startActivity(i);
            }
        });

        invitecontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, InviteFacebookFriend.class);
                startActivity(i);
            }
        });

        addcardlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(SettingActivity.this, AddMemberCard.class);
                Intent i = new Intent(SettingActivity.this, AddMemberCard.class);
                startActivity(i);
            }
        });

        deleteAccount.setOnClickListener(v -> {
            senOTP();
        });


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(SettingActivity.this, AddMemberCard.class);
                Intent i = new Intent(SettingActivity.this, ChangePasswordAct.class);
                i.putExtra("type", "Member");
                startActivity(i);
            }
        });
        changelang.setOnClickListener(v -> setSellPassDialog());
    }

    private void idinti() {
        changelang = findViewById(R.id.changelang);
        changepass = findViewById(R.id.changepass);
        addedcardlist = findViewById(R.id.addedcardlist);
        addedcardlist.setExpanded(true);
        progresbar = findViewById(R.id.progresbar);
        addcardlay = findViewById(R.id.addcardlay);
        invitecontact = findViewById(R.id.invitecontact);
        touchidlay = findViewById(R.id.touchidlay);
        invitefacelay = findViewById(R.id.invitefacelay);
        reportproblem = findViewById(R.id.reportproblem);
        aboutng_rew = findViewById(R.id.aboutng_rew);
        backlay = findViewById(R.id.backlay);
        career_lay = findViewById(R.id.career_lay);
        helpcenter = findViewById(R.id.helpcenter);
        deleteAccount = findViewById(R.id.deleteAccount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.reupdateResources(this);

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
                    customCardAdp = new CustomCardAdp(SettingActivity.this, cardBeanArrayList);
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

    private void deleteAccount(String code) {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        progresbar.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("type", user_type);
        map.put("code", code);

        Call<ResponseBody> call = ApiClient.getApiInterface().deleteMyAccount(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progresbar.setVisibility(View.GONE);
                    try {
                        String responseData = response.body().string();
                        Log.e("SALE DATA", " >>" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

//                            if (AccessToken.getCurrentAccessToken() != null) {
//                                LoginManager.getInstance().logOut();
//                            }

                            mySession.signinusers(false);
                            mySavedCardInfo.clearCardData();
                            myapisession.setKeyAddressdata("");
                            myapisession.setKeyCartitem("");
                            myapisession.setProductdata("");
                            myapisession.setKeyOffercate("");
                            Intent i = new Intent(SettingActivity.this, AccountTypeSelectionAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);

                        } else {
                            Toast.makeText(SettingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progresbar.setVisibility(View.GONE);
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

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    private void selectImage() {

        final Dialog dialogSts = new Dialog(SettingActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_delete_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        Button camera = (Button) dialogSts.findViewById(R.id.camera);
//        Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
//        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);

        EditText etVerification = dialogSts.findViewById(R.id.etVerificationCode);
        TextView tvCancel = dialogSts.findViewById(R.id.tvCancel);
        TextView tvSubmit = dialogSts.findViewById(R.id.tvSubmit);

        String verificationCode = etVerification.getText().toString();

        tvCancel.setOnClickListener(v -> dialogSts.dismiss());

        tvSubmit.setOnClickListener(v -> {
            if (!etVerification.getText().toString().equalsIgnoreCase("")) {
                dialogSts.dismiss();
                deleteAccount(etVerification.getText().toString());
            } else {
                Toast.makeText(SettingActivity.this, "Please enter verification code.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogSts.show();
    }

    private void sureDelete(final String customer, final String id) {
        final Dialog dialogSts = new Dialog(SettingActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.custom_popup);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView no_tv = dialogSts.findViewById(R.id.no_tv);
        TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                new DeleteCardAsc().execute(customer, id);

            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();


            }
        });
        dialogSts.show();
    }

    private void setSellPassDialog() {
        try {
            final Dialog dialogSts = new Dialog(SettingActivity.this, R.style.DialogSlideAnim);
            dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSts.setCancelable(false);
            dialogSts.setContentView(R.layout.switch_lang_item);
            dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button submitt = (Button) dialogSts.findViewById(R.id.submitt);
            TextView close = (TextView) dialogSts.findViewById(R.id.close);
            close.setOnClickListener(v -> {
                dialogSts.dismiss();
            });
            Spinner language_spn = (Spinner) dialogSts.findViewById(R.id.language_spn);
            ArrayList<LanguageBean> language_list = new ArrayList<>();
            language_list.add(new LanguageBean("1", "en", "English", ""));
            language_list.add(new LanguageBean("2", "hi", "Hindi", ""));
            language_list.add(new LanguageBean("3", "es", "Spanish", ""));
            languageListAdapter = new CountryListAdapter(SettingActivity.this, language_list);
            language_spn.setAdapter(languageListAdapter);
            Log.e("TAG", "idint: mySession.getValueOf(KEY_LANGUAGE)" + mySession.getValueOf(KEY_LANGUAGE));
            for (int i = 0; i < language_list.size(); i++) {
                if (mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase(language_list.get(i).getSortname())) {
                    language_spn.setSelection(i);
                }

            }


            language_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (language_list != null && !language_list.isEmpty()) {
                        if (!mySession.getValueOf(KEY_LANGUAGE)
                                .equalsIgnoreCase(language_list.get(position).getSortname())) {
                            selected_lang = language_list.get(position).getSortname();

                        } else {
                            selected_lang = "";

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selected_lang = "";
                }
            });

            submitt.setOnClickListener(v -> {
                if (selected_lang.equalsIgnoreCase("")) {
                    dialogSts.dismiss();
                } else {
                    Tools.updateResources(getApplicationContext(), selected_lang);
                    mySession.setValueOf(KEY_LANGUAGE, selected_lang);
                    dialogSts.dismiss();
                    Intent i = new Intent(SettingActivity.this, MainTabActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();


                }

            });

            dialogSts.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getLastfour(String myString) {
        if (myString.length() > 4) return myString.substring(myString.length() - 4);
        else return myString;
    }

    public static class LanguageBean {
        String id;
        String sortname;
        String name;
        String flag_url;

        public LanguageBean(String id, String sortname, String name, String flag_url) {
            this.id = id;
            this.sortname = sortname;
            this.name = name;
            this.flag_url = flag_url;
        }

        public String getFlag_url() {
            return flag_url;
        }

        public void setFlag_url(String flag_url) {
            this.flag_url = flag_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class CountryListAdapter extends BaseAdapter {
        private final ArrayList<LanguageBean> values;
        Context context;
        LayoutInflater inflter;

        public CountryListAdapter(Context applicationContext, ArrayList<LanguageBean> values) {
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
        /*    //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            if (values.get(i).getFlag_url() == null || values.get(i).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(context)
                        .load(values.get(i).getFlag_url())
                        .thumbnail(0.5f)
                        .override(50, 50)
                        .centerCrop()
                         
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                         
                        .into(country_flag);
            }*/

            country_flag.setVisibility(View.GONE);
            names.setText(values.get(i).getName());


            return view;
        }
    }

    public class CustomCardAdp extends BaseAdapter {
        Context context;
        ArrayList<CardBean> cardBeanArrayList;
        private LayoutInflater inflater = null;

        public CustomCardAdp(Context contexts, ArrayList<CardBean> cardBeanArrayList) {
            this.context = contexts;
            this.cardBeanArrayList = cardBeanArrayList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            rowView = inflater.inflate(R.layout.custom_card_itemlay, null);
            TextView savedcardnumber = rowView.findViewById(R.id.savedcardnumber);
            TextView savedcardtv = rowView.findViewById(R.id.savedcardtv);
            TextView validdate = rowView.findViewById(R.id.validdate);
            TextView cardbrand = rowView.findViewById(R.id.cardbrand);
            TextView cardtype = rowView.findViewById(R.id.cardtype);
            ImageView delete_card = rowView.findViewById(R.id.delete_card);
            ImageView update_card = rowView.findViewById(R.id.update_card);

            if (position == 0) {
                savedcardtv.setVisibility(View.VISIBLE);
            } else {
                savedcardtv.setVisibility(View.GONE);
            }

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

            delete_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sureDelete(cardBeanArrayList.get(position).getCustomer(), cardBeanArrayList.get(position).getId());

                }
            });

            update_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(SettingActivity.this, UpdateMemberCard.class);
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
                        customCardAdp = new CustomCardAdp(SettingActivity.this, cardBeanArrayList);
                        addedcardlist.setAdapter(customCardAdp);
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

    private class DeleteCardAsc extends AsyncTask<String, String, String> {
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
                String postReceiverUrl = BaseUrl.baseurl + "delete_customer_card.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("Customer Card", ">>>>>>>>>>>>" + strings[0]);
                Log.e("Customer card_id", ">>>>>>>>>>>>" + strings[1]);
                params.put("customer_id", strings[0]);
                params.put("card_id", strings[1]);

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
                Log.e("Delete Card", ">>>>>>>>>>>>" + response);

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
                        new GetAddedCard().execute();


                        //  new TransferAmount().execute(customer_id);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

}
