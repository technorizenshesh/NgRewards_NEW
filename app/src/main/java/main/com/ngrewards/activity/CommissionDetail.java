package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.CommissionData;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommissionDetail extends AppCompatActivity {

    private ExpandableHeightListView commision_list;
    private RelativeLayout backlay;
    private CommisionDetAdapter commisionDetAdapter;
    private Spinner amountpercant;
    private AmountSeleAdapter amountSeleAdapter;
    private ArrayList<String> amountlist;
    private TextView withdraw_amount_but, weekdatemont_tv, total_week_commision, withdraw_bank_amt, ngcash_remain;
    private String total_week_count = "", availabel_week_withdraw_status = "", total_week_commision_str = "0.00", week_str = "", week_start_month_str = "", week_end_month_str = "", week_year_str = "";
    private String user_id = "", stripe_account_id = "", withdraw_status = "", withdaraw_accoutn_str = "0.00", remain_ng_cash_str = "0.00", affiliate_number = "";
    private MySession mySession;
    private ProgressBar progressabar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_commission_detail);
        amountlist = new ArrayList<>();
        amountlist.add("25 %");
        amountlist.add("50 %");
        amountlist.add("75 %");
        amountlist.add("100 %");
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    affiliate_number = jsonObject1.getString("affiliate_number");
                    // image_url = jsonObject1.getString("member_image");
                    Log.e("affiliate_number ", " .> " + affiliate_number);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.isEmpty()) {

        } else {
            week_str = bundle.getString("week_str");
            withdraw_status = bundle.getString("withdraw_status");
            total_week_count = bundle.getString("total_week_count");
            week_start_month_str = bundle.getString("week_start_month_str");
            week_end_month_str = bundle.getString("week_end_month_str");
            week_year_str = bundle.getString("week_year_str");
            stripe_account_id = bundle.getString("stripe_account_id");
            total_week_commision_str = bundle.getString("total_week_commision_str");
            availabel_week_withdraw_status = bundle.getString("availabel_week_withdraw_status");
        }

        idinit();
        clickevent();
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        withdraw_amount_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (availabel_week_withdraw_status == null) {

                } else {
                    if (withdraw_status != null && !withdraw_status.equalsIgnoreCase("") && withdraw_status.equalsIgnoreCase("Confirm")) {

                    } else {
                        if (availabel_week_withdraw_status.equalsIgnoreCase("No")) {
                            Toast.makeText(CommissionDetail.this, getResources().getString(R.string.pleasewaitforweekcomplete), Toast.LENGTH_LONG).show();

                        } else {
                            if (stripe_account_id != null && !stripe_account_id.equalsIgnoreCase("")) {
                                withdrawAmount();
                            } else {
                                Toast.makeText(CommissionDetail.this, getResources().getString(R.string.addstripeaccount), Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                }
            }
        });
    }


    private void withdrawAmount() {
        progressabar.setVisibility(View.VISIBLE);
        // swipeToRefresh.setRefreshing(true);
        Log.e(" para meter value >>", " >> " + user_id + " , " + week_str + " , " + week_year_str + " , " + total_week_commision_str + " , " + remain_ng_cash_str + " , " + withdaraw_accoutn_str);
        Call<ResponseBody> call = ApiClient.getApiInterface().withdrawCommision(user_id, week_str, week_year_str, total_week_commision_str, remain_ng_cash_str, withdaraw_accoutn_str, stripe_account_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressabar.setVisibility(View.GONE);
                //swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("With draw comm res>", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {


                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressabar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }


    private void idinit() {
        progressabar = findViewById(R.id.progressabar);
        ngcash_remain = findViewById(R.id.ngcash_remain);
        withdraw_amount_but = findViewById(R.id.withdraw_amount_but);

        if (withdraw_status != null && withdraw_status.equalsIgnoreCase("Confirm")) {
            withdraw_amount_but.setBackgroundResource(R.color.darkgreen);
            withdraw_amount_but.setText("Completed");
        }

        withdraw_bank_amt = findViewById(R.id.withdraw_bank_amt);
        weekdatemont_tv = findViewById(R.id.weekdatemont_tv);
        total_week_commision = findViewById(R.id.total_week_commision);
        amountpercant = findViewById(R.id.amountpercant);
        commision_list = findViewById(R.id.commision_list);
        commision_list.setExpanded(true);
        backlay = findViewById(R.id.backlay);
        commisionDetAdapter = new CommisionDetAdapter(CommissionDetail.this, CommisionActivity.commissionDataArrayList);
        commision_list.setAdapter(commisionDetAdapter);
        commisionDetAdapter.notifyDataSetChanged();
        amountSeleAdapter = new AmountSeleAdapter(CommissionDetail.this, amountlist);
        amountpercant.setAdapter(amountSeleAdapter);

        weekdatemont_tv.setText("Week " + week_str + "/" + total_week_count + "\n" + week_start_month_str + "-" + week_end_month_str);
        total_week_commision.setText(mySession.getValueOf(MySession.CurrencySign) + total_week_commision_str);
        amountpercant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (amountlist != null && !amountlist.isEmpty()) {
                    if (amountlist.get(position).equalsIgnoreCase("25 %")) {
                        if (total_week_commision_str == null || total_week_commision_str.equalsIgnoreCase("") || total_week_commision_str.equalsIgnoreCase("null")) {
                            total_week_commision_str = "0";
                        }

                        double comission_str = Double.parseDouble(total_week_commision_str);

                        double res = (comission_str / 100) * 25;
                        Log.e("Data 25 >> ", " >> " + res);

                        ngcash_remain.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(res)));
                        double withdraw_amt = comission_str - res;
                        withdraw_bank_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(withdraw_amt)));
                        withdaraw_accoutn_str = String.format("%.2f", new BigDecimal(withdraw_amt));
                        remain_ng_cash_str = String.format("%.2f", new BigDecimal(res));
                    } else if (amountlist.get(position).equalsIgnoreCase("50 %")) {
                        double comission_str = Double.parseDouble(total_week_commision_str);

                        double res = (comission_str / 100) * 50;
                        Log.e("Data 50 >> ", " >> " + res);

                        ngcash_remain.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(res)));
                        double withdraw_amt = comission_str - res;
                        withdraw_bank_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(withdraw_amt)));
                        withdaraw_accoutn_str = String.format("%.2f", new BigDecimal(withdraw_amt));
                        remain_ng_cash_str = String.format("%.2f", new BigDecimal(res));

                    } else if (amountlist.get(position).equalsIgnoreCase("75 %")) {
                        double comission_str = Double.parseDouble(total_week_commision_str);

                        double res = (comission_str / 100) * 75;
                        ngcash_remain.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(res)));
                        Log.e("Data 75 >> ", " >> " + res);
                        double withdraw_amt = comission_str - res;
                        withdraw_bank_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(withdraw_amt)));
                        withdaraw_accoutn_str = String.format("%.2f", new BigDecimal(withdraw_amt));
                        remain_ng_cash_str = String.format("%.2f", new BigDecimal(res));
                    } else if (amountlist.get(position).equalsIgnoreCase("100 %")) {
                        double comission_str = Double.parseDouble(total_week_commision_str);

                        double res = (comission_str / 100) * 100;
                        Log.e("Data 100 >> ", " >> " + res);
                        ngcash_remain.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(res)));
                        double withdraw_amt = comission_str - res;
                        withdraw_bank_amt.setText(mySession.getValueOf(MySession.CurrencySign) + " " + String.format("%.2f", new BigDecimal(withdraw_amt)));
                        withdaraw_accoutn_str = String.format("%.2f", new BigDecimal(withdraw_amt));
                        remain_ng_cash_str = String.format("%.2f", new BigDecimal(res));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CommisionDetAdapter extends BaseAdapter {
        Context context;
        List<CommissionData> commissionDataArrayList;
        private LayoutInflater inflater = null;

        public CommisionDetAdapter(Activity chatActivity, List<CommissionData> commissionDataArrayList) {
            // TODO Auto-generated constructor stub
            context = chatActivity;
            this.commissionDataArrayList = commissionDataArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            //  return  2;
            return commissionDataArrayList == null ? 0 : commissionDataArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            rowView = inflater.inflate(R.layout.custom_commision_detail_item, null);
            CircleImageView marchant_img = rowView.findViewById(R.id.marchant_img);
            TextView mer_name_tv = rowView.findViewById(R.id.mer_name_tv);
            TextView merchant_no_tv = rowView.findViewById(R.id.merchant_no_tv);
            TextView phone_number_tv = rowView.findViewById(R.id.phone_number_tv);
            TextView address_tv = rowView.findViewById(R.id.address_tv);
            TextView commission_amount = rowView.findViewById(R.id.commission_amount);
            String merchant_img_str = commissionDataArrayList.get(position).getMerchantDetail().getMerchantImage();
            if (merchant_img_str == null || merchant_img_str.equalsIgnoreCase("") || merchant_img_str.equalsIgnoreCase(BaseUrl.image_baseurl)) {

            } else {
                Glide.with(CommissionDetail.this).load(BaseUrl.image_baseurl + merchant_img_str).placeholder(R.drawable.user_propf).into(marchant_img);

            }
            mer_name_tv.setText("" + commissionDataArrayList.get(position).getMerchantDetail().getBusinessName());
            merchant_no_tv.setText("" + commissionDataArrayList.get(position).getMerchantDetail().getBusinessNo());
            phone_number_tv.setText("" + commissionDataArrayList.get(position).getMerchantDetail().getContactNumber());
            address_tv.setText("" + commissionDataArrayList.get(position).getMerchantDetail().getAddress());
            commission_amount.setText("Commission :  " + mySession.getValueOf(MySession.CurrencySign) + commissionDataArrayList.get(position).getMerchantComision());
            return rowView;
        }

    }


    public class CommisionAdpter extends RecyclerView.Adapter<CommisionAdpter.MyViewHolder> {
        Context context;

        public CommisionAdpter(Activity myContacts) {
            this.context = myContacts;
        }

        @Override
        public CommisionAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_commision_detail_item, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);

            return holder;
            // return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CommisionAdpter.MyViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

        @Override
        public int getItemCount() {
            return 2;
            //return chatBeanArrayList == null ? 0 : chatBeanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView payment_sts;

            public MyViewHolder(View view) {
                super(view);
                payment_sts = itemView.findViewById(R.id.payment_sts);


            }
        }
    }

    public class AmountSeleAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ArrayList<String> amountlist;

        public AmountSeleAdapter(Context applicationContext, ArrayList<String> amountlist) {
            this.context = applicationContext;
            this.amountlist = amountlist;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {

            return amountlist == null ? 0 : amountlist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.country_item_lay_flag, null);

            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            country_flag.setVisibility(View.GONE);
            names.setText(amountlist.get(i));


            return view;
        }
    }

}
