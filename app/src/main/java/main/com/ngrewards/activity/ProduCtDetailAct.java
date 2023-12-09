package main.com.ngrewards.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.beanclasses.DetailList;
import main.com.ngrewards.beanclasses.ProductDetail;
import main.com.ngrewards.productfragment.ProAboutFrag;
import main.com.ngrewards.productfragment.ProOffersFrag;
import main.com.ngrewards.productfragment.ProPhotoFrag;
import main.com.ngrewards.productfragment.ProReviewsFrag;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProduCtDetailAct extends AppCompatActivity {

    public static ArrayList<DetailList> productDetailArrayList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ScrollView scrollView;
    private RelativeLayout backlay;
    private String product_id = "", user_id = "", product_name_str = "";
    private TextView product_name_head, product_name, product_number;
    private ProgressBar progresbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produ_ct_detail);

        idinita();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
            product_id = bundle.getString("product_id");
            product_name_str = bundle.getString("product_name_str");
            product_name.setText("" + product_name_str);
            product_name_head.setText("" + product_name_str);
            getFeaturedProductsDetail(product_id);
        }

        clickevent();

    }

    private void getFeaturedProductsDetail(String product_id) {

        progresbar.setVisibility(View.VISIBLE);
        productDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getProductDetail(product_id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Featured Products >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            ProductDetail successData = new Gson().fromJson(responseData, ProductDetail.class);
                            productDetailArrayList.addAll(successData.getResult());

                        }

                        setupviewpager();

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

    private void setupviewpager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void idinita() {
        progresbar = findViewById(R.id.progresbar);
        product_name = findViewById(R.id.product_name);
        product_number = findViewById(R.id.product_number);
        product_name_head = findViewById(R.id.product_name_head);
        backlay = findViewById(R.id.backlay);
        scrollView = findViewById(R.id.scrollView);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProAboutFrag(), getResources().getString(R.string.about));
        adapter.addFragment(new ProPhotoFrag(), getResources().getString(R.string.photo));
        adapter.addFragment(new ProOffersFrag(), getResources().getString(R.string.offers));
        adapter.addFragment(new ProReviewsFrag(), getResources().getString(R.string.review));
        viewPager.setAdapter(adapter);
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