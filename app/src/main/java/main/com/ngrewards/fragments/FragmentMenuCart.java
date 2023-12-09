package main.com.ngrewards.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import main.com.ngrewards.Models.ModelMenuSetting;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.ManualActivity;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.databinding.FragmentMemberMenuBinding;
import www.develpoeramit.mapicall.ApiCallBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMenuCart extends Fragment {

    private final ArrayList<ModelItem> arrayList = new ArrayList<>();
    private FragmentMemberMenuBinding binding;
    private ModelMenuSetting data;
    private String user_id;
    private String total_quantity;
    private String total_price, tax, tax_amount, amount_due;
    private MerchantListBean merchantData;
    private String postData;
    private TextView special;
    private String dhf;
    private TextView tv_other_note;
    private MySession mySession;

    public FragmentMenuCart() {

    }

    public FragmentMenuCart setData(ModelMenuSetting data, MerchantListBean merchantListBean, String postData) {
        this.data = data;
        this.merchantData = merchantListBean;
        this.postData = postData;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_menu, container, false);
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

            AddOrder();
        });

        return binding.getRoot();
    }

    private void getMenuList() {

        HashMap<String, String> param = new HashMap<>();
        param.put("merchant_id", merchantData.getId());
        param.put("member_id", user_id);
        new ApiCallBuilder().build(getActivity())
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(BaseUrl.memberMenuCardList())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {

                        try {

                            JSONObject object = new JSONObject(response);
                            boolean status = object.getString("status").contains("1");

                            if (status) {
                                arrayList.clear();

                                total_price = object.getString("total_price");
                                total_quantity = object.getString("total_quantity");
                                tax = object.getString("tax");
                                tax_amount = object.getString("tax_amount");
                                amount_due = object.getString("amount_due");
                                binding.tvItemCount.setText("Items(" + total_quantity + ")");
                                binding.tvTex.setText("Tax(" + tax + "%)");
                                binding.tvTexPrice.setText(mySession.getValueOf(MySession.CurrencySign) + tax_amount);
                                binding.tvAmountDuePrice.setText(mySession.getValueOf(MySession.CurrencySign) + amount_due);
                                binding.tvItemTotal.setText(mySession.getValueOf(MySession.CurrencySign) + total_price);
                                binding.footer.setVisibility(total_quantity.equals("0") ? View.GONE : View.VISIBLE);
                                JSONArray array = object.getJSONArray("result");

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object2 = array.getJSONObject(i);
                                    ModelItem item = new ModelItem();
                                    item.setId(object2.getString("id"));
                                    item.setTitle(object2.getString("title"));
                                    item.setDescription(object2.getString("description"));
                                    item.setMenu_image(object2.getString("menu_image"));
                                    item.setPrice(object2.getString("price"));
                                    item.setMenu_id(object2.getString("menu_id"));
                                    item.setMerchant_id(object2.getString("merchant_id"));
                                    item.setP_quantity(object2.getString("p_quantity"));
                                    item.setNewquantity(object2.getString("newquantity"));
                                    item.setOther_notes(object2.getString("other_notes"));

                                    arrayList.add(item);
                                }

                                binding.recyList.setAdapter(new SubMenuAdapter(arrayList, data.getName()));
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

    private void addUpdateQty(int pos, int count) {

        dhf = special.getText().toString().trim();

        HashMap<String, String> param = new HashMap<>();
        param.put("item_id", arrayList.get(pos).getId());
        param.put("quantity", "" + count);
        param.put("member_id", user_id);
        param.put("other_notes", tv_other_note.getText().toString().trim());

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
                                getMenuList();
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

    private void AddOrder() {

        StringBuilder itemIds = new StringBuilder();
        StringBuilder prices = new StringBuilder();
        StringBuilder quantities = new StringBuilder();
        StringBuilder othernotes = new StringBuilder();

        Log.e("item.getOther_special()", "" + "otehernotes" + othernotes);

        for (ModelItem item : arrayList) {
            itemIds.append(item.getId() + ",");
            prices.append(item.getPrice() + ",");
            quantities.append(item.getNewquantity() + ",");
            othernotes.append(item.getOther_notes() + ",");
        }

        Log.e("total_quantity123456", total_quantity + "quantitity:" + quantities.substring(0, quantities.length() - 1));

        HashMap<String, String> param = new HashMap<>();

        param.put("item_id", itemIds.substring(0, itemIds.length() - 1));
        param.put("price", prices.substring(0, prices.length() - 1));
        param.put("quantity", quantities.substring(0, quantities.length() - 1));
        param.put("other_notes", othernotes.substring(0, othernotes.length() - 1));
        param.put("total_item", total_quantity);
        param.put("sub_total_price", total_price);
        param.put("tax_percent", tax);
        param.put("tax_price", tax_amount);
        param.put("total_amount_due", amount_due);
        param.put("delivery_charge", "");
        param.put("address_type", data.getName().equalsIgnoreCase("Delivery") ? "Yes" : "No");

        new ApiCallBuilder().build(getActivity())
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(BaseUrl.addOrderCart())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {

                        Log.e("OrderResponse", "=====>" + response);

                        try {

                            JSONObject object1 = new JSONObject(response);
                            boolean status = object1.getString("status").contains("1");

                            if (status) {

                                JSONObject putData = new JSONObject(postData);
                                JSONObject object = object1.getJSONArray("result").getJSONObject(0);
                                String OrderID = object.getString("id");

                                Log.e("object", String.valueOf(object));
                                Log.e("OrderID", OrderID);

                                Log.e("getAffiliateName", merchantData.getAffiliateName());

                                //   Toast.makeText(getContext(), "getAffiliateName"+  merchantData.getAffiliateName(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "employee_slaes_id" + merchantData.getEmployee_sale_id(), Toast.LENGTH_SHORT).show();

                                putData.put("id", OrderID);
                                putData.put("sub_total_price", total_price);
                                putData.put("tax_percent", tax);
                                putData.put("tax_price", tax_amount);
                                putData.put("total_amount_due", amount_due);

                                Intent intent = new Intent(getActivity(), ManualActivity.class);
                                intent.putExtra("merchant_date", merchantData);
                                intent.putExtra("sub_total_price", total_price);
                                intent.putExtra("merchant_id", merchantData.getId());
                                intent.putExtra("merchant_name", merchantData.getAffiliateName());
                                intent.putExtra("tax_percent", tax);
                                intent.putExtra("tax_price", tax_amount);
                                intent.putExtra("total_amount_due", amount_due);
                                intent.putExtra("type", "order");
                                intent.putExtra("order_cart_id", OrderID);
                                intent.putExtra("result", putData.toString());

                                intent.putExtra("employee_sales_id", merchantData.getEmployee_sale_id());
                                intent.putExtra("employee_slaes_name", merchantData.getEmployee_sale_name());

                                intent.putExtra("merchant_name", merchantData.getBusinessName());
                                intent.putExtra("merchant_number", merchantData.getBusinessNo());


                                startActivity(intent);
                                getActivity().finish();
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

    class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.MyViewHolder> {
        private final String name;
        ArrayList<ModelItem> items;

        public SubMenuAdapter(ArrayList<ModelItem> items, String name) {
            this.items = items;
            this.name = name;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_cart_menu, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int possion) {
            TextView tv_name = holder.itemView.findViewById(R.id.tv_name);
            TextView tv_descri = holder.itemView.findViewById(R.id.tv_descri);
            TextView tv_price = holder.itemView.findViewById(R.id.tv_price);
            TextView tv_qty = holder.itemView.findViewById(R.id.tv_qty);
            TextView tv_count = holder.itemView.findViewById(R.id.tv_count);
            ImageView image = holder.itemView.findViewById(R.id.image);
            ImageView img_add = holder.itemView.findViewById(R.id.img_add);
            ImageView img_remove = holder.itemView.findViewById(R.id.img_remove);
            ImageView img_delete = holder.itemView.findViewById(R.id.img_delete);

            tv_other_note = holder.itemView.findViewById(R.id.tv_other_note);
            special = holder.itemView.findViewById(R.id.special);

            //   special.setText(name);

            tv_name.setText(items.get(possion).getTitle());
            tv_descri.setText(items.get(possion).getDescription());
            tv_price.setText(mySession.getValueOf(MySession.CurrencySign) + items.get(possion).getPrice());
            tv_qty.setText("(" + items.get(possion).getNewquantity() + ")");
            tv_other_note.setText("" + items.get(possion).getOther_notes());

            special.setText("" + items.get(possion).getOther_notes());


            tv_other_note.setVisibility(items.get(possion).getOther_notes().isEmpty() ? View.GONE : View.VISIBLE);
            tv_count.setText(items.get(possion).getNewquantity());
            tv_qty.setVisibility(items.get(possion).getNewquantity().equals("0") ? View.GONE : View.VISIBLE);
            Glide.with(getActivity()).load(BaseUrl.image_baseurl + items.get(possion).getMenu_image()).into(image);

            holder.itemView.setOnClickListener(v -> {
                new FragmentItemDetails().setData(items.get(possion), FragmentMenuCart.this::getMenuList).show(getChildFragmentManager(), "");
            });

            img_add.setOnClickListener(v -> {
                int count = Integer.parseInt(items.get(possion).getNewquantity());
                count++;
                addUpdateQty(possion, count);

            });
            img_remove.setOnClickListener(v -> {
                int count1 = Integer.parseInt(items.get(possion).getNewquantity());
                count1--;
                addUpdateQty(possion, count1);
            });
            img_delete.setOnClickListener(v -> {
                addUpdateQty(possion, 0);
                // DeleteItem(possion);
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
