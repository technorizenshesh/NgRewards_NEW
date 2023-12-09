package main.com.ngrewards.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import main.com.ngrewards.Models.ModelMenuSetting;
import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.databinding.ActivityOrderBinding;
import main.com.ngrewards.fragments.FragmentOrderSubmit;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class OrderActivity extends AppCompatActivity {
    private final ArrayList<ModelMenuSetting> arrayList = new ArrayList<>();
    private ActivityOrderBinding binding;
    private MerchantListBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        data = (MerchantListBean) getIntent().getExtras().getSerializable("data");

        getMerchantMenuSettingList();

        binding.btnContinue.setOnClickListener(v -> {
            for (ModelMenuSetting setting : arrayList) {
                if (setting.isSelected()) {
                    binding.tvTitle.setText(setting.getName());
                    FragTrans(new FragmentOrderSubmit().setData(setting, data));
                    break;
                }


            }
        });

        binding.imgBack.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }

    private void getMerchantMenuSettingList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("merchant_id", data.getId());
        new ApiCallBuilder().build(this)
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(BaseUrl.getMerchantMenuSettingList())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean status = object.getString("status").contains("1");
                            if (status) {
                                JSONArray array = object.getJSONArray("result").getJSONObject(0).getJSONArray("menu_setting_list");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String image = jsonObject.getString("image");
                                    arrayList.add(new ModelMenuSetting(id, image, name));
                                    binding.recyList.setAdapter(new orderAdapter());
                                }
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

    public void FragTrans(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_down);
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack("fragments");
        transaction.commit();
    }

    class orderAdapter extends RecyclerView.Adapter<orderAdapter.MyViewHolder> {

        @NonNull
        @Override
        public orderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(OrderActivity.this).inflate(R.layout.layout_menu_setting, viewGroup, false);
            return new orderAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull orderAdapter.MyViewHolder holder, int possion) {
            ImageView image = holder.itemView.findViewById(R.id.image);
            TextView tv_name = holder.itemView.findViewById(R.id.tv_name);
            LinearLayout parent_layout = holder.itemView.findViewById(R.id.parent_layout);
            tv_name.setText(arrayList.get(possion).getName());

            Glide.with(OrderActivity.this).load(BaseUrl.image_baseurl + arrayList.get(possion).getImage()).into(image);
            parent_layout.setBackgroundResource(arrayList.get(possion).isSelected() ? R.drawable.bg_gray_light : R.drawable.border_grey_whtstroke);
            parent_layout.setOnClickListener(v -> {
                for (int i = 0; i < arrayList.size(); i++) {
                    arrayList.get(i).setSelected(false);
                }
                arrayList.get(possion).setSelected(true);
                binding.btnContinue.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
