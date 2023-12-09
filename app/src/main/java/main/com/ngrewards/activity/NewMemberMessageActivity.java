package main.com.ngrewards.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.MarchantBean;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.beanclasses.MerchantListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMemberMessageActivity extends AppCompatActivity {
    private final String time_zone = "";
    private final String messagetext = "";
    private final String date_time = "";
    private final String ImagePath = "";
    int count = 0;
    ArrayList<MerchantListBean> merchantListBeanArrayList;
    private RelativeLayout backlay;
    private AutoCompleteTextView merchant_number;
    private MySession mySession;
    private String user_id = "", image_url = "";
    private String receiver_name = "";
    private String receiver_id = "";
    private String receiver_fullname = "";
    private String receiver_img = "";
    private String receiver_type_str = "Merchant";
    private String type = "";
    private Myapisession myapisession;
    private ProgressBar progresbar;
    private ArrayList<MemberDetail> memberDetailArrayList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_new_message);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        String user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    image_url = jsonObject1.getString("member_image");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idint();
        clickevent();
        getBusnessNumber();
        getUsername();
       /* if (myapisession.getKeyBusinessnumber()==null||myapisession.getKeyBusinessnumber().equalsIgnoreCase("")){
            getBusnessNumber();
        }
        else {
            try {
                merchantListBeanArrayList = new ArrayList<>();
                JSONObject object = new JSONObject(myapisession.getKeyBusinessnumber());
                Log.e("Product Category >", " >" + myapisession.getKeyBusinessnumber());
                if (object.getString("status").equals("1")) {
                    MarchantBean successData = new Gson().fromJson(myapisession.getKeyBusinessnumber(), MarchantBean.class);
                    merchantListBeanArrayList.addAll(successData.getResult());
                }
                else {
                    getBusnessNumber();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        if (myapisession.getKeyMemberusername()==null||myapisession.getKeyMemberusername().equalsIgnoreCase("")){
            getUsername();
        }
        else {
            try {
                memberDetailArrayList = new ArrayList<>();
                JSONObject object = new JSONObject(myapisession.getKeyMemberusername());
                Log.e("Product Category >", " >" + myapisession.getKeyMemberusername());
                if (object.getString("status").equals("1")) {
                    MemberBean successData = new Gson().fromJson(myapisession.getKeyMemberusername(), MemberBean.class);
                    memberDetailArrayList.addAll(successData.getResult());
                }
                else {
                    getUsername();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }*/

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idint() {
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        merchant_number = findViewById(R.id.merchant_number);

        merchant_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s == null || s.equals("")) {

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {
                    ArrayList<MerchantListBean> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MerchantListBean memberlist = new MerchantListBean();
                        memberlist.setBusinessNo(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(
                                NewMemberMessageActivity.this, l1, "", "");
                        merchant_number.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }

                }
                count++;


            }
        });


    }

    private void getBusnessNumber() {
        progresbar.setVisibility(View.VISIBLE);
        merchantListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantBusNum(mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyBusinessnumber(responseData);
                            MarchantBean successData = new Gson().fromJson(responseData, MarchantBean.class);
                            merchantListBeanArrayList.addAll(successData.getResult());

                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }

    private void getUsername() {

        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername(user_id, mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("User name list>", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyMemberusername(responseData);
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);
                            memberDetailArrayList.addAll(successData.getResult());
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progresbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private ArrayList<MerchantListBean> l2 = new ArrayList<>();

        public GeoAutoCompleteAdapter(Activity context, ArrayList<MerchantListBean> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return l2.size();
        }

        @Override
        public Object getItem(int i) {
            return l2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
            TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
            try {
                geo_search_result_text.setText(l2.get(i).getBusinessNo());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        // checkic.setImageResource(R.drawable.check);
                        if (l2 != null && (l2.size() >= 1)) {
                            if (l2.get(i).getBusinessNo() != null) {
                                merchant_number.setText(l2.get(i).getBusinessNo());
                            }
                            try {
                                if (l2 != null && l2.get(i).getId() != null && !(l2.size() == 0)) {
                                    try {

                                        receiver_id = l2.get(i).getId();
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (l2 != null && l2.get(i).getMerchantImage() != null) {
                                receiver_img = l2.get(i).getMerchantImage();
                            }
                            if (l2 != null && !l2.isEmpty() && l2.get(i).getBusinessNo() != null) {

                                if (l2.size() != 0) {
                                    receiver_name = l2.get(i).getBusinessNo();
                                } else {
                                    Toast.makeText(getApplicationContext(), "sucess!!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                          /*  if (l2!=null&&l2.get(i).getBusinessName()!=null){
                                receiver_name = l2.get(i).getBusinessName();
                            }*/
                            if (l2 != null && l2.get(i).getContactName() != null) {
                                try {
                                    receiver_fullname = l2.get(i).getContactName();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            type = "Member";
                            if (l2 == null || l2.get(i).getReceiver_type() == null || l2.get(i).getReceiver_type().equalsIgnoreCase("")) {
                                receiver_type_str = "Merchant";
                                receiver_fullname = l2.get(i).getBusinessName();
                            } else {
                                receiver_type_str = l2.get(i).getReceiver_type();
                            }
                            Log.e("TAG", "onClick: " + "NewMemberMessageActivityNewMemberMessageActivity");
                            Log.e("receiver_id-=-=-=-=", receiver_id);
                            Log.e("type-=-=-=-=", "Member");
                            Log.e("receiver_type-=-=-=-=", receiver_type_str);
                            Log.e("receiver_e-=-=-=-=", receiver_fullname);
                            Log.e("receiver_img-=-=-=-=", BaseUrl.image_baseurl + receiver_img);
                            Log.e("receiver_name-=-=-=-=", receiver_name);
/* receiver_id = bun
 type = bundle.get
 type = type.trim(
// receiver_img =
 receiver_type = b
 receiver_name = b
 receiver_fullname*/
                            if (receiver_id.equalsIgnoreCase("")) {
                            } else {
                                Intent i = new Intent(NewMemberMessageActivity.this, MemberChatAct.class);
                                i.putExtra("receiver_id", receiver_id);
                                i.putExtra("type", "Member");
                                i.putExtra("receiver_type", receiver_type_str);
                                i.putExtra("receiver_fullname", receiver_fullname);
                                i.putExtra("receiver_img", BaseUrl.image_baseurl + receiver_img);
                                i.putExtra("receiver_name", receiver_name);
                                startActivity(i);
                                merchant_number.dismissDropDown();
                            }
                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        if (constraint.length() == 0) {
                        } else {
                            l2.clear();
                            for (MerchantListBean wp : merchantListBeanArrayList) {
                                if (wp.getBusinessNo().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                {

                                    try {
                                        l2.add(wp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {

                                }
                            }

                            for (MemberDetail mem : memberDetailArrayList) {
                                if (mem.getUsername().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                {
                                    Log.e("NewMembers", "performFiltering: ");
                                    MerchantListBean memberDetail = new MerchantListBean();
                                    memberDetail.setId(mem.getId());
                                    memberDetail.setBusinessName(mem.getUsername());
                                    memberDetail.setMerchantImage(mem.getMemberImage());
                                    memberDetail.setBusinessNo(mem.getUsername());
                                    memberDetail.setContactName(mem.getFullname());
                                    memberDetail.setReceiver_type("Member");
                                    l2.add(memberDetail);
                                }
                            }

                        }
                        // Assign the data to the FilterResults
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l2 = (ArrayList<MerchantListBean>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }


    }

}
