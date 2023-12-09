package main.com.ngrewards.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import main.com.ngrewards.Models.ModelItem;
import main.com.ngrewards.Models.ModelItemList;
import main.com.ngrewards.Models.ModelMenuSetting;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.OrderActivity;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.databinding.FragmentMemberMenuBinding;
import www.develpoeramit.mapicall.ApiCallBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMemberMenu extends Fragment {
    private final ArrayList<ModelItemList> arrayList = new ArrayList<>();
    private FragmentMemberMenuBinding binding;
    private ModelMenuSetting data;
    private String URL = "";
    private String user_id;
    private MerchantListBean MerchentData;
    private String postData;
    private String dfhj;
    private String employeesales_id;
    private MySession mySession;

    public FragmentMemberMenu() {
        // Required empty public constructor
    }

    public FragmentMemberMenu setData(ModelMenuSetting data, MerchantListBean bean, String postData) {
        this.data = data;
        this.MerchentData = bean;
        this.postData = postData;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_menu, container, false);
        URL = data.getName().equalsIgnoreCase("Delivery") ? BaseUrl.memberDelivery() : BaseUrl.memberMenuList();
        mySession = new MySession(getActivity());
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

        getMenuList();

        binding.btnContinue.setOnClickListener(v -> {

            ((OrderActivity) getActivity()).FragTrans(new FragmentMenuCart().setData(data, MerchentData, postData));
            dfhj = MerchentData.getAffiliateName();

            employeesales_id = MerchentData.getEmployee_sale_id();


        });

        return binding.getRoot();
    }

    private void getMenuList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("merchant_id", MerchentData.getId());
        param.put("member_id", user_id);
        new ApiCallBuilder().build(getActivity())
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(URL)
                .execute(new ApiCallBuilder.onResponse() {
                             @Override
                             public void Success(String response) {
                                 try {
                                     JSONObject object = new JSONObject(response);
                                     boolean status = object.getString("status").contains("1");
                                     if (status) {
                                         arrayList.clear();
                                         String total_price = object.getString("total_price");
                                         String total_quantity = object.getString("total_quantity");
                                         String tax = object.getString("tax");
                                         String tax_amount = object.getString("tax_amount");
                                         String amount_due = object.getString("amount_due");

                                         binding.tvItemCount.setText("Items(" + total_quantity + ")");
                                         binding.tvTex.setText("Tax(" + tax + "%)");
                                         binding.tvTexPrice.setText(mySession.getValueOf(MySession.CurrencySign) + tax_amount);
                                         binding.tvAmountDuePrice.setText(mySession.getValueOf(MySession.CurrencySign) + amount_due);
                                         binding.tvItemTotal.setText(mySession.getValueOf(MySession.CurrencySign) + total_price);

                                         binding.footer.setVisibility(total_quantity.equals("0") ? View.GONE : View.VISIBLE);

                                         JSONArray array = object.getJSONArray("result");
                                         for (int i = 0; i < array.length(); i++) {
                                             JSONObject jsonObject = array.getJSONObject(i);
                                             Log.e("ItemList", "=====>" + jsonObject.getString("item_list"));
                                             if (jsonObject.getString("item_list").length() > 5) {
                                                 ModelItemList itemList = new ModelItemList();
                                                 itemList.setId(jsonObject.getString("id"));
                                                 itemList.setName(jsonObject.getString("name"));
                                                 itemList.setItem_list(jsonObject.getString("item_list"));
                                                 arrayList.add(itemList);
                                             }
                                             binding.recyList.setAdapter(new MenuAdapter());
                                         }
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }

                             @Override
                             public void Failed(String error) {

                             }
                         }
                );
    }

    class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_main_menu, viewGroup, false);
            return new MenuAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuAdapter.MyViewHolder holder, int possion) {
            TextView tv_name = holder.itemView.findViewById(R.id.tv_name);
            RecyclerView recy_sub_menu = holder.itemView.findViewById(R.id.recy_sub_menu);
            tv_name.setText(arrayList.get(possion).getName());
            ArrayList<ModelItem> items = new ArrayList<>();

            try {

                JSONArray array = new JSONArray(arrayList.get(possion).getItem_list());
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    ModelItem item = new ModelItem();
                    item.setId(object.getString("id"));
                    item.setTitle(object.getString("title"));
                    item.setDescription(object.getString("description"));
                    item.setMenu_image(object.getString("menu_image"));
                    item.setPrice(object.getString("price"));
                    item.setMenu_id(object.getString("menu_id"));
                    item.setMerchant_id(object.getString("merchant_id"));
                    item.setP_quantity(object.getString("p_quantity"));
                    item.setNewquantity(object.getString("newquantity"));
                    item.setOther_notes(object.getString("other_notes"));
                    items.add(item);
                }
                recy_sub_menu.setAdapter(new SubMenuAdapter(items));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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


    class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.MyViewHolder> {
        ArrayList<ModelItem> items;

        public SubMenuAdapter(ArrayList<ModelItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_sub_menu, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int possion) {
            TextView tv_name = holder.itemView.findViewById(R.id.tv_name);
            //TextView tv_descri=holder.itemView.findViewById(R.id.tv_descri);
            TextView tv_price = holder.itemView.findViewById(R.id.tv_price);
            TextView tv_qty = holder.itemView.findViewById(R.id.tv_qty);
            TextView tv_other_note = holder.itemView.findViewById(R.id.tv_other_note);
            ImageView image = holder.itemView.findViewById(R.id.image);
            tv_name.setText(items.get(possion).getTitle());
            tv_other_note.setText(items.get(possion).getSpecial());
            tv_price.setText(mySession.getValueOf(MySession.CurrencySign) + items.get(possion).getPrice());

            tv_other_note.setText(items.get(possion).getDescription());
            tv_other_note.setVisibility(items.get(possion).getOther_notes().isEmpty() ? View.GONE : View.VISIBLE);
            tv_qty.setText("(" + items.get(possion).getNewquantity() + ")");
            tv_qty.setVisibility(items.get(possion).getNewquantity().equals("0") ? View.GONE : View.VISIBLE);
            Glide.with(getActivity()).load(BaseUrl.image_baseurl + items.get(possion).getMenu_image()).into(image);

            holder.itemView.setOnClickListener(v -> {
                new FragmentItemDetails().setData(items.get(possion), FragmentMemberMenu.this::getMenuList).show(getChildFragmentManager(), "");
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
