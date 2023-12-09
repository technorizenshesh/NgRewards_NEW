package main.com.ngrewards.start_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.activity.TakePermissionAct;

/**
 * Created by technorizen on 5/7/18.
 */

public class SliderLastFrag extends Fragment {

    View v;
    private TextView start_tv;

    public SliderLastFrag() {
        // Required empty public constructor
    }

    protected void attachBaseContext(Context base) {
        super.onAttach(LocaleHelper.onAttach(base));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.slider_last_lay, container, false);
        idinti();
        return v;
    }

    private void idinti() {
        start_tv = v.findViewById(R.id.start_tv);
        start_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TakePermissionAct.class);
                startActivity(i);

            }
        });
    }

}