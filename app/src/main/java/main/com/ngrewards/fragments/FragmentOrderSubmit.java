package main.com.ngrewards.fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import main.com.ngrewards.Models.ModelMenuSetting;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.OrderActivity;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.databinding.FragmentOrderSubmitBinding;
import main.com.ngrewards.placeorderclasses.AllAddedAddressAct;


public class FragmentOrderSubmit extends Fragment {
    private FragmentOrderSubmitBinding binding;
    private ModelMenuSetting data;
    private MerchantListBean merchantData;
    private String user_id, phone, fullname;
    private String name_affilited;

    public FragmentOrderSubmit() {
        // Required empty public constructor
    }

    public FragmentOrderSubmit setData(ModelMenuSetting data, MerchantListBean bean) {
        this.data = data;
        this.merchantData = bean;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_submit, container, false);

        BindView();

        return binding.getRoot();
    }


    private void BindView() {
        MySession mySession = new MySession(getActivity());
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data != null) {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    Log.e("UserRecord", "========>" + jsonObject1);
                    user_id = jsonObject1.getString("id");
                    phone = jsonObject1.getString("phone");
                    fullname = jsonObject1.getString("fullname");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.tvName.setText(fullname);
        binding.tvNumber.setText(phone);
        binding.tvMerchantContact.setText(getString(R.string.merchant_mobile) + merchantData.getContactNumber());

        if (data.getName().equalsIgnoreCase("Take Out")) {
            binding.li2.setVisibility(View.GONE);

        }

        if (data.getName().equalsIgnoreCase("Delivery")) {
            binding.li2.setVisibility(View.GONE);
            binding.liAddress.setVisibility(View.VISIBLE);
        }

        binding.tvSelectDate.setOnClickListener(v -> Tools.DatePicker(getActivity(), binding.tvSelectDate::setText));
        //  binding.tvSelectTime.setOnClickListener(v -> Tools.TimePicker(getActivity(), binding.tvSelectTime::setText, true, false));
        binding.tvSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String timeSet = "";

                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            timeSet = "PM";
                        } else if (selectedHour == 0) {
                            selectedHour += 12;
                            timeSet = "AM";
                        } else if (selectedHour == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";

                        int hour = selectedHour % 12;
                        if (hour == 0)
                            hour = 12;

                        binding.tvSelectTime.setText(String.format("%02d:%02d %s", hour, selectedMinute, timeSet));

                        //binding.tvSelectTime.setText(String.format(selectedHour + ":" + selectedMinute + " " + timeSet));
                        //  binding.tvSelectTime.setText(new SimpleDateFormat("hh:mm aa").format(hour + minute + am_pm));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {

            name_affilited = merchantData.getContactName();

            JSONObject object = new JSONObject();

            if (Validation()) {

                try {

                    object.put("order_guset_No", binding.etGuests.getText().toString());
                    object.put("order_Table_No", binding.etTableNo.getText().toString());
                    object.put("order_Address_Id", AllAddedAddressAct.AddressID);
                    object.put("order_special_request", binding.etDes.getText().toString());
                    object.put("order_Date", binding.tvSelectDate.getText().toString());
                    object.put("order_Time", binding.tvSelectTime.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ((OrderActivity) getActivity()).FragTrans(new FragmentMemberMenu().setData(data, merchantData, object.toString()));

                PreferenceConnector.writeString(getContext(), PreferenceConnector.guest_no, binding.etGuests.getText().toString());
                PreferenceConnector.writeString(getContext(), PreferenceConnector.table_no, binding.etTableNo.getText().toString());
                PreferenceConnector.writeString(getContext(), PreferenceConnector.order_Address_Id, AllAddedAddressAct.AddressID);
                PreferenceConnector.writeString(getContext(), PreferenceConnector.order_Date, binding.tvSelectDate.getText().toString());
                PreferenceConnector.writeString(getContext(), PreferenceConnector.order_Time, binding.tvSelectTime.getText().toString());
                PreferenceConnector.writeString(getContext(), PreferenceConnector.order_special_request, binding.etDes.getText().toString());

            }
        });

        binding.tvAddAddress.setOnClickListener(v -> startActivity(new Intent(getActivity(), AllAddedAddressAct.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (data.getName().equalsIgnoreCase("Delivery")) {
            if (AllAddedAddressAct.address1_str != "") {
                binding.tvAddAddress.setText(getString(R.string.edit));
                binding.liSelectedAddess.setVisibility(View.VISIBLE);
                binding.tvAddressLine1.setText(AllAddedAddressAct.address1_str);
                binding.tvAddressLine2.setText(AllAddedAddressAct.address2_str);
                binding.tvCountry.setText(AllAddedAddressAct.countrytv_str);
                binding.tvState.setText(AllAddedAddressAct.state_str);
                binding.tvContact.setText(AllAddedAddressAct.phonetv_str);
            }
        }
    }

    boolean Validation() {

        if (binding.tvSelectDate.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.select_time), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.tvSelectTime.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.select_time), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (data.getName().equalsIgnoreCase("Dine In")) {
            if (binding.etGuests.getText().toString().isEmpty()) {
                binding.etGuests.setError("Required");
                binding.etGuests.requestFocus();
                return false;
            }
            if (binding.etTableNo.getText().toString().isEmpty()) {
                binding.etTableNo.setError("Required");
                binding.etTableNo.requestFocus();
                return false;
            }
        }

        if (data.getName().equalsIgnoreCase("Delivery")) {
            if (AllAddedAddressAct.address1_str == "") {
                Toast.makeText(getActivity(), getString(R.string.addaddress), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}
