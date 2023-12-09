package main.com.ngrewards.placeorderclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.beanclasses.CartBean;
import main.com.ngrewards.beanclasses.CartListBean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.drawlocation.MyTask;
import main.com.ngrewards.drawlocation.WebOperations;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderAct extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<LocationSettingsResult> {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    public static LatLng orderlatlong;
    private final Integer THRESHOLD = 2;
    GPSTracker gpsTracker;
    int initial_flag = 0;
    String address_complete = "";
    Location location;
    Location location_ar;
    LocationManager locationManager;
    boolean click_sts = false;
    SwipeRefreshLayout swipeToRefresh;
    MySession mySession;
    private int count = 0;
    private FrameLayout contentFrameLayout;
    private GoogleMap gMap;
    private double longitude = 0.0, latitude = 0.0;
    private AutoCompleteTextView pickuplocation;
    private GoogleApiClient googleApiClient;
    private ImageView clear_pick_ic;
    private ProgressBar progresbar;
    //cart dynamic
    private RecyclerView mycartlist;
    private String user_id = "";
    private ArrayList<CartListBean> cartListBeanArrayList;
    private MycartAdapter mycartAdapter;
    private TextView total_amount, nocartitem, place_order_but;
    private RelativeLayout backlay;
    private EditText optionaladdress, zipcode;
    private String email_str = "", time_zone = "", phone_str = "", fullname_str = "", order_address = "", streat_address = "", zipcode_code_str = "", product_id_comma = "", product_quantity_comm = "";
    private Myapisession myapisession;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.reupdateResources(this);
        setContentView(R.layout.activity_place_order);
        myapisession = new Myapisession(this);
        gpsTracker = new GPSTracker(this);
        mySession = new MySession(this);
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    email_str = jsonObject1.getString("email");
                    phone_str = jsonObject1.getString("phone");
                    fullname_str = jsonObject1.getString("fullname");
                    Log.e("user_id >>", " >" + user_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        getLatLong();
        idinits();
        autocompleteView();
        clickevetn();
        getCurrentLocation();
        try {

            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getMyCartDetail() {
        swipeToRefresh.setRefreshing(true);
        //progresbar.setVisibility(View.VISIBLE);
        cartListBeanArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMyCart(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Cart Items >", " >" + responseData);
                        if (object.getString("status").equals("1")) {

                            CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                            cartListBeanArrayList.addAll(successData.getResult());
                            total_amount.setText(mySession.getValueOf(MySession.CurrencySign) + successData.getTotalPrice());
                        }
                        if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty() || cartListBeanArrayList.size() == 0) {
                            // nocartitem.setVisibility(View.VISIBLE);
                            total_amount.setText(mySession.getValueOf(MySession.CurrencySign) + " 0.00");
                            mycartAdapter = new MycartAdapter(cartListBeanArrayList);
                            mycartlist.setAdapter(mycartAdapter);
                            mycartAdapter.notifyDataSetChanged();
                        } else {
                            // nocartitem.setVisibility(View.GONE);
                            mycartAdapter = new MycartAdapter(cartListBeanArrayList);
                            mycartlist.setAdapter(mycartAdapter);
                            mycartAdapter.notifyDataSetChanged();
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
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void clickevetn() {
        clear_pick_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickuplocation.setText("");
            }
        });

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        place_order_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_address = pickuplocation.getText().toString();
                streat_address = optionaladdress.getText().toString();
                zipcode_code_str = zipcode.getText().toString();
                if (cartListBeanArrayList == null || cartListBeanArrayList.isEmpty()) {

                } else {
                    product_id_comma = "";
                    product_quantity_comm = "";
                    StringBuilder quantity = new StringBuilder();
                    StringBuilder productid = new StringBuilder();
                    for (int i = 0; i < cartListBeanArrayList.size(); i++) {

                        productid.append(cartListBeanArrayList.get(i).getProductId());
                        quantity.append(cartListBeanArrayList.get(i).getQuantity());
                        if (i != cartListBeanArrayList.size() - 1) {
                            productid.append(",");
                            quantity.append(",");
                        }


                    }

                    product_id_comma = productid.toString();
                    product_quantity_comm = quantity.toString();
                }

                if (order_address == null || order_address.equalsIgnoreCase("")) {
                    Toast.makeText(PlaceOrderAct.this, getResources().getString(R.string.enterdeliveradd), Toast.LENGTH_LONG).show();
                } else if (zipcode_code_str == null || zipcode_code_str.equalsIgnoreCase("")) {
                    Toast.makeText(PlaceOrderAct.this, getResources().getString(R.string.enterzipcode), Toast.LENGTH_LONG).show();

                } else if (product_id_comma == null || product_id_comma.equalsIgnoreCase("")) {
                    Toast.makeText(PlaceOrderAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();
                } else {
                    Log.e("product_id_comma >>", " >> " + product_id_comma);
                    Log.e("product_qua_comm >>", " >> " + product_quantity_comm);
                    new PlaceOrderAsc().execute();
                }

            }
        });

    }

    private void initilizeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(PlaceOrderAct.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PlaceOrderAct.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).flat(true).anchor(0.5f, 0.5f);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 19);
        // gMap.addMarker(marker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pngs));
        gMap.animateCamera(cameraUpdate);
        /*order_address = loadAddress(latLng.latitude, latLng.longitude);
        orderlatlong = new LatLng(latLng.latitude, latLng.longitude);*/

        gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                System.out.println("Camera idle worked");
                if (initial_flag != 0) {
                    Log.e("CenterLatLng", ">>>>" + gMap.getCameraPosition().target);

                    LatLng latLng = gMap.getCameraPosition().target;
                    //  getAddress(latLng.latitude,latLng.longitude);


                  /*  if (!click_sts){
                        address_complete = loadAddress(latLng.latitude, latLng.longitude);
                        pickuplocation.setText(address_complete);
                        order_address = loadAddress(latLng.latitude, latLng.longitude);
                        orderlatlong = new LatLng(latLng.latitude, latLng.longitude);
                    }
                    else {
                        click_sts =false;
                    }*/


                }
                initial_flag++;
                System.out.println("Camera Value of initial_flag =" + initial_flag);
            }
        });


    }

    private void idinits() {

        place_order_but = findViewById(R.id.place_order_but);
        optionaladdress = findViewById(R.id.optionaladdress);
        zipcode = findViewById(R.id.zipcode);
        clear_pick_ic = (ImageView) findViewById(R.id.clear_pick_ic);
        progresbar = (ProgressBar) findViewById(R.id.progresbar);

        pickuplocation = (AutoCompleteTextView) findViewById(R.id.pickuplocation_auto);

        backlay = findViewById(R.id.backlay);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        nocartitem = findViewById(R.id.nocartitem);
        total_amount = findViewById(R.id.total_amount);
        mycartlist = findViewById(R.id.mycartlist);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(PlaceOrderAct.this, LinearLayoutManager.VERTICAL, false);
        mycartlist.setLayoutManager(horizontalLayoutManagaer);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyCartDetail();
            }
        });
        getMyCartDetail();


    }

    private void loadData(String s) {
        try {
            if (count == 0) {
                List<String> l1 = new ArrayList<>();
                if (s == null) {

                } else {

                    l1.add(s);

                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(PlaceOrderAct.this, l1, "" + latitude, "" + longitude);
                    pickuplocation.setAdapter(ga);

                }

            }
            count++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLatLong() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location_ar = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location_ar != null) {
            //Getting longitude and latitude
            longitude = location_ar.getLongitude();
            latitude = location_ar.getLatitude();
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + latitude);
           /* address_complete = loadAddress(latitude, longitude);
            pickuplocation.setText(address_complete);*/


        } else {
            System.out.println("----------------geting Location from GPS----------------");

            location_ar = gpsTracker.getLocation();
            if (location_ar == null) {


            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
                Log.e("Lat >>", "GPS " + latitude);
                /*address_complete = loadAddress(latitude, longitude);
                pickuplocation.setText(address_complete);*/
            }
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + longitude);
            //moving the map to location

        }
    }

    private void autocompleteView() {
        pickuplocation.setThreshold(THRESHOLD);
        pickuplocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    loadData(pickuplocation.getText().toString());
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //    updateLocationUI();

    }

    class MycartAdapter extends RecyclerView.Adapter<MycartAdapter.MyViewHolder> {
        ArrayList<CartListBean> mycartlist;

        public MycartAdapter(ArrayList<CartListBean> mycartlist) {
            this.mycartlist = mycartlist;
        }

        @Override
        public MycartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_cart_item_lay, parent, false);
            MycartAdapter.MyViewHolder myViewHolder = new MycartAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MycartAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            holder.product_desc.setText("" + mycartlist.get(listPosition).getProductDetail().getProductDescription());
            holder.product_name.setText("" + mycartlist.get(listPosition).getProductDetail().getProductName());
            holder.merchant_name.setText("" + mycartlist.get(listPosition).getUserDetails().get(0).getBusinessName());
            holder.quant_tv.setText("" + mycartlist.get(listPosition).getQuantity());
            holder.mainprice.setText(mySession.getValueOf(MySession.CurrencySign) + mycartlist.get(listPosition).getProductDetail().getProduct_cart_price());

            String image_url = mycartlist.get(listPosition).getProductDetail().getThumbnailImage();
            if (image_url != null && !image_url.equalsIgnoreCase("") && !image_url.equalsIgnoreCase(BaseUrl.image_baseurl)) {
                Glide.with(PlaceOrderAct.this).load(image_url).placeholder(R.drawable.placeholder).into(holder.product_img);
            }


            holder.plusq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mycartlist.get(listPosition).getQuantity() != null && !mycartlist.get(listPosition).getQuantity().equalsIgnoreCase("")) {
                        int total_stock_count = 0;
                        int total_count = Integer.parseInt(mycartlist.get(listPosition).getQuantity());
                        if (mycartlist.get(listPosition).getProductDetail().getStock() != null && !mycartlist.get(listPosition).getProductDetail().getStock().equalsIgnoreCase("")) {
                            total_stock_count = Integer.parseInt(mycartlist.get(listPosition).getProductDetail().getStock());

                        }
                        if (total_count < total_stock_count) {
                            int new_count = ++total_count;
                            // updateMyCartItemQuantity(mycartlist.get(listPosition).getProductId(),""+new_count );
                        }

                    }

                }
            });

            holder.minusq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mycartlist.get(listPosition).getQuantity() != null && !mycartlist.get(listPosition).getQuantity().equalsIgnoreCase("")) {
                        int total_count = Integer.parseInt(mycartlist.get(listPosition).getQuantity());
                        if (total_count > 1) {
                            int new_count = --total_count;
                            // updateMyCartItemQuantity(mycartlist.get(listPosition).getProductId(),""+new_count );
                        }

                    }
                }
            });
            holder.removecartitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  removeMySingleCartItem(mycartlist.get(listPosition).getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            //return 4;
            return mycartlist == null ? 0 : mycartlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView product_name, mainprice, merchant_name, product_desc, quant_tv;
            ImageView product_img, removecartitem;
            Button plusq, minusq;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.product_name = itemView.findViewById(R.id.product_name);
                this.merchant_name = itemView.findViewById(R.id.merchant_name);
                this.product_desc = itemView.findViewById(R.id.product_desc);
                this.quant_tv = itemView.findViewById(R.id.quant_tv);

                this.product_img = itemView.findViewById(R.id.product_img);
                this.mainprice = itemView.findViewById(R.id.mainprice);
                this.plusq = itemView.findViewById(R.id.plusq);
                this.minusq = itemView.findViewById(R.id.minusq);
                this.removecartitem = itemView.findViewById(R.id.removecartitem);

            }
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
                        order_address = l2.get(i);
                        pickuplocation.setText("" + order_address);

                        pickuplocation.dismissDropDown();
                        click_sts = true;
                        // new GetPickUp().execute();


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
                        wo.setUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=" + getResources().getString(R.string.google_search) + "&input=" + constraint.toString().trim().replaceAll(" ", "+") + "&location=" + lat + "," + lon + "+&radius=20000&sensor=true");

                        String result = null;
                        try {
                            result = new MyTask(wo, 3).execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        parseJson(result);

                        System.out.println("FilterResults===============================" + result);
                        Log.e("Location===========", "Come" + result);
                        // Assign the data to the FilterResults
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    System.out.println("publishResults===============================" + results);
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
                Log.e("Check this GACA jk", ">>>" + jk);
                JSONArray predictions = jk.getJSONArray("predictions");
                for (int i = 0; i < predictions.length(); i++) {
                    JSONObject js = predictions.getJSONObject(i);
                    l2.add(js.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            String address = order_address.trim().replaceAll(" ", "+");
            Log.e("order_address >>>", "" + order_address);
            String postReceiverUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + getResources().getString(R.string.google_search);

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
                    double pickup_lat_str = location.getDouble("lat");
                    double pickup_lon_str = location.getDouble("lng");
                    orderlatlong = new LatLng(pickup_lat_str, pickup_lon_str);
                    Log.e("event_lat >>>>>>>", "" + pickup_lat_str);
                    Log.e("event_lon >>>>>>>", "" + pickup_lon_str);
                    LatLng latLng = new LatLng(pickup_lat_str, pickup_lon_str);
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(pickup_lat_str, pickup_lon_str)).flat(true).anchor(0.5f, 0.5f);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    // gMap.addMarker(marker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pngs));
                    gMap.animateCamera(cameraUpdate);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    private class PlaceOrderAsc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresbar.setVisibility(View.VISIBLE);
            // prgressbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//http://myngrewards.com/demo/wp-content/plugins/webservice/place_order.php
            try {

                String postReceiverUrl = BaseUrl.baseurl + "place_order.php?";
                Log.e("Place URL", " URL TRUE " + postReceiverUrl + "user_id=" + user_id + "&product_id=" + product_id_comma + "&quantity=" + product_quantity_comm + "&email=" + email_str + "&first_name=" + fullname_str + "&last_name=&company=&phone=" + phone_str + "&address_1=" + order_address + "&address_2=" + streat_address + "&city=&state=&postcode=" + zipcode_code_str + "&timezone=" + time_zone + "&currency=" + mySession.getValueOf(MySession.CurrencyCode));
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", user_id);
                params.put("product_id", product_id_comma);
                params.put("quantity", product_quantity_comm);
                params.put("email", email_str);
                params.put("first_name", fullname_str);
                params.put("last_name", "");
                params.put("company", "");
                params.put("phone", phone_str);
                params.put("address_1", order_address);
                params.put("address_2", streat_address);
                params.put("city", "");
                params.put("state", "");
                params.put("postcode", zipcode_code_str);
                params.put("timezone", time_zone);
                params.put("currency", mySession.getValueOf(MySession.CurrencyCode));


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
            // prgressbar.setVisibility(View.GONE);
            progresbar.setVisibility(View.GONE);
            Log.e("Place Order", ">>>>>>>>>>>>" + result);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(PlaceOrderAct.this, getResources().getString(R.string.orderplacedsucc), Toast.LENGTH_LONG).show();
                        myapisession.setKeyCartitem("");
                        AllAddedAddressAct.phonetv_str = "";
                        AllAddedAddressAct.fullname_str = "";
                        AllAddedAddressAct.address1_str = "";
                        AllAddedAddressAct.address2_str = "";
                        AllAddedAddressAct.city_str = "";
                        AllAddedAddressAct.state_str = "";
                        AllAddedAddressAct.zippcode_str = "";
                        AllAddedAddressAct.countrytv_str = "";
                        SelectPaymentMethodAct.card_id = "";
                        SelectPaymentMethodAct.card_brand = "";
                        SelectPaymentMethodAct.card_number = "";
                        finish();
                    } else {
                        Toast.makeText(PlaceOrderAct.this, getResources().getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // BaseActivity.Card_Added_Sts="Added";

            }


        }
    }


}
/*


 */