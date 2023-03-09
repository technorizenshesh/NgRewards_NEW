package main.com.ngrewards.marchant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import main.com.ngrewards.R;
import main.com.ngrewards.marchant.activity.MerchantSignupSlider;

/**
 * Created by technorizen on 13/6/18.
 */

public class MerGiveNgReward extends Fragment {

    private SeekBar seekbar;
    private TextView percantae;
    private ImageView minus,add;
    private int cur_progress=6;
    View v;
    public MerGiveNgReward() {
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
         v= inflater.inflate(R.layout.frag_mer_givengreward, container, false);
         idint();
         clickevent();
         return v;
    }

    private void clickevent() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cur_progress==100){

                }
                else {
                    cur_progress = cur_progress+1;
                    seekbar.setProgress(cur_progress);
                    percantae.setText(""+cur_progress+" %");
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cur_progress==6){

                }
                else {
                    cur_progress = cur_progress-1;
                    seekbar.setProgress(cur_progress);
                    percantae.setText(""+cur_progress+" %");
                }


            }
        });
    }

    private void idint() {
        add = v.findViewById(R.id.add);
        minus = v.findViewById(R.id.minus);
        percantae = v.findViewById(R.id.percantae);
        seekbar = v.findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               /* cur_progress =progress;
                MerchantSignupSlider.mer_reward= String.valueOf(progress);
                percantae.setText(""+cur_progress+" %");*/

                int MIN = 6;
                if (progress <= MIN) {
                    cur_progress=6;
                    MerchantSignupSlider.mer_reward= String.valueOf(cur_progress);
                    percantae.setText(""+cur_progress+" %");

                } else {
                    cur_progress =progress;
                    MerchantSignupSlider.mer_reward= String.valueOf(progress);
                    percantae.setText(""+cur_progress+" %");
                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}