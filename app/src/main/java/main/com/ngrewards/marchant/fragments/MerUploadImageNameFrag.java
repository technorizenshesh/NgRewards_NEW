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

public class MerUploadImageNameFrag extends Fragment {

    View v;
    private EditText first_last_name;

    public MerUploadImageNameFrag() {
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
        v = inflater.inflate(R.layout.frag_mer_firstlastname, container, false);
        idint();
        return v;
    }

    private void idint() {
        first_last_name = v.findViewById(R.id.first_last_name);
        first_last_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    MerchantSignupSlider.mer_fullname = "";
                } else {
                    MerchantSignupSlider.mer_fullname = s.toString();
                    Log.e("sa >.", "> " + MerchantSignupSlider.mer_fullname);
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