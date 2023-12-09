package main.com.ngrewards.marchant.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import main.com.ngrewards.R;
import main.com.ngrewards.beanclasses.CityLoc;
import main.com.ngrewards.beanclasses.State;
import main.com.ngrewards.constant.ExpandableHeightListView;
import main.com.ngrewards.marchant.merchantbottum.MerStatusAct;

/**
 * Created by technorizen on 21/6/18.
 */

public class FragMerAudience extends Fragment {

    private TextView menlay, alltv, womentv, citytv, state, menpercant, womenpercant;
    private View mainview, allview, womenview, cityview, stateview, v;
    private SwipeRefreshLayout swipeToRefresh;
    private ExpandableHeightListView locationlist;
    private CustomLocationAdp customLocationAdp;
    private ProgressBar range1progress, range2progress, range3progress, range4progress, range5progress, range6progress;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                    menpercant.setText("" + MerStatusAct.salesAudienceBeanArrayList.get(0).getMalePercent() + " %");
                    womenpercant.setText("" + MerStatusAct.salesAudienceBeanArrayList.get(0).getMalePercent() + " %");

                }
                if (mainview.getVisibility() == View.VISIBLE) {
                    if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                        range1progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange1PercentMale());
                        range2progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange2PercentMale());
                        range3progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange3PercentMale());
                        range4progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange4PercentMale());
                        range5progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange5PercentMale());
                        range6progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange6PercentMale());

                    }

                } else if (womenview.getVisibility() == View.VISIBLE) {
                    if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                        range1progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange1PercentFemale());
                        range2progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange2PercentFemale());
                        range3progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange3PercentFemale());
                        range4progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange4PercentFemale());
                        range5progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange5PercentFemale());
                        range6progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange6PercentFemale());

                    }

                } else {
                    if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                        range1progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange1PercentAll());
                        range2progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange2PercentAll());
                        range3progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange3PercentAll());
                        range4progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange4PercentAll());
                        range5progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange5PercentAll());
                        range6progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange6PercentAll());

                    }

                }

                if (cityview.getVisibility() == View.VISIBLE) {
                    if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                        customLocationAdp = new CustomLocationAdp(getActivity(), MerStatusAct.salesAudienceBeanArrayList.get(0).getCity());
                        locationlist.setAdapter(customLocationAdp);
                        customLocationAdp.notifyDataSetChanged();
                    }

                } else {
                    if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                        customLocationAdp = new CustomLocationAdp(getActivity(), MerStatusAct.salesAudienceBeanArrayList.get(0).getState(), "state");
                        locationlist.setAdapter(customLocationAdp);
                        customLocationAdp.notifyDataSetChanged();
                    }

                }


            } catch (Exception e) {

            }


        }
    };

    public FragMerAudience() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_mer_audience_lay, container, false);
        idinit();
        return v;
    }

    private void idinit() {
        locationlist = v.findViewById(R.id.locationlist);
        locationlist.setExpanded(true);
        swipeToRefresh = v.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefresh.setRefreshing(false);
            }
        });
        womenpercant = v.findViewById(R.id.womenpercant);
        menpercant = v.findViewById(R.id.menpercant);

        range1progress = v.findViewById(R.id.range1progress);
        range2progress = v.findViewById(R.id.range2progress);
        range3progress = v.findViewById(R.id.range3progress);
        range4progress = v.findViewById(R.id.range4progress);
        range5progress = v.findViewById(R.id.range5progress);
        range6progress = v.findViewById(R.id.range6progress);

        stateview = v.findViewById(R.id.stateview);
        state = v.findViewById(R.id.state);
        citytv = v.findViewById(R.id.citytv);
        cityview = v.findViewById(R.id.cityview);
        womenview = v.findViewById(R.id.womenview);
        womentv = v.findViewById(R.id.womentv);
        menlay = v.findViewById(R.id.menlay);
        alltv = v.findViewById(R.id.alltv);
        mainview = v.findViewById(R.id.mainview);
        allview = v.findViewById(R.id.allview);
        if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
            menpercant.setText("" + MerStatusAct.salesAudienceBeanArrayList.get(0).getMalePercent() + " %");
            womenpercant.setText("" + MerStatusAct.salesAudienceBeanArrayList.get(0).getMalePercent() + " %");

        }

        womentv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                    range1progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange1PercentFemale());
                    range2progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange2PercentFemale());
                    range3progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange3PercentFemale());
                    range4progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange4PercentFemale());
                    range5progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange5PercentFemale());
                    range6progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange6PercentFemale());

                }
                womentv.setTextColor(getResources().getColor(R.color.red));
                womenview.setBackgroundResource(R.color.red);
                womenview.setVisibility(View.VISIBLE);

                menlay.setTextColor(getResources().getColor(R.color.black));
                mainview.setVisibility(View.GONE);

                alltv.setTextColor(getResources().getColor(R.color.black));
                allview.setVisibility(View.GONE);

            }
        });
        menlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                    range1progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange1PercentMale());
                    range2progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange2PercentMale());
                    range3progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange3PercentMale());
                    range4progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange4PercentMale());
                    range5progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange5PercentMale());
                    range6progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange6PercentMale());

                }

                menlay.setTextColor(getResources().getColor(R.color.red));
                mainview.setBackgroundResource(R.color.red);
                mainview.setVisibility(View.VISIBLE);

                womentv.setTextColor(getResources().getColor(R.color.black));
                womenview.setVisibility(View.GONE);

                alltv.setTextColor(getResources().getColor(R.color.black));
                allview.setVisibility(View.GONE);

            }
        });
        alltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                    range1progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange1PercentAll());
                    range2progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange2PercentAll());
                    range3progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange3PercentAll());
                    range4progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange4PercentAll());
                    range5progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange5PercentAll());
                    range6progress.setProgress((int) MerStatusAct.salesAudienceBeanArrayList.get(0).getTotalAgeRange6PercentAll());

                }

                alltv.setTextColor(getResources().getColor(R.color.red));
                allview.setBackgroundResource(R.color.red);
                allview.setVisibility(View.VISIBLE);

                womentv.setTextColor(getResources().getColor(R.color.black));
                womenview.setVisibility(View.GONE);

                menlay.setTextColor(getResources().getColor(R.color.black));
                mainview.setVisibility(View.GONE);

            }
        });
        citytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                citytv.setTextColor(getResources().getColor(R.color.red));
                cityview.setBackgroundResource(R.color.red);
                cityview.setVisibility(View.VISIBLE);

                state.setTextColor(getResources().getColor(R.color.black));
                stateview.setVisibility(View.GONE);
                if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                    customLocationAdp = new CustomLocationAdp(getActivity(), MerStatusAct.salesAudienceBeanArrayList.get(0).getCity());
                    locationlist.setAdapter(customLocationAdp);
                    customLocationAdp.notifyDataSetChanged();
                }

            }
        });
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                state.setTextColor(getResources().getColor(R.color.red));
                stateview.setBackgroundResource(R.color.red);
                stateview.setVisibility(View.VISIBLE);

                citytv.setTextColor(getResources().getColor(R.color.black));
                cityview.setVisibility(View.GONE);
                if (MerStatusAct.salesAudienceBeanArrayList != null && !MerStatusAct.salesAudienceBeanArrayList.isEmpty()) {
                    customLocationAdp = new CustomLocationAdp(getActivity(), MerStatusAct.salesAudienceBeanArrayList.get(0).getState(), "state");
                    locationlist.setAdapter(customLocationAdp);
                    customLocationAdp.notifyDataSetChanged();
                }


            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("SalesData"));
    }


    public class CustomLocationAdp extends BaseAdapter {
        Context context;
        String type = "";
        List<CityLoc> cityLocList;
        List<State> stateList;
        private LayoutInflater inflater = null;

        public CustomLocationAdp(Context contexts, List<CityLoc> cityLocList) {
            this.context = contexts;
            this.type = "";
            this.cityLocList = cityLocList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomLocationAdp(Context contexts, List<State> stateList, String type) {
            this.type = type;
            this.context = contexts;
            this.stateList = stateList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (type != null && type.equalsIgnoreCase("state")) {
                return stateList == null ? 0 : stateList.size();
            } else {
                return cityLocList == null ? 0 : cityLocList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View rowView;

            rowView = inflater.inflate(R.layout.custom_location_progress, null);
            TextView name_loc = rowView.findViewById(R.id.name_loc);
            ProgressBar progress_percant = rowView.findViewById(R.id.progress_percant);
            if (type != null && type.equalsIgnoreCase("state")) {
                if (stateList.get(position).getState() != null && !stateList.get(position).getState().equalsIgnoreCase("")) {
                    progress_percant.setProgress(Integer.parseInt(stateList.get(position).getStatePercent()));

                }
                name_loc.setText("" + stateList.get(position).getState());

            } else {
                if (cityLocList.get(position).getCity() != null && !cityLocList.get(position).getCity().equalsIgnoreCase("")) {
                    progress_percant.setProgress(Integer.parseInt(cityLocList.get(position).getCityPercent()));

                }
                name_loc.setText("" + cityLocList.get(position).getCity());

            }


            // cardnumber.setText(""+getLastfour(cardBeanArrayList.get(position).getCard_number()));
            return rowView;
        }

        public class Holder {

        }

    }


}