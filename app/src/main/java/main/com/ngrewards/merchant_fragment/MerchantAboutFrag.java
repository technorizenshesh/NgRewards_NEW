package main.com.ngrewards.merchant_fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.com.ngrewards.R;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.constant.GPSTracker;

public class MerchantAboutFrag extends Fragment implements OnMapReadyCallback {
    MapView mapview;
    GoogleMap map;
    GPSTracker gpsTracker;
    Marker drivermarker;
    private TextView open_close_status, description_tv, phone_number, openinghours, address, merchant_name, businesscategory_name;
    private TextView sundayopen, mondayopen, tuesdayopen, wednesdayopen, thursdayopen, fridayopen, saturdayopen;
    private View v;
    private double latitude = 0, longitude = 0;
    private ImageView google_link, facebook_link, twitter_link, youtube_link, website_link, linkedin_link, insta_link;
    private SwipeRefreshLayout swipeToRefresh;
    private RatingBar rating;
    private Boolean sunday_sts = false, monday_sts = false, tuesday_sts = false, wednes_sts = false, thursday_sts = false, friday_sts = false, saturday_sts = false;
    private int hour;
    private String openingtime;
    private String closingtime;
    private String time;
    private int timeint;
    private int hour11;
    private String status;
    private TextView open_close_status123;
    private String openingtime0;
    private String closingtime0;
    private String openingtime01;
    private String closingtime01;
    private String openingtime2;
    private String closingtime2;
    private String openingtime3;
    private String closingtime3;
    private String openingtime4;
    private String closingtime4;
    private String openingtime5;
    private String closingtime5;
    private String openingtime6;
    private String closingtime6;
    private String openingtime7;
    private String closingtime7;
    private String merchant_id;
    private String opening_time;
    private String closing_time;
    private String get_openingtime;
    private String get_closingtime;

