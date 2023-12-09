package main.com.ngrewards.settingclasses;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;

public class TouchIdAct extends AppCompatActivity {
    MySession mySession;
    private RelativeLayout backlay;
    private TextView usefingerprint;
    private Switch member_touch_id;
    private ProgressBar progresbar;
    private String user_id = "", status_touchid = "";

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_id);
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
        Log.e("User Login Data", ">> " + user_log_data);
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    status_touchid = jsonObject1.getString("touch_status");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        idint();
        clickeet();
    }

    private void clickeet() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        member_touch_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mySession.touchid(true);
                    status_touchid = "Yes";
                    new UpdateToychId().execute();
                } else {
                    status_touchid = "No";

                    new UpdateToychId().execute();
                }
            }
        });
    }

    private void idint() {
        progresbar = findViewById(R.id.progresbar);
        usefingerprint = findViewById(R.id.usefingerprint);
        member_touch_id = findViewById(R.id.member_touch_id);
        member_touch_id.setChecked(status_touchid.equalsIgnoreCase("Yes"));
        String first = getResources().getString(R.string.youusefing);
        String second = getResources().getString(R.string.appand);
        String next = "<font color='#f60241'>" + getResources().getString(R.string.ngrewars) + "</font>";
        usefingerprint.setText(Html.fromHtml(first + " " + next + " " + second));
        backlay = findViewById(R.id.backlay);
    }

    private class UpdateToychId extends AsyncTask<String, String, String> {
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
                String postReceiverUrl = BaseUrl.baseurl + "update_member_touch_id.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
                params.put("touch_status", status_touchid);
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
                Log.e("Update Member Touch", ">>>>>>>>>>>>" + response);
                return response;
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String result_chk = jsonObject.getString("status");
                if (result_chk.equalsIgnoreCase("1")) {
                    mySession.setlogindata(result);
                    mySession.touchid(!status_touchid.equalsIgnoreCase("No"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}

/*package main.com.ngrewards.settingclasses;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

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
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouchIdAct extends AppCompatActivity {
    private RelativeLayout backlay;
    private TextView usefingerprint;
    private Switch member_touch_id;
    MySession mySession;
    private ProgressBar progresbar;
    private String user_id="",status_touchid="";
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_id);
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
        Log.e("User Login Data",">> "+user_log_data);
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    status_touchid = jsonObject1.getString("touch_status");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        idint();
        clickeet();
    }

    private void clickeet() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        member_touch_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mySession.touchid(true);
                    status_touchid ="Yes";
                    //new UpdateToychId().execute();
                    UpdateToychId();
                }
                else {
                    status_touchid ="No";
                    mySession.touchid(false);
                   // new UpdateToychId().execute();
                    UpdateToychId();

                }
            }
        });
    }

    private void idint() {
        progresbar = findViewById(R.id.progresbar);
        usefingerprint = findViewById(R.id.usefingerprint);
        member_touch_id = findViewById(R.id.member_touch_id);
        member_touch_id.setChecked(status_touchid.equalsIgnoreCase("Yes"));
        String first = getResources().getString(R.string.youusefing);
        String second = getResources().getString(R.string.appand);
        String next = "<font color='#f60241'>" + getResources().getString(R.string.ngrewars) + "</font>";
        usefingerprint.setText(Html.fromHtml(first + " " + next+" "+second));
        backlay = findViewById(R.id.backlay);
    }

    private class UpdateToychId extends AsyncTask<String, String, String> {
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
                String postReceiverUrl = BaseUrl.baseurl + "update_member_touch_id.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("member_id", user_id);
                params.put("touch_status", status_touchid);
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
                Log.e("Update", ">>>>>>>>>>>>" + response);
                return response;
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            try {
               JSONObject jsonObject = new JSONObject(result);
                String result_chk = jsonObject.getString("status");
                if (result_chk.equalsIgnoreCase("1")) {
                    mySession.setlogindata(result);
                    mySession.touchid(!status_touchid.equalsIgnoreCase("No"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private void UpdateToychId() {
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().update_member_touch_id(user_id,status_touchid);
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
                              //  mySession.setlogindata(object);
                                mySession.touchid(!status_touchid.equalsIgnoreCase("No"));
                                 }
                    } catch (Exception  e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }


}

*/