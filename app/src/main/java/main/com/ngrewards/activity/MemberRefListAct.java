package main.com.ngrewards.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.Utils.LocaleHelper;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberRefListAct extends AppCompatActivity {

    MySession mySession;
    ProgressBar progressbar;
    SwipeRefreshLayout swipeToRefresh;
    private RecyclerView commision_list;
    private RelativeLayout backlay;
    private RefAdapter commisionAdpter;
    private String user_id = "";
    private String affiliate_number = "";
    private final String stripe_account_id = "";
    private final String stripe_account_login_link = "";
    private ArrayList<Result> firstDataArrayList;
    private TextView nocommission;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_list);
        idinit();
        mySession = new MySession(this);
        String user_log_data = mySession.getKeyAlldata();

        if (user_log_data == null) {

        } else {

            try {
                Log.e("TAG", "onCreate:user_log_data " + user_log_data);
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    affiliate_number = jsonObject1.getString("affiliate_number");
                    // image_url = jsonObject1.getString("member_image");
                    Log.e("affiliate_number ", " .> " + affiliate_number);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        clickevent();

    }

    private void clickevent() {
        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCommission();


    }

    private void idinit() {


        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        nocommission = findViewById(R.id.nocommission);
        progressbar = findViewById(R.id.progressbar);
        commision_list = findViewById(R.id.commision_list);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MemberRefListAct.this, LinearLayoutManager.VERTICAL, false);

        commision_list.setLayoutManager(horizontalLayoutManagaer);
        backlay = findViewById(R.id.backlay);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMyCommission();


                // swipeToRefresh.setRefreshing(false);
            }
        });
    }

    private void getMyCommission() {

        swipeToRefresh.setRefreshing(true);
        progressbar.setVisibility(View.VISIBLE);
        firstDataArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().get_merchant_referral(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);

                if (response.isSuccessful()) {

                    try {

                        String responseData = response.body().string();

                        Log.e("affiliate_number!", affiliate_number);
                        Log.e("responseData!!!s", responseData);

                        Log.e("Get Commision Data>", " >" + responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            RefList successData = new Gson().fromJson(responseData, RefList.class);
                            if (successData.getResult() != null) {
                                firstDataArrayList.addAll(successData.getResult());
                            }
                        }

                        if (firstDataArrayList == null || firstDataArrayList.isEmpty()) {
                            nocommission.setVisibility(View.VISIBLE);
                        } else {
                            nocommission.setVisibility(View.GONE);
                        }

                        commisionAdpter = new RefAdapter(MemberRefListAct.this, firstDataArrayList);
                        commision_list.setAdapter(commisionAdpter);
                        commisionAdpter.notifyDataSetChanged();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                swipeToRefresh.setRefreshing(false);
                progressbar.setVisibility(View.GONE);

                Log.e("TAG", t.toString());
            }
        });
    }

    public static class RefList {
        @SerializedName("result")
        @Expose
        private List<Result> result;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public static class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("business_no")
        @Expose
        private String businessNo;
        @SerializedName("business_description")
        @Expose
        private String businessDescription;
        @SerializedName("keyword")
        @Expose
        private String keyword;
        @SerializedName("team_Email")
        @Expose
        private String teamEmail;
        @SerializedName("team_Password")
        @Expose
        private String teamPassword;
        @SerializedName("business_name")
        @Expose
        private String businessName;
        @SerializedName("pay_out")
        @Expose
        private String payOut;
        @SerializedName("contact_name")
        @Expose
        private String contactName;
        @SerializedName("bank_name")
        @Expose
        private String bankName;
        @SerializedName("routing_no")
        @Expose
        private String routingNo;
        @SerializedName("account_no")
        @Expose
        private String accountNo;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("registration_id")
        @Expose
        private String registrationId;
        @SerializedName("zip_code")
        @Expose
        private String zipCode;
        @SerializedName("contact_number")
        @Expose
        private String contactNumber;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("address_two")
        @Expose
        private String addressTwo;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("session_id")
        @Expose
        private String sessionId;
        @SerializedName("reward_percent")
        @Expose
        private String rewardPercent;
        @SerializedName("how_invite_you")
        @Expose
        private String howInviteYou;
        @SerializedName("affiliate")
        @Expose
        private String affiliate;
        @SerializedName("recurring_payments")
        @Expose
        private String recurringPayments;
        @SerializedName("reward_percentage")
        @Expose
        private String rewardPercentage;
        @SerializedName("sales_agent_key")
        @Expose
        private String salesAgentKey;
        @SerializedName("sales_agent_no")
        @Expose
        private String salesAgentNo;
        @SerializedName("sales_agent_name")
        @Expose
        private String salesAgentName;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("website_url")
        @Expose
        private String websiteUrl;
        @SerializedName("facebook_url")
        @Expose
        private String facebookUrl;
        @SerializedName("twitter_url")
        @Expose
        private String twitterUrl;
        @SerializedName("linkdin_url")
        @Expose
        private String linkdinUrl;
        @SerializedName("google_plus_url")
        @Expose
        private String googlePlusUrl;
        @SerializedName("instagram_url")
        @Expose
        private String instagramUrl;
        @SerializedName("youtube_url")
        @Expose
        private String youtubeUrl;
        @SerializedName("business_category")
        @Expose
        private String businessCategory;
        @SerializedName("merchant_image")
        @Expose
        private String merchantImage;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("social_id")
        @Expose
        private String socialId;
        @SerializedName("touch_status")
        @Expose
        private String touchStatus;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("ios_device_token")
        @Expose
        private String iosDeviceToken;
        @SerializedName("last_login")
        @Expose
        private String lastLogin;
        @SerializedName("share_link")
        @Expose
        private String shareLink;
        @SerializedName("avg_rate")
        @Expose
        private String avgRate;
        @SerializedName("like_count")
        @Expose
        private String likeCount;
        @SerializedName("stripe_account_id")
        @Expose
        private String stripeAccountId;
        @SerializedName("stripe_account_login_link")
        @Expose
        private String stripeAccountLoginLink;
        @SerializedName("stripe_account_email")
        @Expose
        private String stripeAccountEmail;
        @SerializedName("merchant_date")
        @Expose
        private String merchantDate;
        @SerializedName("delete_status")
        @Expose
        private String deleteStatus;
        @SerializedName("merchant_qrcode_image")
        @Expose
        private String merchantQrcodeImage;
        @SerializedName("employee_name")
        @Expose
        private String employeeName;
        @SerializedName("employee_id")
        @Expose
        private String employeeId;
        @SerializedName("Isoffline")
        @Expose
        private String isoffline;
        @SerializedName("deactivation_code")
        @Expose
        private String deactivationCode;
        @SerializedName("tax_form")
        @Expose
        private String taxForm;
        @SerializedName("admin_created_password")
        @Expose
        private String adminCreatedPassword;
        @SerializedName("sell_items_reomve_access")
        @Expose
        private String sellItemsReomveAccess;
        @SerializedName("profile_status")
        @Expose
        private String profileStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBusinessNo() {
            return businessNo;
        }

        public void setBusinessNo(String businessNo) {
            this.businessNo = businessNo;
        }

        public String getBusinessDescription() {
            return businessDescription;
        }

        public void setBusinessDescription(String businessDescription) {
            this.businessDescription = businessDescription;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getTeamEmail() {
            return teamEmail;
        }

        public void setTeamEmail(String teamEmail) {
            this.teamEmail = teamEmail;
        }

        public String getTeamPassword() {
            return teamPassword;
        }

        public void setTeamPassword(String teamPassword) {
            this.teamPassword = teamPassword;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getPayOut() {
            return payOut;
        }

        public void setPayOut(String payOut) {
            this.payOut = payOut;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getRoutingNo() {
            return routingNo;
        }

        public void setRoutingNo(String routingNo) {
            this.routingNo = routingNo;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getRegistrationId() {
            return registrationId;
        }

        public void setRegistrationId(String registrationId) {
            this.registrationId = registrationId;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressTwo() {
            return addressTwo;
        }

        public void setAddressTwo(String addressTwo) {
            this.addressTwo = addressTwo;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getRewardPercent() {
            return rewardPercent;
        }

        public void setRewardPercent(String rewardPercent) {
            this.rewardPercent = rewardPercent;
        }

        public String getHowInviteYou() {
            return howInviteYou;
        }

        public void setHowInviteYou(String howInviteYou) {
            this.howInviteYou = howInviteYou;
        }

        public String getAffiliate() {
            return affiliate;
        }

        public void setAffiliate(String affiliate) {
            this.affiliate = affiliate;
        }

        public String getRecurringPayments() {
            return recurringPayments;
        }

        public void setRecurringPayments(String recurringPayments) {
            this.recurringPayments = recurringPayments;
        }

        public String getRewardPercentage() {
            return rewardPercentage;
        }

        public void setRewardPercentage(String rewardPercentage) {
            this.rewardPercentage = rewardPercentage;
        }

        public String getSalesAgentKey() {
            return salesAgentKey;
        }

        public void setSalesAgentKey(String salesAgentKey) {
            this.salesAgentKey = salesAgentKey;
        }

        public String getSalesAgentNo() {
            return salesAgentNo;
        }

        public void setSalesAgentNo(String salesAgentNo) {
            this.salesAgentNo = salesAgentNo;
        }

        public String getSalesAgentName() {
            return salesAgentName;
        }

        public void setSalesAgentName(String salesAgentName) {
            this.salesAgentName = salesAgentName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public String getFacebookUrl() {
            return facebookUrl;
        }

        public void setFacebookUrl(String facebookUrl) {
            this.facebookUrl = facebookUrl;
        }

        public String getTwitterUrl() {
            return twitterUrl;
        }

        public void setTwitterUrl(String twitterUrl) {
            this.twitterUrl = twitterUrl;
        }

        public String getLinkdinUrl() {
            return linkdinUrl;
        }

        public void setLinkdinUrl(String linkdinUrl) {
            this.linkdinUrl = linkdinUrl;
        }

        public String getGooglePlusUrl() {
            return googlePlusUrl;
        }

        public void setGooglePlusUrl(String googlePlusUrl) {
            this.googlePlusUrl = googlePlusUrl;
        }

        public String getInstagramUrl() {
            return instagramUrl;
        }

        public void setInstagramUrl(String instagramUrl) {
            this.instagramUrl = instagramUrl;
        }

        public String getYoutubeUrl() {
            return youtubeUrl;
        }

        public void setYoutubeUrl(String youtubeUrl) {
            this.youtubeUrl = youtubeUrl;
        }

        public String getBusinessCategory() {
            return businessCategory;
        }

        public void setBusinessCategory(String businessCategory) {
            this.businessCategory = businessCategory;
        }

        public String getMerchantImage() {
            return merchantImage;
        }

        public void setMerchantImage(String merchantImage) {
            this.merchantImage = merchantImage;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getTouchStatus() {
            return touchStatus;
        }

        public void setTouchStatus(String touchStatus) {
            this.touchStatus = touchStatus;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getIosDeviceToken() {
            return iosDeviceToken;
        }

        public void setIosDeviceToken(String iosDeviceToken) {
            this.iosDeviceToken = iosDeviceToken;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getShareLink() {
            return shareLink;
        }

        public void setShareLink(String shareLink) {
            this.shareLink = shareLink;
        }

        public String getAvgRate() {
            return avgRate;
        }

        public void setAvgRate(String avgRate) {
            this.avgRate = avgRate;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getStripeAccountId() {
            return stripeAccountId;
        }

        public void setStripeAccountId(String stripeAccountId) {
            this.stripeAccountId = stripeAccountId;
        }

        public String getStripeAccountLoginLink() {
            return stripeAccountLoginLink;
        }

        public void setStripeAccountLoginLink(String stripeAccountLoginLink) {
            this.stripeAccountLoginLink = stripeAccountLoginLink;
        }

        public String getStripeAccountEmail() {
            return stripeAccountEmail;
        }

        public void setStripeAccountEmail(String stripeAccountEmail) {
            this.stripeAccountEmail = stripeAccountEmail;
        }

        public String getMerchantDate() {
            return merchantDate;
        }

        public void setMerchantDate(String merchantDate) {
            this.merchantDate = merchantDate;
        }

        public String getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(String deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getMerchantQrcodeImage() {
            return merchantQrcodeImage;
        }

        public void setMerchantQrcodeImage(String merchantQrcodeImage) {
            this.merchantQrcodeImage = merchantQrcodeImage;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getIsoffline() {
            return isoffline;
        }

        public void setIsoffline(String isoffline) {
            this.isoffline = isoffline;
        }

        public String getDeactivationCode() {
            return deactivationCode;
        }

        public void setDeactivationCode(String deactivationCode) {
            this.deactivationCode = deactivationCode;
        }

        public String getTaxForm() {
            return taxForm;
        }

        public void setTaxForm(String taxForm) {
            this.taxForm = taxForm;
        }

        public String getAdminCreatedPassword() {
            return adminCreatedPassword;
        }

        public void setAdminCreatedPassword(String adminCreatedPassword) {
            this.adminCreatedPassword = adminCreatedPassword;
        }

        public String getSellItemsReomveAccess() {
            return sellItemsReomveAccess;
        }

        public void setSellItemsReomveAccess(String sellItemsReomveAccess) {
            this.sellItemsReomveAccess = sellItemsReomveAccess;
        }

        public String getProfileStatus() {
            return profileStatus;
        }

        public void setProfileStatus(String profileStatus) {
            this.profileStatus = profileStatus;
        }

    }

    public class RefAdapter extends RecyclerView.Adapter<RefAdapter.MyViewHolder> {
        Context context;
        ArrayList<Result> firstDataArrayList;

        public RefAdapter(Activity myContacts, ArrayList<Result> firstDataArrayList) {
            this.context = myContacts;
            this.firstDataArrayList = firstDataArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_ref_lay, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);

            return holder;
            // return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.weeknumber_tv.setText("Email : " + firstDataArrayList.get(position).getTeamEmail());
            holder.week_date_range.setText("Business Name : " + firstDataArrayList.get(position).getBusinessName());
            Glide.with(context).load(firstDataArrayList.get(position).getMerchantImage()).into(holder.weekearning_amount);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* commissionDataArrayList =   firstDataArrayList.get(position).getData();
                    Intent i = new Intent(MemberRefListAct.this, CommissionDetail.class);
                    i.putExtra("week_str",firstDataArrayList.get(position).getWeek());
                    i.putExtra("stripe_account_id",stripe_account_id);
                    i.putExtra("week_year_str",firstDataArrayList.get(position).getYear());
                    i.putExtra("week_start_month_str",firstDataArrayList.get(position).getData().get(0).getWeekStart());
                    i.putExtra("week_end_month_str",firstDataArrayList.get(position).getData().get(0).getWeekEnd());
                    i.putExtra("total_week_count","53");
                    i.putExtra("total_week_commision_str",""+firstDataArrayList.get(position).getWeekComission());
                    i.putExtra("availabel_week_withdraw_status",""+firstDataArrayList.get(position).getAvailabel_week_withdraw_status());
                    i.putExtra("withdraw_status",""+firstDataArrayList.get(position).getWithdraw_status());
                    startActivity(i);*/
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 6;
            return firstDataArrayList == null ? 0 : firstDataArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView weeknumber_tv, week_date_range;
            CircleImageView weekearning_amount;

            public MyViewHolder(View view) {
                super(view);
                weeknumber_tv = itemView.findViewById(R.id.weeknumber_tv);
                week_date_range = itemView.findViewById(R.id.week_date_range);
                weekearning_amount = itemView.findViewById(R.id.weekearning_amount);
            }
        }
    }
}


