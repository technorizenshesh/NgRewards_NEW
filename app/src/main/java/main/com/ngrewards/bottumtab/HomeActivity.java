package main.com.ngrewards.bottumtab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.fragments.ItemsFrag;
import main.com.ngrewards.fragments.NearbyFrag;
import main.com.ngrewards.fragments.OffersFrag;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Fragment {

    FrameLayout contentFrameLayout;
    Myapisession myapisession;
    int selectedPosition = -1;
    View root;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String facebook_name;
    private String facebook_image;
    private String notification_data;
    private String result, itemResult = "", merchantItem = "", offerItem = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_home, container, false);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void setupviewpager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void clickevent() {
    }

    private void idinita() {
        tabLayout = root.findViewById(R.id.tabs);
        viewPager = root.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        // adapter.addFragment(new FeaturedFrag(), getResources().getString(R.string.featured));
        adapter.addFragment(new NearbyFrag(merchantItem), getResources().getString(R.string.nearby));
        adapter.addFragment(new OffersFrag(offerItem), getResources().getString(R.string.offers));
        adapter.addFragment(new ItemsFrag(itemResult), getResources().getString(R.string.items));
        viewPager.setAdapter(adapter);
        itemResult = "";
        merchantItem = "";
        offerItem = "";
        if (selectedPosition != -1) {
            viewPager.setCurrentItem(selectedPosition);
        }
    }

    @Override
    public void onResume() {
        Tools.reupdateResources(requireActivity());
        super.onResume();
        myapisession = new Myapisession(requireActivity());
        result = PreferenceConnector.readString(requireActivity(),
                PreferenceConnector.reult_intent_mem, "");
        PreferenceConnector.writeString(requireActivity(),
                PreferenceConnector.reult_intent_mem, "");
        if (result.contains("item")) {
            selectedPosition = 2;
            itemResult = result.replace("item", "");
        } else if (result.contains("merchant")) {
            selectedPosition = -1;
            merchantItem = result.replace("merchant", "");
        } else if (result.contains("offer")) {
            selectedPosition = 1;
            offerItem = result.replace("offer", "");
        } else {
            itemResult = "";
            merchantItem = "";
            offerItem = "";
        }
        result = "";
        idinita();
        clickevent();
        setupviewpager();
        if (myapisession.getKeyMerchantcate() == null ||
                myapisession.getKeyMerchantcate().equalsIgnoreCase("")) {
            // getCategoryType();
            getBusinesscategory();
        }
        if (myapisession.getProductdata() == null ||
                myapisession.getProductdata().equalsIgnoreCase("")) {
            // getCategoryType();
            getCategoryType();
        }

        Bundle bundle = getArguments();
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
                    Log.e("TAG", "Notification: " + e.getLocalizedMessage());
                    Log.e("TAG", "Notification: " + e.getMessage());
                    Log.e("TAG", "Notification: " + e.getCause());

                }
            }
        }

    }

    protected void attachBaseContext(Context base) {
        super.onAttach(LocaleHelper.onAttach(base));
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

                    } catch (Exception e) {
                        Log.e("TAG", "loginCall: " + e.getLocalizedMessage());
                        Log.e("TAG", "loginCall: " + e.getMessage());
                        Log.e("TAG", "loginCall: " + e.getCause());

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

                    } catch (Exception e) {
                        Log.e("TAG", "getBusinesscategory: " + e.getLocalizedMessage());
                        Log.e("TAG", "getBusinesscategory: " + e.getMessage());
                        Log.e("TAG", "getBusinesscategory: " + e.getCause());

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

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
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
