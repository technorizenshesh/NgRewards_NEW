package main.com.ngrewards.productfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.ProduCtDetailAct;

/**
 * Created by technorizen on 13/6/18.
 */

public class ProAboutFrag extends Fragment {

    private TextView description_tv, phone_number, openinghours, address;
    private View v;

    public ProAboutFrag() {
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
        v = inflater.inflate(R.layout.pro_aboutfrag_lay, container, false);
        idinit();
        return v;
    }

    private void idinit() {

        phone_number = v.findViewById(R.id.phone_number);
        address = v.findViewById(R.id.address);
        openinghours = v.findViewById(R.id.openinghours);
        description_tv = v.findViewById(R.id.description_tv);


        if (ProduCtDetailAct.productDetailArrayList != null && !ProduCtDetailAct.productDetailArrayList.isEmpty()) {

            description_tv.setText("" + ProduCtDetailAct.productDetailArrayList.get(0).getProductDescription());
        }
    }

}
