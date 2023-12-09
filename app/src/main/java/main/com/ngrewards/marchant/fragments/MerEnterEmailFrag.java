package main.com.ngrewards.marchant.fragments;

import android.os.Bundle;
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

public class MerEnterEmailFrag extends Fragment {

    View v;
    private EditText entered_email;

    public MerEnterEmailFrag() {
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
        v = inflater.inflate(R.layout.frag_mer_enteremail, container, false);
        idint();
        return v;
    }

    private void idint() {
        entered_email = v.findViewById(R.id.entered_email);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Merchant Email >>", " >> " + MerchantSignupSlider.mer_email);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            entered_email.setText("" + MerchantSignupSlider.mer_email);

        }
    }

}
