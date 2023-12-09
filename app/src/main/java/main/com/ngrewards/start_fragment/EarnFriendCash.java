package main.com.ngrewards.start_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import main.com.ngrewards.R;

/**
 * Created by technorizen on 5/7/18.
 */

public class EarnFriendCash extends Fragment {

    public EarnFriendCash() {
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
        return inflater.inflate(R.layout.earn_friendlay, container, false);
    }

}
