package main.com.ngrewards.settingclasses;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordAct extends AppCompatActivity {

    private TextView change_tv;
    private ProgressBar progresbar;
    private EditText cur_password_et, new_password_et, ver_password_et;
    private String cur_password_str = "", new_password_str = "", ver_password_str = "";
    private MySession mySession;
    private String user_id = "", type_str;
    private RelativeLayout backlay;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_change_password);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            type_str = bundle.getString("type");

        }

        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    if (type_str.equalsIgnoreCase("Member")) {
                        user_id = jsonObject1.getString("id");
                    } else {
                        user_id = jsonObject1.getString("id");
                    }


                    // image_url = jsonObject1.getString("member_image");
                    Log.e("user_id ", " .> " + user_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinit();
        clickevt();
    }

    private void clickevt() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_password_str = cur_password_et.getText().toString();
                new_password_str = new_password_et.getText().toString();
                ver_password_str = ver_password_et.getText().toString();
                if (cur_password_str == null || cur_password_str.equalsIgnoreCase("")) {
                    Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.enteryourcurrnetpass), Toast.LENGTH_LONG).show();
                } else if (new_password_str == null || new_password_str.equalsIgnoreCase("")) {
                    Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.enternewpass), Toast.LENGTH_LONG).show();

                } else if (ver_password_str == null || ver_password_str.equalsIgnoreCase("")) {
                    Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.enterverifypass), Toast.LENGTH_LONG).show();

                } else {
                    if (new_password_str.equalsIgnoreCase(ver_password_str)) {
                        if (type_str.equalsIgnoreCase("Member")) {
                            changePass();
                        } else {
                            changePassMerchant();
                        }


                    } else {
                        Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.newpascverpas), Toast.LENGTH_LONG).show();

                    }
                }

            }
        });
    }

    private void idinit() {
        change_tv = findViewById(R.id.change_tv);
        backlay = findViewById(R.id.backlay);
        progresbar = findViewById(R.id.progresbar);
        cur_password_et = findViewById(R.id.cur_password_et);
        new_password_et = findViewById(R.id.new_password_et);
        ver_password_et = findViewById(R.id.ver_password_et);
    }

    private void changePass() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().changeMemberPass(user_id, new_password_str, cur_password_str);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progresbar.setVisibility(View.GONE);
                    try {

                        String responseData = response.body().string();
                        Log.e("SALE DATA", " >>" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.yourpasswordchangedsucc), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.youenteredwrongpass), Toast.LENGTH_LONG).show();

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

    private void changePassMerchant() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().changeMerchnatPass(user_id, new_password_str, cur_password_str);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progresbar.setVisibility(View.GONE);
                    try {

                        String responseData = response.body().string();
                        Log.e("SALE DATA", " >>" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.yourpasswordchangedsucc), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordAct.this, getResources().getString(R.string.youenteredwrongpass), Toast.LENGTH_LONG).show();

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

}