    public MerchantAboutFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.pro_aboutfrag_lay, container, false);

        MerchantDetailAct activity = (MerchantDetailAct) getActivity();

        get_openingtime = activity.getOpeningTime();
        get_closingtime = activity.getClosing_time();

        Calendar cal = Calendar.getInstance(); // get current time in a Calendar

        // then you can do lots with the Calendar instance, such as get the Hours or the Minutes - like:

        hour = cal.get(Calendar.HOUR);
        hour11 = cal.get(Calendar.HOUR_OF_DAY);

        mapview = v.findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(this);

        checkGps();
        idinit();
        clciekvet();


        return v;
    }

    private void clciekvet() {
        facebook_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getFacebookUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });

        twitter_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getTwitterUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });

        website_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getWebsiteUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });

        google_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getGooglePlusUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });

        youtube_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getYoutube_url();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });

        insta_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getInstagramUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });

        linkedin_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MerchantDetailAct.merchantListBeanArrayList.get(0).getLinkdinUrl();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + url));
                startActivity(i);
            }
        });
    }

    private void checkGps() {

        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            if (latitude == 0.0) {
                latitude = SplashActivity.latitude;
                longitude = SplashActivity.longitude;

            }
        }

    }

    @SuppressLint("ResourceAsColor")

    private void idinit() {

        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  getMerchantsDetail(MerchantDetailAct.merchant_id);
                swipeToRefresh.setRefreshing(false);
            }
        });

        rating = v.findViewById(R.id.rating);
        google_link = v.findViewById(R.id.google_link);
        open_close_status = v.findViewById(R.id.open_close_status);
        merchant_name = v.findViewById(R.id.merchant_name);
        businesscategory_name = v.findViewById(R.id.businesscategory_name);
        facebook_link = v.findViewById(R.id.facebook_link);
        twitter_link = v.findViewById(R.id.twitter_link);
        youtube_link = v.findViewById(R.id.youtube_link);
        website_link = v.findViewById(R.id.website_link);
        insta_link = v.findViewById(R.id.insta_link);
        linkedin_link = v.findViewById(R.id.linkedin_link);
        sundayopen = v.findViewById(R.id.sundayopen);
        mondayopen = v.findViewById(R.id.mondayopen);
        tuesdayopen = v.findViewById(R.id.tuesdayopen);
        wednesdayopen = v.findViewById(R.id.wednesdayopen);
        thursdayopen = v.findViewById(R.id.thursdayopen);
        fridayopen = v.findViewById(R.id.fridayopen);
        saturdayopen = v.findViewById(R.id.saturdayopen);
        phone_number = v.findViewById(R.id.phone_number);
        address = v.findViewById(R.id.address);
        openinghours = v.findViewById(R.id.openinghours);
        description_tv = v.findViewById(R.id.description_tv);


        if (MerchantDetailAct.merchantListBeanArrayList != null && !MerchantDetailAct.merchantListBeanArrayList.isEmpty()) {
            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getFacebookUrl() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getFacebookUrl().equalsIgnoreCase("")) {
                facebook_link.setVisibility(View.GONE);
            }

            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getTwitterUrl() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getTwitterUrl().equalsIgnoreCase("")) {
                twitter_link.setVisibility(View.GONE);
            }
            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getGooglePlusUrl() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getGooglePlusUrl().equalsIgnoreCase("")) {
                google_link.setVisibility(View.GONE);
            }

            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getYoutube_url() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getYoutube_url().equalsIgnoreCase("")) {
                youtube_link.setVisibility(View.GONE);
            }

            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getWebsiteUrl() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getWebsiteUrl().equalsIgnoreCase("")) {
                website_link.setVisibility(View.GONE);
            }

            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getLinkdinUrl() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getLinkdinUrl().equalsIgnoreCase("")) {
                linkedin_link.setVisibility(View.GONE);
            }

            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getInstagramUrl() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getInstagramUrl().equalsIgnoreCase("")) {
                insta_link.setVisibility(View.GONE);
            }

            String rat_str = MerchantDetailAct.merchantListBeanArrayList.get(0).getAverageRating();

            if (rat_str != null && !rat_str.equalsIgnoreCase("")) {

                rating.setRating(Float.parseFloat(rat_str));
            }

            if (MerchantDetailAct.merchantListBeanArrayList.get(0).getBusinessCategoryName() != null && !MerchantDetailAct.merchantListBeanArrayList.get(0).getBusinessCategoryName().equalsIgnoreCase("")) {
                businesscategory_name.setText("" + MerchantDetailAct.merchantListBeanArrayList.get(0).getBusinessCategoryName() + "  " + MerchantDetailAct.merchantListBeanArrayList.get(0).getDistance() + getString(R.string.mi));

            } else {

                businesscategory_name.setText("" + MerchantDetailAct.merchantListBeanArrayList.get(0).getDistance() + getString(R.string.mi));
            }

            merchant_name.setText("" + MerchantDetailAct.merchantListBeanArrayList.get(0).getBusinessName());
            description_tv.setText("" + MerchantDetailAct.merchantListBeanArrayList.get(0).getBusinessDescription());
            phone_number.setText("" + MerchantDetailAct.merchantListBeanArrayList.get(0).getContactNumber());
            address.setText("" + MerchantDetailAct.merchantListBeanArrayList.get(0).getAddress());

            try {

                Date mToday = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                String curTime = sdf.format(mToday);

                Date start = sdf.parse("" + get_openingtime);
                Date end = sdf.parse("" + get_closingtime);

                Date userDate = sdf.parse(curTime);

                if (end.before(start)) {

                    Calendar mCal = Calendar.getInstance();
                    mCal.setTime(end);
                    mCal.add(Calendar.DAY_OF_YEAR, 1);
                    end.setTime(mCal.getTimeInMillis());
                }

                if (userDate.after(start) && userDate.before(end)) {

                    open_close_status.setText("OPEN");
                    open_close_status.setTextColor(R.color.green);

                    Log.e("resulttime", "falls between start and end , go to screen 1 ");

                } else {

                    open_close_status.setText("CLOSED");

                    Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                }

            } catch (ParseException e) {

            }

            for (int k = 0; k < MerchantDetailAct.merchantListBeanArrayList.get(0).getHours().size(); k++) {

                merchant_id = MerchantDetailAct.merchantListBeanArrayList.get(0).getId();
                String dayname = MerchantDetailAct.merchantListBeanArrayList.get(0).getHours().get(k).getDayName();

                openingtime = MerchantDetailAct.merchantListBeanArrayList.get(0).getHours().get(k).getOpeningTime();
                closingtime = MerchantDetailAct.merchantListBeanArrayList.get(0).getHours().get(k).getClosingTime();

                /* open_close_status.setText(openingtime + closingtime);*/

                String openclosestatus = MerchantDetailAct.merchantListBeanArrayList.get(0).getHours().get(k).getOpening_status();
                String fulldaystatus = MerchantDetailAct.merchantListBeanArrayList.get(0).getHours().get(k).getFullday_open_status();

                switch (dayname) {

                    case "Sunday":

                        sunday_sts = true;


                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN") &&
                                openingtime.trim().equals("0.0") && openingtime.trim().equals("0.0")) {
                            sundayopen.setText("OPEN");

                        } else {

                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {
                                sundayopen.setText("CLOSED");
                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {
                                sundayopen.setText("CLOSED");

                            } else {

                                sundayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }


                   /*     try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);

                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            if (userDate.after(start) && userDate.before(end)) {

                                Toast.makeText(getContext(), "success!", Toast.LENGTH_SHORT).show();

                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);

                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                status = "CLOSED";
                                open_close_status.setText("CLOSED");

                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {

                        }
*/
                        break;

                    case "Monday":

                        monday_sts = true;

                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN")) {
                            mondayopen.setText("OPEN");

                        } else {

                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {
                                mondayopen.setText("CLOSED");
                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {

                                mondayopen.setText("CLOSED");

                            } else {

                                mondayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }

                        if (openingtime.equals("0:0") && closingtime.equals("0:0")) {
                            mondayopen.setText("OPEN");

                        }

                    /*    String currentString11 = closingtime;
                        String[] separated11 = currentString11.split(":");
                        time = separated11[0];
                        timeint = Integer.parseInt(time);

                        try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);

                            Log.e("curTime", curTime);

                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);

                            Date userDate = sdf.parse(curTime);

                            // Date userDate = sdf.parse("11:00 PM");

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            Log.e("UserDatestart: ", String.valueOf(userDate.after(start)));
                            Log.e("UserDateend: ", String.valueOf(userDate.before(end)));

                            if (userDate.after(start) && userDate.before(end)) {

                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);
                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                status = "CLOSED";
                                open_close_status.setText("CLOSED");
                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {


                        }*/

                        break;

                    case "Tuesday":

                        tuesday_sts = true;

                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN")) {
                            tuesdayopen.setText("OPEN");
                        } else {
                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {
                                tuesdayopen.setText("CLOSED");
                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {
                                tuesdayopen.setText("CLOSED");

                            } else {
                                tuesdayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }

                        if (openingtime.equals("0:0") && closingtime.equals("0:0")) {

                            tuesdayopen.setText("OPEN");

                        }

                     /*   String currentString1 = closingtime;
                        String[] separated1 = currentString1.split(":");
                        time = separated1[0];
                        timeint = Integer.parseInt(time);

                        try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            if (userDate.after(start) && userDate.before(end)) {
                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);
                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                status = "CLOSED";
                                open_close_status.setText("CLOSED");

                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {
                            // Invalid date was entered
                        }*/

                        break;

                    case "Wednesday":


                        wednes_sts = true;
                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN")) {
                            wednesdayopen.setText("OPEN");
                        } else {
                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {
                                wednesdayopen.setText("CLOSED");
                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {
                                wednesdayopen.setText("CLOSED");

                            } else {
                                wednesdayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }

                        if (openingtime.equals("0:0") && closingtime.equals("0:0")) {

                            wednesdayopen.setText("OPEN");

                        }

                       /* String currentString2 = closingtime;
                        String[] separated2 = currentString2.split(":");
                        time = separated2[0];
                        timeint = Integer.parseInt(time);

                        Log.e("closingtimeesa", time);
                        Log.e("current", String.valueOf(hour));


                        try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            if (userDate.after(start) && userDate.before(end)) {
                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);
                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                status = "CLOSED";
                                open_close_status.setText("CLOSED");

                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {
                            // Invalid date was entered
                        }*/


                        break;

                    case "Thursday":

                        thursday_sts = true;

                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN")) {
                            thursdayopen.setText("OPEN");

                        } else {
                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {
                                thursdayopen.setText("CLOSED");
                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {
                                thursdayopen.setText("CLOSED");

                            } else {
                                thursdayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }

                        if (openingtime.equals("0:0") && closingtime.equals("0:0")) {

                            thursdayopen.setText("OPEN");

                        }
/*


                        String currentString4 = closingtime;
                        String[] separated4 = currentString4.split(":");
                        time = separated4[0];
                        timeint = Integer.parseInt(time);

                        Log.e("closingtimeesa", time);
                        Log.e("current", String.valueOf(hour));


                        try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            if (userDate.after(start) && userDate.before(end)) {
                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);

                                //   StatusUpdateApi("OPEN", merchant_id);
                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                status = "CLOSED";
                                open_close_status.setText("CLOSED");

                                //StatusUpdateApi("CLOSED", merchant_id);

                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {
                            // Invalid date was entered
                        }
*/


                        break;


                    case "Friday":

                        friday_sts = true;
                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN")) {
                            fridayopen.setText("OPEN");
                        } else {
                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {
                                fridayopen.setText("CLOSED");
                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {
                                fridayopen.setText("CLOSED");

                            } else {
                                fridayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }

                        if (openingtime.equals("0:0") && closingtime.equals("0:0")) {

                            fridayopen.setText("OPEN");

                        }

/*
                        try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            if (userDate.after(start) && userDate.before(end)) {
                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);
                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                status = "CLOSED";
                                open_close_status.setText("CLOSED");

                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {
                            // Invalid date was entered
                        }*/

                        break;

                    case "Saturday":

                        saturday_sts = true;

                        if (fulldaystatus != null && fulldaystatus.trim().equalsIgnoreCase("OPEN")) {

                            saturdayopen.setText("OPEN");

                        } else {

                            if (openclosestatus == null || openclosestatus.equalsIgnoreCase("")) {

                                saturdayopen.setText("CLOSED");

                            } else if (openclosestatus.equalsIgnoreCase("CLOSE")) {

                                saturdayopen.setText("CLOSED");

                            } else {

                                saturdayopen.setText("" + openingtime.trim() + " - " + closingtime.trim());
                            }
                        }

                        if (openingtime.equals("0:0") && closingtime.equals("0:0")) {

                            saturdayopen.setText("OPEN");

                        }

/*
                        String currentString6 = closingtime;

                        String[] separated6 = currentString6.split(":");
                        time = separated6[0];
                        timeint = Integer.parseInt(time);

                        try {

                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(openingtime);
                            Date end = sdf.parse(closingtime);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            if (userDate.after(start) && userDate.before(end)) {

                                open_close_status.setText("OPEN");
                                open_close_status.setTextColor(R.color.green);

                                Log.e("resulttime", "falls between start and end , go to screen 1 ");

                            } else {

                                open_close_status.setText("CLOSED");
                                Log.e("resulttime", "does not fall between start and end , go to screen 2 ");
                            }

                        } catch (ParseException e) {

                            open_close_status.setText("" + "OPEN");

                        }*/

                        break;


                    default:

                        if (!sunday_sts) {

                            sundayopen.setText("CLOSED");
                        }
                        if (!monday_sts) {

                            mondayopen.setText("CLOSED");
                        }
                        if (!tuesday_sts) {
                            tuesdayopen.setText("CLOSED");
                        }
                        if (!wednes_sts) {
                            wednesdayopen.setText("CLOSED");
                        }
                        if (!thursday_sts) {
                            thursdayopen.setText("CLOSED");
                        }
                        if (!friday_sts) {
                            fridayopen.setText("CLOSED");
                        }

                        if (!saturday_sts) {
                            saturdayopen.setText("CLOSED");
                        }

                        break;

                }
            }

        }
    }

    private void StatusUpdateApi(String status, String merchant_id) {


   /*     AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });*/

        ///here is a impliment here sagars panse //

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);

        try {
            if (MerchantDetailAct.merchantListBeanArrayList != null) {

                if (MerchantDetailAct.merchantListBeanArrayList.get(0).getLatitude() == null || MerchantDetailAct.merchantListBeanArrayList.get(0).getLatitude().equalsIgnoreCase("") || MerchantDetailAct.merchantListBeanArrayList.get(0).getLatitude().equalsIgnoreCase("null") || MerchantDetailAct.merchantListBeanArrayList.get(0).getLatitude().equalsIgnoreCase("0")) {

                } else {
                    double lat = Double.parseDouble(MerchantDetailAct.merchantListBeanArrayList.get(0).getLatitude());
                    double lon = Double.parseDouble(MerchantDetailAct.merchantListBeanArrayList.get(0).getLongitude());
                    latLng = new LatLng(lat, lon);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        MarkerOptions marker = new MarkerOptions().position(latLng).flat(true).anchor(0.5f, 0.5f);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        drivermarker = map.addMarker(marker);
        drivermarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pick_marker));
        map.animateCamera(cameraUpdate);

    }

    @Override
    public void onResume() {
        mapview.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }
}
