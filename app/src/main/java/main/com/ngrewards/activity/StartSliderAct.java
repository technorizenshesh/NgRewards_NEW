package main.com.ngrewards.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.ngrewards.start_fragment.DiscoverFrag;
import main.com.ngrewards.start_fragment.EarnFriendCash;
import main.com.ngrewards.start_fragment.NgHelpFrag;
import main.com.ngrewards.start_fragment.SliderLastFrag;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import main.com.ngrewards.R;
import main.com.ngrewards.constant.CountryBean;
import main.com.ngrewards.constant.CustomViewPager;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.start_fragment.BecontrolFrag;
import main.com.ngrewards.start_fragment.Easyfrag;

public class StartSliderAct extends AppCompatActivity {
    private Button continue_button;
    private CustomViewPager viewPager;
    private CirclePageIndicator indicator;
    private RelativeLayout backlay;
    ViewPagerAdapter adapter;
    private boolean click_sts = false;
    public static String country_str="",country_id="";
    public static ArrayList<CountryBean> countryBeanArrayList;
    MySession mySession;
    Myapisession myapisession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_slider);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        countryBeanArrayList = new ArrayList<>();
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicatortwo);
        continue_button = (Button) findViewById(R.id.continue_button);
        idint();
        clicklistener();
/*
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (country_id==null||country_id.equalsIgnoreCase("0")||country_id.equalsIgnoreCase("")){
                    Toast.makeText(StartSliderAct.this,"Select Your Country",Toast.LENGTH_LONG).show();
                    viewPager.setPagingEnabled(false);
                }
                else {
                    viewPager.setPagingEnabled(true);

                }
                return false;
            }
        });
*/

        getCategoryType();
        getBusinesscategory();
        getOfferCategory();
    }

    private void idint() {

    }

    private void clicklistener() {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                indicator.setViewPager(viewPager);

                final float density = getResources().getDisplayMetrics().density;
                indicator.setRadius(5 * density);
                if (i == 4) {
                    click_sts = true;
                    continue_button.setText(getResources().getString(R.string.finish));

                }


            }

            @Override
            public void onPageSelected(int i) {



            }

            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NgHelpFrag(), "ONE");
        adapter.addFragment(new DiscoverFrag(), "TWO");
        adapter.addFragment(new Easyfrag(), "ONE");
        adapter.addFragment(new BecontrolFrag(), "TWO");
        adapter.addFragment(new EarnFriendCash(), "TWO");
        adapter.addFragment(new SliderLastFrag(), "TWO");
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

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getCategoryType() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
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


                Log.e("TAG", t.toString());
            }
        });


    }
    private void getOfferCategory() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
      Call<ResponseBody> call = ApiClient.getApiInterface().getOfferCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyOffercate(responseData);
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


                Log.e("TAG", t.toString());
            }
        });


    }

    private void getBusinesscategory() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0

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


                Log.e("TAG", t.toString());
            }
        });
    }

}