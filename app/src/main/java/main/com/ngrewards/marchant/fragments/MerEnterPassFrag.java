package main.com.ngrewards.marchant.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import main.com.ngrewards.R;
import main.com.ngrewards.marchant.activity.MerchantSignupSlider;

/**
 * Created by technorizen on 13/6/18.
 */

public class MerEnterPassFrag extends Fragment {

    View v;
    private EditText password_et;

    public MerEnterPassFrag() {
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
        v = inflater.inflate(R.layout.frag_mer_enterpass, container, false);
        iditn();
        return v;
    }

    private void iditn() {
        password_et = v.findViewById(R.id.password_et);
        password_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    MerchantSignupSlider.mer_pass = "";
                } else {
                    MerchantSignupSlider.mer_pass = s.toString();
                    Log.e("sa >.", "> " + MerchantSignupSlider.mer_pass);
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