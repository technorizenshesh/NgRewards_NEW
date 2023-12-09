package main.com.ngrewards.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import main.com.ngrewards.Interfaces.onNotify;
import main.com.ngrewards.Models.ModelItem;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.databinding.FragmentItemDetailsBinding;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class FragmentItemDetails extends BottomSheetDialogFragment {

    private FragmentItemDetailsBinding binding;
    private BottomSheetBehavior<View> behavior;
    private ModelItem data;
    private int count = 0;
    private String user_id;
    private onNotify notify;
    private String dfhh;

    public FragmentItemDetails setData(ModelItem data, onNotify notify) {
        this.data = data;
        this.notify = notify;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_item_details, null, false);
        dialog.setContentView(binding.getRoot());
        behavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        BindView();
        return dialog;
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.tvTitle.setText(data.getTitle());
        binding.tvName.setText(data.getTitle());
        binding.tvDescription.setText(data.getDescription());
        binding.tvPrice.setText(mySession.getValueOf(MySession.CurrencySign) + data.getPrice());

        try {
            if (data.getOther_notes().equals("null")) {
                binding.etDes.setText("");
            } else {
                binding.etDes.setText(data.getOther_notes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        dfhh = data.getOther_notes();
        Glide.with(getActivity()).load(BaseUrl.image_baseurl + data.getMenu_image()).into(binding.image);
        count = Integer.parseInt(data.getNewquantity());
        binding.tvCount.setText("" + count);

        binding.imgAdd.setOnClickListener(v -> {
            count++;
            binding.tvCount.setText("" + count);
        });

        binding.imgRemove.setOnClickListener(v -> {
            if (count > 0) {
                count--;
                binding.tvCount.setText("" + count);
            }
        });

        binding.btnOrder.setOnClickListener(v -> {
            addUpdateQty();
        });
    }

    private void addUpdateQty() {

        HashMap<String, String> param = new HashMap<>();
        param.put("item_id", data.getId());
        param.put("quantity", "" + count);
        param.put("member_id", user_id);
        param.put("other_notes", binding.etDes.getText().toString());

        new ApiCallBuilder().build(getActivity())
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(BaseUrl.addUpdateQuantity())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean status = object.getString("status").contains("1");
                            if (status) {
                                Log.e("response", response);
                                notify.Refresh();
                                dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failed(String error) {

                    }
                });
    }
}
