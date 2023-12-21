package main.com.ngrewards.fragments;

import static main.com.ngrewards.androidmigx.MainTabActivity.DEEP_LINK_URL;
import static main.com.ngrewards.constant.MySession.KEY_LANGUAGE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.Utils.Tools;
import main.com.ngrewards.activity.MerchantDetailAct;
import main.com.ngrewards.activity.SplashActivity;
import main.com.ngrewards.beanclasses.CategoryBean;
import main.com.ngrewards.beanclasses.CategoryBeanList;
import main.com.ngrewards.beanclasses.OfferBeanList;
import main.com.ngrewards.beanclasses.Offerbean;
import main.com.ngrewards.constant.BaseUrl;
import main.com.ngrewards.constant.GPSTracker;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by technorizen on 14/6/18.
 */

public class OffersFrag extends Fragment {
    View v;
    ArrayList<Boolean> selecteded;
    OffersAdpter offersAdpter;
    SwipeRefreshLayout swipeToRefresh;
    ArrayList<CategoryBeanList> categoryBeanListArrayList=new ArrayList<>();
    GPSTracker gpsTracker;
    private ListView item_product;
    private RecyclerView offers_product_rec;
    private ProgressBar progresbar;
    private ArrayList<OfferBeanList> offerBeanListArrayList=new ArrayList<>();
    private MySession mySession;
    private Myapisession myapisession;
    private String user_id = "", fill_category_id = "", rating_filter_str = "", distance_filter_str = "", fill_category_id_loc = "", like_filter_str = "";
    private TextView filter_tv, nooffertv;
    private int current_offer_pos;
    private EditText search_et_home;
    private DistanceAdapter distanceAdapter;
    private ArrayList<String> distance_filter_list;
    private double latitude = 0, longitude = 0;
    private String result;

    @SuppressLint("ValidFragment")
    public OffersFrag(String result) {
        this.result = result;
    }

