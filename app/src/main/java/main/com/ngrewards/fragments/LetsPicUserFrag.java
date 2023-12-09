package main.com.ngrewards.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.SliderActivity;

/**
 * Created by technorizen on 13/6/18.
 */

public class LetsPicUserFrag extends Fragment {

    View v;
    private EditText user_name;
    private CircleImageView member_img;
    private TextView availablename, availablename1, availablename2;

    public LetsPicUserFrag() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (SliderActivity.member_bitmap != null) {
                member_img.setImageBitmap(SliderActivity.member_bitmap);
            }
            if (SliderActivity.member_first_last != null && !SliderActivity.member_first_last.equalsIgnoreCase("")) {
                String str = SliderActivity.member_first_last;
                String[] splitStr = str.split("\\s+");
                Log.e("SLI NAME =", " >> " + SliderActivity.member_first_last);
            /*
    Example: If member’s full name is John Snow then app can recommend usernames: johnsnow1, snowjohn, johnsnow02

    */
                String first = "", second = "", third = "";
                if (splitStr != null && splitStr.length > 1) {
                    first = splitStr[0] + splitStr[1] + "1";
                    third = splitStr[0] + splitStr[1] + "02";
                    second = splitStr[1] + splitStr[0];
                    //availablename.setText(""+first+" , "+second+" , "+third);
                    availablename.setText("" + first);
                    availablename1.setText("" + second);
                    availablename2.setText("" + third);
                } else if (splitStr != null && splitStr.length > 0) {
                    first = splitStr[0] + "1";
                    third = splitStr[0] + "02";
                    // second = splitStr[1] + splitStr[0];
                    availablename.setText("" + first + " , " + third);
                    availablename.setText("" + first);
                    availablename1.setText("" + third);
                    availablename2.setText("");

                }

                Log.e("AVB NAME =", " >> " + first + " ," + second + " ," + third);

            }

        } else {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_letspicusername, container, false);
        idinti();
        clickevent();
        return v;
    }

    private void clickevent() {
        availablename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name.setText("" + availablename.getText().toString());
            }
        });
        availablename1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name.setText("" + availablename1.getText().toString());

            }
        });
        availablename2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name.setText("" + availablename2.getText().toString());

            }
        });
    }

    private void idinti() {
        member_img = v.findViewById(R.id.member_img);
        availablename = v.findViewById(R.id.availablename);
        availablename1 = v.findViewById(R.id.availablename1);
        availablename2 = v.findViewById(R.id.availablename2);

        user_name = v.findViewById(R.id.user_name);
        user_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    SliderActivity.user_name_str = "";
                } else {
                    SliderActivity.user_name_str = s.toString();
                    Log.e("sa >.", "> " + SliderActivity.user_name_str);
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

    @Override
    public void onResume() {
        super.onResume();

    }
}