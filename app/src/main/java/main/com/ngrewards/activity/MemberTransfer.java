package main.com.ngrewards.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.constant.Myapisession;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.restapi.ApiClient;

public class MemberTransfer extends AppCompatActivity {

    private TextView toafriend,international;
    private RelativeLayout backlay;
    private ProgressBar progresbar;
    public static ArrayList<MemberDetail> memberDetailArrayList;
    private Myapisession myapisession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_transfer);
        myapisession = new Myapisession(this);
        idint();
        clickevt();
    }

    private void clickevt() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); toafriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MemberTransfer.this,TransferToaFriend.class);
                startActivity(i);
            }
        }); international.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MemberTransfer.this,InternationalTransAct.class);
                startActivity(i);
            }
        });
    }

    private void idint() {
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        international = findViewById(R.id.international);
        toafriend = findViewById(R.id.toafriend);
        if (myapisession.getKeyMemberusername()==null||myapisession.getKeyMemberusername().equalsIgnoreCase("")){
            getUsername();
        }
        else {
            try {
                memberDetailArrayList = new ArrayList<>();
                JSONObject object = new JSONObject(myapisession.getKeyMemberusername());
                Log.e("Product Category >", " >" + myapisession.getKeyMemberusername());
                if (object.getString("status").equals("1")) {
                    MemberBean successData = new Gson().fromJson(myapisession.getKeyMemberusername(), MemberBean.class);
                    memberDetailArrayList.addAll(successData.getResult());
                }
                else {
                    getUsername();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    private void getUsername() {
        Log.e("User name list>", " >GET NAME");

        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("User name list>", " >" + responseData);
                        if (object.getString("status").equals("1"))
                        {
                            myapisession.setKeyMemberusername(responseData);
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);
                            memberDetailArrayList.addAll(successData.getResult());
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
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
