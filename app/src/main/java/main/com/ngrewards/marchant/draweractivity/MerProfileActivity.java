package main.com.ngrewards.marchant.draweractivity;

import static main.com.ngrewards.Utils.Tools.ToolsShowDialog;
import static main.com.ngrewards.constant.MySession.KEY_LANGUAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.beanclasses.GalleryBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.CountryBean;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MultipartUtility;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.drawlocation.MyTask;
import main.com.ngrewards.drawlocation.WebOperations;
import main.com.ngrewards.marchant.activity.MerchantSignupSlider;
import main.com.ngrewards.marchant.merchantbottum.MultiPhotoSelectActivity;
import main.com.ngrewards.marchant.merchantbottum.MultiPhotoSelectActivity2;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerProfileActivity extends AppCompatActivity {
    //code for lat long
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static ArrayList<GalleryBean> ImagePathArrayList;
    public static ArrayList<String> ImagePathArrayList_str;
    private final Integer THRESHOLD = 2;
    MySession mySession;
    CountryListAdapter countryListAdapter;
    ProgressBar progresbar;
    LocationManager locationManager;
    Location location;
    GPSTracker gpsTracker;
    boolean sts;
    CategoryAdpters categoryAdpters;
    String business_number = "", youtubelink_str = "", day_name_str = "", open_close_str = "", added_open_close_str = "", opening_hour_str = "", closing_hour_str = "", aded_day_name_str = "", add_open_str = "", add_close_str = "";
    //  public static ArrayList<GalleryBean> galleryimagelist;
    File[] filearray;
    HorizontalAdapter horizontalAdapter;
    private String user_id = "";
    private RelativeLayout backlay;
    private CircleImageView user_img;
    private EditText youtubelink, businesskeywords, googlelink, instalink, linkedinlink, facebooklink, twittelink, businessname, bus_number, busines_email, zipcode, businessdescription, bus_website;
    private String businesskeywords_str = "", googlelink_str = "", reward_per_str = "", contact_name_str = "", businessname_str = "", bus_number_str = "", busines_email_str = "", streetaddress_str = "", zipcode_str = "", businessdescription_str = "";
    private String address_tv_str = "", ImagePath = "", city_str = "", state_str = "", country_str = "", instalink_str = "", linkedinlink_str = "", bus_website_str = "", facebooklink_str = "", twittelink_str = "";
    private ArrayList<CountryBean> countryBeanArrayList, statelistbean, citylistbean;
    private Spinner state_spn, country_spn, city_spn;
    private TextView update_tv;
    private double mer_latitude, mer_longitude;
    private double latitude = 0, longitude = 0;
    private AutoCompleteTextView streetaddress;
    private int count = 0;
    private ArrayList<CategoryBeanList> categoryBeanListArrayList;
    private Spinner category_spinner;
    private String category_id = "";
    private TextView address_tv, sundayopen, sundayclose, mondayopen, mondayclose, tuesdayopen, tuesdayclose, wednesdayopen, wednesdayclose, thursdayopen, thursdayclose, fridayopen, fridayclose, saturdayopen, saturdayclose;
    private TextView sunday_status, monday_status, tuesday_status, wednesday_status, thursday_status, friday_status, saturday_status;
    private TextView sunday_full_day, monday_full_day, tuesday_full_day, wednesday_full_day, thursday_full_day, friday_full_day, saturday_full_day;
    private String sunday_full_day_ststus = "DEFAULT", monday_full_day_ststus = "DEFAULT", tuesday_full_day_ststus = "DEFAULT", wednesday_full_day_ststus = "DEFAULT", thursday_full_day_ststus = "DEFAULT", friday_full_day_ststus = "DEFAULT", saturday_full_day_ststus = "DEFAULT";
    private String sunday_status_opcls = "OPEN", monday_status_opcls = "OPEN", tuesday_status_opcls = "OPEN", wednesday_status_opcls = "OPEN", thursday_status_opcls = "OPEN", friday_status_opcls = "OPEN", saturday_status_opcls = "OPEN";
    private ImageView uploadimg;
    private RecyclerView business_galleryimage;
    private int remove_pos;
    private Myapisession myapisession;
    private String full_day_opne_status = "DEFAULT,DEFAULT,DEFAULT,DEFAULT,DEFAULT,DEFAULT,DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mer_profile);
        mySession = new MySession(this);
        myapisession = new Myapisession(this);
        filearray = new File[0];
        ImagePathArrayList = new ArrayList<>();
        ImagePathArrayList_str = new ArrayList<>();

        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MerProfileActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MerProfileActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        checkGps();
        idinit();
        clickevent();
        autocompleteView();

        new GetProfile().execute();
    }

    private void autocompleteView() {
        Log.e("CALL THIS METH", "INNER");

        streetaddress.setThreshold(THRESHOLD);
        streetaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sts = true;

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    Log.e("CALL THIS LOAD", "INNER");
                    //clear_pick_ic.setVisibility(View.VISIBLE);
                    loadData(streetaddress.getText().toString());
                } else {
                    //clear_pick_ic.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadData(String s) {
        Log.e("CALL THIS", "OUT" + count);

        try {
            if (count == 0) {
                List<String> l1 = new ArrayList<>();
                if (s == null) {

                } else {
                    Log.e("CALL THIS", "INNER");
                    l1.add(s);
                    sts = true;
                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(MerProfileActivity.this, l1, "" + latitude, "" + longitude);
                    streetaddress.setAdapter(ga);
                    ga.notifyDataSetChanged();

                }

            }
            count++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoryType() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        Log.e("loginCall >", " > FIRST");
        progresbar.setVisibility(View.VISIBLE);
        categoryBeanListArrayList = new ArrayList<>();
        CategoryBeanList categoryBeanList = new CategoryBeanList();
        categoryBeanList.setCategoryId("0");
        categoryBeanList.setCategoryName(getString(R.string.selectcat));
        categoryBeanList.setCategory_name_spanish(getString(R.string.selectcat));
        categoryBeanList.setCategory_name_hindi(getString(R.string.selectcat));
        categoryBeanListArrayList.add(categoryBeanList);
        Call<ResponseBody> call = ApiClient.getApiInterface().getBusnessCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("loginCall >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setMerchantcat(responseData);

                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
                            categoryBeanListArrayList.addAll(successData.getResult());

                        }

                        categoryAdpters = new CategoryAdpters(MerProfileActivity.this, categoryBeanListArrayList);
                        category_spinner.setAdapter(categoryAdpters);
                        if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                            for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
                                if (category_id.equalsIgnoreCase(categoryBeanListArrayList.get(i).getCategoryId())) {
                                    category_spinner.setSelection(i);
                                }
                            }
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

    private void clickevent() {

        sunday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    sunday_full_day_ststus = "OPEN";
                    sunday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    sunday_full_day_ststus = "DEFAULT";
                    sunday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        monday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    monday_full_day_ststus = "OPEN";
                    monday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    monday_full_day_ststus = "DEFAULT";
                    monday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        tuesday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuesday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    tuesday_full_day_ststus = "OPEN";
                    tuesday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    tuesday_full_day_ststus = "DEFAULT";
                    tuesday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        wednesday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wednesday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    wednesday_full_day_ststus = "OPEN";
                    wednesday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    wednesday_full_day_ststus = "DEFAULT";
                    wednesday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        thursday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thursday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    thursday_full_day_ststus = "OPEN";
                    thursday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    thursday_full_day_ststus = "DEFAULT";
                    thursday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        friday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    friday_full_day_ststus = "OPEN";
                    friday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    friday_full_day_ststus = "DEFAULT";
                    friday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        saturday_full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saturday_full_day_ststus.equalsIgnoreCase("DEFAULT")) {
                    saturday_full_day_ststus = "OPEN";
                    saturday_full_day.setBackgroundResource(R.drawable.pink_border);
                } else {
                    saturday_full_day_ststus = "DEFAULT";
                    saturday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });

        sunday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    sunday_status_opcls = "OPEN";
                    sunday_status.setText("" + getResources().getString(R.string.open));
                    sunday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    sunday_status_opcls = "CLOSE";
                    sunday_status.setText("" + getResources().getString(R.string.closed));
                    sunday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        monday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    monday_status_opcls = "OPEN";
                    monday_status.setText("" + getResources().getString(R.string.open));
                    monday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    monday_status_opcls = "CLOSE";
                    monday_status.setText("" + getResources().getString(R.string.closed));
                    monday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        tuesday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuesday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    tuesday_status_opcls = "OPEN";
                    tuesday_status.setText("" + getResources().getString(R.string.open));
                    tuesday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    tuesday_status_opcls = "CLOSE";
                    tuesday_status.setText("" + getResources().getString(R.string.closed));
                    tuesday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        wednesday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wednesday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    wednesday_status_opcls = "OPEN";
                    wednesday_status.setText("" + getResources().getString(R.string.open));
                    wednesday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    wednesday_status_opcls = "CLOSE";
                    wednesday_status.setText("" + getResources().getString(R.string.closed));
                    wednesday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        thursday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thursday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    thursday_status_opcls = "OPEN";
                    thursday_status.setText("" + getResources().getString(R.string.open));
                    thursday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    thursday_status_opcls = "CLOSE";
                    thursday_status.setText("" + getResources().getString(R.string.closed));
                    thursday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        friday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    friday_status_opcls = "OPEN";
                    friday_status.setText("" + getResources().getString(R.string.open));
                    friday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    friday_status_opcls = "CLOSE";
                    friday_status.setText("" + getResources().getString(R.string.closed));
                    friday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });
        saturday_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saturday_status_opcls.equalsIgnoreCase("CLOSE")) {
                    saturday_status_opcls = "OPEN";
                    saturday_status.setText("" + getResources().getString(R.string.open));
                    saturday_status.setBackgroundResource(R.drawable.pink_border);
                } else {
                    saturday_status_opcls = "CLOSE";
                    saturday_status.setText("" + getResources().getString(R.string.closed));
                    saturday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                }
            }
        });


        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImagePathArrayList.size() == 7) {
                    Toast.makeText(MerProfileActivity.this, "Only 7 images Uploaded", Toast.LENGTH_LONG).show();
                } else if (ImagePathArrayList.size() < 7) {
                    selectImageMultiple();
                }
            }
        });
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cameraIntent.setType("image*//*");
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 1000);
                }*/
                selectImage();
            }
        });
        update_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_name_str = "";
                aded_day_name_str = "";
                opening_hour_str = "";
                closing_hour_str = "";
                add_close_str = "";
                add_open_str = "";
                open_close_str = "";
                full_day_opne_status = "";
                full_day_opne_status = sunday_full_day_ststus + "," + monday_full_day_ststus + "," + tuesday_full_day_ststus + "," + wednesday_full_day_ststus + "," + thursday_full_day_ststus + "," + friday_full_day_ststus + "," + saturday_full_day_ststus;
                String sunday_openstr = sundayopen.getText().toString();
                String sunday_closestr = sundayclose.getText().toString();

                String monday_openstr = mondayopen.getText().toString();
                String monday_closestr = mondayclose.getText().toString();

                String tuesday_openstr = tuesdayopen.getText().toString();
                String tuesday_closestr = tuesdayclose.getText().toString();

                String wednes_openstr = wednesdayopen.getText().toString();
                String wednes_closestr = wednesdayclose.getText().toString();

                String thurs_openstr = thursdayopen.getText().toString();
                String thurs_closestr = thursdayclose.getText().toString();

                String friday_openstr = fridayopen.getText().toString();
                String friday_closestr = fridayclose.getText().toString();

                String saturday_openstr = saturdayopen.getText().toString();
                String saturday_closestr = saturdayclose.getText().toString();

                if (sunday_openstr != null && !sunday_openstr.equalsIgnoreCase("") && sunday_closestr != null && !sunday_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = "Sunday";
                    add_close_str = sunday_closestr;
                    add_open_str = sunday_openstr;
                    added_open_close_str = sunday_status_opcls;
                }
                if (monday_openstr != null && !monday_openstr.equalsIgnoreCase("") && monday_closestr != null && !monday_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = aded_day_name_str + ",Monday";
                    add_close_str = add_close_str + "," + monday_closestr;
                    add_open_str = add_open_str + "," + monday_openstr;
                    added_open_close_str = added_open_close_str + "," + monday_status_opcls;
                }
                if (tuesday_openstr != null && !tuesday_openstr.equalsIgnoreCase("") && tuesday_closestr != null && !tuesday_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = aded_day_name_str + ",Tuesday";
                    add_close_str = add_close_str + "," + tuesday_closestr;
                    add_open_str = add_open_str + "," + tuesday_openstr;
                    added_open_close_str = added_open_close_str + "," + tuesday_status_opcls;

                }
                if (wednes_openstr != null && !wednes_openstr.equalsIgnoreCase("") && wednes_closestr != null && !wednes_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = aded_day_name_str + ",Wednesday";
                    add_close_str = add_close_str + "," + wednes_closestr;
                    add_open_str = add_open_str + "," + wednes_openstr;
                    added_open_close_str = added_open_close_str + "," + wednesday_status_opcls;

                }
                if (thurs_openstr != null && !thurs_openstr.equalsIgnoreCase("") && thurs_closestr != null && !thurs_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = aded_day_name_str + ",Thursday";
                    add_close_str = add_close_str + "," + thurs_closestr;
                    add_open_str = add_open_str + "," + thurs_openstr;
                    added_open_close_str = added_open_close_str + "," + thursday_status_opcls;

                }
                if (friday_openstr != null && !friday_openstr.equalsIgnoreCase("") && friday_closestr != null && !friday_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = aded_day_name_str + ",Friday";
                    add_close_str = add_close_str + "," + friday_closestr;
                    add_open_str = add_open_str + "," + friday_openstr;
                    added_open_close_str = added_open_close_str + "," + friday_status_opcls;

                }
                if (saturday_openstr != null && !saturday_openstr.equalsIgnoreCase("") && saturday_closestr != null && !saturday_closestr.equalsIgnoreCase("")) {
                    aded_day_name_str = aded_day_name_str + ",Saturday";
                    add_close_str = add_close_str + "," + saturday_closestr;
                    add_open_str = add_open_str + "," + saturday_openstr;
                    added_open_close_str = added_open_close_str + "," + saturday_status_opcls;

                }
                day_name_str = aded_day_name_str.trim();
                open_close_str = added_open_close_str.trim();
                closing_hour_str = add_close_str.trim();
                opening_hour_str = add_open_str.trim();

                Log.e("day_name_str >>", "> " + day_name_str);
                Log.e("closing_hour_str >>", "> " + closing_hour_str);
                Log.e("opening_hour_str >>", "> " + opening_hour_str);
                Log.e("open_close_str >>", "> " + open_close_str);
                bus_number_str = bus_number.getText().toString();
                address_tv_str = address_tv.getText().toString();
                businessname_str = businessname.getText().toString();
                Log.e("businessname_str >>", " ." + businessname_str);
                businessname_str = businessname_str.replaceAll("'", "'");

                busines_email_str = busines_email.getText().toString();
                streetaddress_str = streetaddress.getText().toString();
                businessdescription_str = businessdescription.getText().toString();
                zipcode_str = zipcode.getText().toString();
                bus_website_str = bus_website.getText().toString();
                instalink_str = instalink.getText().toString();
                linkedinlink_str = linkedinlink.getText().toString();
                facebooklink_str = facebooklink.getText().toString();
                twittelink_str = twittelink.getText().toString();
                googlelink_str = googlelink.getText().toString();
                youtubelink_str = youtubelink.getText().toString();
                businesskeywords_str = businesskeywords.getText().toString();

                if (bus_number_str == null || bus_number_str.equalsIgnoreCase("")) {
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                } else if (businessname_str == null || businessname_str.equalsIgnoreCase("")) {
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                } else if (busines_email_str == null || busines_email_str.equalsIgnoreCase("")) {
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                } else if (streetaddress_str == null || streetaddress_str.equalsIgnoreCase("")) {
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                }
               /* else if (country_str==null||country_str.equalsIgnoreCase("")){
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                }
                else if (state_str==null||state_str.equalsIgnoreCase("")){
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                }
                else if (city_str==null||city_str.equalsIgnoreCase("")){
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                }*/
                else if (zipcode_str == null || zipcode_str.equalsIgnoreCase("")) {
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.filldetail), Toast.LENGTH_LONG).show();

                } else {
                    Log.e("Image size", " > " + ImagePathArrayList_str.size());
                    filearray = new File[ImagePathArrayList_str.size()];
                    Log.e("filearray size", " > " + filearray.length);

                    for (int i = 0; i < ImagePathArrayList_str.size(); i++) {
                        Log.e("Image", " > " + ImagePathArrayList_str.get(i));
                        if (i < 8) {

                        }
                        File ImageFile = new File(ImagePathArrayList_str.get(i));
                        filearray[i] = ImageFile;
                    }
                    new UpdateProfile().execute();
                }

            }
        });
        sundayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Sun");
            }
        });
        sundayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Suncls");
            }
        });
        mondayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Mon");
            }
        });
        mondayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Moncls");
            }
        });
        tuesdayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Tus");
            }
        });
        tuesdayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Tuscls");
            }
        });
        wednesdayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Wed");
            }
        });
        wednesdayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Wedcls");
            }
        });
        thursdayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Thu");
            }
        });
        thursdayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Thucls");
            }
        });
        fridayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Fri");
            }
        });
        fridayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Fricls");
            }
        });
        saturdayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Sat");
            }
        });
        saturdayclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime("Satcls");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MultiPhotoSelectActivity.image == null) {

        } else if (MultiPhotoSelectActivity.image.isEmpty()) {

        } else {
            for (int i = 0; i < MultiPhotoSelectActivity.image.size(); i++) {
                if (ImagePathArrayList.size() < 7) {
                    Log.e("Select Photo ", " > " + MultiPhotoSelectActivity.image.get(i));
                    GalleryBean galleryBean = new GalleryBean();
                    galleryBean.setId("0");
                    galleryBean.setGallery_image(MultiPhotoSelectActivity.image.get(i));
                    ImagePathArrayList.add(galleryBean);
                    ImagePathArrayList_str.add(MultiPhotoSelectActivity.image.get(i));
                    Log.e("Select Photo add", " > " + ImagePathArrayList.get(i));

                }

            }
            MultiPhotoSelectActivity.image = null;
            business_galleryimage.setVisibility(View.VISIBLE);
            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
            business_galleryimage.setAdapter(horizontalAdapter);
            horizontalAdapter.notifyDataSetChanged();
        }
    }

    private void removeImages(final int remove_pos, String id) {
        //http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        Log.e("loginCall >", " > FIRST");
        progresbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = ApiClient.getApiInterface().removeImages(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("Remove Images >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            ImagePathArrayList.remove(remove_pos);
                            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                            business_galleryimage.setAdapter(horizontalAdapter);
                            horizontalAdapter.notifyDataSetChanged();

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
            switch (requestCode) {
                case 1:
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (data == null) return;
                        // Get photo picker response for single select.
                        final Uri selectedImage = data.getData();
                        try {
                            assert selectedImage != null;
                            try (final InputStream stream = getApplicationContext().getContentResolver().openInputStream(selectedImage)) {
                                final Bitmap bitmap = BitmapFactory.decodeStream(stream);
                                // user_img.setImageBitmap(bitmap);
                                user_img.setImageBitmap(bitmap);
                                File tempfile = Tools.persistImage(bitmap, getApplicationContext());
                                ImagePath = tempfile.getAbsolutePath();
                                Log.e("ImagePath", "onActivityResult: " + ImagePath);
                                //MerchantSignupSlider.ImagePath=ImagePath;
                            }
                        } catch (IOException e) {
                            ToolsShowDialog(getApplicationContext(), e.getLocalizedMessage());
                        }
                    } else {
                        try {


                            Uri selectedImage = data.getData();
                            //getPath(selectedImage);
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String FinalPath = cursor.getString(columnIndex);
                            cursor.close();
                            String ImagePath = getPath(selectedImage);
                            decodeFile(ImagePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    String cameraPath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + cameraPath);
                    //  String ImagePath = getPath(selectedImage);

                    decodeFile(cameraPath);
                    break;
                case 3:
                    Bitmap photo1 = (Bitmap) data.getExtras().get("data");
                    String cameraPath1 = saveToInternalStorage(photo1);
                    Log.e("PATH Camera", "" + cameraPath1);
                    //  String ImagePath = getPath(selectedImage);
                    GalleryBean galleryBean = new GalleryBean();
                    galleryBean.setId("0");
                    galleryBean.setGallery_image(cameraPath1);
                    ImagePathArrayList.add(galleryBean);
                    ImagePathArrayList_str.add(cameraPath1);
                    business_galleryimage.setVisibility(View.VISIBLE);
                    horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                    business_galleryimage.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();
                    //  decodeFile(cameraPath);
                    break;


            }
        }

    }

    private void idinit() {

        youtubelink = findViewById(R.id.youtubelink);
        businesskeywords = findViewById(R.id.businesskeywords);
        sunday_status = findViewById(R.id.sunday_status);
        monday_status = findViewById(R.id.monday_status);
        tuesday_status = findViewById(R.id.tuesday_status);
        wednesday_status = findViewById(R.id.wednesday_status);
        thursday_status = findViewById(R.id.thursday_status);
        friday_status = findViewById(R.id.friday_status);
        saturday_status = findViewById(R.id.saturday_status);

        saturday_full_day = findViewById(R.id.saturday_full_day);
        friday_full_day = findViewById(R.id.friday_full_day);
        thursday_full_day = findViewById(R.id.thursday_full_day);
        wednesday_full_day = findViewById(R.id.wednesday_full_day);
        tuesday_full_day = findViewById(R.id.tuesday_full_day);
        monday_full_day = findViewById(R.id.monday_full_day);
        sunday_full_day = findViewById(R.id.sunday_full_day);

        sundayopen = findViewById(R.id.sundayopen);
        sundayclose = findViewById(R.id.sundayclose);
        mondayopen = findViewById(R.id.mondayopen);
        mondayclose = findViewById(R.id.mondayclose);
        tuesdayopen = findViewById(R.id.tuesdayopen);
        tuesdayclose = findViewById(R.id.tuesdayclose);
        wednesdayopen = findViewById(R.id.wednesdayopen);
        wednesdayclose = findViewById(R.id.wednesdayclose);
        thursdayopen = findViewById(R.id.thursdayopen);
        thursdayclose = findViewById(R.id.thursdayclose);
        fridayopen = findViewById(R.id.fridayopen);
        fridayclose = findViewById(R.id.fridayclose);
        saturdayopen = findViewById(R.id.saturdayopen);
        saturdayclose = findViewById(R.id.saturdayclose);

        uploadimg = findViewById(R.id.uploadimg);
        business_galleryimage = findViewById(R.id.business_galleryimage);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(MerProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        business_galleryimage.setLayoutManager(horizontalLayoutManagaer);
        address_tv = findViewById(R.id.address);
        category_spinner = findViewById(R.id.category_spinner);
        googlelink = findViewById(R.id.googlelink);
        update_tv = findViewById(R.id.update_tv);
        zipcode = findViewById(R.id.zipcode);
        businessdescription = findViewById(R.id.businessdescription);
        progresbar = findViewById(R.id.progresbar);
        backlay = findViewById(R.id.backlay);
        user_img = findViewById(R.id.user_img);
        bus_number = findViewById(R.id.bus_number);
        businessname = findViewById(R.id.businessname);
        busines_email = findViewById(R.id.busines_email);
        streetaddress = findViewById(R.id.streetaddress);
        bus_website = findViewById(R.id.bus_website);
        facebooklink = findViewById(R.id.facebooklink);
        twittelink = findViewById(R.id.twittelink);
        linkedinlink = findViewById(R.id.linkedinlink);
        instalink = findViewById(R.id.instalink);
        state_spn = findViewById(R.id.state_spn);
        country_spn = findViewById(R.id.country_spn);
        city_spn = findViewById(R.id.city_spn);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    if (categoryBeanListArrayList.get(position).getCategoryId().equalsIgnoreCase("0")) {

                    } else {
                        category_id = categoryBeanListArrayList.get(position).getCategoryId();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        country_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (countryBeanArrayList != null && !countryBeanArrayList.isEmpty()) {
                }
                if (countryBeanArrayList.get(position).getId() == null || countryBeanArrayList.get(position).getId().equalsIgnoreCase("0")) {

                } else {
                    country_str = countryBeanArrayList.get(position).getName();
                    new GetStateList().execute(countryBeanArrayList.get(position).getId());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        state_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (statelistbean != null && !statelistbean.isEmpty()) {
                    if (statelistbean.get(position).getId() == null || statelistbean.get(position).getId().equalsIgnoreCase("0")) {

                    } else {
                        state_str = statelistbean.get(position).getName();
                        new GetCityList().execute(statelistbean.get(position).getId());
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (citylistbean != null && !citylistbean.isEmpty()) {
                    if (citylistbean.get(position).getId() == null || citylistbean.get(position).getId().equalsIgnoreCase("0")) {

                    } else {
                        city_str = citylistbean.get(position).getName();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @SuppressLint("Range")
    public String getPath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //  Log.e("image_path.===..", "" + path);
        }
        cursor.close();
        return path;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(MerProfileActivity.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_" + dateToStr + ".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        ImagePath = saveToInternalStorage(bitmap);
        Log.e("DECODE PATH", "ff " + ImagePath);
        user_img.setImageBitmap(bitmap);
    }

    public void addTime(final String time_set_tv) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(MerProfileActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        int hour = hourOfDay;
                        int fullhour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }
                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);
                        String time_str = "" + hour + ":" + min + " " + timeSet;
                        if (time_set_tv.equalsIgnoreCase("Sun")) {
                            sundayopen.setText("" + time_str);
                        } else if (time_set_tv.equalsIgnoreCase("Suncls")) {
                            sundayclose.setText("" + time_str);
                        } else if (time_set_tv.equalsIgnoreCase("Mon")) {
                            mondayopen.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Moncls")) {
                            mondayclose.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Tus")) {
                            tuesdayopen.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Tuscls")) {
                            tuesdayclose.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Wed")) {
                            wednesdayopen.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Wedcls")) {
                            wednesdayclose.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Thu")) {
                            thursdayopen.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Thucls")) {
                            thursdayclose.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Fri")) {
                            fridayopen.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Fricls")) {
                            fridayclose.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Sat")) {
                            saturdayopen.setText("" + time_str);

                        } else if (time_set_tv.equalsIgnoreCase("Satcls")) {
                            saturdayclose.setText("" + time_str);

                        }
                        Calendar c = Calendar.getInstance();
                        Date date = c.getTime();


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void selectImage() {
        final Dialog dialogSts = new Dialog(MerProfileActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_img_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button camera = (Button) dialogSts.findViewById(R.id.camera);
        Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);

            }
        });
        cont_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    private void checkGps() {
        gpsTracker = new GPSTracker(MerProfileActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            if (latitude == 0.0) {
                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;

            }
        } else {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else {
                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;
                Log.e("LAT", "" + latitude);
                Log.e("LON", "" + longitude);

            }
        }


    }

    private void selectImageMultiple() {
        final Dialog dialogSts = new Dialog(MerProfileActivity.this, R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.select_img_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button camera = (Button) dialogSts.findViewById(R.id.camera);
        Button gallary = (Button) dialogSts.findViewById(R.id.gallary);
        TextView cont_find = (TextView) dialogSts.findViewById(R.id.cont_find);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();

                if (Build.VERSION.SDK_INT >= 33) {
                    Intent i = new Intent(MerProfileActivity.this, MultiPhotoSelectActivity2.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MerProfileActivity.this, MultiPhotoSelectActivity.class);
                    startActivity(i);
                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                if (ImagePathArrayList.size() < 7) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.alreadyadded), Toast.LENGTH_LONG).show();
                }

            }
        });
        cont_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    public class CategoryAdpters extends BaseAdapter {
        private final ArrayList<CategoryBeanList> categoryBeanLists;
        Context context;
        LayoutInflater inflter;

        public CategoryAdpters(Context applicationContext, ArrayList<CategoryBeanList> categoryBeanLists) {
            this.context = applicationContext;
            this.categoryBeanLists = categoryBeanLists;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return categoryBeanLists == null ? 0 : categoryBeanLists.size();
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
            view = inflter.inflate(R.layout.spinner_layout, null);
            TextView names = (TextView) view.findViewById(R.id.name_tv);
            ImageView country_flag = (ImageView) view.findViewById(R.id.country_flag);
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);
            if (mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase("es")) {
                names.setText(categoryBeanLists.get(i).getCategory_name_spanish());
            } else if (mySession.getValueOf(KEY_LANGUAGE).equalsIgnoreCase("hi")) {
                names.setText(categoryBeanLists.get(i).getCategory_name_hindi());
            } else {
                names.setText(categoryBeanLists.get(i).getCategoryName());
            }
            return view;
        }
    }

    private class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private final ArrayList<GalleryBean> ImagePathArray;
        private ArrayList<Bitmap> horizontalList;

        public HorizontalAdapter(ArrayList<GalleryBean> ImagePathArray) {
            this.horizontalList = horizontalList;
            this.ImagePathArray = ImagePathArray;
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.business_lay_img, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (ImagePathArray.get(position) != null) {

                if (ImagePathArray.get(position).getId().equalsIgnoreCase("0")) {
                    holder.ProductImageImagevies.setImageURI(Uri.fromFile(new File(ImagePathArray.get(position).getGallery_image())));
                } else {
                    String product_img = ImagePathArray.get(position).getGallery_image();
                    if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {

                    } else {

                        Glide.with(MerProfileActivity.this).load(product_img).placeholder(R.drawable.placeholder).into(holder.ProductImageImagevies);

                    }
                }

            }

            holder.remove_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImagePathArray.get(position).getId().equalsIgnoreCase("0")) {
                        ImagePathArrayList.remove(position);
                        horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                        business_galleryimage.setAdapter(horizontalAdapter);
                        horizontalAdapter.notifyDataSetChanged();
                    } else {
                        remove_pos = position;
                        removeImages(remove_pos, ImagePathArray.get(position).getId());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return ImagePathArray == null ? 0 : ImagePathArray.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView ProductImageImagevies, remove_images;
            //   RelativeLayout RLRemovePhoto;

            public MyViewHolder(View view) {
                super(view);

                ProductImageImagevies = (ImageView) view.findViewById(R.id.productimage);
                remove_images = (ImageView) view.findViewById(R.id.remove_images);
                //    RLRemovePhoto = (RelativeLayout) view.findViewById(R.id.RLRemovePhoto);

            }
        }
    }

    private class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            ImagePathArrayList = new ArrayList<>();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "merchant_profile.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("merchant_id", user_id);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("GetProfile Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("status");
                    if (message.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                        String business_name = jsonObject1.getString("business_name");
                        String contact_name = jsonObject1.getString("contact_name");
                        if (business_name == null || business_name.equalsIgnoreCase("")) {

                            businessname.setText("" + contact_name);
                        } else {
                            businessname.setText("" + business_name);
                        }

                        business_number = jsonObject1.getString("business_no");

                        //businessname.setText("" + jsonObject1.getString("business_name"));
                        //bus_number.setText("" + jsonObject1.getString("business_no"));
                        bus_number.setText("" + jsonObject1.getString("contact_number"));
                        businessdescription.setText("" + jsonObject1.getString("business_description"));
                        businesskeywords.setText("" + jsonObject1.getString("keyword"));
                        address_tv.setText("" + jsonObject1.getString("address_two"));
                        busines_email.setText("" + jsonObject1.getString("email"));
                        streetaddress.setText("" + jsonObject1.getString("address"));
                        zipcode.setText("" + jsonObject1.getString("zip_code"));
                        bus_website.setText("" + jsonObject1.getString("website_url"));
                        facebooklink.setText("" + jsonObject1.getString("facebook_url"));
                        twittelink.setText("" + jsonObject1.getString("twitter_url"));
                        linkedinlink.setText("" + jsonObject1.getString("linkdin_url"));
                        instalink.setText("" + jsonObject1.getString("instagram_url"));
                        googlelink.setText("" + jsonObject1.getString("google_plus_url"));
                        youtubelink.setText("" + jsonObject1.getString("youtube_url"));
                        country_str = jsonObject1.getString("country");
                        contact_name_str = jsonObject1.getString("contact_name");
                        reward_per_str = jsonObject1.getString("reward_percent");
                        state_str = jsonObject1.getString("state");
                        city_str = jsonObject1.getString("city");
                        category_id = jsonObject1.getString("business_category");

                        if (jsonObject1.getString("latitude") != null && !jsonObject1.getString("latitude").equalsIgnoreCase("")) {
                            mer_latitude = Double.parseDouble(jsonObject1.getString("latitude"));
                            mer_longitude = Double.parseDouble(jsonObject1.getString("longitude"));
                        }


                        String image_url = jsonObject1.getString("merchant_image");
                        if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                            Glide.with(MerProfileActivity.this).load(image_url).placeholder(R.drawable.user_propf).into(user_img);
                        }
                        JSONArray jsonArray = jsonObject1.getJSONArray("hours");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(k);
                            switch (jsonObject2.getString("day_name")) {
                                case "Sunday":
                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        sunday_full_day_ststus = "OPEN";
                                        sunday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        sunday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        sunday_full_day_ststus = "DEFAULT";
                                    }
                                    sundayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    sundayclose.setText("" + jsonObject2.getString("closing_time"));
                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        sunday_status_opcls = jsonObject2.getString("opening_status");
                                        sunday_status.setText("" + getResources().getString(R.string.closed));
                                        sunday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        sunday_status_opcls = "OPEN";
                                        sunday_status.setText(getResources().getString(R.string.open));
                                        sunday_status.setBackgroundResource(R.drawable.pink_border);
                                    }
                                    break;
                                case "Monday":
                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        monday_full_day_ststus = "OPEN";
                                        monday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        monday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        monday_full_day_ststus = "DEFAULT";
                                    }
                                    mondayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    mondayclose.setText("" + jsonObject2.getString("closing_time").trim());
                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        monday_status_opcls = jsonObject2.getString("opening_status");
                                        monday_status.setText("" + getResources().getString(R.string.closed));
                                        monday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        monday_status_opcls = "OPEN";
                                        monday_status.setText(getResources().getString(R.string.open));
                                        monday_status.setBackgroundResource(R.drawable.pink_border);
                                    }

                                    break;
                                case "Tuesday":

                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        tuesday_full_day_ststus = "OPEN";
                                        tuesday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        tuesday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        tuesday_full_day_ststus = "DEFAULT";
                                    }

                                    tuesdayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    tuesdayclose.setText("" + jsonObject2.getString("closing_time").trim());

                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        tuesday_status_opcls = jsonObject2.getString("opening_status");
                                        tuesday_status.setText("" + getResources().getString(R.string.closed));
                                        tuesday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        tuesday_status_opcls = "OPEN";
                                        tuesday_status.setText(getResources().getString(R.string.open));
                                        tuesday_status.setBackgroundResource(R.drawable.pink_border);
                                    }
                                    break;
                                case "Wednesday":

                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        wednesday_full_day_ststus = "OPEN";
                                        wednesday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        wednesday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        wednesday_full_day_ststus = "DEFAULT";
                                    }

                                    wednesdayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    wednesdayclose.setText("" + jsonObject2.getString("closing_time").trim());
                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        wednesday_status_opcls = jsonObject2.getString("opening_status");
                                        wednesday_status.setText("" + getResources().getString(R.string.closed));
                                        wednesday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        wednesday_status_opcls = "OPEN";
                                        wednesday_status.setText(getResources().getString(R.string.open));
                                        wednesday_status.setBackgroundResource(R.drawable.pink_border);
                                    }

                                    break;
                                case "Thursday":
                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        thursday_full_day_ststus = "OPEN";
                                        thursday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        thursday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        thursday_full_day_ststus = "DEFAULT";
                                    }
                                    thursdayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    thursdayclose.setText("" + jsonObject2.getString("closing_time").trim());
                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        thursday_status_opcls = jsonObject2.getString("opening_status");
                                        thursday_status.setText("" + getResources().getString(R.string.closed));
                                        thursday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        thursday_status_opcls = "OPEN";
                                        thursday_status.setText(getResources().getString(R.string.open));
                                        thursday_status.setBackgroundResource(R.drawable.pink_border);
                                    }

                                    break;
                                case "Friday":
                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        friday_full_day_ststus = "OPEN";
                                        friday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        friday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        friday_full_day_ststus = "DEFAULT";
                                    }
                                    fridayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    fridayclose.setText("" + jsonObject2.getString("closing_time").trim());
                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        friday_status_opcls = jsonObject2.getString("opening_status");
                                        friday_status.setText("" + getResources().getString(R.string.closed));
                                        friday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        friday_status_opcls = "OPEN";
                                        friday_status.setText(getResources().getString(R.string.open));
                                        friday_status.setBackgroundResource(R.drawable.pink_border);
                                    }

                                    break;
                                case "Saturday":
                                    if (jsonObject2.getString("fullday_open_status").trim().equalsIgnoreCase("OPEN")) {
                                        saturday_full_day_ststus = "OPEN";
                                        saturday_full_day.setBackgroundResource(R.drawable.pink_border);
                                    } else {
                                        saturday_full_day.setBackgroundResource(R.drawable.bordr_dark_grey);
                                        saturday_full_day_ststus = "DEFAULT";
                                    }
                                    saturdayopen.setText("" + jsonObject2.getString("opening_time").trim());
                                    saturdayclose.setText("" + jsonObject2.getString("closing_time").trim());
                                    if (jsonObject2.getString("opening_status").equalsIgnoreCase("CLOSE")) {
                                        saturday_status_opcls = jsonObject2.getString("opening_status");
                                        saturday_status.setText("" + getResources().getString(R.string.closed));
                                        saturday_status.setBackgroundResource(R.drawable.bordr_dark_grey);
                                    } else {
                                        saturday_status_opcls = "OPEN";
                                        saturday_status.setText(getResources().getString(R.string.open));
                                        saturday_status.setBackgroundResource(R.drawable.pink_border);
                                    }

                                    break;

                            }
                        }

                        JSONArray jsonArray1 = jsonObject1.getJSONArray("gallery_images");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                            GalleryBean galleryBean = new GalleryBean();
                            galleryBean.setId(jsonObject2.getString("id"));
                            galleryBean.setGallery_image(jsonObject2.getString("gallery_image"));
                            ImagePathArrayList.add(galleryBean);
                        }
