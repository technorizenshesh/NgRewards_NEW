package main.com.ngrewards.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.com.ngrewards.Models.ModelItem;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.databinding.FragmentOrderBinding;
import www.develpoeramit.mapicall.ApiCallBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrder extends BottomSheetDialogFragment {
    private final ArrayList<ModelItem> arrayList = new ArrayList<>();
    private final ArrayList<ModelItem> arrayList1 = new ArrayList<>();
    private FragmentOrderBinding binding;
    private String user_id;
    private String total_quantity;
    private String total_price, tax, tax_amount, amount_due;
    private BottomSheetBehavior<View> behavior;
    private String CartID;
    private String sub_total_price;
    private String other_notes;
    private ArrayList[] new_total;
    private String[] new_quanitity;
    private String[] other_notes1;
    private TextView tv_qty;
    private ArrayList<String> listOfString;
    private ModelItem item;
    private String stringnew;
    private ArrayList<String> listOfString1;
    private String stringnew1;
    private MySession mySession;

    public FragmentOrder() {
        // Required empty public constructor
    }

    public FragmentOrder setData(String id) {
        this.CartID = id;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_order, null, false);
        dialog.setContentView(binding.getRoot());
        behavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                Log.e("TAG", "onCreateDialog: " + e.getMessage());
                Log.e("TAG", "onCreateDialog: " + e.getLocalizedMessage());
            }
        }

        getMenuList();

        binding.imgBack.setOnClickListener(v -> dismiss());
        return dialog;
    }

    private void getMenuList() {
        HashMap<String, String> param = new HashMap<>();
        param.put("order_cart_id", CartID);
        Log.e("CartID", CartID);

        new ApiCallBuilder().build(getActivity())
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(BaseUrl.order_cart_list())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {

                        try {
                            JSONObject object_rs = new JSONObject(response);
                            boolean status = object_rs.getString("status").contains("1");

                            if (status) {

                                JSONObject object = object_rs.getJSONArray("result").getJSONObject(0);
                                arrayList.clear();

                                Log.e("responseeeeee>===", response);

                                total_price = object.getString("total_amount_due");
                                total_quantity = object.getString("quantity");
                                tax = object.getString("tax_percent");
                                tax_amount = object.getString("tax_price");
                                amount_due = object.getString("total_amount_due");
                                sub_total_price = object.getString("sub_total_price");
                                other_notes = object.getString("other_notes");
                                new_quanitity = total_quantity.split(",");
                                other_notes1 = other_notes.split(",");

                                binding.tvItemCount.setText("Items(" + total_quantity + ")");
                                binding.tvTex.setText("Tax(" + tax + "%)");
                                binding.tvTexPrice.setText(mySession.getValueOf(MySession.CurrencySign) + tax_amount);
                                binding.tvAmountDuePrice.setText(mySession.getValueOf(MySession.CurrencySign) + amount_due);
                                binding.tvItemTotal.setText(mySession.getValueOf(MySession.CurrencySign) + sub_total_price);

                                JSONArray array = object.getJSONArray("item_list");

                                List<String> fixedLenghtList = Arrays.asList(new_quanitity);

                                List<String> fixedLenghtList1 = Arrays.asList(other_notes1);

                                listOfString = new ArrayList<String>(fixedLenghtList);
                                listOfString1 = new ArrayList<String>(fixedLenghtList1);

                                Log.e("listOfString", String.valueOf(listOfString));

                                for (int j = 0; j < listOfString.size(); j++) {

                                    stringnew = listOfString.get(j);

                                }

                                Log.e("listOfString1", String.valueOf(listOfString1));

                                for (int k = 0; k < listOfString1.size(); k++) {

                                    stringnew1 = listOfString1.get(k);

                                }


                                for (int i = 0; i < array.length(); i++) {

                                    item = new ModelItem();

                                    JSONObject object2 = array.getJSONObject(i);

                                    item.setId(object2.getString("id"));
                                    item.setTitle(object2.getString("title"));
                                    item.setDescription(object2.getString("description"));
                                    item.setMenu_image(object2.getString("menu_image"));
                                    item.setPrice(object2.getString("price"));
                                    item.setMenu_id(object2.getString("menu_id"));
                                    item.setMerchant_id(object2.getString("merchant_id"));
                                    item.setP_quantity(object2.getString("quantity"));
                                    item.setNewquantity(object2.getString("newquantity"));
                                    item.setOther_notes(object2.getString("other_notes"));
                                    item.setSpecial(other_notes);
                                    arrayList.add(item);
                                }

                                binding.recyList.setAdapter(new SubMenuAdapter(arrayList, listOfString, listOfString1));
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
        private final ArrayList items1;
        private final ArrayList items2;
        ArrayList<ModelItem> items;

        public SubMenuAdapter(ArrayList<ModelItem> items, ArrayList<String> stringnew, ArrayList<String> stringnew1) {
            this.items = items;
            this.items1 = stringnew;
            this.items2 = stringnew1;

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
            tv_qty = holder.itemView.findViewById(R.id.tv_qty);
            TextView tv_count = holder.itemView.findViewById(R.id.tv_count);
            ImageView image = holder.itemView.findViewById(R.id.image);
            ImageView img_add = holder.itemView.findViewById(R.id.img_add);
            ImageView img_remove = holder.itemView.findViewById(R.id.img_remove);
            ImageView img_delete = holder.itemView.findViewById(R.id.img_delete);

            LinearLayout lay_right = holder.itemView.findViewById(R.id.lay_right);
            TextView special = holder.itemView.findViewById(R.id.special);
            lay_right.setVisibility(View.GONE);
            img_delete.setVisibility(View.GONE);

            TextView tv_other_note = holder.itemView.findViewById(R.id.tv_other_note);
            tv_name.setText(items.get(possion).getTitle());
            tv_descri.setText(items.get(possion).getDescription());
            tv_price.setText(mySession.getValueOf(MySession.CurrencySign) + items.get(possion).getPrice());

            Log.e("items12311231234", items.get(possion).getPrice());

            //    tv_qty.setText("(" + items.get(possion).getNewquantity1234() + ")");

            tv_qty.setText("(" + items1.get(possion).toString() + ")");

            tv_other_note.setText("" + items2.get(possion).toString());

            tv_count.setText(items.get(possion).getNewquantity());
            //tv_qty.setVisibility(items.get(possion).getNewquantity().equals("0") ? View.GONE : View.VISIBLE);
            Glide.with(getActivity()).load(BaseUrl.image_baseurl + items.get(possion).getMenu_image()).into(image);


            holder.itemView.setOnClickListener(v -> {

                //Toast.makeText(getContext(),"Sagar!!",Toast.LENGTH_SHORT).show();
                new FragmentItemDetails().setData(items.get(possion), FragmentOrder.this::getMenuList).show(getChildFragmentManager(), "");
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
