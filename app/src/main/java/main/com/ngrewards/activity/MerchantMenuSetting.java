package main.com.ngrewards.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.com.ngrewards.Adapter.AdapterMenu;
import main.com.ngrewards.Models.ModalMenuList;
import main.com.ngrewards.R;
import main.com.ngrewards.RecyclerViewClickListener1;
import main.com.ngrewards.SeverCommunication.VolleyMultipartRequest;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.draweractivity.Die_TakeOut;

public class MerchantMenuSetting extends AppCompatActivity implements IMethodCaller {

    private RecyclerView menu_item;
    private JSONArray result;
    private ArrayList<ModalMenuList> all_category_menu;
    private AdapterMenu adapterMenu;
    private ModalMenuList modelItem;
    private String delivery;
    private String total_price;
    private String amount_due;
    private String tax_amount;
    private TextView taxamount;
    private TextView dilevery_fee;
    MySession mySession;
    private String user_id;
    private LayoutInflater li;
    private View promptsView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private String editTextString;
    private TextView setting;
    private RelativeLayout back_btn;
    private String dilevery_fees_string;
    private String taxamount_string;
    private ArrayList<ModalMenuList> all_category_menu1;
    private String affiliate_name;
    private TextView user_name;
    private String Afflited_name_String;
    private RecyclerViewClickListener1 listener;
    private String employe_sale_name;
    private String employe_sale_id;
    private IMethodCaller IMethodCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuoption);

        SetupUi();

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if (extras == null) {

                amount_due = null;

            } else {

                dilevery_fees_string = extras.getString("amount_due");
                taxamount_string = extras.getString("tax_amount");
                Afflited_name_String = extras.getString("Afflited_name_String");

                employe_sale_name = extras.getString("employe_sale_name");
                employe_sale_id = extras.getString("employe_sale_id");
            }

        } else {

            taxamount_string = (String) savedInstanceState.getSerializable("tax_amount");
            dilevery_fees_string = (String) savedInstanceState.getSerializable("amount_due");
        }

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
                    InvestigatinList(user_id);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listener = new RecyclerViewClickListener1() {
            @Override
            public void onClick1(String id_item) {
                UpdateCategory(id_item);
            }
        };
    }


    private void InvestigatinList(String user_id) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                "https://myngrewards.com/wp-content/plugins/webservice/menu_list.php?",

                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        Log.e("response123", String.valueOf(response));

                        String resultResponse = new String(response.data);

                        JSONObject resObj = null;
                        try {
                            resObj = new JSONObject(resultResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("result123", "" + resObj);


                        String message = resObj.optString("message");
                        String status = resObj.optString("status");

                        all_category_menu = new ArrayList<ModalMenuList>();

                        if (status.equals("1")) {
                            try {

                                String delivery = resObj.getString("delivery");

                                String total_price = resObj.getString("total_price");
                                String amount_due = resObj.getString("amount_due");
                                String tax_amount = resObj.getString("tax");

                                PreferenceConnector.writeString(MerchantMenuSetting.this, PreferenceConnector.Delevry, delivery);
                                PreferenceConnector.writeString(MerchantMenuSetting.this, PreferenceConnector.TaxAmout, tax_amount);

                                taxamount.setText(tax_amount);
                                dilevery_fee.setText(delivery);

                                result = resObj.getJSONArray("result");

                                Log.e("result123454", "" + result);

                                for (int i = 0; i < result.length(); i++) {

                                    ModalMenuList modelItem = new ModalMenuList();
                                    JSONObject object = result.getJSONObject(i);
                                    String name = object.getString("name");
                                    String id = object.getString("id");

                                    JSONArray item_list1 = object.getJSONArray("item_list");
                                    modelItem.setName(name);
                                    modelItem.setId(id);

                                    for (int j = 0; j < item_list1.length(); j++) {

                                        JSONObject object1 = item_list1.getJSONObject(j);

                                        String item_id = object1.getString("id");
                                        String title = object1.getString("title");
                                        String discription = object1.getString("description");
                                        String price = object1.getString("price");
                                        String menu_image = object1.getString("menu_image");
                                        String status_item = object1.getString("status");
                                        String menu_id = object1.getString("menu_id");

                                        modelItem.setTitle(title);
                                        modelItem.setItem_id(item_id);
                                        modelItem.setTitleDiscription(discription);
                                        modelItem.setPrice(price);
                                        modelItem.setImage(menu_image);
                                        modelItem.setStatus(status_item);
                                        modelItem.setName(name);
                                        modelItem.setMenu_id(menu_id);

                                        // all_category_menu1.add(modelItem);
                                    }

                                    all_category_menu.add(modelItem);
                                    menu_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    adapterMenu = new AdapterMenu(getApplicationContext(), all_category_menu, listener ,IMethodCaller);
                                    menu_item.setAdapter(adapterMenu);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse networkResponse = error.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";
                            } else if (error.getClass().equals(NoConnectionError.class)) {
                                errorMessage = "Failed to connect server";

                            }
                        } else {

                            String result = new String(networkResponse.data);

                            try {

                                JSONObject response = new JSONObject(result);
                                //String error1 = response.getString("error");
                                String message = response.getString("message");


                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("merchant_id", user_id);
                Log.e("reg paramssssssss", "" + params);

                return params;

            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);

    }

    private void UpdateCategory(String id_item) {

        li = LayoutInflater.from(this);
        promptsView = li.inflate(R.layout.categorydialoge, null);
        EditText editTextDialogOtpInput = promptsView.findViewById(R.id.editTextDialogOtpInput);
        ImageView cancle_btn = promptsView.findViewById(R.id.cancle_btn);
        Button save_btn = promptsView.findViewById(R.id.save_btn);

        alertDialogBuilder = new AlertDialog.Builder(this);
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
                AddUpdateCategory(editTextString, id_item);

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void AddUpdateCategory(String editTextString, String id_item) {
        AndroidNetworking.get("https://myngrewards.com/wp-content/plugins/webservice/update_menu.php?id=" + id_item + "&name=" + editTextString)
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
                                Intent intent = new Intent(MerchantMenuSetting.this, MerchantMenuSetting.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                String message = response.getString("message");
                                Toast.makeText(MerchantMenuSetting.this, message, Toast.LENGTH_SHORT).show();
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

    private void SetupUi() {

        IMethodCaller = new IMethodCaller() {
            @Override
            public void yourDesiredMethod() {

                finish();

            }
        };

        menu_item = (RecyclerView) findViewById(R.id.menu_item_);
        taxamount = (TextView) findViewById(R.id.taxamount);
        dilevery_fee = (TextView) findViewById(R.id.dilevery_fee);
        setting = (TextView) findViewById(R.id.setting);
        back_btn = (RelativeLayout) findViewById(R.id.backlay);
        dilevery_fees_string = PreferenceConnector.readString(MerchantMenuSetting.this, PreferenceConnector.Delevry, "");
        taxamount_string = PreferenceConnector.readString(MerchantMenuSetting.this, PreferenceConnector.TaxAmout, "");
        dilevery_fee.setText(dilevery_fees_string);
        taxamount.setText(taxamount_string);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), employe_sale_id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MerchantMenuSetting.this, Die_TakeOut.class);
                intent.putExtra("employe_sale_name", employe_sale_name);
                intent.putExtra("employe_sale_id", employe_sale_id);
                startActivity(intent);
            }
        });
    }

    public void EditBtnOne(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(dialogLayout);
        builder.show();
    }

    @Override
    public void yourDesiredMethod() {
        finish();
    }
}