//Monday , Tuesday , Wednesday , Thursday , Friday , Saturday , Sunday

                        if (ImagePathArrayList != null && !ImagePathArrayList.isEmpty()) {
                            business_galleryimage.setVisibility(View.VISIBLE);
                            horizontalAdapter = new HorizontalAdapter(ImagePathArrayList);
                            business_galleryimage.setAdapter(horizontalAdapter);
                            horizontalAdapter.notifyDataSetChanged();
                        }

                        if (myapisession.getKeyMerchantcate() == null || myapisession.getKeyMerchantcate().equalsIgnoreCase("")) {
                            getCategoryType();
                        } else {
                            try {
                                categoryBeanListArrayList = new ArrayList<>();
                                CategoryBeanList categoryBeanList = new CategoryBeanList();
                                categoryBeanList.setCategoryId("0");
                                categoryBeanList.setCategoryName(getString(R.string.selectcat));
                                categoryBeanList.setCategory_name_spanish(getString(R.string.selectcat));
                                categoryBeanList.setCategory_name_hindi(getString(R.string.selectcat));
                                categoryBeanListArrayList.add(categoryBeanList);
                                JSONObject object = new JSONObject(myapisession.getKeyMerchantcate());
                                Log.e("loginCall >", " >" + myapisession.getKeyMerchantcate());
                                if (object.getString("status").equals("1")) {

                                    CategoryBean successData = new Gson().fromJson(myapisession.getKeyMerchantcate(), CategoryBean.class);
                                    categoryBeanListArrayList.addAll(successData.getResult());

                                }

                                categoryAdpters = new CategoryAdpters(MerProfileActivity.this, categoryBeanListArrayList);
                                category_spinner.setAdapter(categoryAdpters);
                                Log.e("category_id >>", "dddd " + category_id);
                                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                                    for (int i = 0; i < categoryBeanListArrayList.size(); i++) {
                                        Log.e("category_id >>", " dd " + categoryBeanListArrayList.get(i).getCategoryId());
                                        if (category_id.equalsIgnoreCase(categoryBeanListArrayList.get(i).getCategoryId())) {
                                            category_spinner.setSelection(i);
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    private class GetCountryList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            countryBeanArrayList = new ArrayList<>();
            CountryBean countryListBean = new CountryBean();
            countryListBean.setId("0");
            countryListBean.setName("Country");
            countryListBean.setSortname("");
            countryBeanArrayList.add(countryListBean);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "country_lists.php?contry_id=" + mySession.getValueOf(MySession.CountryId);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Json Country Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("successful")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryBean countryBean = new CountryBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            countryBean.setId(jsonObject1.getString("id"));
                            countryBean.setName(jsonObject1.getString("name"));
                            countryBean.setSortname(jsonObject1.getString("sortname"));
                            countryBeanArrayList.add(countryBean);
                        }

                        Log.e("country_str >>", "> " + country_str);
                        Log.e("country_str >>", "> " + countryBeanArrayList.size());
                        countryListAdapter = new CountryListAdapter(MerProfileActivity.this, countryBeanArrayList);
                        country_spn.setAdapter(countryListAdapter);
                        for (int i = 0; i < countryBeanArrayList.size(); i++) {
                            String countryName = countryBeanArrayList.get(i).getName();
                            {
                                Log.e("country id >>", "> " + countryName + " >> " + countryBeanArrayList.get(i).getId());

                                if (countryName.equalsIgnoreCase(country_str)) {
                                    country_spn.setSelection(i, true);
                                    Log.e("country id >>", "> " + countryBeanArrayList.get(i).getId());
                                    new GetStateList().execute(countryBeanArrayList.get(i).getId());
                                    break;
                                }//for default selection


                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    private class GetStateList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            statelistbean = new ArrayList<>();
            CountryBean countryListBean = new CountryBean();
            countryListBean.setId("0");
            countryListBean.setName("State");
            countryListBean.setSortname("");
            statelistbean.add(countryListBean);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "state_lists.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("country_id", strings[0]);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Json State Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("successful")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryBean countryBean = new CountryBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            countryBean.setId(jsonObject1.getString("id"));
                            countryBean.setName(jsonObject1.getString("name"));
                            statelistbean.add(countryBean);
                        }

                        Log.e("state_str >>", " >" + state_str);
                        countryListAdapter = new CountryListAdapter(MerProfileActivity.this, statelistbean);
                        state_spn.setAdapter(countryListAdapter);
                        for (int i = 0; i < statelistbean.size(); i++) {
                            String statename = statelistbean.get(i).getName();
                            {
                                if (statename.equalsIgnoreCase(state_str))//for default selection
                                    state_spn.setSelection(i, true);
                                Log.e("state_str id>>", " >" + statelistbean.get(i).getId());
                                new GetCityList().execute(statelistbean.get(i).getId());
                                break;


                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private class GetCityList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            citylistbean = new ArrayList<>();
            CountryBean countryListBean = new CountryBean();
            countryListBean.setId("0");
            countryListBean.setName("City");
            countryListBean.setSortname("");
            citylistbean.add(countryListBean);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String postReceiverUrl = BaseUrl.baseurl + "city_lists.php?";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("state_id", strings[0]);
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Json CIty Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progresbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("successful")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryBean countryBean = new CountryBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            countryBean.setId(jsonObject1.getString("id"));
                            countryBean.setName(jsonObject1.getString("name"));
                            citylistbean.add(countryBean);
                        }

                        countryListAdapter = new CountryListAdapter(MerProfileActivity.this, citylistbean);
                        city_spn.setAdapter(countryListAdapter);
                        for (int i = 0; i < citylistbean.size(); i++) {
                            String cityname = citylistbean.get(i).getName();
                            {
                                if (cityname.equalsIgnoreCase(city_str))//for default selection
                                    city_spn.setSelection(i, true);
                                break;


                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    public class CountryListAdapter extends BaseAdapter {
        private final ArrayList<CountryBean> values;
        Context context;
        LayoutInflater inflter;

        public CountryListAdapter(Context applicationContext, ArrayList<CountryBean> values) {
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
            view = inflter.inflate(R.layout.spinner_lay, null);

            TextView names = (TextView) view.findViewById(R.id.name_tv);
            //  TextView countryname = (TextView) view.findViewById(R.id.countryname);


            names.setText(values.get(i).getName());


            return view;
        }
    }

    public class UpdateProfile extends AsyncTask<String, String, String> {
        String Jsondata;

        protected void onPreExecute() {
            try {
                super.onPreExecute();
                progresbar.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "update_merchant_profile.php?";
            Log.e("requestURL >>", requestURL + "merchant_id=" + user_id + "&email=" + busines_email_str + "&day_name=" + day_name_str + "&opening_time=" + opening_hour_str + "&closing_time=" + closing_hour_str + "&opening_status=" + open_close_str);

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("merchant_id", user_id);
                multipart.addFormField("email", busines_email_str);
                multipart.addFormField("contact_number", bus_number_str);
                multipart.addFormField("business_no", business_number);

                multipart.addFormField("business_description", businessdescription_str);
                multipart.addFormField("business_name", businessname_str);
                multipart.addFormField("contact_name", contact_name_str);
                multipart.addFormField("account_no", "");
               /* multipart.addFormField("longitude", "");
                multipart.addFormField("latitude", "");*/

                multipart.addFormField("zip_code", zipcode_str);
                multipart.addFormField("address", streetaddress_str);
                multipart.addFormField("address_two", address_tv_str);
                multipart.addFormField("country", country_str);
                multipart.addFormField("state", state_str);
                multipart.addFormField("city", city_str);
                multipart.addFormField("reward_percent", reward_per_str);
                multipart.addFormField("website_url", bus_website_str);
                multipart.addFormField("facebook_url", facebooklink_str);
                multipart.addFormField("twitter_url", twittelink_str);
                multipart.addFormField("linkdin_url", linkedinlink_str);
                multipart.addFormField("google_plus_url", googlelink_str);
                multipart.addFormField("instagram_url", instalink_str);
                multipart.addFormField("youtube_url", youtubelink_str);
                multipart.addFormField("business_category", category_id);
                multipart.addFormField("latitude", "" + mer_latitude);
                multipart.addFormField("longitude", "" + mer_longitude);
                multipart.addFormField("day_name", day_name_str);
                multipart.addFormField("opening_status", open_close_str);
                multipart.addFormField("opening_time", opening_hour_str);
                multipart.addFormField("closing_time", closing_hour_str);
                multipart.addFormField("fullday_open_status", full_day_opne_status);
                multipart.addFormField("keyword", businesskeywords_str);

                Log.e("open_close_str dd>>", " >> " + open_close_str);
                Log.e("day_name_str dd>>", " >> " + day_name_str);

//gallery_image
                if (ImagePathArrayList == null || ImagePathArrayList.isEmpty()) {
//["+k+"]
                } else {
                    for (int k = 0; k < filearray.length; k++) {
                        if (k < 7) {
                            multipart.addFilePart("gallery_image[]", filearray[k]);
                        }

                    }

                    // multipart.addFilePart("member_image[]", filearray);
                }
                if (ImagePath == null || ImagePath.equalsIgnoreCase("")) {

                } else {
                    File ImageFile = new File(ImagePath);
                    multipart.addFilePart("merchant_image", ImageFile);
                }
                List<String> response = multipart.finish();

                for (String line : response) {
                    Jsondata = line;
                }
                JSONObject object = new JSONObject(Jsondata);
                return Jsondata;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progresbar.setVisibility(View.GONE);
            Log.e("Update Mer Prof", ">" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String result_chk = jsonObject.getString("status");
                    if (result_chk.equalsIgnoreCase("1")) {
                        mySession.setlogindata(result);
                        if (ImagePathArrayList != null) {
                            ImagePathArrayList.clear();
                        }
                        if (ImagePathArrayList_str != null) {
                            ImagePathArrayList_str.clear();
                        }


                        Toast.makeText(MerProfileActivity.this, getResources().getString(R.string.updateprosuccess), Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }


    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private final LayoutInflater layoutInflater;
        private final WebOperations wo;
        private final String lat;
        private final String lon;
        private List<String> l2 = new ArrayList<>();

        public GeoAutoCompleteAdapter(Activity context, List<String> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            this.lat = lat;
            this.lon = lon;
            layoutInflater = LayoutInflater.from(context);
            wo = new WebOperations(context);
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
                geo_search_result_text.setText(l2.get(i));
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        if (l2 == null || l2.isEmpty()) {

                        } else {
                            streetaddress.setText("" + l2.get(i));
                            streetaddress.dismissDropDown();

                            streetaddress_str = streetaddress.getText().toString();
                            if (streetaddress_str == null || streetaddress_str.equalsIgnoreCase("")) {
                            } else {
                                new GetPickUp().execute();
                            }
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
                        wo.setUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?key="+getString(R.string.googlekey)+"&input=" + constraint.toString().trim().replaceAll(" ", "+") + "&location=" + lat + "," + lon + "+&radius=20000&types=geocode&sensor=true");
                        String result = null;
                        try {
                            result = new MyTask(wo, 3).execute().get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        parseJson(result);


                        // Assign the data to the FilterResults
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l2 = (List) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        private void parseJson(String result) {
            try {
                l2 = new ArrayList<>();
                JSONObject jk = new JSONObject(result);

                JSONArray predictions = jk.getJSONArray("predictions");
                for (int i = 0; i < predictions.length(); i++) {
                    JSONObject js = predictions.getJSONObject(i);
                    l2.add(js.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private List<Address> findLocations(Context context, String query_text) {

            List<Address> geo_search_results = new ArrayList<Address>();

            Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
            List<Address> addresses = null;

            try {
                // Getting a maximum of 15 Address that matches the input text
                addresses = geocoder.getFromLocationName(query_text, 15);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return addresses;
        }
    }

    private class GetPickUp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                super.onPreExecute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = streetaddress_str.trim().replaceAll(" ", "+");
            Log.e("Murchant Location >>>", "" + streetaddress_str);
            String postReceiverUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key="+getString(R.string.googlekey);

            try {
                //  String postReceiverUrl = "https://api.ctlf.co.uk/";
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("JsonShyam", ">>>>>>>>>>>>" + response);


                return response;

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else if (result.equalsIgnoreCase("")) {

            } else {
                JSONObject location = null;
                try {
                    location = new JSONObject(result).getJSONArray("results")
                            .getJSONObject(0).getJSONObject("geometry")
                            .getJSONObject("location");


                    //    pickup_lat_str,pickup_lon_str,drop_lat_str,drop_lon_str,
                    mer_latitude = location.getDouble("lat");
                    mer_longitude = location.getDouble("lng");
                    Log.e("event_lat >>>>>>>", "" + MerchantSignupSlider.mer_latitude);
                    Log.e("event_lon >>>>>>>", "" + MerchantSignupSlider.mer_longitude);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}