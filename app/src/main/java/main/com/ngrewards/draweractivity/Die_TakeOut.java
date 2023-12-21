package main.com.ngrewards.draweractivity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.com.ngrewards.Adapter.AdapterDive;
import main.com.ngrewards.Models.DiveInModal;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.MerchantMenuSetting;
import main.com.ngrewards.activity.PreferenceConnector;
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
import www.develpoeramit.mapicall.ApiCallBuilder;

public class Die_TakeOut extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_QR_SCAN = 3;
    public ArrayList<MemberDetail> memberDetailArrayList  ;
    ArrayList<MerchantListBean> merchantListBeanArrayList;
    int count = 0;
    private RecyclerView re;
    private DiveInModal modelItem;
    private ArrayList<DiveInModal> all_category_menu;
    private AdapterDive adapterMenu;
    private LinearLayout dilevry_fee, linear_tax, update_linear;
    private LayoutInflater li;
    private View promptsView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private String editTextString;
    private ProgressBar progressBar;
    private JSONArray result;
    private String delivery;
    private String total_price;
    private Myapisession myapisession;
    private String amount_due;
    private String tax_amount;
    private TextView dilevery_fee;
    private TextView taxamount;
    private String delevery_string;
    private String TaxAmount_String;
    private ProgressBar progresbar;
    private MySession mySession;
    private String user_id;
    private String strPerorFix;
    private String Afflited_name_String;
    private String merchant_id;
    private ImageView qrcode;
    private String employee_name;
    private String employee_id;
    private String affiliate_name;
    private String affiliate_name123;
    private String get_how_to_inveted;
    private String employe_sale_name;
    private String employe_sale_id;
    private String employee_name1;

    private AutoCompleteTextView merchant_num_auto;
    private AutoCompleteTextView employyee_id;
    private String employyee_id_string;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_die__take_out);
        progresbar = findViewById(R.id.progresbar);
        merchant_num_auto = (AutoCompleteTextView) findViewById(R.id.user_name);
        employyee_id = (AutoCompleteTextView) findViewById(R.id.employyee_id);
        myapisession = new Myapisession(this);
        merchant_num_auto.setText("");

        merchant_num_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {

                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    Log.e("kala", String.valueOf(l1));
                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setAffiliateName(s.toString());
                        l1.add(memberlist);
                        Die_TakeOut.GeoAutoCompleteAdapter ga = new Die_TakeOut.GeoAutoCompleteAdapter(Die_TakeOut.this, l1, "", "");
                        merchant_num_auto.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }
                }
                count++;
            }
        });

        qrcode = findViewById(R.id.qrcode);

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Die_TakeOut.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });


        mySession = new MySession(this);

        String user_log_data = mySession.getKeyAlldata();

        Log.e("user_log_data", user_log_data);

        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    affiliate_name = jsonObject1.getString("affiliate_name");

                    GetProfileAPi(user_id);

                    merchant_num_auto.setText(affiliate_name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SetupUi();

        getMerchantMenuSettingList();

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if (extras == null) {

            } else {

                affiliate_name = extras.getString("Afflited_name_String");
                affiliate_name123 = extras.getString("affiliate_name");

                employe_sale_name = extras.getString("employe_sale_name");
                employe_sale_id = extras.getString("employe_sale_id");

            }

        } else {

        }

        getUsername();
        // getemployeename();
    }

    private void GetProfileAPi(String user_id) {

        Log.e("user_idsdsd", user_id);

        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/merchant_profile.php?merchant_id=" + user_id)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            boolean status = response.getString("status").contains("1");

                            JSONObject result = response.getJSONObject("result");


                            Log.e("rsesuolfdfgb", String.valueOf(result));

                            affiliate_name = result.getString("Afflited_name_String");

                            employe_sale_name = result.getString("employe_sale_name");
                            employe_sale_id = result.getString("employe_sale_id");

                            Log.e("employe_sale_name", employe_sale_name);
                            Log.e("employe_sale_id", employe_sale_id);

                            if (employe_sale_name.equals("null")) {
                                merchant_num_auto.setText(employe_sale_name);

                            }

                            merchant_num_auto.setText(employee_name1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getemployeename() {

        Log.e("user_id", user_id);
        Toast.makeText(getApplicationContext(), employee_id, Toast.LENGTH_SHORT).show();
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/update_merchant.php?id=" + user_id)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), Afflited_name_String + response, Toast.LENGTH_SHORT).show();

                        try {

                            boolean status = response.getString("status").contains("1");

                            if (status) {

                                Afflited_name_String = merchant_num_auto.getText().toString().trim();

                                Intent intent = new Intent(Die_TakeOut.this, MerchantMenuSetting.class);
                                intent.putExtra("amount_due", amount_due);
                                intent.putExtra("tax_amount", tax_amount);
                                intent.putExtra("Afflited_name_String", Afflited_name_String);
                                intent.putExtra("employe_sale_name", employe_sale_name);
                                intent.putExtra("employe_sale_id", employe_sale_id);

                                startActivity(intent);

                                PreferenceConnector.writeString(Die_TakeOut.this, PreferenceConnector.AfflitedName, Afflited_name_String);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    private void getUsername() {

        Log.e("Get User Name>", " >GET NAME");
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

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 3) {
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");

                try {

                    String[] arr = result.split(",");
                    merchant_num_auto.setText(arr[2]);
                    employee_name1 = arr[2];
                    employee_id = arr[4];
                    employyee_id.setText(arr[4]);
                    merchant_num_auto.setVisibility(View.VISIBLE);

                    Log.e("arrayliiist", "" + result);

                } catch (Exception e) {

                    Toast.makeText(Die_TakeOut.this, "Wrong QR Code!!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private void SetupUi() {

        re = (RecyclerView) findViewById(R.id.re);
        dilevry_fee = (LinearLayout) findViewById(R.id.dilevry_fee);
        linear_tax = (LinearLayout) findViewById(R.id.linear_tax);
        update_linear = (LinearLayout) findViewById(R.id.update_linear);
        dilevery_fee = (TextView) findViewById(R.id.dilevery_fee);
        taxamount = (TextView) findViewById(R.id.taxamount);
        merchant_num_auto = (AutoCompleteTextView) findViewById(R.id.user_name);
        delevery_string = PreferenceConnector.readString(Die_TakeOut.this, PreferenceConnector.Delevry, "");
        TaxAmount_String = PreferenceConnector.readString(Die_TakeOut.this, PreferenceConnector.TaxAmout, "");

        Afflited_name_String = PreferenceConnector.readString(Die_TakeOut.this, PreferenceConnector.AfflitedName, "");
        merchant_num_auto.setText("" + Afflited_name_String);
        taxamount.setText(TaxAmount_String + " % ");
        dilevery_fee.setText(delevery_string + "%");
        progressBar = new ProgressBar(this);
        dilevry_fee.setOnClickListener(this);
        linear_tax.setOnClickListener(this);
        update_linear.setOnClickListener(this);

        merchant_num_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.equals("")) {

                    merchant_num_auto.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {

                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setAffiliateName(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(Die_TakeOut.this, l1, "", "");
                        merchant_num_auto.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }
                }

                count++;

            }
        });

    }

    private void ApiUpdateUser() {

        Afflited_name_String = merchant_num_auto.getText().toString().trim();
        employyee_id_string = employyee_id.getText().toString().trim();

        Toast.makeText(this, employyee_id_string, Toast.LENGTH_SHORT).show();

        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/update_merchant.php?id=" + user_id + "&employee_name=" + Afflited_name_String + "&employee_id=" + employyee_id_string)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            boolean status = response.getString("status").contains("1");

                            if (status) {

                                Afflited_name_String = merchant_num_auto.getText().toString().trim();

                                Intent intent = new Intent(Die_TakeOut.this, MerchantMenuSetting.class);
                                intent.putExtra("amount_due", amount_due);
                                intent.putExtra("tax_amount", tax_amount);
                                intent.putExtra("Afflited_name_String", Afflited_name_String);
                                intent.putExtra("employe_sale_name", employe_sale_name);
                                intent.putExtra("employe_sale_id", employe_sale_id);

                                startActivity(intent);


                                //JSONObject result = response.getJSONObject("result");
                                // Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_SHORT).show();

                                PreferenceConnector.writeString(Die_TakeOut.this, PreferenceConnector.AfflitedName, Afflited_name_String);

                                //merchant_num_auto.setText(Afflited_name_String);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getMerchantMenuSettingList() {

        HashMap<String, String> param = new HashMap<>();
        param.put("merchant_id", user_id);
        new ApiCallBuilder().build(this)
                .isShowProgressBar(true)
                .setParam(param)
                .setUrl(BaseUrl.getMerchantMenuSettingList())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {

                        all_category_menu = new ArrayList<DiveInModal>();

                        try {

                            JSONObject object = new JSONObject(response);
                            Log.e("response", response);

                            boolean status = object.getString("status").contains("1");
                            if (status) {
                                JSONArray array = object.getJSONArray("result").getJSONObject(0).getJSONArray("menu_setting_list");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String image = jsonObject.getString("image");

                                    modelItem = new DiveInModal();
                                    modelItem.setId(id);
                                    modelItem.setName(name);
                                    modelItem.setImage(image);
                                    all_category_menu.add(modelItem);
                                }

                                re.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                adapterMenu = new AdapterDive(getApplicationContext(), all_category_menu);
                                re.setAdapter(adapterMenu);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dilevry_fee:

                AlertDialoge_Dilevery();

                break;

            case R.id.linear_tax:

                AlertDialoge_Tax();

                break;

            case R.id.update_linear:

                ApiUpdateUser();

                //   finish();

                break;
        }
    }

    private void AddUpdateTax(String editTextString) {
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/add_tax.php?merchant_id=" + user_id + "&tax=" + editTextString)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            boolean status = response.getString("status").contains("1");
                            if (status) {

                                MenuItemApiCAll();

                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                alertDialog.hide();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void AlertDialoge_Tax() {

        li = LayoutInflater.from(Die_TakeOut.this);
        promptsView = li.inflate(R.layout.tax, null);
        EditText editTextDialogOtpInput = promptsView.findViewById(R.id.editTextDialogOtpInput);
        ImageView cancle_btn = promptsView.findViewById(R.id.cancle_btn);

        Button save_btn = promptsView.findViewById(R.id.save_btn);

        alertDialogBuilder = new AlertDialog.Builder(Die_TakeOut.this);
        alertDialogBuilder.setView(promptsView);

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextString = editTextDialogOtpInput.getText().toString().trim();
                AddUpdateTax(editTextString);

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void AddUpdateDelvery(String editTextString, String strPerorFix) {
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/add_delivery.php?merchant_id=" + user_id + "&delivery=" + editTextString + "&delivery_percent=" + strPerorFix)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            boolean status = response.getString("status").contains("1");
                            if (status) {

                                MenuItemApiCAll();
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                alertDialog.hide();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void MenuItemApiCAll() {
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/menu_list.php?merchant_id=" + user_id)
                .addPathParameter("pageNumber", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getString("status").contains("1");
                            result = response.getJSONArray("result");
                            delivery = response.getString("delivery");

                            total_price = response.getString("total_price");
                            amount_due = response.getString("amount_due");
                            tax_amount = response.getString("tax");

                            taxamount.setText(tax_amount + " % ");
                            dilevery_fee.setText(delivery + " % ");

                            PreferenceConnector.writeString(Die_TakeOut.this, PreferenceConnector.Delevry, delivery);
                            PreferenceConnector.writeString(Die_TakeOut.this, PreferenceConnector.TaxAmout, tax_amount);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void AlertDialoge_Dilevery() {

        li = LayoutInflater.from(Die_TakeOut.this);
        promptsView = li.inflate(R.layout.dilevrery, null);
        EditText editTextDialogOtpInput = promptsView.findViewById(R.id.editTextDialogOtpInput);
        ImageView cancle_btn = promptsView.findViewById(R.id.cancle_btn);
        TextView fix_perchant = promptsView.findViewById(R.id.fix_perchant);
        TextView fix_perchant1 = promptsView.findViewById(R.id.fix_perchant1);

        fix_perchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fix_perchant1.setVisibility(View.VISIBLE);
                fix_perchant.setVisibility(View.GONE);
            }
        });

        fix_perchant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fix_perchant.setVisibility(View.VISIBLE);
                fix_perchant1.setVisibility(View.GONE);
            }
        });

        String delivery_percent = fix_perchant.getText().toString().trim();
        String delivery_percent1 = fix_perchant1.getText().toString().trim();

        if (delivery_percent.equals("%") && delivery_percent1.equals("")) {

            strPerorFix = "%";

        } else {

            strPerorFix = "Fix Charge";
        }

        Button save_btn = promptsView.findViewById(R.id.save_btn);

        alertDialogBuilder = new AlertDialog.Builder(Die_TakeOut.this);
        alertDialogBuilder.setView(promptsView);

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextString = editTextDialogOtpInput.getText().toString().trim();
                AddUpdateDelvery(editTextString, strPerorFix);

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private ArrayList<MemberDetail> l2 = new ArrayList<>();

        public GeoAutoCompleteAdapter(Activity context, ArrayList<MemberDetail> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            layoutInflater = LayoutInflater.from(context);
            Log.e("FIRST", "CONS");
        }

        @Override
        public int getCount() {

            return l2 == null ? 0 : l2.size();
        }

        @Override
        public Object getItem(int i) {
            return l2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
            TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
            try {
                geo_search_result_text.setText(l2.get(i).getAffiliateName());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        // checkic.setImageResource(R.drawable.check);
                        if (l2 != null && !l2.isEmpty()) {
                            ArrayList<MemberDetail> test = new ArrayList<>();
                            test.add(l2.get(i));
                            if (test != null) {
                                merchant_num_auto.setText(test.get(0).getAffiliateName());

                                get_how_to_inveted = test.get(0).getHowInvitedYou();
                                // memname.setText(test.get(0).getFullname());
                                //  member_id = test.get(0).getId();
                                merchant_num_auto.dismissDropDown();
                            }
                           /*   usernameauto.setText(l2.get(i).getAffiliateName());
                            memname.setText(l2.get(i).getFullname());
                            member_id = l2.get(i).getId();
                            usernameauto.dismissDropDown();*/
                        }

                    }
                });

            } catch (Exception e) {

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
                            if (memberDetailArrayList != null && !memberDetailArrayList.isEmpty()) {
                                for (MemberDetail wp : memberDetailArrayList) {
                                    if (wp.getAffiliateName().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                    {
                                        Log.e("TRUE", " >> FILTER" + wp.getAffiliateName());
                                        l2.add(wp);
                                    }
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
                        l2 = (ArrayList<MemberDetail>) results.values;
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
