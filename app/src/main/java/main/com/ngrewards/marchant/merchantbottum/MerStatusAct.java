package main.com.ngrewards.marchant.merchantbottum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.SalesAudienceBean;
import main.com.ngrewards.beanclasses.SalesBean;
import main.com.ngrewards.beanclasses.SalesBeanList;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.marchant.draweractivity.MerchantBaseActivity;
import main.com.ngrewards.marchant.fragments.FragMerSales;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerStatusAct extends MerchantBaseActivity {
    public static ArrayList<SalesAudienceBean> salesAudienceBeanArrayList;
    public static ArrayList<SalesBeanList> salesBeanListArrayList;
    FrameLayout contentFrameLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView userimg;
    private TextView merchant_name;
    private MySession mySession;
    private String user_id = "", merchant_number = "", murchant_name = "", merchant_img_url = "";
    private ProgressBar progresbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_mer_status, contentFrameLayout);
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
                    String business_name = jsonObject1.getString("business_name");
                    merchant_number = jsonObject1.getString("business_no");
                    String contact_name = jsonObject1.getString("contact_name");
                    if (business_name == null || business_name.equalsIgnoreCase("")) {
                        murchant_name = contact_name;
                    } else {
                        murchant_name = business_name;
                    }
                    merchant_img_url = jsonObject1.getString("merchant_image");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        idinits();
        clickevent();
        setupviewpager();
        getMySales();
        // getMySalesAudience();

    }

    private void clickevent() {

    }

    private void idinits() {
        merchant_name = findViewById(R.id.merchant_name);
        progresbar = findViewById(R.id.progresbar);

        userimg = findViewById(R.id.userimg);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);


        merchant_name.setText("" + murchant_name);
        if (merchant_img_url != null && !merchant_img_url.equalsIgnoreCase("") && !merchant_img_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
            Glide.with(MerStatusAct.this).load(merchant_img_url).placeholder(R.drawable.user_propf).into(userimg);
        }
    }

    private void setupviewpager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragMerSales(), getResources().getString(R.string.sales));
        //  adapter.addFragment(new FragMerAudience(), getResources().getString(R.string.audience));

        viewPager.setAdapter(adapter);
    }

    private void getMySales() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        salesBeanListArrayList = new ArrayList<>();
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().getSalesMerchantData(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progresbar.setVisibility(View.GONE);
                    try {

                        String responseData = response.body().string();
                        Log.e("SALE DATA", " >>" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            SalesBean successData = new Gson().fromJson(responseData, SalesBean.class);
                            salesBeanListArrayList.add(successData.getResult());
                        }
                        Intent j = new Intent("SalesEarningData");
                        sendBroadcast(j);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progresbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();


                Log.e("TAG", t.toString());
            }
        });


    }

    private void getMySalesAudience() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        salesAudienceBeanArrayList = new ArrayList<>();
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().getMerchantSalesAudience(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progresbar.setVisibility(View.GONE);
                    try {

                        String responseData = response.body().string();
                        Log.e("SALE OUDI RES", " >>" + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            SalesAudienceBean successData = new Gson().fromJson(responseData, SalesAudienceBean.class);
                            salesAudienceBeanArrayList.add(successData);
                        }
                        Intent j = new Intent("SalesData");
                        sendBroadcast(j);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progresbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();


                Log.e("TAG", t.toString());
            }
        });


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
