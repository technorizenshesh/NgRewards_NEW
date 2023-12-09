package main.com.ngrewards.marchant.draweractivity;

import static main.com.ngrewards.constant.MySession.KEY_LANGUAGE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.AccountTypeSelectionAct;
import main.com.ngrewards.activity.SeeMyStripeDashBoardAct;
import main.com.ngrewards.activity.StripeExpressAcountAct;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.draweractivity.SettingActivity;
import main.com.ngrewards.marchant.merchantbottum.MerchantBottumAct;
import main.com.ngrewards.marchant.stripemerchantclasses.AddStripeConnectAccount;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.settingclasses.AboutNgReward;
import main.com.ngrewards.settingclasses.CareeersAct;
import main.com.ngrewards.settingclasses.ChangePasswordAct;
import main.com.ngrewards.settingclasses.NgHelpCenter;
import main.com.ngrewards.settingclasses.ReportProblem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerSettingActivity extends AppCompatActivity {

    SettingActivity.CountryListAdapter languageListAdapter;
    String selected_lang = "";
    private RelativeLayout changelang, changepass, seestripedashboard, genrateloginlinklay, addstripeact, addcardlay, backlay, career_lay, aboutng_rew, helpcenter, reportproblem, touchidlay, deleteAccount;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id = "", stripe_account_id = "", stripe_account_login_link = "", user_type = "";
    private Myapisession myapisession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mer_setting);
        idinti();

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
                    user_type = jsonObject1.getString("user_type");
                    stripe_account_id = jsonObject1.getString("stripe_account_id");
                    stripe_account_login_link = jsonObject1.getString("stripe_account_login_link");
                    if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                        addstripeact.setVisibility(View.GONE);
                        if (stripe_account_login_link != null && !stripe_account_login_link.equalsIgnoreCase("")) {
                            genrateloginlinklay.setVisibility(View.GONE);
                            seestripedashboard.setVisibility(View.VISIBLE);
                        } else {
                            genrateloginlinklay.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        clickevent();
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
                Intent i = new Intent(MerSettingActivity.this, CareeersAct.class);
                startActivity(i);
            }
        });
        aboutng_rew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerSettingActivity.this, AboutNgReward.class);
                startActivity(i);
            }
        });
        helpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerSettingActivity.this, NgHelpCenter.class);
                startActivity(i);
            }
        });
        reportproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerSettingActivity.this, ReportProblem.class);
                startActivity(i);
            }
        });
        touchidlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerSettingActivity.this, MerchantTouchIdAct.class);
                startActivity(i);
            }
        });
        addcardlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(MerSettingActivity.this, AddPaypalEmail.class);
                Intent i = new Intent(MerSettingActivity.this, AddStripeConnectAccount.class);
                startActivity(i);
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(MerSettingActivity.this, AddPaypalEmail.class);
                Intent i = new Intent(MerSettingActivity.this, ChangePasswordAct.class);
                i.putExtra("type", "Merchant");
                startActivity(i);
            }
        });
        changelang.setOnClickListener(v -> setSellPassDialog());

        addstripeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progresbar.getVisibility() != View.VISIBLE) {
                    Intent i = new Intent(MerSettingActivity.this, StripeExpressAcountAct.class);
                    startActivity(i);
                }
                // Intent i = new Intent(MerSettingActivity.this, AddPaypalEmail.class);
            }
        });
        genrateloginlinklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                    if (progresbar.getVisibility() != View.VISIBLE) {
                        new GenrateLoginLink().execute();
                    }
                }
            }
        });

        deleteAccount.setOnClickListener(v ->
                {
                    senOTP();
                }
        );

        seestripedashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progresbar.getVisibility() != View.VISIBLE) {
                    Intent i = new Intent(MerSettingActivity.this, SeeMyStripeDashBoardAct.class);
                    i.putExtra("stripe_login_url", stripe_account_login_link);
                    startActivity(i);
                }

            }
        });
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
                            Toast.makeText(MerSettingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
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

    private void selectImage() {

        final Dialog dialogSts = new Dialog(MerSettingActivity.this, R.style.DialogSlideAnim);
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

        tvSubmit.setOnClickListener(v ->
                {
                    if (!etVerification.getText().toString().equalsIgnoreCase("")) {
                        dialogSts.dismiss();
                        deleteAccount(etVerification.getText().toString());
                    } else {
                        Toast.makeText(MerSettingActivity.this, "Please enter verification code.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        dialogSts.show();
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

                            /*if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }*/

                            mySession.signinusers(false);
                            myapisession.setKeyAddressdata("");
                            myapisession.setKeyCartitem("");
                            myapisession.setProductdata("");
                            myapisession.setKeyOffercate("");
                            Intent i = new Intent(MerSettingActivity.this, AccountTypeSelectionAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);

                        } else {
                            Toast.makeText(MerSettingActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
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

    private void idinti() {
        changelang = findViewById(R.id.changelang);
        changepass = findViewById(R.id.changepass);
        seestripedashboard = findViewById(R.id.seestripedashboard);
        genrateloginlinklay = findViewById(R.id.genrateloginlinklay);
        progresbar = findViewById(R.id.progresbar);
        addstripeact = findViewById(R.id.addstripeact);
        addcardlay = findViewById(R.id.addcardlay);

        touchidlay = findViewById(R.id.touchidlay);

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
        if (stripe_account_id == null || stripe_account_id.equalsIgnoreCase("") || stripe_account_login_link == null || stripe_account_login_link.equalsIgnoreCase("")) {
            new GetProfile().execute();
        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void setSellPassDialog() {
        try {
            final Dialog dialogSts = new Dialog(MerSettingActivity.this, R.style.DialogSlideAnim);
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
            ArrayList<SettingActivity.LanguageBean> language_list = new ArrayList<>();
            language_list.add(new SettingActivity.LanguageBean("1", "en", "English", ""));
            language_list.add(new SettingActivity.LanguageBean("2", "hi", "Hindi", ""));
            language_list.add(new SettingActivity.LanguageBean("3", "es", "Spanish", ""));
            languageListAdapter = new SettingActivity.CountryListAdapter(MerSettingActivity.this, language_list);
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
                    Intent i = new Intent(MerSettingActivity.this, MerchantBottumAct.class);
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
                String postReceiverUrl = BaseUrl.baseurl + "merchant_profile.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("merchant_id", user_id);
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
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        mySession.setlogindata(result);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        stripe_account_id = jsonObject1.getString("stripe_account_id");
                        stripe_account_login_link = jsonObject1.getString("stripe_account_login_link");
                        if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                            addstripeact.setVisibility(View.GONE);
                            if (stripe_account_login_link != null && !stripe_account_login_link.equalsIgnoreCase("")) {
                                genrateloginlinklay.setVisibility(View.GONE);
                                seestripedashboard.setVisibility(View.VISIBLE);
                            } else {
                                genrateloginlinklay.setVisibility(View.VISIBLE);
                            }

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    private class GenrateLoginLink extends AsyncTask<String, String, String> {
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
                String postReceiverUrl = BaseUrl.baseurl + "create_stripe_login_link.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("merchant_id", user_id);
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
                Log.e("StripeLogin RES", ">>>>>>>>>>>>" + response);
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
                        new GetProfile().execute();

                    } else {
                        String messages = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_LONG).show();
                        stripe_account_id = "";
                        //new GenrateLoginLink().execute();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }


}
