package main.com.ngrewards.fragments;

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
import main.com.ngrewards.activity.SliderActivity;

/**
 * Created by technorizen on 13/7/18.
 */

public class EnterMobfrag extends Fragment {
    View v;
    private EditText entered_mobile;

    public EnterMobfrag() {
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
        v = inflater.inflate(R.layout.enter_mem_mobile, container, false);
        idinit();
        return v;
    }

    private void idinit() {
        entered_mobile = v.findViewById(R.id.entered_mobile);
        entered_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    SliderActivity.user_number_str = "";
                } else {
                    SliderActivity.user_number_str = s.toString();
                    Log.e("sa >.", "> " + SliderActivity.user_number_str);
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