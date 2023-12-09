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
import main.com.ngrewards.activity.WelcomeActivity;

/**
 * Created by technorizen on 13/6/18.
 */

public class EnterEmailFrag extends Fragment {
    View v;
    private EditText email_id;

    public EnterEmailFrag() {
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
        v = inflater.inflate(R.layout.frag_enteremail, container, false);
        idinit();
        return v;
    }

    private void idinit() {
        email_id = v.findViewById(R.id.email_id);
        email_id.setText(WelcomeActivity.member_email_str);
        email_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    SliderActivity.member_email_str = "";
                } else {
                    SliderActivity.member_email_str = s.toString();
                    Log.e("sa >.", "> " + SliderActivity.member_email_str);
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
