package main.com.ngrewards.bottumtab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.draweractivity.BaseActivity;
import main.com.ngrewards.fragments.ItemsFrag;
import main.com.ngrewards.fragments.NearbyFrag;
import main.com.ngrewards.fragments.OffersFrag;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    FrameLayout contentFrameLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Myapisession myapisession;
    private String facebook_name;
    private String facebook_image;
    private String notification_data;

    private String result,itemResult="",merchantItem="",offerItem="";

    int selectedPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);
        myapisession = new Myapisession(this);
        result = getIntent().getExtras().getString("result");
        if (result.contains("item"))
        {
            selectedPosition = 2;
            itemResult = result.replace("item","");
        } else if (result.contains("merchant"))
        {
            selectedPosition = -1;
            merchantItem = result.replace("merchant","");
        }else if (result.contains("offer"))
        {
            selectedPosition = 1;
            offerItem = result.replace("offer","");
        }
        idinita();
        clickevent();
        setupviewpager();
        if (myapisession.getKeyMerchantcate() == null || myapisession.getKeyMerchantcate().equalsIgnoreCase("")) {
            // getCategoryType();
            getBusinesscategory();
        }
        if (myapisession.getProductdata() == null ||
                myapisession.getProductdata().equalsIgnoreCase("")) {
            // getCategoryType();
            getCategoryType();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.e("Get Notification >>", "NULL");
        } else {

            String message = bundle.getString("message");
            facebook_name = bundle.getString("facebook_name");
            facebook_image = bundle.getString("facebook_image");

            Log.e("Get Notification >>", "" + message);
            if (message == null || message.equalsIgnoreCase("") || message.equalsIgnoreCase("null")) {
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    String keys = jsonObject.getString("key").trim();
                    notification_data = message;
                } catch (JSONException e) {
                    Log.e("TAG", "Notification: "+e.getLocalizedMessage());
                    Log.e("TAG", "Notification: "+e.getMessage());
                    Log.e("TAG", "Notification: "+e.getCause());

                }
            }
        }
    }

    private void setupviewpager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void clickevent() {
    }

    private void idinita() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // adapter.addFragment(new FeaturedFrag(), getResources().getString(R.string.featured));
        adapter.addFragment(new NearbyFrag(merchantItem), getResources().getString(R.string.nearby));
        adapter.addFragment(new OffersFrag(offerItem), getResources().getString(R.string.offers));
        adapter.addFragment(new ItemsFrag(itemResult), getResources().getString(R.string.items));
        viewPager.setAdapter(adapter);
        if(selectedPosition!=-1)
        {
            viewPager.setCurrentItem(selectedPosition);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getCategoryType() {

        Log.e("loginCall >", " > FIRST");
        Call<ResponseBody> call = ApiClient.getApiInterface().getCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("loginCall >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setProductdata(responseData);
                        }

                    } catch (IOException | JSONException e) {
                        Log.e("TAG", "loginCall: "+e.getLocalizedMessage());
                        Log.e("TAG", "loginCall: "+e.getMessage());
                        Log.e("TAG", "loginCall: "+e.getCause());

                    }
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

    private void getBusinesscategory() {

        Call<ResponseBody> call = ApiClient.getApiInterface().getBusnessCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("loginCall >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setMerchantcat(responseData);
                        }

                    } catch (IOException | JSONException e) {
                        Log.e("TAG", "getBusinesscategory: "+e.getLocalizedMessage());
                        Log.e("TAG", "getBusinesscategory: "+e.getMessage());
                        Log.e("TAG", "getBusinesscategory: "+e.getCause());

                    }
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

}
