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
 * Created by technorizen on 13/6/18.
 */

public class EnterPassFrag extends Fragment {
    View v;
    private EditText password_et;

    public EnterPassFrag() {
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
        v = inflater.inflate(R.layout.frag_enterpass, container, false);
        idinit();
        return v;
    }

    private void idinit() {
        password_et = v.findViewById(R.id.password_et);
        password_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    SliderActivity.member_pass_str = "";
                } else {
                    SliderActivity.member_pass_str = s.toString();
                    Log.e("sa >.", "> " + SliderActivity.member_pass_str);
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