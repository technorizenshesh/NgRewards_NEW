package main.com.ngrewards.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.ngrewards.R;
import main.com.ngrewards.activity.MemberTransfer;
import main.com.ngrewards.activity.PreferenceConnector;
import main.com.ngrewards.activity.SliderActivity;
import main.com.ngrewards.beanclasses.MemberBean;
import main.com.ngrewards.beanclasses.MemberDetail;
import main.com.ngrewards.constant.MySession;
import main.com.ngrewards.constant.Myapisession;
import main.com.ngrewards.restapi.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sagar technorizen on 13/6/18.
 */

public class WhoInvitedFrag extends Fragment {
    private static final int REQUEST_CODE_QR_SCAN = 3;
    public static AutoCompleteTextView whoinvite;
    public static String who_invite_id;
    int count = 0;
    View v;
    private CircleImageView picked_user_img;
    private ImageView checkic, qrcode;
    private ProgressBar progresbar;
    private ArrayList<MemberDetail> memberDetailArrayList;
    private Myapisession myapisession;
    private final boolean sts = false;
    private  String user_id="",country_id="";
    private MySession mySession;

    public WhoInvitedFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_whoinvite, container, false);
        idinti();
        myapisession = new Myapisession(getActivity());
        getUsername();

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });

        return v;
    }

    private void idinti() {
        checkic = v.findViewById(R.id.checkic);
        qrcode = v.findViewById(R.id.qrcode);
        progresbar = v.findViewById(R.id.progresbar);
        whoinvite = v.findViewById(R.id.whoinvite);
        picked_user_img = v.findViewById(R.id.picked_user_img);

        whoinvite.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (count == 0) {
                    Log.e("FIRST", "KK");
                    ArrayList<MemberDetail> l1 = new ArrayList<>();
                    if (s == null) {

                    } else {
                        MemberDetail memberlist = new MemberDetail();
                        memberlist.setAffiliateName(s.toString());
                        l1.add(memberlist);

                        GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(getActivity(), l1, "", "");
                        whoinvite.setAdapter(ga);
                        ga.notifyDataSetChanged();

                    }
                }
                count++;
            }
        });

        mySession = new MySession(requireActivity());
        String user_log_data = mySession.getKeyAlldata();
        if (user_log_data == null) {

        } else {

            try {
                JSONObject jsonObject = new JSONObject(user_log_data);
                String message = jsonObject.getString("status");
                if (message.equalsIgnoreCase("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    user_id = jsonObject1.getString("id");
                    country_id = jsonObject1.getString("country_id");
                    Log.e("country_id >>", " >" + country_id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("memberDetailArrayList", "> " + memberDetailArrayList.size());
            if (SliderActivity.member_bitmap != null) {
                picked_user_img.setImageBitmap(SliderActivity.member_bitmap);
            }
        }
    }

    private void getUsername() {
        Log.e("User name list>", " >GET NAME");
        progresbar.setVisibility(View.VISIBLE);
        memberDetailArrayList = new ArrayList<>();
        Call<ResponseBody> call = ApiClient.getApiInterface().getMembersusername(user_id,mySession.getValueOf(MySession.CountryId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progresbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("User name list>", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            myapisession.setKeyMemberusername(responseData);
                            MemberBean successData = new Gson().fromJson(responseData, MemberBean.class);
                            memberDetailArrayList.addAll(successData.getResult());
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

            if (requestCode == 3) {
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                try {

                    String[] arr = result.split(",");
                    whoinvite.setText(arr[1]);
                    who_invite_id = arr[3];

                    Log.e("result123", result);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Wrong QR Code!!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private final Activity context;
        private ArrayList<MemberDetail> l2 = new ArrayList<>();
        private final LayoutInflater layoutInflater;

        public GeoAutoCompleteAdapter(Activity context, ArrayList<MemberDetail> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            layoutInflater = LayoutInflater.from(context);

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
                geo_search_result_text.setText(l2.get(i).getAffiliateName());
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        whoinvite.setText(l2.get(i).getAffiliateName());



                        who_invite_id = l2.get(i).getAffiliateNumber();

                        PreferenceConnector.writeString(getContext(), PreferenceConnector.Who_invited, who_invite_id);
                        whoinvite.dismissDropDown();
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();
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
                        if (constraint.length() == 0) {
                        } else {
                            l2.clear();
                            for (MemberDetail wp : memberDetailArrayList) {
                                if (wp.getAffiliateName().toLowerCase().startsWith((String) constraint))//.toLowerCase(Locale.getDefault())
                                {
                                    Log.e("WHO_INVITE ", " >> FILTER >>>>>   " + wp.getAffiliateName());
                                    l2.add(wp);
                                }
                            }
                        }
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count != 0) {
                        l2 = (ArrayList<MemberDetail>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}