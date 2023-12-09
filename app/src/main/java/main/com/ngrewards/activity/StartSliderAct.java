package main.com.ngrewards.activity;

import static main.com.ngrewards.constant.MySession.KEY_LANGUAGE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.constant.CountryBean;
import main.com.ngrewards.constant.CustomViewPager;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import main.com.ngrewards.start_fragment.BecontrolFrag;
import main.com.ngrewards.start_fragment.DiscoverFrag;
import main.com.ngrewards.start_fragment.EarnFriendCash;
import main.com.ngrewards.start_fragment.Easyfrag;
import main.com.ngrewards.start_fragment.NgHelpFrag;
import main.com.ngrewards.start_fragment.SliderLastFrag;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartSliderAct extends AppCompatActivity {
    public static String country_str = "", country_id = "";
    public static ArrayList<CountryBean> countryBeanArrayList;
    public ArrayList<LanguageBean> language_list = new ArrayList<>();
    ViewPagerAdapter adapter;
    MySession mySession;
    Myapisession myapisession;
    CountryListAdapter languageListAdapter;
    private Button continue_button;
    private CustomViewPager viewPager;
    private CirclePageIndicator indicator;
    private RelativeLayout backlay;
    private boolean click_sts = false;
    private Spinner language_spn;

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
        language_spn = findViewById(R.id.language_spn);
        language_list.add(new LanguageBean("1", "en", "English", ""));
        language_list.add(new LanguageBean("2", "hi", "Hindi", ""));
        language_list.add(new LanguageBean("3", "es", "Spanish", ""));
        languageListAdapter = new
                CountryListAdapter(
                StartSliderAct.this,
                language_list);
        language_spn.setAdapter(languageListAdapter);
        Log.e("TAG", "idint: mySession.getValueOf(KEY_LANGUAGE)" + mySession.getValueOf(KEY_LANGUAGE));
        for (int i = 0; i < language_list.size(); i++) {
            if (mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase(language_list.get(i).sortname)) {
                language_spn.setSelection(i);
            }

        }
        language_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (language_list != null && !language_list.isEmpty()) {
                    if (!mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase(language_list.get(position).getSortname())) {
                        Tools.updateResources(StartSliderAct.this, language_list.get(position).getSortname());
                        mySession.setValueOf(KEY_LANGUAGE, language_list.get(position).getSortname());
                        startActivity(new Intent(getApplicationContext(), StartSliderAct.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public static class CountryListAdapter extends BaseAdapter {
        private final ArrayList<LanguageBean> values;
        Context context;
        LayoutInflater inflter;

        public CountryListAdapter(Context applicationContext, ArrayList<LanguageBean> values) {
            this.context = applicationContext;
            this.values = values;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {

            return values == null ? 0 : values.size();
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
        /*    //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            if (values.get(i).getFlag_url() == null || values.get(i).getFlag_url().equalsIgnoreCase("")) {

            } else {
                Glide.with(context)
                        .load(values.get(i).getFlag_url())
                        .thumbnail(0.5f)
                        .override(50, 50)
                        .centerCrop()
                         
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                         
                        .into(country_flag);
            }*/

            country_flag.setVisibility(View.GONE);
            names.setText(values.get(i).getName());


            return view;
        }
    }

    /**
     * Created by technorizen on 2/7/18.
     */

    public static class LanguageBean {
        String id;
        String sortname;
        String name;
        String flag_url;

        public LanguageBean(String id, String sortname, String name, String flag_url) {
            this.id = id;
            this.sortname = sortname;
            this.name = name;
            this.flag_url = flag_url;
        }

        public String getFlag_url() {
            return flag_url;
        }

        public void setFlag_url(String flag_url) {
            this.flag_url = flag_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

}