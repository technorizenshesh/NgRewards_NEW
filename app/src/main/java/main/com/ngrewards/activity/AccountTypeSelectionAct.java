package main.com.ngrewards.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.draweractivity.ProfileActivity;
import main.com.ngrewards.marchant.MarchantLogin;

public class AccountTypeSelectionAct extends AppCompatActivity {

    public String account_type = "";
    private ImageView member_img, marchant_img;
    private TextView member_txt_lay, marchant_txt, next_tv, memtxt, martxt;
    private LinearLayout merchant_lay, member_lay;
    private Dialog dialogSts;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_account_type_selection);

        PreferenceConnector.writeString(AccountTypeSelectionAct.this, PreferenceConnector.Create_Profile, "create_profile");
        idint();
        clikevent();
    }

    private void clikevent() {

        merchant_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_type = "marchant";
                member_img.setImageResource(R.drawable.member_new_ic);
                member_txt_lay.setTextColor(getResources().getColor(R.color.black));
                marchant_img.setImageResource(R.drawable.marchant_new_ic_sel);
                marchant_txt.setTextColor(getResources().getColor(R.color.pinkborder));
                member_lay.setBackgroundResource(R.drawable.grey_rectangle);
                merchant_lay.setBackgroundResource(R.drawable.pink_rectangle);
                martxt.setTextColor(getResources().getColor(R.color.pinkborder));
                memtxt.setTextColor(getResources().getColor(R.color.black));
            }
        });

        member_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_type = "member";
                member_img.setImageResource(R.drawable.member_new_ic_sel);
                member_txt_lay.setTextColor(getResources().getColor(R.color.pinkborder));
                marchant_img.setImageResource(R.drawable.marchant_new_ic);
                marchant_txt.setTextColor(getResources().getColor(R.color.black));
                martxt.setTextColor(getResources().getColor(R.color.black));
                memtxt.setTextColor(getResources().getColor(R.color.pinkborder));
                member_lay.setBackgroundResource(R.drawable.pink_rectangle);
                merchant_lay.setBackgroundResource(R.drawable.grey_rectangle);

            }
        });

        next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.employee_id, "");
                PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector.employee_name, "");


                if (account_type == null || account_type.equalsIgnoreCase("")) {
                    Toast.makeText(AccountTypeSelectionAct.this, getResources().getString(R.string.selectaccouttype), Toast.LENGTH_LONG).show();
                } else if (account_type.equalsIgnoreCase("marchant")) {
                    Intent i = new Intent(AccountTypeSelectionAct.this, MarchantLogin.class);
                    startActivity(i);/* Intent i = new Intent(AccountTypeSelectionAct.this, MerchantWelLogSig.class);
                    startActivity(i);*/
                } else {
                    Intent i = new Intent(AccountTypeSelectionAct.this, LoginActivity.class);
                    i.putExtra("logout_status", "true");
                    startActivity(i);
                }

            }

        });

    }

    private void idint() {

        createUserName();

        PreferenceConnector.writeString(AccountTypeSelectionAct.this, PreferenceConnector.UserNAme, "");
        PreferenceConnector.writeString(AccountTypeSelectionAct.this, PreferenceConnector.UserNAme1, "");

        martxt = findViewById(R.id.martxt);
        memtxt = findViewById(R.id.memtxt);
        next_tv = findViewById(R.id.next_tv);
        marchant_img = findViewById(R.id.marchant_img);
        member_img = findViewById(R.id.member_img);
        marchant_txt = findViewById(R.id.marchant_txt);
        member_txt_lay = findViewById(R.id.member_txt_lay);
        merchant_lay = findViewById(R.id.merchant_lay);
        member_lay = findViewById(R.id.member_lay);
    }

    private void createUserName() {

        dialogSts = new Dialog(AccountTypeSelectionAct.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.custom_popup_create_memberuser);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView no_tv = dialogSts.findViewById(R.id.no_tv);
        TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);

        yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(AccountTypeSelectionAct.this, ProfileActivity.class);
                startActivity(i);

            }
        });

        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();


            }
        });

        try {

            dialogSts.dismiss();

        } catch (Exception e) {

        }

    }
}