    public OffersFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void attachBaseContext(Context base) {
        super.onAttach(LocaleHelper.onAttach(base));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Tools.reupdateResources(requireActivity());
        v = inflater.inflate(R.layout.offers_frag_lay, container, false);
        mySession = new MySession(getActivity());
        myapisession = new Myapisession(getActivity());
        distance_filter_list = new ArrayList<>();
        distance_filter_list.add(getString(R.string.any_distance));
        distance_filter_list.add("5.0");
        distance_filter_list.add("10.0");
        distance_filter_list.add("20.0");
        distance_filter_list.add("50.0");
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        selecteded = new ArrayList<>();
        selecteded.add(false);
        selecteded.add(false);
        checkGps();
        idinit();
        clickevent();
        getOffers();
        return v;
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

    private void clickevent() {
        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterlay();
            }
        });

    }

    private void idinit() {
        search_et_home = getActivity().findViewById(R.id.search_et_home);
        offers_product_rec = v.findViewById(R.id.offers_product_rec);
        nooffertv = v.findViewById(R.id.nooffertv);
        filter_tv = v.findViewById(R.id.filter_tv);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        progresbar = v.findViewById(R.id.progresbar);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        offers_product_rec.setLayoutManager(horizontalLayoutManagaer);
        item_product = v.findViewById(R.id.item_product);
       /* customItemPro = new CustomItemPro(getActivity());
        item_product.setAdapter(customItemPro);*/
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOffers();
                // swipeToRefresh.setRefreshing(false);
            }
        });

        search_et_home.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s == null) {

                } else {

                    if (s.toString().length() > 0) {
                        //  clear_text.setVisibility(View.VISIBLE);
                        if (offersAdpter == null) {

                        } else {
                            offersAdpter.filter(s.toString());
                        }

                    }/**/ else {
                        // clear_text.setVisibility(View.GONE);
                        if (offersAdpter == null) {

                        } else {
                            offersAdpter.filter("");
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_et_home.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  customItemPro = new CustomItemPro(getActivity());
        item_product.setAdapter(customItemPro);*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    public void shortenLongLink(String link) {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(link))
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Log.d("TAG", "onComplete: " + shortLink);

                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);

                            shareIntent.putExtra(Intent.EXTRA_TEXT, shortLink + "");
                            shareIntent.setType("text/plain");
                            // Launch sharing dialog for image
                            startActivity(shareIntent);

                        } else {

                            Log.d("TAG", "onComplete: Error" + task.getException());
                            // Error
                            // ...
                        }
                    }
                });


    }

    public void likedislikeoffer_fun(String id) {

        progresbar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = ApiClient.getApiInterface().likedislikeoffer(id, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("My Offer Products >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            if (object.getString("result").equalsIgnoreCase("Offer Unlike Successfully")) {
                                OfferBeanList offerBeanList = new OfferBeanList();
                                offerBeanList.setLikeStatus("dislike");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);
                                int like = Integer.parseInt(offerBeanListArrayList.get(current_offer_pos).getLikeCount());
                                if (like > 0) {
                                    int likett = like - 1;
                                    offerBeanListArrayList.get(current_offer_pos).setLikeCount(likett + "");
                                }
                                offerBeanListArrayList.get(current_offer_pos).setLikeStatus("dislike");
                            } else {
                                OfferBeanList offerBeanList = new OfferBeanList();
                                offerBeanList.setLikeStatus("like");
                                //offerBeanListArrayList.set(current_offer_pos, offerBeanList);

                                offerBeanListArrayList.get(current_offer_pos).setLikeStatus("like");
                                int like = Integer.parseInt(offerBeanListArrayList.get(current_offer_pos).getLikeCount());
                                int likett = like + 1;
                                offerBeanListArrayList.get(current_offer_pos).setLikeCount(likett + "");

                            }
                            offersAdpter = new OffersAdpter(offerBeanListArrayList, requireActivity());
                            offers_product_rec.setAdapter(offersAdpter);
                            offersAdpter.notifyDataSetChanged();
                            offers_product_rec.scrollToPosition(current_offer_pos);
                        }


                    } catch (Exception e) {
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

    private void getOffers() {


        swipeToRefresh.setRefreshing(true);
        // progresbar.setVisibility(View.VISIBLE);
        offerBeanListArrayList = new ArrayList<>();
        Log.e("params!!! >>", " >> user_id =" + user_id + "  fill_category_id>" + fill_category_id + " rr>>" + rating_filter_str + " like>> " + like_filter_str + " dis>> " + distance_filter_str + " lat>>" + latitude + " lon>>" + longitude);
        Call<ResponseBody> call = ApiClient.getApiInterface().getMemberOffer(user_id, fill_category_id, rating_filter_str, like_filter_str, distance_filter_str, "" + latitude, "" + longitude);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // progresbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("response112232", responseData);
                        if (object.getString("status").equals("1")) {
                            Offerbean successData = new Gson().fromJson(responseData, Offerbean.class);
                            offerBeanListArrayList.addAll(successData.getResult());
                        }
                        if (offerBeanListArrayList == null || offerBeanListArrayList.isEmpty() || offerBeanListArrayList.size() == 0) {
                            nooffertv.setVisibility(View.VISIBLE);
                        } else {
                            nooffertv.setVisibility(View.GONE);
                        }

                        offersAdpter = new OffersAdpter(offerBeanListArrayList, requireActivity());
                        offers_product_rec.setAdapter(offersAdpter);

                        if (!result.equalsIgnoreCase("")) {
                            gotoOffer();
                        }

                        offersAdpter.notifyDataSetChanged();

                    } catch (Exception e) {
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

    public void gotoOffer() {
        OfferBeanList offerItem = null;
        for (OfferBeanList offerMyItem : offerBeanListArrayList) {
            if (result.equalsIgnoreCase(offerMyItem.getMerchant_id())) {
                offerItem = offerMyItem;
            }
        }

        if (offerItem != null) {
            result="";
            Intent i = new Intent(getActivity(), MerchantDetailAct.class);
            i.putExtra("user_id", user_id + "demo");
            i.putExtra("merchant_contact_name", offerItem.getContact_name());
            i.putExtra("merchant_id", offerItem.getMerchant_id());
            i.putExtra("merchant_name", offerItem.getBusiness_name());
            i.putExtra("merchant_number", offerItem.getBusiness_no());
            i.putExtra("merchant_img", offerItem.getMerchant_image());
            startActivity(i);
        }

    }

    private void filterlay() {

        CategoryAdpters categoryAdpters;
        final Dialog dialogSts = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.filter_nearby_lay);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.back_pop_col)));
        //dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView reset_tv = dialogSts.findViewById(R.id.reset_tv);
        TextView filter_tv = dialogSts.findViewById(R.id.filter_tv);
        final RadioButton lowtohigh = dialogSts.findViewById(R.id.lowtohigh);
        final RadioButton hightolow = dialogSts.findViewById(R.id.hightolow);
        final RatingBar rating = dialogSts.findViewById(R.id.rating);
        Spinner distance_spinner = dialogSts.findViewById(R.id.distance_spinner);
        distanceAdapter = new DistanceAdapter(getActivity(), distance_filter_list);
        distance_spinner.setAdapter(distanceAdapter);
        Spinner category_spinner = dialogSts.findViewById(R.id.category_spinner);
        distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (distance_filter_list != null && !distance_filter_list.isEmpty()) {
                    if (distance_filter_list.get(position).equalsIgnoreCase(getString(R.string.any_distance))) {
                        distance_filter_str = "";
                    } else {
                        distance_filter_str = distance_filter_list.get(position);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryBeanListArrayList != null && !categoryBeanListArrayList.isEmpty()) {
                    if (categoryBeanListArrayList.get(position).getCategoryId().equalsIgnoreCase("0")) {
                        fill_category_id_loc = "";
                    } else {
                        fill_category_id_loc = categoryBeanListArrayList.get(position).getCategoryId();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (myapisession.getKeyOffercate() == null || myapisession.getKeyOffercate().equalsIgnoreCase("")) {
            getOfferCategory();
        } else {
            try {
                categoryBeanListArrayList = new ArrayList<>();
                CategoryBeanList categoryBeanList = new CategoryBeanList();
                categoryBeanList.setCategoryId("0");
                categoryBeanList.setCategoryName(getString(R.string.selectcat));
                categoryBeanList.setCategory_name_spanish(getString(R.string.selectcat));
                categoryBeanList.setCategory_name_hindi(getString(R.string.selectcat));
                categoryBeanListArrayList.add(categoryBeanList);
                JSONObject object = new JSONObject(myapisession.getKeyOffercate());
                Log.e("Offer Category >", " >" + myapisession.getKeyOffercate());
                if (object.getString("status").equals("1")) {

                    CategoryBean successData = new Gson().fromJson(myapisession.getKeyOffercate(), CategoryBean.class);
                    categoryBeanListArrayList.addAll(successData.getResult());

                }

                categoryAdpters = new CategoryAdpters(getActivity(), categoryBeanListArrayList);
                category_spinner.setAdapter(categoryAdpters);
                //  Log.e("category_id >>","dddd "+category_id);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double rat = rating.getRating();
                if (rat == 0) {
                    rating_filter_str = "";
                } else {
                    rating_filter_str = String.valueOf(rat);
                }
                if (hightolow.isChecked()) {
                    like_filter_str = "HighToLow";
                }
                if (lowtohigh.isChecked()) {
                    like_filter_str = "LowToHigh";
                }
                fill_category_id = fill_category_id_loc;
                Log.e("Filter Cat Id", " >" + fill_category_id);
                if (fill_category_id != null && !fill_category_id.equalsIgnoreCase("") && !fill_category_id.equalsIgnoreCase("0")) {
                    getOffers();
                } else if (rating_filter_str != null && !rating_filter_str.equalsIgnoreCase("")) {
                    getOffers();
                } else if (like_filter_str != null && !like_filter_str.equalsIgnoreCase("")) {
                    getOffers();
                } else if (distance_filter_str != null && !distance_filter_str.equalsIgnoreCase("")) {
                    getOffers();
                }
                dialogSts.dismiss();
            }
        });
        reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fill_category_id != null && !fill_category_id.equalsIgnoreCase("") && !fill_category_id.equalsIgnoreCase("0")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    getOffers();
                } else if (rating_filter_str != null && !rating_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    distance_filter_str = "";
                    getOffers();
                } else if (distance_filter_str != null && !distance_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    distance_filter_str = "";
                    getOffers();
                } else if (like_filter_str != null && !like_filter_str.equalsIgnoreCase("")) {
                    fill_category_id = "";
                    fill_category_id_loc = "";
                    rating_filter_str = "";
                    like_filter_str = "";
                    distance_filter_str = "";
                    getOffers();
                }
                dialogSts.dismiss();
            }
        });
        dialogSts.show();
    }

    private void getOfferCategory() {
//http://testing.bigclicki.com/webservice/loginapp?email=0&password=0
        categoryBeanListArrayList = new ArrayList<>();
        CategoryBeanList categoryBeanList = new CategoryBeanList();
        categoryBeanList.setCategoryId("0");
        categoryBeanList.setCategoryName(getString(R.string.selectcat));
        categoryBeanList.setCategory_name_spanish(getString(R.string.selectcat));
        categoryBeanList.setCategory_name_hindi(getString(R.string.selectcat));
        categoryBeanListArrayList.add(categoryBeanList);
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

                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
                            categoryBeanListArrayList.addAll(successData.getResult());

                        }


                    } catch (Exception e) {
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


    public class DistanceAdapter extends BaseAdapter {
        private final ArrayList<String> distancelist;
        Context context;
        LayoutInflater inflter;

        public DistanceAdapter(Context applicationContext, ArrayList<String> distancelist) {
            this.context = applicationContext;
            this.distancelist = distancelist;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return distancelist == null ? 0 : distancelist.size();
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
            if (i == 0) {
                names.setText(getString(R.string.any_distance));
            } else {
                names.setText(getString(R.string.within) + distancelist.get(i) + getString(R.string.mi));
            }

            return view;
        }
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


    public class OffersAdpter extends RecyclerView.Adapter<OffersAdpter.MyViewHolder> {
        ArrayList<OfferBeanList> offerBeanLists;
        ArrayList<OfferBeanList> searchofferBeanLists;
        MySession mySession;
        Context context;

        public OffersAdpter(ArrayList<OfferBeanList> offerBeanLists, Context context) {
            this.context = context;
            this.offerBeanLists = offerBeanLists;
            this.searchofferBeanLists = new ArrayList<>();
            searchofferBeanLists.addAll(offerBeanLists);
        }

        public void filter(String charText) {
            if (charText == null) {
            } else {
                charText = charText.toLowerCase();
                offerBeanLists.clear();
                if (charText.length() == 0) {
                    offerBeanLists.addAll(searchofferBeanLists);
                } else {
                    for (OfferBeanList wp : searchofferBeanLists) {
                        if (wp.getOfferName().toLowerCase().startsWith(charText))//.toLowerCase(Locale.getDefault())
                        {
                            offerBeanLists.add(wp);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_pro_offers_lay, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
            mySession = new MySession(context);
            Log.e("TAG", "onBindViewHolder:  mySession.getValueOf(MySession.CurrencySign) " + mySession.getValueOf(MySession.CurrencySign));
            if (offerBeanLists.get(listPosition).getOffer_discount_price() == null) {
                holder.discounts.setVisibility(View.GONE);
            } else {
                if (offerBeanLists.get(listPosition).getOffer_discount_price() != null && !offerBeanLists.get(listPosition)
                        .getOffer_discount_price().equalsIgnoreCase("")
                        && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("0")) {
                    holder.discounts.setVisibility(View.VISIBLE);
                    holder.pricediscount.setText(String.format("%s%s", mySession.getValueOf(MySession.CurrencySign), offerBeanLists.get(listPosition).getOffer_discount_price().trim()));
                    double offresp = Double.parseDouble(offerBeanLists.get(listPosition).getOfferDiscount());
                    int ddd = (int) offresp;
                    holder.discounts.setText("" + ddd + "% OFF");
                    // holder.discounts.setText("(" + offerBeanLists.get(listPosition).getOfferDiscount() + "% OFF)");
                } else {
                    holder.discounts.setVisibility(View.GONE);
                }
            }
            holder.merchant_name.setText("" + offerBeanLists.get(listPosition).getBusiness_name());
            holder.distance_tv.setText("" + offerBeanLists.get(listPosition).getDistance() + getString(R.string.mi));
            holder.offername.setText("" + offerBeanLists.get(listPosition).getOfferName());
            holder.offer_desc.setText("" + offerBeanLists.get(listPosition).getOfferDescription());
            holder.likecount.setText("" + offerBeanLists.get(listPosition).getLikeCount());
            if (offerBeanLists.get(listPosition).getOfferPrice() == null) {
            } else {
                if (offerBeanLists.get(listPosition).getOffer_discount_price() != null && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("") && !offerBeanLists.get(listPosition).getOffer_discount_price().equalsIgnoreCase("0")) {
                    holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanLists.get(listPosition).getOfferPrice().trim());
                    holder.real_price.setTextColor(getResources().getColor(R.color.back_pop_col));
                    holder.real_price.setPaintFlags(holder.real_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.real_price.setText(mySession.getValueOf(MySession.CurrencySign) + offerBeanLists.get(listPosition).getOfferPrice().trim());
                }
            }
            String product_img = offerBeanLists.get(listPosition).getOfferImage();
            if (product_img == null || product_img.equalsIgnoreCase("") || product_img.equalsIgnoreCase(BaseUrl.image_baseurl)) {
            } else {
                Glide.with(getActivity()).load(product_img).placeholder(R.drawable.placeholder).into(holder.offerimage);
            }
            if (offerBeanLists.get(listPosition).getLikeStatus().equalsIgnoreCase("like")) {
                holder.likeimg.setImageResource(R.drawable.filled_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
                //holder.liketv.setText("" + getResources().getString(R.string.dislike));
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);
                holder.liketv.setText("" + getResources().getString(R.string.like));
            }

            holder.sharelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get access to the URI for the bitmap
                    try {
                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                .setLink(Uri.parse("https://www.ngrewards.com/data/Offer?" + offerBeanLists.get(listPosition).getMerchant_id()))
                                .setDomainUriPrefix("https://ngtechn.page.link")
                                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                                        .setFallbackUrl(Uri.parse(DEEP_LINK_URL)
                                                .normalizeScheme()).build())
                                .setIosParameters(new DynamicLink.IosParameters.Builder("com.ios.ngreward")
                                        .setFallbackUrl(Uri.parse(DEEP_LINK_URL)).build())
                                .buildDynamicLink();
                        Uri dynamicLinkUri = dynamicLink.getUri();
                        Log.d("TAG", "onCreate: " + dynamicLinkUri);

                        shortenLongLink(dynamicLinkUri.toString());

                    } catch (Exception e) {
                        Log.e("EXC", " > " + e.getMessage());
                        e.printStackTrace();
                    }


                }
            });


            holder.likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_offer_pos = listPosition;
                    likedislikeoffer_fun(offerBeanLists.get(listPosition).getId());
                }

            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MerchantDetailAct.class);
                    i.putExtra("user_id", user_id);
                    i.putExtra("merchant_contact_name", offerBeanLists.get(listPosition).getContact_name());
                    i.putExtra("merchant_id", offerBeanLists.get(listPosition).getMerchant_id());
                    i.putExtra("merchant_name", offerBeanLists.get(listPosition).getBusiness_name());
                    i.putExtra("merchant_number", offerBeanLists.get(listPosition).getBusiness_no());
                    i.putExtra("merchant_img", offerBeanLists.get(listPosition).getMerchant_image());
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            //return 2;
            return offerBeanLists == null ? 0 : offerBeanLists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView liketv, distance_tv, offername, merchant_name, offer_desc, pricediscount, likecount, discounts, real_price;
            ImageView likeimg, offerimage;
            LinearLayout sharelay, likebut;

            public MyViewHolder(View itemView) {
                super(itemView);
                // this.viewLine = (View) itemView.findViewById(R.id.viewLine);
                this.offerimage = itemView.findViewById(R.id.offerimage);
                this.likecount = itemView.findViewById(R.id.likecount);
                this.pricediscount = itemView.findViewById(R.id.pricediscount);
                this.discounts = itemView.findViewById(R.id.discounts);
                this.offername = itemView.findViewById(R.id.offername);
                this.offer_desc = itemView.findViewById(R.id.offer_desc);
                this.sharelay = itemView.findViewById(R.id.sharelay);
                this.likebut = itemView.findViewById(R.id.likebut);
                this.likeimg = itemView.findViewById(R.id.likeimg);
                this.liketv = itemView.findViewById(R.id.liketv);
                this.real_price = itemView.findViewById(R.id.real_price);
                this.merchant_name = itemView.findViewById(R.id.merchant_name);
                this.distance_tv = itemView.findViewById(R.id.distance_tv);
            }
        }
    }


}
