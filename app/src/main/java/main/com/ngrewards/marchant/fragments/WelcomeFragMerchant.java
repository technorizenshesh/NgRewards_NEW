package main.com.ngrewards.marchant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.TermsAndCondition;
import main.com.ngrewards.marchant.activity.MerchantSignupSlider;

/**
 * Created by technorizen on 13/6/18.
 */

public class WelcomeFragMerchant extends Fragment {

    private EditText email_id;
    private View v;
    private TextView termscheck;

    public WelcomeFragMerchant() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.merchant_welcome_lay, container, false);
        idint();
        return v;
    }

    private void idint() {
        email_id = v.findViewById(R.id.email_id);
        termscheck = v.findViewById(R.id.termscheck);
        String first = getResources().getString(R.string.byreg);

        String next = "<font color='#f60241'>" + getResources().getString(R.string.termsser) + "</font>";
        termscheck.setText(Html.fromHtml(first + " " + next));
        termscheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TermsAndCondition.class);
                intent.putExtra("status", "terms");
                startActivity(intent);
            }
        });


        email_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    MerchantSignupSlider.mer_email = "";
                } else {
                    MerchantSignupSlider.mer_email = s.toString();
                    Log.e("sa >.", "> " + MerchantSignupSlider.mer_email);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
    }

}
